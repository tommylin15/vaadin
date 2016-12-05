package com.scsb.db.bean; 

import java.io.Serializable;

public class SessionDb implements Serializable {
	private String beanKey="";
	private String sessionId= "" ;
	private String sessionUserid= "" ;
	private String sessionIp= "" ;
	private String sessionXml= "" ;
	private String sessionDatetime= "" ;
	private String sessionGroupid= "" ;
	private String sessionProgramid= "" ;

	public SessionDb() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getSessionId() { 
		return sessionId;
	}

	public void setSessionId(String sessionId) { 
  		this.sessionId = sessionId; 
		this.beanKey=this.sessionId+"_"+this.sessionUserid+"";
	}

	public String getSessionUserid() { 
		return sessionUserid;
	}

	public void setSessionUserid(String sessionUserid) { 
  		this.sessionUserid = sessionUserid; 
		this.beanKey=this.sessionId+"_"+this.sessionUserid+"";
	}

	public String getSessionIp() { 
		return sessionIp;
	}

	public void setSessionIp(String sessionIp) { 
  		this.sessionIp = sessionIp; 
	}

	public String getSessionXml() { 
		return sessionXml;
	}

	public void setSessionXml(String sessionXml) { 
  		this.sessionXml = sessionXml; 
	}

	public String getSessionDatetime() { 
		return sessionDatetime;
	}

	public void setSessionDatetime(String sessionDatetime) { 
  		this.sessionDatetime = sessionDatetime; 
	}

	public String getSessionGroupid() { 
		return sessionGroupid;
	}

	public void setSessionGroupid(String sessionGroupid) { 
  		this.sessionGroupid = sessionGroupid; 
	}

	public String getSessionProgramid() { 
		return sessionProgramid;
	}

	public void setSessionProgramid(String sessionProgramid) { 
  		this.sessionProgramid = sessionProgramid; 
	}


}
