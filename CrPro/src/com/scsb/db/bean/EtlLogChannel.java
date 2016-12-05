package com.scsb.db.bean; 

import java.io.Serializable;

public class EtlLogChannel implements Serializable {
	private String beanKey="";
	private String idBatch= "" ;
	private String channelId= "" ;
	private String logDate= "" ;
	private String loggingObjectType= "" ;
	private String objectName= "" ;
	private String objectCopy= "" ;
	private String repositoryDirectory= "" ;
	private String filename= "" ;
	private String objectId= "" ;
	private String objectRevision= "" ;
	private String parentChannelId= "" ;
	private String rootChannelId= "" ;
	private String status= "" ;

	public EtlLogChannel() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//外加status
	public String getStatus() { 
		return status;
	}

	public void setStatus(String status) { 
  		this.status = status; 
	}	
	
	//Field Data=========================================
	public String getIdBatch() { 
		return idBatch;
	}

	public void setIdBatch(String idBatch) { 
  		this.idBatch = idBatch; 
	}

	public String getChannelId() { 
		return channelId;
	}

	public void setChannelId(String channelId) { 
  		this.channelId = channelId; 
		this.beanKey=this.channelId+"";
	}

	public String getLogDate() { 
		return logDate;
	}

	public void setLogDate(String logDate) { 
  		this.logDate = logDate; 
	}

	public String getLoggingObjectType() { 
		return loggingObjectType;
	}

	public void setLoggingObjectType(String loggingObjectType) { 
  		this.loggingObjectType = loggingObjectType; 
	}

	public String getObjectName() { 
		return objectName;
	}

	public void setObjectName(String objectName) { 
  		this.objectName = objectName; 
	}

	public String getObjectCopy() { 
		return objectCopy;
	}

	public void setObjectCopy(String objectCopy) { 
  		this.objectCopy = objectCopy; 
	}

	public String getRepositoryDirectory() { 
		return repositoryDirectory;
	}

	public void setRepositoryDirectory(String repositoryDirectory) { 
  		this.repositoryDirectory = repositoryDirectory; 
	}

	public String getFilename() { 
		return filename;
	}

	public void setFilename(String filename) { 
  		this.filename = filename; 
	}

	public String getObjectId() { 
		return objectId;
	}

	public void setObjectId(String objectId) { 
  		this.objectId = objectId; 
	}

	public String getObjectRevision() { 
		return objectRevision;
	}

	public void setObjectRevision(String objectRevision) { 
  		this.objectRevision = objectRevision; 
	}

	public String getParentChannelId() { 
		return parentChannelId;
	}

	public void setParentChannelId(String parentChannelId) { 
  		this.parentChannelId = parentChannelId; 
	}

	public String getRootChannelId() { 
		return rootChannelId;
	}

	public void setRootChannelId(String rootChannelId) { 
  		this.rootChannelId = rootChannelId; 
	}


}
