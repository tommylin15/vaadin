package com.scsb.db.bean; 

import java.io.Serializable;

public class Fnbct0 implements Serializable {
	private String beanKey="";
	private String brhCod= "" ;
	private String dbAppN= "" ;
	private String chinFul= "" ;
	private String chinAl1= "" ;
	private String chinAl2= "" ;
	private String chinAd1= "" ;
	private String chinAd2= "" ;
	private String areaAls= "" ;
	private String englFu1= "" ;
	private String englFu2= "" ;
	private String englAls= "" ;
	private String englAd1= "" ;
	private String englAd2= "" ;
	private String englAd3= "" ;
	private String brhTyp= "" ;
	private String bnkCod= "" ;
	private String brhSts= "" ;
	private String tel= "" ;
	private String excgBl= "" ;
	private String reInCt= "" ;
	private String excgMic= "" ;
	private String ofcCtbr= "" ;
	private String frmtBus= "" ;
	private String frmt001= "" ;
	private String norlBus= "" ;
	private String lasWkD= "" ;
	private String signonT= "" ;
	private String signoff= "" ;
	private String lasUpS= "" ;
	private String prsSts= "" ;
	private String clSts= "" ;
	private String corPrs= "" ;
	private String swiftNo= "" ;
	private String auditCo= "" ;
	private String inactNo= "" ;
	private String eftDat= "" ;
	private String abbrNam= "" ;
	private int excpDat=0;

	public Fnbct0() {
	}

	//BeanKey===========================================
	public String getBeanKey() { 
		return beanKey;
	}

	public void setBeanKey(String beanKey) { 
  		this.beanKey = beanKey; 
	}

	//Field Data=========================================
	public String getBrhCod() { 
		return brhCod;
	}

	public void setBrhCod(String brhCod) { 
  		this.brhCod = brhCod; 
		this.beanKey=this.brhCod+"";
	}

	public String getDbAppN() { 
		return dbAppN;
	}

	public void setDbAppN(String dbAppN) { 
  		this.dbAppN = dbAppN; 
	}

	public String getChinFul() { 
		return chinFul;
	}

	public void setChinFul(String chinFul) { 
  		this.chinFul = chinFul; 
	}

	public String getChinAl1() { 
		return chinAl1;
	}

	public void setChinAl1(String chinAl1) { 
  		this.chinAl1 = chinAl1; 
	}

	public String getChinAl2() { 
		return chinAl2;
	}

	public void setChinAl2(String chinAl2) { 
  		this.chinAl2 = chinAl2; 
	}

	public String getChinAd1() { 
		return chinAd1;
	}

	public void setChinAd1(String chinAd1) { 
  		this.chinAd1 = chinAd1; 
	}

	public String getChinAd2() { 
		return chinAd2;
	}

	public void setChinAd2(String chinAd2) { 
  		this.chinAd2 = chinAd2; 
	}

	public String getAreaAls() { 
		return areaAls;
	}

	public void setAreaAls(String areaAls) { 
  		this.areaAls = areaAls; 
	}

	public String getEnglFu1() { 
		return englFu1;
	}

	public void setEnglFu1(String englFu1) { 
  		this.englFu1 = englFu1; 
	}

	public String getEnglFu2() { 
		return englFu2;
	}

	public void setEnglFu2(String englFu2) { 
  		this.englFu2 = englFu2; 
	}

	public String getEnglAls() { 
		return englAls;
	}

	public void setEnglAls(String englAls) { 
  		this.englAls = englAls; 
	}

	public String getEnglAd1() { 
		return englAd1;
	}

	public void setEnglAd1(String englAd1) { 
  		this.englAd1 = englAd1; 
	}

	public String getEnglAd2() { 
		return englAd2;
	}

	public void setEnglAd2(String englAd2) { 
  		this.englAd2 = englAd2; 
	}

	public String getEnglAd3() { 
		return englAd3;
	}

