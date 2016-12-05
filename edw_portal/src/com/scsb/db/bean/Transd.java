package com.scsb.db.bean; 

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class Transd implements Serializable {
	private String beanKey="";
	private String programid= "" ;
	private String programname= "" ;
	private String programmode= "" ;
	private String menuhide= "" ;
	private String confirmmode= "" ;
	private String setmode= "" ;
	private String authorizemode= "" ;
	private String reportmode= "" ;
	private String browsemode= "" ;
	private String downloadmode= "" ;
	private String groupid= "" ;
	private String programtype= "" ;
	private String actionCode= "" ;
	private String deptactionmode= "" ;
	private String sortKey= "" ;
	private Collection<String> actionItemList =new HashSet<String>();

	public Transd() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	
	//---元件使用----------------------------------------------------------------------
    public Collection<String> getActionItemList() {   	
    	return actionItemList;
    }
    
    public void setActionItemList(Collection<String> collection) {   	
    	this.actionItemList =collection;
    	 Iterator itr = collection.iterator();
    	 String itemIds="";
         while (itr.hasNext()) {
        	 itemIds+=(String) itr.next();
         }    	
        this.actionCode=itemIds;
    }
	
	//Field Data=========================================
	public String getProgramid() { 
		return programid;
	}

	public void setProgramid(String programid) { 
  		this.programid = programid; 
		this.beanKey=this.sortKey+"_"+this.groupid+"_"+this.programid;
	}

	public String getProgramname() { 
		return programname;
	}

	public void setProgramname(String programname) { 
  		this.programname = programname; 
	}

	public String getProgrammode() { 
		return programmode;
	}

	public void setProgrammode(String programmode) { 
  		this.programmode = programmode; 
	}

	public String getMenuhide() { 
		return menuhide;
	}

	public void setMenuhide(String menuhide) { 
  		this.menuhide = menuhide; 
	}

	public String getConfirmmode() { 
		return confirmmode;
	}

	public void setConfirmmode(String confirmmode) { 
  		this.confirmmode = confirmmode; 
	}

	public String getSetmode() { 
		return setmode;
	}

	public void setSetmode(String setmode) { 
  		this.setmode = setmode; 
	}

	public String getAuthorizemode() { 
		return authorizemode;
	}

	public void setAuthorizemode(String authorizemode) { 
  		this.authorizemode = authorizemode; 
	}

	public String getReportmode() { 
		return reportmode;
	}

	public void setReportmode(String reportmode) { 
  		this.reportmode = reportmode; 
	}

	public String getBrowsemode() { 
		return browsemode;
	}

	public void setBrowsemode(String browsemode) { 
  		this.browsemode = browsemode; 
	}

	public String getDownloadmode() { 
		return downloadmode;
	}

	public void setDownloadmode(String downloadmode) { 
  		this.downloadmode = downloadmode; 
	}

	public String getGroupid() { 
		return groupid;
	}

	public void setGroupid(String groupid) { 
  		this.groupid = groupid; 
		this.beanKey=this.programid+"_"+this.groupid+"";
	}

	public String getProgramtype() { 
		return programtype;
	}

	public void setProgramtype(String programtype) { 
  		this.programtype = programtype; 
	}

	public String getDeptactionmode() { 
		return deptactionmode;
	}

	public void setDeptactionmode(String deptactionmode) { 
  		this.deptactionmode = deptactionmode; 
	}	
	
	public String getSortKey() { 
		return sortKey;
	}

	public void setSortKey(String sortKey) { 
  		this.sortKey = sortKey; 
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
