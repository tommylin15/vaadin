package com.scsb.db.bean; 

import java.io.Serializable;

public class Ldapou implements Serializable {
	private String beanKey="";
	private String ou= "" ;
	private String description= "" ;
	private String mail= "" ;
	private String manacctno= "" ;
	private String observer= "" ;
	private String ou6digit= "" ;
	private String id= "" ;
	private String parentou= "" ;	

	public Ldapou() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getOu() { 
		return ou;
	}

	public void setOu(String ou) { 
  		this.ou = ou; 
		this.beanKey=this.ou+"";
	}

	public String getDescription() { 
		return description;
	}

	public void setDescription(String description) { 
  		this.description = description; 
	}

	public String getMail() { 
		return mail;
	}

	public void setMail(String mail) { 
  		this.mail = mail; 
	}

	public String getManacctno() { 
		return manacctno;
	}

	public void setManacctno(String manacctno) { 
  		this.manacctno = manacctno; 
	}

	public String getObserver() { 
		return observer;
	}

	public void setObserver(String observer) { 
  		this.observer = observer; 
	}

	public String getOu6digit() { 
		return ou6digit;
	}

	public void setOu6digit(String ou6digit) { 
  		this.ou6digit = ou6digit; 
	}

	public String getParentou() { 
		return parentou;
	}

	public void setParentou(String parentou) { 
  		this.parentou = parentou; 
	}

	public String getId() { 
		return id;
	}

	public void setId(String id) { 
  		this.id = id; 
	}

}
