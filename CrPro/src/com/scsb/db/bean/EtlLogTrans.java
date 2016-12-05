package com.scsb.db.bean; 

import java.io.Serializable;

public class EtlLogTrans implements Serializable {
	private String beanKey="";
	private String idBatch= "" ;
	private String channelId= "" ;
	private String transname= "" ;
	private String status= "" ;
	private String linesRead= "" ;
	private String linesWritten= "" ;
	private String linesUpdated= "" ;
	private String linesInput= "" ;
	private String linesOutput= "" ;
	private String linesRejected= "" ;
	private String errors= "" ;
	private String startdate= "" ;
	private String enddate= "" ;
	private String logdate= "" ;
	private String depdate= "" ;
	private String replaydate= "" ;
	private String logField= "" ;

	public EtlLogTrans() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
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

	public String getTransname() { 
		return transname;
	}

	public void setTransname(String transname) { 
  		this.transname = transname; 
	}

	public String getStatus() { 
		return status;
	}

	public void setStatus(String status) { 
  		this.status = status; 
	}

	public String getLinesRead() { 
		return linesRead;
	}

	public void setLinesRead(String linesRead) { 
  		this.linesRead = linesRead; 
	}

	public String getLinesWritten() { 
		return linesWritten;
	}

	public void setLinesWritten(String linesWritten) { 
  		this.linesWritten = linesWritten; 
	}

	public String getLinesUpdated() { 
		return linesUpdated;
	}

	public void setLinesUpdated(String linesUpdated) { 
  		this.linesUpdated = linesUpdated; 
	}

	public String getLinesInput() { 
		return linesInput;
	}

	public void setLinesInput(String linesInput) { 
  		this.linesInput = linesInput; 
	}

	public String getLinesOutput() { 
		return linesOutput;
	}

	public void setLinesOutput(String linesOutput) { 
  		this.linesOutput = linesOutput; 
	}

	public String getLinesRejected() { 
		return linesRejected;
	}

	public void setLinesRejected(String linesRejected) { 
  		this.linesRejected = linesRejected; 
	}

	public String getErrors() { 
		return errors;
	}

	public void setErrors(String errors) { 
  		this.errors = errors; 
	}

	public String getStartdate() { 
		return startdate;
	}

	public void setStartdate(String startdate) { 
  		this.startdate = startdate; 
	}

	public String getEnddate() { 
		return enddate;
	}

	public void setEnddate(String enddate) { 
  		this.enddate = enddate; 
	}

	public String getLogdate() { 
		return logdate;
	}

	public void setLogdate(String logdate) { 
  		this.logdate = logdate; 
	}

	public String getDepdate() { 
		return depdate;
	}

	public void setDepdate(String depdate) { 
  		this.depdate = depdate; 
	}

	public String getReplaydate() { 
		return replaydate;
	}

	public void setReplaydate(String replaydate) { 
  		this.replaydate = replaydate; 
	}

	public String getLogField() { 
		return logField;
	}

	public void setLogField(String logField) { 
  		this.logField = logField; 
	}


}
