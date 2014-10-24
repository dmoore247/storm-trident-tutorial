## Storm Trident Tutorial ![](images/logo.png)
This repository contains Storm Trident examples and tutorial material. 
* The [Boston Storm Meetup presentation](/presentations/Boston%20Storm%20Meetup%20-%20Storm%20Trident%20-%20August%202013-v1DM.pptx) is included here too.
* Video of the Meetup presentation: http://vimeo.com/channels/505762/72864166

## Topologies
The sample topologies run in local mode and utilize data files located in the ./data folder. The spouts open the stock price data file and starts reading and injecting the data as tuples into the topology.
* SimpleTridentTopology - Demonstrate Trident and DRPC.
...

## Requirements
* Java JDK 1.7
* Eclipse Juno Service Release 2 or better
* Maven 3.2.3 or better

## Steps to run

* Download from Github
* mvn eclipse:eclipse
* mvn package
* open in eclipse
* Import existing Maven project
* select <....>Topology.java
* Run as Java Program
and this will start the running topology in local mode.

## Experimenting with the Example
* Change Trident example aggregates (from Sum() to Count())
* Uncomment .each ... Debug() lines one by one

---
(c) Copyright, 2011-2014, Think Big Analytics, Inc. All Rights Reserved
