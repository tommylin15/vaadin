package com.scsb.domain;

import java.util.Hashtable;
import java.util.Properties;

import org.math.R.RserverConf;
import org.rosuda.REngine.Rserve.RConnection;

import com.scsb.util.IntegerUtil;

public class RsessionPool{
	protected static HashRsession    instance   = HashRsession.getInstance();
	private Hashtable<Integer,Properties> hashRsession=new Hashtable<Integer, Properties>();

	public RsessionPool(){
		hashRsession=instance.getHashRsession();
	}

	private RserverConf getConf(int key){
		RserverConf rconf = null;
		String host =hashRsession.get(key).getProperty("HOST");
		int port =Integer.parseInt(hashRsession.get(key).getProperty("PORT"));
		rconf =new RserverConf(host, port, "", "",new Properties() );
		RConnection rcon =rconf.connect();
		if (rcon != null){
			rcon.close();
			return rconf;
		}
		return null;		
	}
	
	public RserverConf getRserverConf(){
		RserverConf rconf = null;
		//先排列隨機的順序,避免每次都由第一組RSERVE開始檢查
		int[] arrR =IntegerUtil.randomArray(1, instance.getRSERVE_S(),instance.getRSERVE_S());
		
		//若全部的RSERVE都在忙祿中,則暫停N秒後再檢查一次,時間*次數若逾時,則出現連線失敗 
		for(int i=0;i<instance.getWAIT_COUNT();i++){
			for(int x=0;x<arrR.length;x++){
				rconf=getConf(arrR[x]);
				if (rconf != null){
					System.out.println("rconf.port:"+rconf.port);
					return rconf;
				}
			}//for check rserve
			try {
				Thread.sleep(instance.getWAIT_TIMES()*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}//for wait
		return null;
	}
	
}

