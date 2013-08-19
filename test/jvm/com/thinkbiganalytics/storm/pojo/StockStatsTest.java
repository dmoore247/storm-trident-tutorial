package com.thinkbiganalytics.storm.pojo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class StockStatsTest {

    @SuppressWarnings("deprecation")
    @Test
    public void test() {
        StockStats s1 = new StockStats( 25.0, 100 );
        StockStats s2 = new StockStats( 50.0, 200 );

        StockStats r1 = new StockStats( s1, null );
        assertEquals( 25.0, r1.avgPrice, 0.0 );
        assertEquals( 100L, r1.shareVolume, 0.0 );

        StockStats r2 = new StockStats( null, s2 );
        assertEquals( 50.0, r2.avgPrice, 0.0 );
        assertEquals( 200L, r2.shareVolume, 0.0 );

    }

    @Test
    public void test1() {
        StockStats s = new StockStats();
        assertEquals( 0.0, s.avgPrice, 0.0 );
        assertEquals( 0L, s.shareVolume, 0.0 );
    }

    @Test
    public void test2() {
        StockStats r1 = new StockStats( null, null );
        assertEquals( 0.0, r1.avgPrice, 0.0 );
        assertEquals( 0L, r1.shareVolume, 0.0 );
    }

    @Test
    public void test3() {
        StockStats s = new StockStats( 25.0, 100 );
        assertEquals( 25.0, s.avgPrice, 0.0 );
        assertEquals( 100L, s.shareVolume, 0.0 );
    }

    @Test
    public void test4() {
        StockStats s1 = new StockStats( 25.0, 100 );
        StockStats s2 = new StockStats( 50.0, 200 );
        StockStats r3 = new StockStats( s1, s2 );
        assertEquals( (25.0 + 50.0) / 2.0, r3.avgPrice, 0.0 );
        assertEquals( (25.0 * 100 + 50.0 * 200) / (100L + 200L), r3.weightedAvgPrice, 0.0 );
        assertEquals( 300L, r3.shareVolume, 0.0 );
    }

}
