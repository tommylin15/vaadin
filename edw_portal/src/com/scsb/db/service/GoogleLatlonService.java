package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.GoogleLatlon;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class GoogleLatlonService{
	private static Logger logger = Logger.getLogger(GoogleLatlonService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public GoogleLatlonService() {
	}

	public GoogleLatlonService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM GOOGLE_LATLON ";
	/**
 	 * 取得GoogleLatlon資料表中,所有資料
 	 * @return BeanContainer<String,GoogleLatlon>
 	 */	
	public BeanContainer<String,GoogleLatlon> getGoogleLatlon_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,GoogleLatlon> beanContainer =new BeanContainer<String,GoogleLatlon>(GoogleLatlon.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =GoogleLatlonIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM GOOGLE_LATLON WHERE ADDR =? ";
	//取得GoogleLatlon  PrimaryKey ;
	public BeanContainer<String,GoogleLatlon> getGoogleLatlon_PKs(GoogleLatlon bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,GoogleLatlon> beanContainer =new BeanContainer<String,GoogleLatlon>(GoogleLatlon.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getAddr()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =GoogleLatlonIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM GOOGLE_LATLON WHERE ADDR =? ";
	/** 
	 * 取得GoogleLatlon  PrimaryKey 回傳 GoogleLatlon ;
	 * @param bean 
	 * @return
	 */
	public GoogleLatlon getGoogleLatlon_PK(GoogleLatlon bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,GoogleLatlon> beanContainer =new BeanContainer<String,GoogleLatlon>(GoogleLatlon.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		GoogleLatlon retbean=new GoogleLatlon();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getAddr()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =GoogleLatlonIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<GoogleLatlon> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
		dbResultSet = (new DBAction()).executeQuery(sqlstmt);
		return dbResultSet;
	}		

	//寫入GOOGLE_LATLON ;
	public boolean insertGoogleLatlon(GoogleLatlon bean){
		return insertGoogleLatlon(null,bean);
	}
	public boolean insertGoogleLatlon(Connection con ,GoogleLatlon bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO GOOGLE_LATLON( ADDR  ,LAT  ,LON  ,UPDATE_USER  ,UPDATE_DATETIME ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getAddr()));
						hashSet.add(DBUtil.setData(2,"String",bean.getLat()));
						hashSet.add(DBUtil.setData(3,"String",bean.getLon()));
						hashSet.add(DBUtil.setData(4,"String",bean.getUpdateUser()));
						hashSet.add(DBUtil.setData(5,"String",bean.getUpdateDatetime()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改GOOGLE_LATLON ;
	public boolean updateGoogleLatlon(GoogleLatlon bean){
		return updateGoogleLatlon(null,bean);
	}
	public boolean updateGoogleLatlon(Connection con ,GoogleLatlon bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE GOOGLE_LATLON set ";
				sql+=" LAT =? ";
				sql+=", LON =? ";
				sql+=", UPDATE_USER =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=" WHERE ADDR =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getLat()));
				hashSet.add(DBUtil.setData(2,"String",bean.getLon()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(5,"String",bean.getAddr()));

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
	 * 依據傳入欄位.修改GOOGLE_LATLON ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateGoogleLatlon(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE GOOGLE_LATLON set ";
				sql+=sUpdateCode;
				sql+=" WHERE ADDR =? ";
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

	//刪除GOOGLE_LATLON ;
	public boolean deleteGoogleLatlon(GoogleLatlon bean){
		return deleteGoogleLatlon(null,bean);
	}
	public boolean deleteGoogleLatlon(Connection con ,GoogleLatlon bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  GOOGLE_LATLON ";
				sql+=" WHERE ADDR =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getAddr()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private GoogleLatlon setGoogleLatlon(Item item){
		GoogleLatlon bean =new GoogleLatlon();
			bean.setAddr(item.getItemProperty("ADDR").getValue()+"");
			bean.setLat(item.getItemProperty("LAT").getValue()+"");
			bean.setLon(item.getItemProperty("LON").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");

		return bean;
	}

	private BeanContainer<String,GoogleLatlon> GoogleLatlonIndexToBean(IndexedContainer container){
		BeanContainer<String,GoogleLatlon> beanContainer =new BeanContainer<String,GoogleLatlon>(GoogleLatlon.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			GoogleLatlon bean =setGoogleLatlon(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
