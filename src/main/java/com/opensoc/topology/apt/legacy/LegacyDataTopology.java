package com.opensoc.topology.apt.legacy;

import com.opensoc.topology.apt.KafkaSpoutBuilder;

import com.opensoc.topology.apt.legacy.ips.StoreBoltIPS;
//import com.opensoc.topology.apt.legacy.ips.StoreBoltUTM;
//import com.opensoc.topology.apt.legacy.ips.StoreBoltWAF;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.KeyValueSchemeAsMultiScheme;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.spout.SchemeAsMultiScheme;

public class LegacyDataTopology {

	public static void main(String[] args) throws Exception {
	
		String LEGACY_TOPOLOGY_NAME = "legacy";

		String ZK_CONN			= "opensoc1:2181";
		String ZK_BROKERS_PATH	= "/brokers";
	
		String LEGACY_IPS_TOPIC 				= "ips_log";
		int    LEGACY_IPS_SPOUT_PARALLELISM 	= 1;
		int    LEGACY_IPS_STORE_PARALLELISM 	= 1;
	
		String LEGACY_UTM_TOPIC 				= "utm_log";
		int    LEGACY_UTM_SPOUT_PARALLELISM 	= 1;
		int    LEGACY_UTM_STORE_PARALLELISM 	= 1;
	
		String LEGACY_WAF_TOPIC 				= "waf_log";
		int    LEGACY_WAF_SPOUT_PARALLELISM 	= 1;
		int    LEGACY_WAF_STORE_PARALLELISM 	= 1;

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("spout4IPS", KafkaSpoutBuilder.getSpout(ZK_CONN, ZK_BROKERS_PATH, LEGACY_IPS_TOPIC), LEGACY_IPS_SPOUT_PARALLELISM);
		builder.setBolt("store4IPS", new StoreBoltIPS(), LEGACY_IPS_STORE_PARALLELISM).shuffleGrouping("spout4IPS");

		/*
		builder.setSpout("spout4IPS", KafkaSpoutBuilder.getSpout(ZK_CONN, ZK_BROKERS_PATH, LEGACY_IPS_TOPIC), LEGACY_IPS_SPOUT_PARALLELISM);
		
		builder.setBolt("store4IPS", new StoreBolt4IPS(), LEGACY_IPS_STORE_PARALLELISM).shuffleGrouping("spout4IPS");
		builder.setBolt("drop4IPS",  new CleanBolt4IPS(), 1);
	
		builder.setSpout("spout4UTM", KafkaSpoutBuilder.getSpout(ZK_CONN, ZK_BROKERS_PATH, LEGACY_UTM_TOPIC), LEGACY_UTM_SPOUT_PARALLELISM);

		builder.setBolt("store4UTM", new StoreBolt4IPS(), LEGACY_UTM_STORE_PARALLELISM).shuffleGrouping("spout4UTM");
		builder.setBolt("drop4UTM",  new CleanBolt4IPS(), 1);

		builder.setSpout("spout4WAF", KafkaSpoutBuilder.getSpout(ZK_CONN, ZK_BROKERS_PATH, LEGACY_WAF_TOPIC), LEGACY_WAF_SPOUT_PARALLELISM);

		builder.setBolt("store4WAF", new StoreBolt4WAF(), LEGACY_WAF_STORE_PARALLELISM).shuffleGrouping("spout4WAF");
		builder.setBolt("drop4WAF",  new CleanBolt4WAF(), 1);
		*/

		Config conf = new Config();
		conf.setDebug(false);
		conf.setNumWorkers(2);
		
		StormSubmitter.submitTopologyWithProgressBar(LEGACY_TOPOLOGY_NAME, conf, builder.createTopology());
	}	
}