	public void setEnglAd3(String englAd3) { 
  		this.englAd3 = englAd3; 
	}

	public String getBrhTyp() { 
		return brhTyp;
	}

	public void setBrhTyp(String brhTyp) { 
  		this.brhTyp = brhTyp; 
	}

	public String getBnkCod() { 
		return bnkCod;
	}

	public void setBnkCod(String bnkCod) { 
  		this.bnkCod = bnkCod; 
	}

	public String getBrhSts() { 
		return brhSts;
	}

	public void setBrhSts(String brhSts) { 
  		this.brhSts = brhSts; 
	}

	public String getTel() { 
		return tel;
	}

	public void setTel(String tel) { 
  		this.tel = tel; 
	}

	public String getExcgBl() { 
		return excgBl;
	}

	public void setExcgBl(String excgBl) { 
  		this.excgBl = excgBl; 
	}

	public String getReInCt() { 
		return reInCt;
	}

	public void setReInCt(String reInCt) { 
  		this.reInCt = reInCt; 
	}

	public String getExcgMic() { 
		return excgMic;
	}

	public void setExcgMic(String excgMic) { 
  		this.excgMic = excgMic; 
	}

	public String getOfcCtbr() { 
		return ofcCtbr;
	}

	public void setOfcCtbr(String ofcCtbr) { 
  		this.ofcCtbr = ofcCtbr; 
	}

	public String getFrmtBus() { 
		return frmtBus;
	}

	public void setFrmtBus(String frmtBus) { 
  		this.frmtBus = frmtBus; 
	}

	public String getFrmt001() { 
		return frmt001;
	}

	public void setFrmt001(String frmt001) { 
  		this.frmt001 = frmt001; 
	}

	public String getNorlBus() { 
		return norlBus;
	}

	public void setNorlBus(String norlBus) { 
  		this.norlBus = norlBus; 
	}

	public String getLasWkD() { 
		return lasWkD;
	}

	public void setLasWkD(String lasWkD) { 
  		this.lasWkD = lasWkD; 
	}

	public String getSignonT() { 
		return signonT;
	}

	public void setSignonT(String signonT) { 
  		this.signonT = signonT; 
	}

	public String getSignoff() { 
		return signoff;
	}

	public void setSignoff(String signoff) { 
  		this.signoff = signoff; 
	}

	public String getLasUpS() { 
		return lasUpS;
	}

	public void setLasUpS(String lasUpS) { 
  		this.lasUpS = lasUpS; 
	}

	public String getPrsSts() { 
		return prsSts;
	}

	public void setPrsSts(String prsSts) { 
  		this.prsSts = prsSts; 
	}

	public String getClSts() { 
		return clSts;
	}

	public void setClSts(String clSts) { 
  		this.clSts = clSts; 
	}

	public String getCorPrs() { 
		return corPrs;
	}

	public void setCorPrs(String corPrs) { 
  		this.corPrs = corPrs; 
	}

	public String getSwiftNo() { 
		return swiftNo;
	}

	public void setSwiftNo(String swiftNo) { 
  		this.swiftNo = swiftNo; 
	}

	public String getAuditCo() { 
		return auditCo;
	}

	public void setAuditCo(String auditCo) { 
  		this.auditCo = auditCo; 
	}

	public String getInactNo() { 
		return inactNo;
	}

	public void setInactNo(String inactNo) { 
  		this.inactNo = inactNo; 
	}

	public String getEftDat() { 
		return eftDat;
	}

	public void setEftDat(String eftDat) { 
  		this.eftDat = eftDat; 
	}

	public String getAbbrNam() { 
		return abbrNam;
	}

	public void setAbbrNam(String abbrNam) { 
  		this.abbrNam = abbrNam; 
	}

	public int getExcpDat() { 
		return excpDat;
	}

	public void setExcpDat(int excpDat) { 
  		this.excpDat = excpDat; 
	}


}
