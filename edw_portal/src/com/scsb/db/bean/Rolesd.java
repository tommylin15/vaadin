package com.scsb.db.bean; 

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Rolesd implements Serializable {
	private String beanKey="";
	private String programid= "" ;
	private String actionCode= "" ;
	private String roleid= "" ;
	private Collection<String> actionItemList =new HashSet<String>();

	private String programName="";
	public Rolesd() {
	}

	//其它元件==========================================
	public String getProgramName() { 
		return programName;
	}

	public void setProgramName(String programName) { 
  		this.programName = programName; 
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
		this.beanKey=this.programid+"_"+this.roleid+"";
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

	public String getRoleid() { 
		return roleid;
	}

	public void setRoleid(String roleid) { 
  		this.roleid = roleid; 
		this.beanKey=this.programid+"_"+this.roleid+"";
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
