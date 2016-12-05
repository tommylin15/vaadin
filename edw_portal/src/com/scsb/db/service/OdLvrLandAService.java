package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.OdLvrLandA;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class OdLvrLandAService{
	private static Logger logger = Logger.getLogger(OdLvrLandAService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool ="";
	public OdLvrLandAService() {
		this.dbPool="defaultPool";

		this.lang="tw";	

	}

	public OdLvrLandAService(String lang) {
		this.lang=lang;	
	}

public void setDbPool(String dbPool) {
		this.dbPool=dbPool;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM OD_LVR_LAND_A  ";
	/**
 	 * 取得OdLvrLandA資料表中,所有資料
 	 * @return BeanContainer<String,OdLvrLandA>
 	 */	
	public BeanContainer<String,OdLvrLandA> getOdLvrLandA_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,OdLvrLandA> beanContainer =new BeanContainer<String,OdLvrLandA>(OdLvrLandA.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALL);	
			beanContainer =OdLvrLandAIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM OD_LVR_LAND_A WHERE KEY_NUMBER =? ";
	//取得OdLvrLandA  PrimaryKey ;
	public BeanContainer<String,OdLvrLandA> getOdLvrLandA_PKs(OdLvrLandA bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,OdLvrLandA> beanContainer =new BeanContainer<String,OdLvrLandA>(OdLvrLandA.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getKeyNumber()));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =OdLvrLandAIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM OD_LVR_LAND_A WHERE KEY_NUMBER =? ";
	/** 
	 * 取得OdLvrLandA  PrimaryKey 回傳 OdLvrLandA ;
	 * @param bean 
	 * @return
	 */
	public OdLvrLandA getOdLvrLandA_PK(OdLvrLandA bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,OdLvrLandA> beanContainer =new BeanContainer<String,OdLvrLandA>(OdLvrLandA.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		OdLvrLandA retbean=new OdLvrLandA();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getKeyNumber()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =OdLvrLandAIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<OdLvrLandA> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
		dbResultSet = (new DBAction()).executeQuery(dbPool,sqlstmt);
		return dbResultSet;
	}		

	//寫入OD_LVR_LAND_A ;
	public boolean insertOdLvrLandA(OdLvrLandA bean){
		return insertOdLvrLandA(null,bean);
	}
	public boolean insertOdLvrLandA(Connection con ,OdLvrLandA bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO OD_LVR_LAND_A( FILE_NAME  ,CITY  ,TRANS_TYPE  ,AREA  ,TRANS_SUBJECT  ,LAND_BUILD  ,LAND_TRANSFER  ,LAND_USE  ,LAND_USE_NON  ,LAND_USE_NONS  ,TRANS_YM  ,TRANS_NUM  ,TRANS_LEVEL  ,TOTAL_FLOOR  ,BUILD_TYPE  ,USE  ,MATERIALS  ,BUILD_YM  ,BUILD_TRANSFER  ,PATTERN_ROOM  ,PATTERN_PARLOR  ,PATTERN_BATHROOM  ,PATTERN_COMPART  ,HAVE_MANAGER  ,TOTAL_PRICE  ,UNIT_PRICE  ,PARKING_TYPE  ,PARKING_TRANSFER  ,PARKING_PRICE  ,MEMO  ,KEY_NUMBER  ,DATADATE ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getFileName()));
						hashSet.add(DBUtil.setData(2,"String",bean.getCity()));
						hashSet.add(DBUtil.setData(3,"String",bean.getTransType()));
						hashSet.add(DBUtil.setData(4,"String",bean.getArea()));
						hashSet.add(DBUtil.setData(5,"String",bean.getTransSubject()));
						hashSet.add(DBUtil.setData(6,"String",bean.getLandBuild()));
						hashSet.add(DBUtil.setData(7,"String",bean.getLandTransfer()));
						hashSet.add(DBUtil.setData(8,"String",bean.getLandUse()));
						hashSet.add(DBUtil.setData(9,"String",bean.getLandUseNon()));
						hashSet.add(DBUtil.setData(10,"String",bean.getLandUseNons()));
						hashSet.add(DBUtil.setData(11,"String",bean.getTransYm()));
						hashSet.add(DBUtil.setData(12,"String",bean.getTransNum()));
						hashSet.add(DBUtil.setData(13,"String",bean.getTransLevel()));
						hashSet.add(DBUtil.setData(14,"String",bean.getTotalFloor()));
						hashSet.add(DBUtil.setData(15,"String",bean.getBuildType()));
						hashSet.add(DBUtil.setData(16,"String",bean.getUse()));
						hashSet.add(DBUtil.setData(17,"String",bean.getMaterials()));
						hashSet.add(DBUtil.setData(18,"String",bean.getBuildYm()));
						hashSet.add(DBUtil.setData(19,"String",bean.getBuildTransfer()));
						hashSet.add(DBUtil.setData(20,"String",bean.getPatternRoom()));
						hashSet.add(DBUtil.setData(21,"String",bean.getPatternParlor()));
						hashSet.add(DBUtil.setData(22,"String",bean.getPatternBathroom()));
						hashSet.add(DBUtil.setData(23,"String",bean.getPatternCompart()));
						hashSet.add(DBUtil.setData(24,"String",bean.getHaveManager()));
						hashSet.add(DBUtil.setData(25,"String",bean.getTotalPrice()));
						hashSet.add(DBUtil.setData(26,"String",bean.getUnitPrice()));
						hashSet.add(DBUtil.setData(27,"String",bean.getParkingType()));
						hashSet.add(DBUtil.setData(28,"String",bean.getParkingTransfer()));
						hashSet.add(DBUtil.setData(29,"String",bean.getParkingPrice()));
						hashSet.add(DBUtil.setData(30,"String",bean.getMemo()));
						hashSet.add(DBUtil.setData(31,"String",bean.getKeyNumber()));
						hashSet.add(DBUtil.setData(32,"String",bean.getDatadate()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改OD_LVR_LAND_A ;
	public boolean updateOdLvrLandA(OdLvrLandA bean){
		return updateOdLvrLandA(null,bean);
	}
	public boolean updateOdLvrLandA(Connection con ,OdLvrLandA bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE OD_LVR_LAND_A set ";
				sql+=" FILE_NAME =? ";
				sql+=", CITY =? ";
				sql+=", TRANS_TYPE =? ";
				sql+=", AREA =? ";
				sql+=", TRANS_SUBJECT =? ";
				sql+=", LAND_BUILD =? ";
				sql+=", LAND_TRANSFER =? ";
				sql+=", LAND_USE =? ";
				sql+=", LAND_USE_NON =? ";
				sql+=", LAND_USE_NONS =? ";
				sql+=", TRANS_YM =? ";
				sql+=", TRANS_NUM =? ";
				sql+=", TRANS_LEVEL =? ";
				sql+=", TOTAL_FLOOR =? ";
				sql+=", BUILD_TYPE =? ";
				sql+=", USE =? ";
				sql+=", MATERIALS =? ";
				sql+=", BUILD_YM =? ";
				sql+=", BUILD_TRANSFER =? ";
				sql+=", PATTERN_ROOM =? ";
				sql+=", PATTERN_PARLOR =? ";
				sql+=", PATTERN_BATHROOM =? ";
				sql+=", PATTERN_COMPART =? ";
				sql+=", HAVE_MANAGER =? ";
				sql+=", TOTAL_PRICE =? ";
				sql+=", UNIT_PRICE =? ";
				sql+=", PARKING_TYPE =? ";
				sql+=", PARKING_TRANSFER =? ";
				sql+=", PARKING_PRICE =? ";
				sql+=", MEMO =? ";
				sql+=", DATADATE =? ";
				sql+=" WHERE KEY_NUMBER =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getFileName()));
				hashSet.add(DBUtil.setData(2,"String",bean.getCity()));
				hashSet.add(DBUtil.setData(3,"String",bean.getTransType()));
				hashSet.add(DBUtil.setData(4,"String",bean.getArea()));
				hashSet.add(DBUtil.setData(5,"String",bean.getTransSubject()));
				hashSet.add(DBUtil.setData(6,"String",bean.getLandBuild()));
				hashSet.add(DBUtil.setData(7,"String",bean.getLandTransfer()));
				hashSet.add(DBUtil.setData(8,"String",bean.getLandUse()));
				hashSet.add(DBUtil.setData(9,"String",bean.getLandUseNon()));
				hashSet.add(DBUtil.setData(10,"String",bean.getLandUseNons()));
				hashSet.add(DBUtil.setData(11,"String",bean.getTransYm()));
				hashSet.add(DBUtil.setData(12,"String",bean.getTransNum()));
				hashSet.add(DBUtil.setData(13,"String",bean.getTransLevel()));
				hashSet.add(DBUtil.setData(14,"String",bean.getTotalFloor()));
				hashSet.add(DBUtil.setData(15,"String",bean.getBuildType()));
				hashSet.add(DBUtil.setData(16,"String",bean.getUse()));
				hashSet.add(DBUtil.setData(17,"String",bean.getMaterials()));
				hashSet.add(DBUtil.setData(18,"String",bean.getBuildYm()));
				hashSet.add(DBUtil.setData(19,"String",bean.getBuildTransfer()));
				hashSet.add(DBUtil.setData(20,"String",bean.getPatternRoom()));
				hashSet.add(DBUtil.setData(21,"String",bean.getPatternParlor()));
				hashSet.add(DBUtil.setData(22,"String",bean.getPatternBathroom()));
				hashSet.add(DBUtil.setData(23,"String",bean.getPatternCompart()));
				hashSet.add(DBUtil.setData(24,"String",bean.getHaveManager()));
				hashSet.add(DBUtil.setData(25,"String",bean.getTotalPrice()));
				hashSet.add(DBUtil.setData(26,"String",bean.getUnitPrice()));
				hashSet.add(DBUtil.setData(27,"String",bean.getParkingType()));
				hashSet.add(DBUtil.setData(28,"String",bean.getParkingTransfer()));
				hashSet.add(DBUtil.setData(29,"String",bean.getParkingPrice()));
				hashSet.add(DBUtil.setData(30,"String",bean.getMemo()));
				hashSet.add(DBUtil.setData(31,"String",bean.getDatadate()));
				hashSet.add(DBUtil.setData(32,"String",bean.getKeyNumber()));

			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	/**
	 * 依據傳入欄位.修改OD_LVR_LAND_A ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateOdLvrLandA(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE OD_LVR_LAND_A set ";
				sql+=sUpdateCode;
				sql+=" WHERE KEY_NUMBER =? ";
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

	//刪除OD_LVR_LAND_A ;
	public boolean deleteOdLvrLandA(OdLvrLandA bean){
		return deleteOdLvrLandA(null,bean);
	}
	public boolean deleteOdLvrLandA(Connection con ,OdLvrLandA bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  OD_LVR_LAND_A ";
				sql+=" WHERE KEY_NUMBER =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getKeyNumber()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private OdLvrLandA setOdLvrLandA(Item item){
		OdLvrLandA bean =new OdLvrLandA();
			bean.setFileName(item.getItemProperty("FILE_NAME").getValue()+"");
			bean.setCity(item.getItemProperty("CITY").getValue()+"");
			bean.setTransType(item.getItemProperty("TRANS_TYPE").getValue()+"");
			bean.setArea(item.getItemProperty("AREA").getValue()+"");
			bean.setTransSubject(item.getItemProperty("TRANS_SUBJECT").getValue()+"");
			bean.setLandBuild(item.getItemProperty("LAND_BUILD").getValue()+"");
			bean.setLandTransfer(item.getItemProperty("LAND_TRANSFER").getValue()+"");
			bean.setLandUse(item.getItemProperty("LAND_USE").getValue()+"");
			bean.setLandUseNon(item.getItemProperty("LAND_USE_NON").getValue()+"");
			bean.setLandUseNons(item.getItemProperty("LAND_USE_NONS").getValue()+"");
			bean.setTransYm(item.getItemProperty("TRANS_YM").getValue()+"");
			bean.setTransNum(item.getItemProperty("TRANS_NUM").getValue()+"");
			bean.setTransLevel(item.getItemProperty("TRANS_LEVEL").getValue()+"");
			bean.setTotalFloor(item.getItemProperty("TOTAL_FLOOR").getValue()+"");
			bean.setBuildType(item.getItemProperty("BUILD_TYPE").getValue()+"");
			bean.setUse(item.getItemProperty("USE").getValue()+"");
			bean.setMaterials(item.getItemProperty("MATERIALS").getValue()+"");
			bean.setBuildYm(item.getItemProperty("BUILD_YM").getValue()+"");
			bean.setBuildTransfer(item.getItemProperty("BUILD_TRANSFER").getValue()+"");
			bean.setPatternRoom(item.getItemProperty("PATTERN_ROOM").getValue()+"");
			bean.setPatternParlor(item.getItemProperty("PATTERN_PARLOR").getValue()+"");
			bean.setPatternBathroom(item.getItemProperty("PATTERN_BATHROOM").getValue()+"");
			bean.setPatternCompart(item.getItemProperty("PATTERN_COMPART").getValue()+"");
			bean.setHaveManager(item.getItemProperty("HAVE_MANAGER").getValue()+"");
			bean.setTotalPrice(item.getItemProperty("TOTAL_PRICE").getValue()+"");
			bean.setUnitPrice(item.getItemProperty("UNIT_PRICE").getValue()+"");
			bean.setParkingType(item.getItemProperty("PARKING_TYPE").getValue()+"");
			bean.setParkingTransfer(item.getItemProperty("PARKING_TRANSFER").getValue()+"");
			bean.setParkingPrice(item.getItemProperty("PARKING_PRICE").getValue()+"");
			bean.setMemo(item.getItemProperty("MEMO").getValue()+"");
			bean.setKeyNumber(item.getItemProperty("KEY_NUMBER").getValue()+"");
			bean.setDatadate(item.getItemProperty("DATADATE").getValue()+"");

		return bean;
	}

	private BeanContainer<String,OdLvrLandA> OdLvrLandAIndexToBean(IndexedContainer container){
		BeanContainer<String,OdLvrLandA> beanContainer =new BeanContainer<String,OdLvrLandA>(OdLvrLandA.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			beanContainer.addBean(setOdLvrLandA(container.getItem(container.getIdByIndex(i))));
		}
		return beanContainer;
	}

}
