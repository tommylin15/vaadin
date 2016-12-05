package com.scsb.db.service; 

import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Fnbct0;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class Fnbct0Service{
	private static Logger logger = Logger.getLogger(Fnbct0Service.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public Fnbct0Service() {
	}	
	public Fnbct0Service(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM FNBCT0 ";
	/**
 	 * 取得SasFnbct0資料表中,所有資料
 	 * @return BeanContainer<String,SasFnbct0>
 	 */	
	public BeanContainer<String,Fnbct0> getSasFnbct0_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Fnbct0> beanContainer =new BeanContainer<String,Fnbct0>(Fnbct0.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =SasFnbct0IndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM FNBCT0 WHERE BRH_COD =? ";
	//取得SasFnbct0  PrimaryKey ;
	public BeanContainer<String,Fnbct0> getSasFnbct0_PKs(Fnbct0 bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Fnbct0> beanContainer =new BeanContainer<String,Fnbct0>(Fnbct0.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =SasFnbct0IndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM FNBCT0 WHERE BRH_COD =? ";
	/** 
	 * 取得SasFnbct0  PrimaryKey 回傳 SasFnbct0 ;
	 * @param bean 
	 * @return
	 */
	public Fnbct0 getSasFnbct0_PK(Fnbct0 bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Fnbct0> beanContainer =new BeanContainer<String,Fnbct0>(Fnbct0.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Fnbct0 retbean=new Fnbct0();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =SasFnbct0IndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Fnbct0> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
					retbean = beanitem.getBean();
				}
			} catch (RuntimeException re) {
				logger.error(StrUtil.convException(re));	
				this.ErrMsg=re.toString();	
			}catch (SQLException se) {
				logger.error(StrUtil.convException(se));	
				this.ErrMsg=se.toString();	
			}
		}
		return retbean;
	}

	//取得SasFnbct0  PrimaryKey 回傳 SasFnbct0 ;
	public Fnbct0 getBeanByKeys(String[] keys){
		Fnbct0 qryBean = new Fnbct0();
		// TODO 確認是否產生 setFileNameUp(keys[i]) 程式碼
		qryBean.setBrhCod(keys[0]);
        Fnbct0 bean = new Fnbct0();
        bean = getSasFnbct0_PK(qryBean);
		return bean;
	}



	/**												
	 * 輸入查詢條件欄位名稱.與條件值,取回DBResultSet	
	 * @param flds {欄位名稱1,欄位名稱2..}			
	 * @param values {條件值1,條件值2..}				
	 * @return										
	 */												
	public DBResultSet getBeansByCond(String[] flds, String[] values){ 
		DBResultSet dbResultSet = null; 
		String conditionSQL = ""; 
		if (flds.length != values.length) {
			this.ErrMsg="傳入參數個數不匹配.請重新確認." +
							"欄位:"+StrUtil.convAryToStr(flds, ",")+
							"資料:"+StrUtil.convAryToStr(values, ",");
			logger.fatal(this.ErrMsg);
		} else {
			String tmpSQL = "";
			for (int i=0; i<flds.length; i++){
				tmpSQL = flds[i].toUpperCase() + "='" + values[i] + "'";
				if (i==0) {
					conditionSQL = "WHERE " + tmpSQL ;
				} else {
					conditionSQL = conditionSQL + " AND " + tmpSQL; 
				}
			}
		}		
		String sqlstmt = RETRIEVE_STMT_ALL.concat(conditionSQL);
		dbResultSet = (new DBAction()).executeQuery(sqlstmt);
		return dbResultSet;
	}		

	//寫入FNBCT0 ;
	public boolean insertSasFnbct0(Fnbct0 bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO FNBCT0( BRH_COD  ,DB_APP_N  ,CHIN_FUL  ,CHIN_AL1  ,CHIN_AL2  ,CHIN_AD1  ,CHIN_AD2  ,AREA_ALS  ,ENGL_FU1  ,ENGL_FU2  ,ENGL_ALS  ,ENGL_AD1  ,ENGL_AD2  ,ENGL_AD3  ,BRH_TYP  ,BNK_COD  ,BRH_STS  ,TEL  ,EXCG_BL  ,RE_IN_CT  ,EXCG_MIC  ,OFC_CTBR  ,FRMT_BUS  ,FRMT_001  ,NORL_BUS  ,LAS_WK_D  ,SIGNON_T  ,SIGNOFF  ,LAS_UP_S  ,PRS_STS  ,CL_STS  ,COR_PRS  ,SWIFT_NO  ,AUDIT_CO  ,INACT_NO  ,EFT_DAT  ,ABBR_NAM  ,EXCP_DAT ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
						hashSet.add(DBUtil.setData(2,"String",bean.getDbAppN()));
						hashSet.add(DBUtil.setData(3,"String",bean.getChinFul()));
						hashSet.add(DBUtil.setData(4,"String",bean.getChinAl1()));
						hashSet.add(DBUtil.setData(5,"String",bean.getChinAl2()));
						hashSet.add(DBUtil.setData(6,"String",bean.getChinAd1()));
						hashSet.add(DBUtil.setData(7,"String",bean.getChinAd2()));
						hashSet.add(DBUtil.setData(8,"String",bean.getAreaAls()));
						hashSet.add(DBUtil.setData(9,"String",bean.getEnglFu1()));
						hashSet.add(DBUtil.setData(10,"String",bean.getEnglFu2()));
						hashSet.add(DBUtil.setData(11,"String",bean.getEnglAls()));
						hashSet.add(DBUtil.setData(12,"String",bean.getEnglAd1()));
						hashSet.add(DBUtil.setData(13,"String",bean.getEnglAd2()));
						hashSet.add(DBUtil.setData(14,"String",bean.getEnglAd3()));
						hashSet.add(DBUtil.setData(15,"String",bean.getBrhTyp()));
						hashSet.add(DBUtil.setData(16,"String",bean.getBnkCod()));
						hashSet.add(DBUtil.setData(17,"String",bean.getBrhSts()));
						hashSet.add(DBUtil.setData(18,"String",bean.getTel()));
						hashSet.add(DBUtil.setData(19,"String",bean.getExcgBl()));
						hashSet.add(DBUtil.setData(20,"String",bean.getReInCt()));
						hashSet.add(DBUtil.setData(21,"String",bean.getExcgMic()));
						hashSet.add(DBUtil.setData(22,"String",bean.getOfcCtbr()));
						hashSet.add(DBUtil.setData(23,"String",bean.getFrmtBus()));
						hashSet.add(DBUtil.setData(24,"String",bean.getFrmt001()));
						hashSet.add(DBUtil.setData(25,"String",bean.getNorlBus()));
						hashSet.add(DBUtil.setData(26,"String",bean.getLasWkD()));
						hashSet.add(DBUtil.setData(27,"String",bean.getSignonT()));
						hashSet.add(DBUtil.setData(28,"String",bean.getSignoff()));
						hashSet.add(DBUtil.setData(29,"String",bean.getLasUpS()));
						hashSet.add(DBUtil.setData(30,"String",bean.getPrsSts()));
						hashSet.add(DBUtil.setData(31,"String",bean.getClSts()));
						hashSet.add(DBUtil.setData(32,"String",bean.getCorPrs()));
						hashSet.add(DBUtil.setData(33,"String",bean.getSwiftNo()));
						hashSet.add(DBUtil.setData(34,"String",bean.getAuditCo()));
						hashSet.add(DBUtil.setData(35,"String",bean.getInactNo()));
						hashSet.add(DBUtil.setData(36,"String",bean.getEftDat()));
						hashSet.add(DBUtil.setData(37,"String",bean.getAbbrNam()));
						hashSet.add(DBUtil.setData(38,"int",bean.getExcpDat()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改FNBCT0 ;
	public boolean updateSasFnbct0(Fnbct0 bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE FNBCT0 set ";
				sql+=" DB_APP_N =? ";
				sql+=", CHIN_FUL =? ";
				sql+=", CHIN_AL1 =? ";
				sql+=", CHIN_AL2 =? ";
				sql+=", CHIN_AD1 =? ";
				sql+=", CHIN_AD2 =? ";
				sql+=", AREA_ALS =? ";
				sql+=", ENGL_FU1 =? ";
				sql+=", ENGL_FU2 =? ";
				sql+=", ENGL_ALS =? ";
				sql+=", ENGL_AD1 =? ";
				sql+=", ENGL_AD2 =? ";
				sql+=", ENGL_AD3 =? ";
				sql+=", BRH_TYP =? ";
				sql+=", BNK_COD =? ";
				sql+=", BRH_STS =? ";
				sql+=", TEL =? ";
				sql+=", EXCG_BL =? ";
				sql+=", RE_IN_CT =? ";
				sql+=", EXCG_MIC =? ";
				sql+=", OFC_CTBR =? ";
				sql+=", FRMT_BUS =? ";
				sql+=", FRMT_001 =? ";
				sql+=", NORL_BUS =? ";
				sql+=", LAS_WK_D =? ";
				sql+=", SIGNON_T =? ";
				sql+=", SIGNOFF =? ";
				sql+=", LAS_UP_S =? ";
				sql+=", PRS_STS =? ";
				sql+=", CL_STS =? ";
				sql+=", COR_PRS =? ";
				sql+=", SWIFT_NO =? ";
				sql+=", AUDIT_CO =? ";
				sql+=", INACT_NO =? ";
				sql+=", EFT_DAT =? ";
				sql+=", ABBR_NAM =? ";
				sql+=", EXCP_DAT =? ";
				sql+=" WHERE BRH_COD =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getDbAppN()));
				hashSet.add(DBUtil.setData(2,"String",bean.getChinFul()));
				hashSet.add(DBUtil.setData(3,"String",bean.getChinAl1()));
				hashSet.add(DBUtil.setData(4,"String",bean.getChinAl2()));
				hashSet.add(DBUtil.setData(5,"String",bean.getChinAd1()));
				hashSet.add(DBUtil.setData(6,"String",bean.getChinAd2()));
				hashSet.add(DBUtil.setData(7,"String",bean.getAreaAls()));
				hashSet.add(DBUtil.setData(8,"String",bean.getEnglFu1()));
				hashSet.add(DBUtil.setData(9,"String",bean.getEnglFu2()));
				hashSet.add(DBUtil.setData(10,"String",bean.getEnglAls()));
				hashSet.add(DBUtil.setData(11,"String",bean.getEnglAd1()));
				hashSet.add(DBUtil.setData(12,"String",bean.getEnglAd2()));
				hashSet.add(DBUtil.setData(13,"String",bean.getEnglAd3()));
				hashSet.add(DBUtil.setData(14,"String",bean.getBrhTyp()));
				hashSet.add(DBUtil.setData(15,"String",bean.getBnkCod()));
				hashSet.add(DBUtil.setData(16,"String",bean.getBrhSts()));
				hashSet.add(DBUtil.setData(17,"String",bean.getTel()));
				hashSet.add(DBUtil.setData(18,"String",bean.getExcgBl()));
				hashSet.add(DBUtil.setData(19,"String",bean.getReInCt()));
				hashSet.add(DBUtil.setData(20,"String",bean.getExcgMic()));
				hashSet.add(DBUtil.setData(21,"String",bean.getOfcCtbr()));
				hashSet.add(DBUtil.setData(22,"String",bean.getFrmtBus()));
				hashSet.add(DBUtil.setData(23,"String",bean.getFrmt001()));
				hashSet.add(DBUtil.setData(24,"String",bean.getNorlBus()));
				hashSet.add(DBUtil.setData(25,"String",bean.getLasWkD()));
				hashSet.add(DBUtil.setData(26,"String",bean.getSignonT()));
				hashSet.add(DBUtil.setData(27,"String",bean.getSignoff()));
				hashSet.add(DBUtil.setData(28,"String",bean.getLasUpS()));
				hashSet.add(DBUtil.setData(29,"String",bean.getPrsSts()));
				hashSet.add(DBUtil.setData(30,"String",bean.getClSts()));
				hashSet.add(DBUtil.setData(31,"String",bean.getCorPrs()));
				hashSet.add(DBUtil.setData(32,"String",bean.getSwiftNo()));
				hashSet.add(DBUtil.setData(33,"String",bean.getAuditCo()));
				hashSet.add(DBUtil.setData(34,"String",bean.getInactNo()));
				hashSet.add(DBUtil.setData(35,"String",bean.getEftDat()));
				hashSet.add(DBUtil.setData(36,"String",bean.getAbbrNam()));
				hashSet.add(DBUtil.setData(37,"int",bean.getExcpDat()));
				hashSet.add(DBUtil.setData(38,"String",bean.getBrhCod()));

			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	/**
	 * 依據傳入欄位.修改FNBCT0 ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateSasFnbct0(HashMap<String, Object> hmData, String[] keysValue ){
		boolean isUpdate =false;
		this.ErrMsg="";
		HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
		if (hmData.size() > 0){
			Iterator it = hmData.entrySet().iterator();
			String sUpdateCode = "";
			int i=1;
			while (it.hasNext()) {
				Entry entry = (Entry)it.next();
				if (sUpdateCode==null || sUpdateCode.length()==0) 
					sUpdateCode = entry.getKey().toString().toUpperCase() + " = ?";
				else 
					sUpdateCode = sUpdateCode + "," + entry.getKey().toString().toUpperCase() + " = ?";
				Object[] value = (Object[])entry.getValue();
				if (value[0]==null || ((String)value[0]).length()==0)
					hashSet.add(DBUtil.setData(i,"String",value[1]));
				else 
					hashSet.add(DBUtil.setData(i,(String)value[0],value[1]));
				i++;
			}
			try{
				String sql = "UPDATE FNBCT0 set ";
				sql+=sUpdateCode;
				sql+=" WHERE BRH_COD =? ";
				hashSet.add(DBUtil.setData(i++,"String",keysValue[0]));
				isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			} catch (RuntimeException se) {
				isUpdate=false;
				logger.error(StrUtil.convException(se)); 
				this.ErrMsg=se.toString(); 
			}
		}
		return isUpdate;
	}

	//刪除FNBCT0 ;
	public boolean deleteSasFnbct0(Fnbct0 bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  FNBCT0 ";
				sql+=" WHERE BRH_COD =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Fnbct0 setSasFnbct0(Item item){
		Fnbct0 bean =new Fnbct0();
			bean.setBrhCod(item.getItemProperty("BRH_COD").getValue()+"");
			bean.setDbAppN(item.getItemProperty("DB_APP_N").getValue()+"");
			bean.setChinFul(item.getItemProperty("CHIN_FUL").getValue()+"");
			bean.setChinAl1(item.getItemProperty("CHIN_AL1").getValue()+"");
			bean.setChinAl2(item.getItemProperty("CHIN_AL2").getValue()+"");
			bean.setChinAd1(item.getItemProperty("CHIN_AD1").getValue()+"");
			bean.setChinAd2(item.getItemProperty("CHIN_AD2").getValue()+"");
			bean.setAreaAls(item.getItemProperty("AREA_ALS").getValue()+"");
			bean.setEnglFu1(item.getItemProperty("ENGL_FU1").getValue()+"");
			bean.setEnglFu2(item.getItemProperty("ENGL_FU2").getValue()+"");
			bean.setEnglAls(item.getItemProperty("ENGL_ALS").getValue()+"");
			bean.setEnglAd1(item.getItemProperty("ENGL_AD1").getValue()+"");
			bean.setEnglAd2(item.getItemProperty("ENGL_AD2").getValue()+"");
			bean.setEnglAd3(item.getItemProperty("ENGL_AD3").getValue()+"");
			bean.setBrhTyp(item.getItemProperty("BRH_TYP").getValue()+"");
			bean.setBnkCod(item.getItemProperty("BNK_COD").getValue()+"");
			bean.setBrhSts(item.getItemProperty("BRH_STS").getValue()+"");
			bean.setTel(item.getItemProperty("TEL").getValue()+"");
			bean.setExcgBl(item.getItemProperty("EXCG_BL").getValue()+"");
			bean.setReInCt(item.getItemProperty("RE_IN_CT").getValue()+"");
			bean.setExcgMic(item.getItemProperty("EXCG_MIC").getValue()+"");
			bean.setOfcCtbr(item.getItemProperty("OFC_CTBR").getValue()+"");
			bean.setFrmtBus(item.getItemProperty("FRMT_BUS").getValue()+"");
			bean.setFrmt001(item.getItemProperty("FRMT_001").getValue()+"");
			bean.setNorlBus(item.getItemProperty("NORL_BUS").getValue()+"");
			bean.setLasWkD(item.getItemProperty("LAS_WK_D").getValue()+"");
			bean.setSignonT(item.getItemProperty("SIGNON_T").getValue()+"");
			bean.setSignoff(item.getItemProperty("SIGNOFF").getValue()+"");
			bean.setLasUpS(item.getItemProperty("LAS_UP_S").getValue()+"");
			bean.setPrsSts(item.getItemProperty("PRS_STS").getValue()+"");
			bean.setClSts(item.getItemProperty("CL_STS").getValue()+"");
			bean.setCorPrs(item.getItemProperty("COR_PRS").getValue()+"");
			bean.setSwiftNo(item.getItemProperty("SWIFT_NO").getValue()+"");
			bean.setAuditCo(item.getItemProperty("AUDIT_CO").getValue()+"");
			bean.setInactNo(item.getItemProperty("INACT_NO").getValue()+"");
			bean.setEftDat(item.getItemProperty("EFT_DAT").getValue()+"");
			bean.setAbbrNam(item.getItemProperty("ABBR_NAM").getValue()+"");
			int iexcpDat =0;
			try{
				iexcpDat =Integer.parseInt(item.getItemProperty("EXCP_DAT").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setExcpDat(iexcpDat);

		return bean;
	}

	private BeanContainer<String,Fnbct0> SasFnbct0IndexToBean(IndexedContainer container){
		BeanContainer<String,Fnbct0> beanContainer =new BeanContainer<String,Fnbct0>(Fnbct0.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Fnbct0 bean =setSasFnbct0(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
