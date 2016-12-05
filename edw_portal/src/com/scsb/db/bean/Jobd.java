package com.scsb.db.bean; 

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Jobd implements Serializable {
	private String beanKey="";
	private String programid= "" ;
	private String actionCode= "" ;
	private String jobName= "" ;
	private String updateDatetime= "" ;
	private String updateUser= "" ;
	private Collection<String> actionItemList =new HashSet<String>();

	public Jobd() {
	}
   public Collection<String> getActionItemList() {   	
    	return actionItemList;
    }
    
    public void setActionItemList(Collection<String> collection) {   	
    	if (collection != null){
        	this.actionItemList =collection;
	    	Iterator itr = collection.iterator();
	    	String itemIds="";
	        while (itr.hasNext()) {
	        	itemIds+=(String) itr.next();
	        }    	
	        this.actionCode=itemIds;
    	}
    }	

	
	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getProgramid() { 
		return programid;
	}

	public void setProgramid(String programid) { 
  		this.programid = programid; 
		this.beanKey=this.programid+"_"+this.jobName+"";
	}

	public String getActionCode() { 
		return actionCode;
	}

	public void setActionCode(String actionCode) { 
  		this.actionCode = actionCode;
        char[] cItem =actionCode.toCharArray();
        actionItemList =new HashSet<String>();
        for (int i=0 ;i<cItem.length;i++){
        	actionItemList.add(cItem[i]+"");
        }  		
  		
	}

	public String getJobName() { 
		return jobName;
	}

	public void setJobName(String jobName) { 
  		this.jobName = jobName; 
		this.beanKey=this.programid+"_"+this.jobName+"";
	}

	public String getUpdateDatetime() { 
		return updateDatetime;
	}

	public void setUpdateDatetime(String updateDatetime) { 
  		this.updateDatetime = updateDatetime; 
	}

	public String getUpdateUser() { 
		return updateUser;
	}

	public void setUpdateUser(String updateUser) { 
  		this.updateUser = updateUser; 
	}


}
