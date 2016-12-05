package com.scsb.db.bean; 

import java.io.Serializable;

public class WorkContactListSa implements Serializable {
	private String beanKey="";
	private double itemid=0;
	private String custIdn= "" ;
	private double meetTimes=0;
	private String invdateStr= "" ;
	private String invdateEnd= "" ;
	private String aoCode= "" ;
	private String listType= "" ;
	private String brhCod= "" ;
	private String chinAct= "" ;
	private String tel1= "" ;
	private String tel2= "" ;
	private String datadate= "" ;
	private String title= "" ;
	private String db= "" ;
	private String _class= "" ;
	private String invdateSday= "" ;
	private String invdateEday= "" ;
	private String bday= "" ;
	private String cardFlag= "" ;
	private String mobilPh= "" ;
	private String aoFlag= "" ;
	private String mgrEmp= "" ;
	private String respNo= "" ;
	private String paoId= "" ;
	private String crdUser= "" ;
	private String crdDatetime= "" ;
	private String ischeck= "" ;
	private String changeFlag= "" ;
	private double acuAvg=0;
	private int togetherid=0;
	private String reset= "" ;
	private String showBrh= "" ;
	
	//==外加================================
	private String ischeckName= "" ;
	private String resetName= "" ;

	public WorkContactListSa() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public double getItemid() { 
		return itemid;
	}

	public void setItemid(double itemid) { 
  		this.itemid = itemid; 
		this.beanKey=this.itemid+"";
	}

	public String getCustIdn() { 
		return custIdn;
	}

	public void setCustIdn(String custIdn) { 
  		this.custIdn = custIdn; 
	}

	public double getMeetTimes() { 
		return meetTimes;
	}

	public void setMeetTimes(double meetTimes) { 
  		this.meetTimes = meetTimes; 
	}

	public String getInvdateStr() { 
		return invdateStr;
	}

	public void setInvdateStr(String invdateStr) { 
  		this.invdateStr = invdateStr; 
	}

	public String getInvdateEnd() { 
		return invdateEnd;
	}

	public void setInvdateEnd(String invdateEnd) { 
  		this.invdateEnd = invdateEnd; 
	}

	public String getAoCode() { 
		return aoCode;
	}

	public void setAoCode(String aoCode) { 
  		this.aoCode = aoCode; 
	}

	public String getListType() { 
		return listType;
	}

	public void setListType(String listType) { 
  		this.listType = listType; 
	}

	public String getBrhCod() { 
		return brhCod;
	}

	public void setBrhCod(String brhCod) { 
  		this.brhCod = brhCod; 
	}

	public String getChinAct() { 
		return chinAct;
	}

	public void setChinAct(String chinAct) { 
  		this.chinAct = chinAct; 
	}

	public String getTel1() { 
		return tel1;
	}

	public void setTel1(String tel1) { 
  		this.tel1 = tel1; 
	}

	public String getTel2() { 
		return tel2;
	}

	public void setTel2(String tel2) { 
  		this.tel2 = tel2; 
	}

	public String getDatadate() { 
		return datadate;
	}

	public void setDatadate(String datadate) { 
  		this.datadate = datadate; 
	}

	public String getTitle() { 
		return title;
	}

	public void setTitle(String title) { 
  		this.title = title; 
	}

	public String getDb() { 
		return db;
	}

	public void setDb(String db) { 
  		this.db = db; 
	}

	public String get_Class() { 
		return _class;
	}

	public void set_Class(String _class) { 
  		this._class =_class; 
	}

	public String getInvdateSday() { 
		return invdateSday;
	}

	public void setInvdateSday(String invdateSday) { 
  		this.invdateSday = invdateSday; 
	}

	public String getInvdateEday() { 
		return invdateEday;
	}

	public void setInvdateEday(String invdateEday) { 
  		this.invdateEday = invdateEday; 
	}

	public String getBday() { 
		return bday;
	}

	public void setBday(String bday) { 
  		this.bday = bday; 
	}

	public String getCardFlag() { 
		return cardFlag;
	}

	public void setCardFlag(String cardFlag) { 
  		this.cardFlag = cardFlag; 
	}

	public String getMobilPh() { 
		return mobilPh;
	}

	public void setMobilPh(String mobilPh) { 
  		this.mobilPh = mobilPh; 
	}

	public String getAoFlag() { 
		return aoFlag;
	}

	public void setAoFlag(String aoFlag) { 
  		this.aoFlag = aoFlag; 
	}

	public String getMgrEmp() { 
		return mgrEmp;
	}

	public void setMgrEmp(String mgrEmp) { 
  		this.mgrEmp = mgrEmp; 
	}

	public String getRespNo() { 
		return respNo;
	}

	public void setRespNo(String respNo) { 
  		this.respNo = respNo; 
	}

	public String getPaoId() { 
		return paoId;
	}

	public void setPaoId(String paoId) { 
  		this.paoId = paoId; 
	}

	public String getCrdUser() { 
		return crdUser;
	}

	public void setCrdUser(String crdUser) { 
  		this.crdUser = crdUser; 
	}

	public String getCrdDatetime() { 
		return crdDatetime;
	}

	public void setCrdDatetime(String crdDatetime) { 
  		this.crdDatetime = crdDatetime; 
	}

	public String getIscheck() { 
		return ischeck;
	}

	public void setIscheck(String ischeck) { 
  		this.ischeck = ischeck; 
  		if (ischeck.equals("Y"))	this.ischeckName="正常";
  		if (ischeck.equals("N"))	this.ischeckName="審核中";
  		if (ischeck.equals("D"))	this.ischeckName="已刪除";
	}

	public String getChangeFlag() { 
		return changeFlag;
	}

	public void setChangeFlag(String changeFlag) { 
  		this.changeFlag = changeFlag; 
	}

	public double getAcuAvg() { 
		return acuAvg;
	}

	public void setAcuAvg(double acuAvg) { 
  		this.acuAvg = acuAvg; 
	}

	public int getTogetherid() { 
		return togetherid;
	}

	public void setTogetherid(int togetherid) { 
  		this.togetherid = togetherid; 
	}

	public String getReset() { 
		return reset;
	}

	public void setReset(String reset) { 
  		this.reset = reset; 
  		if (reset.equals("Y")) this.resetName="已撤消";
  		if (reset.equals("N")) this.resetName="審核中";
	}

	public String getShowBrh() { 
		return showBrh;
	}

	public void setShowBrh(String showBrh) { 
  		this.showBrh = showBrh; 
	}

	public String getIscheckName() { 
		return ischeckName;
	}

	public void setIscheckName(String ischeckName) { 
  		this.ischeckName = ischeckName; 
	}	
	
	public String getResetName() { 
		return resetName;
	}

	public void setResetName(String resetName) { 
  		this.resetName = resetName; 
	}		

}
