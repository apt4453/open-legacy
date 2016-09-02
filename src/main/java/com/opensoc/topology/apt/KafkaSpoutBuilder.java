package com.opensoc.topology.apt;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.KeyValueSchemeAsMultiScheme;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;

public class KafkaSpoutBuilder {

    public static void main(String args[]) {
        // String zkConn =
        // "129.254.39.30:2181,129.254.39.22:2181,129.254.39.23:2181,129.254.39.99:2181,129.254.39.100:2181/kafka";

        String zkConn = "opensoc1:2181";
        String brokersPath = "/brokers";
        String topic = "ips_log";
        KafkaSpout spout = KafkaSpoutBuilder.getSpout(zkConn, brokersPath, topic);
        spout.activate();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        spout.deactivate();
        spout.close();
    }

    
    /**
     * KafkaSpout 媛앹껜瑜��쒓났�쒕떎.
     * 
     * @param zkConn
     * @param brokersPath
     * @param topic
     * 
     * @return KafkaSpout 媛앹껜
     */
    public static KafkaSpout getSpout(String zkConn, String brokersPath, String topic) {
        // BrokerHosts hosts = new ZkHosts(zkConn);
        // String brokerPath = "/kafka/brokers";
        // ZkHosts hosts = new ZkHosts(brokerZkStr, zkConn;
        System.out.println(zkConn + ":" + brokersPath);
        BrokerHosts hosts = new ZkHosts(zkConn, brokersPath);

        // String clientId = UUID.randomUUID().toString();
        String clientId = topic;
        System.out.println("clientId: " + clientId);

        // Root to store consumer's offset
        String zkRoot = "/brokers";
        // Directory structure : /storm-kafka/[clientId]/partition-0
        // E.g. /storm-kafka/page-visits/partition-{0, 1, 2, 3}

        SpoutConfig config = new SpoutConfig(hosts, topic, zkRoot, clientId);
        KafkaSpoutBuilder.setConfig(config);        
        
        KafkaSpout kafkaSpout = new KafkaSpout(config);

        return kafkaSpout;
    }
    
    /**
     * KafkaSpout 媛앹껜瑜��ㅼ젙�쒕떎.
     * 
     * @param config
     *            KafkaSpout 媛앹껜
     */
    private static void setConfig(SpoutConfig config) {

        // ----------------------------------------------------
        // How often save the current Kafka offset to ZooKeeper
        // ----------------------------------------------------
        // config.stateUpdateIntervalMs = 2000;
        
        // --------------------------------------------------
        // Read from head (EarliestTime) or tail (LatestTime)
        // --------------------------------------------------
        // config.startOffsetTime = kafka.api.OffsetRequest.EarliestTime();
        config.startOffsetTime = kafka.api.OffsetRequest.LatestTime();

        // -----------------------------
        // Read from head (EarliestTime)
        // -----------------------------
        config.forceFromStart = false;
        // config.forceFromStart = true;

        // ------------------------------------------------
        // Topic's data format : key = null, value = String
        // ------------------------------------------------
        // config.scheme = new RawMultiScheme();
        config.scheme = new SchemeAsMultiScheme(new StringScheme()); 

        // config.zkServers = ImmutableList.of("mclee1");
        // config.zkPort = 2181;

        // Exponential back-off retry settings.
        // These are used when retrying messages after a bolt calls OutputCollector.fail().
        // Note: be sure to set backtype.storm.Config.MESSAGE_TIMEOUT_SECS appropriately to prevent resubmitting the message
        // while still retrying.
        long retryInitialDelayMs = 0;
        double retryDelayMultiplier = 1.0;
        long retryDelayMaxMs = 60 * 1000;

        int fetchSizeBytes = 1024 * 1024;
        int bufferSizeBytes = 1024 * 1024;
        
        int socketTimeoutMs = 10000;
        int fetchMaxWait = 10000;        
        boolean ignoreZkOffsets = false;

        long maxOffsetBehind = Long.MAX_VALUE;
        boolean useStartOffsetTimeIfOffsetOutOfRange = true;
        int metricsTimeBucketSizeInSecs = 60;
        
    }
}
