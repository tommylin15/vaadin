package com.scsb.db.bean; 

import java.io.Serializable;

public class Usersa implements Serializable {
	private String beanKey="";
	private String userid= "" ;
	private String userpwd= "" ;
	private String mustchange= "" ;
	private String accountmode= "" ;
	private String policyid= "" ;
	private double errortimes=0;
	private String remoteaddr= "" ;
	private String recentlogintime= "" ;
	private String createteller= "" ;
	private String createdate= "" ;
	private String createtime= "" ;
	private String binarypassword= "" ;
	private String lastlogouttime= "" ;
	private String repeatlogin= "" ;

	public Usersa() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getUserid() { 
		return userid;
	}

	public void setUserid(String userid) { 
  		this.userid = userid; 
		this.beanKey=this.userid+"";
	}

	public String getUserPwd() { 
		return userpwd;
	}

	public void setUserPwd(String userpwd) { 
  		this.userpwd = userpwd; 
	}

	public String getMustchange() { 
		return mustchange;
	}

	public void setMustchange(String mustchange) { 
  		this.mustchange = mustchange; 
	}

	public String getAccountmode() { 
		return accountmode;
	}

	public void setAccountmode(String accountmode) { 
  		this.accountmode = accountmode; 
	}

	public String getPolicyid() { 
		return policyid;
	}

	public void setPolicyid(String policyid) { 
  		this.policyid = policyid; 
	}

	public double getErrortimes() { 
		return errortimes;
	}

	public void setErrortimes(double errortimes) { 
  		this.errortimes = errortimes; 
	}

	public String getRemoteaddr() { 
		return remoteaddr;
	}

	public void setRemoteaddr(String remoteaddr) { 
  		this.remoteaddr = remoteaddr; 
	}

	public String getRecentlogintime() { 
		return recentlogintime;
	}

	public void setRecentlogintime(String recentlogintime) { 
  		this.recentlogintime = recentlogintime; 
	}

	public String getCreateteller() { 
		return createteller;
	}

	public void setCreateteller(String createteller) { 
  		this.createteller = createteller; 
	}

	public String getCreatedate() { 
		return createdate;
	}

	public void setCreatedate(String createdate) { 
  		this.createdate = createdate; 
	}

	public String getCreatetime() { 
		return createtime;
	}

	public void setCreatetime(String createtime) { 
  		this.createtime = createtime; 
	}

	public String getBinarypassword() { 
		return binarypassword;
	}

	public void setBinarypassword(String binarypassword) { 
  		this.binarypassword = binarypassword; 
	}

	public String getLastlogouttime() { 
		return lastlogouttime;
	}

	public void setLastlogouttime(String lastlogouttime) { 
  		this.lastlogouttime = lastlogouttime; 
	}

	public String getRepeatlogin() { 
		return repeatlogin;
	}

	public void setRepeatlogin(String repeatlogin) { 
  		this.repeatlogin = repeatlogin; 
	}
	private String updateDatetime= "" ;
	public String getUpdateDatetime() { 
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) { 
  		this.updateDatetime = updateDatetime; 
	}	
	
	private String updateUser= "" ;
	public String getUpdateUser() { 
		return updateUser;
	}

	public void setUpdateUser(String updateUser) { 
  		this.updateUser = updateUser; 
	}

}
