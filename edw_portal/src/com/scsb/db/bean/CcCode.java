package com.scsb.db.bean; 

import java.io.Serializable;

public class CcCode implements Serializable {
	private String beanKey="";
	private String codeKind= "" ;
	private String codeId= "" ;
	private String codeParent= "" ;
	private String codeName= "" ;
	private String delFlg= "" ;
	private String sortOrder= "" ;
	private String codeMemo= "" ;
	private String lstModUsrid= "" ;
	private String lstModDt= "" ;

	public CcCode() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getCodeKind() { 
		return codeKind;
	}

	public void setCodeKind(String codeKind) { 
  		this.codeKind = codeKind; 
		this.beanKey=this.codeKind+"_"+this.codeId+"";
	}

	public String getCodeId() { 
		return codeId;
	}

	public void setCodeId(String codeId) { 
  		this.codeId = codeId; 
		this.beanKey=this.codeKind+"_"+this.codeId+"";
	}

	public String getCodeParent() { 
		return codeParent;
	}

	public void setCodeParent(String codeParent) { 
  		this.codeParent = codeParent; 
	}

	public String getCodeName() { 
		return codeName;
	}

	public void setCodeName(String codeName) { 
  		this.codeName = codeName; 
	}

	public String getDelFlg() { 
		return delFlg;
	}

	public void setDelFlg(String delFlg) { 
  		this.delFlg = delFlg; 
	}

	public String getSortOrder() { 
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) { 
  		this.sortOrder = sortOrder; 
	}

	public String getCodeMemo() { 
		return codeMemo;
	}

	public void setCodeMemo(String codeMemo) { 
  		this.codeMemo = codeMemo; 
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
