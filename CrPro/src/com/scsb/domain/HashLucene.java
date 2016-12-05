package com.scsb.domain;

import java.util.Properties;
import org.apache.log4j.Logger;

public class HashLucene{

	static Logger logger = Logger.getLogger(HashLucene.class.getName());
	private static HashLucene instance = new HashLucene();
	private Properties systemProps;

	public static HashLucene getInstance(){
		return instance;
	}

	private HashLucene(){
		systemProps = null;
	}
	
	public void putProperties(Properties properties){
		systemProps = properties;
	}

	public Properties getProperties(){
		return systemProps;
	}

}
