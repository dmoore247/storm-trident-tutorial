package com.thinkbiganalytics.storm.trident;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Debug;
import storm.trident.operation.builtin.FilterNull;
import storm.trident.operation.builtin.MapGet;
import storm.trident.state.StateFactory;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;

public class StockStatsTridentTopology {

    public static final String DRPC_STREAM_NAME = "trades";
    private static final String STOCK_TRIDENT_TOPOLOGY_NAME = "StockTridentTopology";
    private static final String DATA_PATH = "../../data/20130301.csv.gz";
    private static final int NUM_WORKERS = 3;
    private static final int LOCAL_DURATION = 20000;
    private static final Fields fields = new Fields( "date", "symbol", "price", "shares" );

    public static void main( String[] args ) throws Exception {
        Config conf = new Config();
        conf.setDebug( false );
        conf.setMaxSpoutPending( 1 );
        final boolean isLocal = true;

        if (isLocal) {
            // This topology can only be run as local because it is a toy example
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology( STOCK_TRIDENT_TOPOLOGY_NAME, conf, buildTopology( drpc ) );

            for (int i = 0; i < 2000; i++) {
                System.err.println( "DRPC response: " + drpc.execute( DRPC_STREAM_NAME, "INTC GE AAPL" ) );
                Thread.sleep( 1000 );
            }
        } else {
            StormSubmitter.submitTopology( STOCK_TRIDENT_TOPOLOGY_NAME, conf, buildTopology( null ) );
        }
    }

    public static StormTopology buildTopology( LocalDRPC drpc ) {

        TridentTopology topology = new TridentTopology();
        CSVBatchSpout spout = new CSVBatchSpout( DATA_PATH, fields );

        // In this state we will save the real-time counts for each symbol
        StateFactory mapState = new MemoryMapState.Factory();

        // Real-time part of the system: a Trident topology that groups by symbol and stores per-symbol counts
        TridentState tradeCount = topology.newStream( "quotes", spout )
        // debug display fields values.
        // .each( fields, new Debug() )
        //
                .groupBy( new Fields( "symbol" ) )
                // count(symbol) AS count
                .persistentAggregate( mapState, fields, new StockAggregator(), new Fields( "stats" ) );

        topology.newDRPCStream( DRPC_STREAM_NAME, drpc )
        // state query
                .each( new Fields( "args" ), new Split(), new Fields( "symbol" ) )
                //
                .groupBy( new Fields( "symbol" ) )
                //
                .stateQuery( tradeCount, new Fields( "symbol" ), new MapGet(), new Fields( "stats" ) )
                //
                .each( new Fields( "stats" ), new FilterNull() )
                //
                .each( new Fields( "symbol", "stats" ), new Debug() )
                //
                // Project allows us to keep only the interesting results
                .project( new Fields( "symbol", "stats" ) );

        return topology.build();
    }
}
