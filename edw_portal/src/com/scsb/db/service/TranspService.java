package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Branch;
import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Transp;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class TranspService{
	private static Logger logger = Logger.getLogger(TranspService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public TranspService() {
	}

	public TranspService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM TRANSP ";
	/**
 	 * 取得Transp資料表中,所有資料
 	 * @return BeanContainer<String,Transp>
 	 */	
	public BeanContainer<String,Transp> getTransp_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =TranspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM TRANSP WHERE PROGRAMID =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	//取得Transp  PrimaryKey ;
	public BeanContainer<String,Transp> getTransp_PKs(Transp bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TranspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	//取得Transp  PrimaryKey ;
	public Transp getTransp_PKb(Transp bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");
		Transp retbean=new Transp();
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TranspIndexToBean(container);
			if (beanContainer.size()>0){
				BeanItem<Transp> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
				retbean = beanitem.getBean();
			}			
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return retbean;
	}	

	private String RETRIEVE_STMT_PKy = "SELECT * FROM TRANSP WHERE PROPERTY_KEY =?  ";
	/**
	 * 取得Transp  for PROPERTY_KEY ;
	 *
	*/
	public BeanContainer<String,Transp> getTransp_PKs(String property_key){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",property_key));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PKy ,hashSet);	
			beanContainer =TranspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	private String RETRIEVE_STMT_PKx = "SELECT * FROM TRANSP WHERE PROGRAMID =? AND PROPERTY_KEY =?  ";
	/**
	 * 取得Transp  for PROGRAMID ;
	 *
	*/
	public BeanContainer<String,Transp> getTransp_PKs(String programid ,String property_key){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",programid));
				hashSet.add(DBUtil.setData(2,"String",property_key));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PKx ,hashSet);	
			beanContainer =TranspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	private String RETRIEVE_STMT_PK2 = "SELECT * FROM TRANSP WHERE PROGRAMID =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/** 
	 * 取得Transp  PrimaryKey 回傳 Transp ;
	 * @param bean 
	 * @return
	 */
	public Transp getTransp_PK(Transp bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Transp retbean=new Transp();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =TranspIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Transp> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入TRANSP ;
	public boolean insertTransp(Transp bean){
		return insertTransp(null,bean);
	}
	public boolean insertTransp(Connection con ,Transp bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO TRANSP( PROGRAMID  ,PROPERTY_KEY  ,PROPERTY_VALUE  ,PROPERTY_MEMO ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
						hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
						hashSet.add(DBUtil.setData(4,"String",bean.getPropertyMemo()));
						hashSet.add(DBUtil.setData(5,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(6,"String",bean.getUpdateUser()));						
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改TRANSP ;
	public boolean updateTransp(Transp bean){
		return updateTransp(null,bean);
	}
	public boolean updateTransp(Connection con ,Transp bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE TRANSP set ";
				sql+=" PROPERTY_MEMO =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getPropertyMemo()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(6,"String",bean.getPropertyValue()));

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
	 * 修改Transp依照property模式(不同交易不可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateProperty(Connection con ,Transp bean ){
		boolean isUpdate =false;
		//不同交易不可重複,所以要先刪除(在外部已刪除)
		//deleteByKeyValue(con ,bean.getPropertyKey(),bean.getProgramid());
		isUpdate=insertTransp(con ,bean );
		return isUpdate;
	}	
	
	/**
	 * 修改Transp依照property模式(不同交易可重複)
	 * @param bean
	 * @return
	 */
	public boolean updatePropertys(Connection con ,Transp bean ){
		boolean isUpdate =false;
		isUpdate=insertTransp(con ,bean );
		return isUpdate;
	}		
	/**
	 * 依據傳入欄位.修改TRANSP ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateTransp(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE TRANSP set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";
				hashSet.add(DBUtil.setData(i++,"String",keysValue[0]));
				hashSet.add(DBUtil.setData(i++,"String",keysValue[1]));
				hashSet.add(DBUtil.setData(i++,"String",keysValue[2]));
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
	 * 刪除Transp ;
	 * @param con
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean deleteByKeyValue(Connection con ,String key ,String id){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  Transp ";
				sql+=" WHERE  PROPERTY_KEY =? ";
				sql+=" AND PROGRAMID =? ";
				hashSet.add(DBUtil.setData(1,"String",key));
				hashSet.add(DBUtil.setData(2,"String",id));
				if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
				else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}	
	
	//刪除TRANSP ;
	public boolean deleteTransp(Transp bean){
		return deleteTransp(null,bean);
	}
	public boolean deleteTransp(Connection con ,Transp bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  TRANSP ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Transp setTransp(Item item){
		Transp bean =new Transp();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setPropertyKey(item.getItemProperty("PROPERTY_KEY").getValue()+"");
			bean.setPropertyValue(item.getItemProperty("PROPERTY_VALUE").getValue()+"");
			bean.setPropertyMemo(item.getItemProperty("PROPERTY_MEMO").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Transp> TranspIndexToBean(IndexedContainer container){
		BeanContainer<String,Transp> beanContainer =new BeanContainer<String,Transp>(Transp.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Transp bean =setTransp(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
