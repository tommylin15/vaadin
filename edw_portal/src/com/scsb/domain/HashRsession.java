package com.scsb.domain;

import java.util.Hashtable;
import java.util.Properties;

public class HashRsession{
	private static HashRsession instance = new HashRsession();
	private Properties poolProps;
	private int RSERVE_S;
	private int WAIT_TIMES;
	private int WAIT_COUNT;
	private Hashtable<Integer,Properties> hashRsession=new Hashtable<Integer, Properties>();
	public static HashRsession getInstance(){
		return instance;
	}

	private HashRsession(){
	}
	
	public void init(){
		this.RSERVE_S =Integer.parseInt(getProperties().getProperty("RSERVE_S"));
		this.WAIT_TIMES =Integer.parseInt(getProperties().getProperty("WAIT_TIMES"));
		this.WAIT_COUNT =Integer.parseInt(getProperties().getProperty("WAIT_COUNT"));
		for(int i=1;i<=RSERVE_S;i++){
			Properties prop =new Properties();
			prop.put("HOST",getProperties().getProperty("RSERVE"+i+"_HOST"));
			prop.put("PORT",getProperties().getProperty("RSERVE"+i+"_PORT"));
			hashRsession.put(i, prop);
		}
	}
	
	public void putProperties(Properties properties){
		poolProps = properties;
	}

	public Properties getProperties(){
		return poolProps;
	}
	
	public Hashtable<Integer,Properties> getHashRsession(){
		return hashRsession;
	}
	
	public int getRSERVE_S(){
		return RSERVE_S;
	}

	public int getWAIT_TIMES(){
		return WAIT_TIMES;
	}
	
	public int getWAIT_COUNT(){
		return WAIT_COUNT;
	}	
	
}

