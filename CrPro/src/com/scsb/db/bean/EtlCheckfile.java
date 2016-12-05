package com.scsb.db.bean; 

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class EtlCheckfile implements Serializable {
	private String beanKey="";
	private String filename= "" ;
	private String shortFilename= "" ;
	private String path= "" ;
	private String type= "" ;
	private String busDate= "" ;
	private String nowDate= null ;
	private String lastDate= null ;
	private long size=0;
	private String extension= "" ;
	private String status= "" ;
	private String endDate= null;
	private String updateMode= "" ;

	public EtlCheckfile() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getFilename() { 
		return filename;
	}

	public void setFilename(String filename) { 
  		this.filename = filename; 
		this.beanKey=this.filename+"_"+this.shortFilename+"";
	}

	public String getShortFilename() { 
		return shortFilename;
	}

	public void setShortFilename(String shortFilename) { 
  		this.shortFilename = shortFilename; 
		this.beanKey=this.filename+"_"+this.shortFilename+"";
	}

	public String getPath() { 
		return path;
	}

	public void setPath(String path) { 
  		this.path = path; 
	}

	public String getType() { 
		return type;
	}

	public void setType(String type) { 
  		this.type = type; 
	}

	public String getBusDate() { 
		return busDate;
	}

	public void setBusDate(String busDate) { 
  		this.busDate = busDate; 
	}

	public String getNowDate() { 
		return nowDate;
	}

	public void setNowDate(String nowDate) { 
  		this.nowDate = nowDate; 
	}

	public String getLastDate() { 
		return lastDate;
	}

	public void setLastDate(String lastDate) { 
  		this.lastDate = lastDate; 
	}

	public long getSize() { 
		return size;
	}

	public void setSize(long size) { 
  		this.size = size; 
	}

	public String getExtension() { 
		return extension;
	}

	public void setExtension(String extension) { 
  		this.extension = extension; 
	}

	public String getStatus() { 
		return status;
	}

	public void setStatus(String status) { 
  		this.status = status; 
	}

	public String getEndDate() { 
		return endDate;
	}

	public void setEndDate(String endDate) { 
  		this.endDate = endDate; 
	}

	public String getUpdateMode() { 
		return updateMode;
	}

	public void setUpdateMode(String updateMode) { 
  		this.updateMode = updateMode; 
	}


}
