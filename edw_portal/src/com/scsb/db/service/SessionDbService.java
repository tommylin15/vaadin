package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.SessionDb;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class SessionDbService{
	private static Logger logger = Logger.getLogger(SessionDbService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public SessionDbService() {
	}

	public SessionDbService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM SESSION_DB ";
	/**
 	 * 取得SessionDb資料表中,所有資料
 	 * @return BeanContainer<String,SessionDb>
 	 */	
	public BeanContainer<String,SessionDb> getSessionDb_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,SessionDb> beanContainer =new BeanContainer<String,SessionDb>(SessionDb.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =SessionDbIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM SESSION_DB WHERE SESSION_ID =? AND SESSION_USERID =? ";
	//取得SessionDb  PrimaryKey ;
	public BeanContainer<String,SessionDb> getSessionDb_PKs(SessionDb bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,SessionDb> beanContainer =new BeanContainer<String,SessionDb>(SessionDb.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getSessionId()));
				hashSet.add(DBUtil.setData(2,"String",bean.getSessionUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =SessionDbIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM SESSION_DB WHERE SESSION_ID =? AND SESSION_USERID =? ";
	/** 
	 * 取得SessionDb  PrimaryKey 回傳 SessionDb ;
	 * @param bean 
	 * @return
	 */
	public SessionDb getSessionDb_PK(SessionDb bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,SessionDb> beanContainer =new BeanContainer<String,SessionDb>(SessionDb.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		SessionDb retbean=new SessionDb();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getSessionId()));
				hashSet.add(DBUtil.setData(2,"String",bean.getSessionUserid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =SessionDbIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<SessionDb> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入SESSION_DB ;
	public boolean insertSessionDb(SessionDb bean){
		return insertSessionDb(null,bean);
	}
	public boolean insertSessionDb(Connection con ,SessionDb bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO SESSION_DB( SESSION_ID  ,SESSION_USERID  ,SESSION_IP  ,SESSION_XML  ,SESSION_DATETIME  ,SESSION_GROUPID  ,SESSION_PROGRAMID ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getSessionId()));
						hashSet.add(DBUtil.setData(2,"String",bean.getSessionUserid()));
						hashSet.add(DBUtil.setData(3,"String",bean.getSessionIp()));
						hashSet.add(DBUtil.setData(4,"Clob",bean.getSessionXml()));
						hashSet.add(DBUtil.setData(5,"String",bean.getSessionDatetime()));
						hashSet.add(DBUtil.setData(6,"String",bean.getSessionGroupid()));
						hashSet.add(DBUtil.setData(7,"String",bean.getSessionProgramid()));
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
	 * 記錄session data
	 * @param con
	 * @param bean
	 * @return
	 */
	public boolean updateData(SessionDb bean){
		return updateData(null, bean);
	}
	public boolean updateData(Connection con ,SessionDb bean){
		SessionDb querybean =this.getSessionDb_PK(bean);
		if (!querybean.getSessionUserid().equals("")) 
			return updateSessionDb(con ,bean );
		else 
			return insertSessionDb(con ,bean );
	}
	/**
	 * 修改SESSION_DB ;
	 * @param bean
	 * @return
	 */
	public boolean updateSessionDb(SessionDb bean){
		return updateSessionDb(null,bean);
	}
	public boolean updateSessionDb(Connection con ,SessionDb bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE SESSION_DB set ";
				sql+=" SESSION_IP =? ";
				sql+=", SESSION_XML =? ";
				sql+=", SESSION_DATETIME =? ";
				sql+=", SESSION_GROUPID =? ";
				sql+=", SESSION_PROGRAMID =? ";
				sql+=" WHERE SESSION_ID =? ";
				sql+=" AND SESSION_USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getSessionIp()));
				hashSet.add(DBUtil.setData(2,"Clob",bean.getSessionXml()));
				hashSet.add(DBUtil.setData(3,"String",bean.getSessionDatetime()));
				hashSet.add(DBUtil.setData(4,"String",bean.getSessionGroupid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getSessionProgramid()));
				hashSet.add(DBUtil.setData(6,"String",bean.getSessionId()));
				hashSet.add(DBUtil.setData(7,"String",bean.getSessionUserid()));

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
	 * 依據傳入欄位.修改SESSION_DB ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateSessionDb(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE SESSION_DB set ";
				sql+=sUpdateCode;
				sql+=" WHERE SESSION_ID =? ";
				sql+=" AND SESSION_USERID =? ";
				hashSet.add(DBUtil.setData(i++,"String",keysValue[0]));
				hashSet.add(DBUtil.setData(i++,"String",keysValue[1]));
				isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			} catch (RuntimeException se) {
				isUpdate=false;
				logger.error(StrUtil.convException(se)); 
				this.ErrMsg=se.toString(); 
			}
		}
		return isUpdate;
	}

	/**
	 * 刪除SESSION_DB ;
	 * @param bean
	 * @return
	 */
	public boolean deleteSessionDb(SessionDb bean){
		return deleteSessionDb(null,bean);
	}
	public boolean deleteSessionDb(Connection con ,SessionDb bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  SESSION_DB ";
				sql+=" WHERE SESSION_ID =? ";
				sql+=" AND SESSION_USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getSessionId()));
				hashSet.add(DBUtil.setData(2,"String",bean.getSessionUserid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private SessionDb setSessionDb(Item item){
		SessionDb bean =new SessionDb();
			bean.setSessionId(item.getItemProperty("SESSION_ID").getValue()+"");
			bean.setSessionUserid(item.getItemProperty("SESSION_USERID").getValue()+"");
			bean.setSessionIp(item.getItemProperty("SESSION_IP").getValue()+"");
			bean.setSessionXml(item.getItemProperty("SESSION_XML").getValue()+"");
			bean.setSessionDatetime(item.getItemProperty("SESSION_DATETIME").getValue()+"");
			bean.setSessionGroupid(item.getItemProperty("SESSION_GROUPID").getValue()+"");
			bean.setSessionProgramid(item.getItemProperty("SESSION_PROGRAMID").getValue()+"");

		return bean;
	}

	private BeanContainer<String,SessionDb> SessionDbIndexToBean(IndexedContainer container){
		BeanContainer<String,SessionDb> beanContainer =new BeanContainer<String,SessionDb>(SessionDb.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			SessionDb bean =setSessionDb(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
