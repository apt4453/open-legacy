package com.opensoc.topology.apt.legacy.ips;

import java.sql.PreparedStatement;
import java.util.Map;

import com.opensoc.topology.apt.legacy.MySqlConnection;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
 
public class StoreBoltIPS extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;

	private MySqlConnection conn;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context)
	{
		String ip 		= "opensoc1";
		String database = "apt";
		String username = "root";
		String password = "root";

		conn = new MySqlConnection(ip, database, username, password);
		conn.open();
	}

	public void execute(Tuple tuple, BasicOutputCollector collector)
	{
		PreparedStatement statement = null;

		try {
			String sql = "insert into legacy_ips values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			String[] splits = tuple.getString(0).split(",");
			
			statement = conn.getConnection().prepareStatement(sql);
			
			for (int i=0; i<30; i++)
			{
				statement.setString(i+1, splits[i].replace("\'", ""));
			}
			
			statement.executeUpdate();
			
		} catch(Exception ex) {
			ex.printStackTrace();
			
		} finally {
			if(statement != null)
			{
				try {
					statement.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer)
	{
		// TODO Auto-generated method stub
	}
			  
	@Override
	public void cleanup()
	{
		conn.close();
	}

}
