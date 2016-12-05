package com.scsb.domain;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.math.R.RserverConf;
import org.math.R.Rsession;

import com.scsb.util.PropertiesUtil;

public class HashSystem{
	static Logger logger = Logger.getLogger(HashSystem.class.getName());
	private static HashSystem instance = new HashSystem();
	private Properties systemProps;
	
	public static HashSystem getInstance(){
		return instance;
	}

	private HashSystem(){
		systemProps = null;
	}
	
	public void putProperties(Properties properties){
		systemProps = properties;
	}

	public Properties getProperties(){
		return systemProps;
	}
	public void resetHashSystem(String systemFile){
		Properties systemFileProp = PropertiesUtil.loadProperties(systemFile);
		if(systemFileProp != null){
			putProperties(systemFileProp);
		}
		
	}
}
