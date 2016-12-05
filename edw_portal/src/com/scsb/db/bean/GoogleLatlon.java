package com.scsb.db.bean; 

import java.io.Serializable;

public class GoogleLatlon implements Serializable {
	private String beanKey="";
	private String addr= "" ;
	private String lat= "" ;
	private String lon= "" ;
	private String updateUser= "" ;
	private String updateDatetime= "" ;

	public GoogleLatlon() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getAddr() { 
		return addr;
	}

	public void setAddr(String addr) { 
  		this.addr = addr; 
		this.beanKey=this.addr+"";
	}

	public String getLat() { 
		return lat;
	}

	public void setLat(String lat) { 
  		this.lat = lat; 
	}

	public String getLon() { 
		return lon;
	}

	public void setLon(String lon) { 
  		this.lon = lon; 
	}

	public String getUpdateUser() { 
		return updateUser;
	}

	public void setUpdateUser(String updateUser) { 
  		this.updateUser = updateUser; 
	}

	public String getUpdateDatetime() { 
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) { 
  		this.updateDatetime = updateDatetime; 
	}


}
