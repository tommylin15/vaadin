package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Dept;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class DeptService{
	private static Logger logger = Logger.getLogger(DeptService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public DeptService() {
	}

	public DeptService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM DEPT ";
	/**
 	 * 取得DEPT資料表中,所有資料
 	 * @return BeanContainer<String,DEPT>
 	 */	
	public BeanContainer<String,Dept> getDept_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =DEPTIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM DEPT WHERE ou =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/**
	 * 取得DEPT  PrimaryKey ;
	 * @param bean
	 * @return
	 */
	public BeanContainer<String,Dept> getDept_PKs(Dept bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =DEPTIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_PROPERTYKEY = "SELECT * FROM DEPT WHERE Property_Key=? and Property_Value=? ";
	/**
	 * 取得DEPT  BeanContainer ;
	 * @param propertyKey
	 * @param propertyValue
	 * @return
	 */
	public BeanContainer<String,Dept> getOu(String propertyKey ,String propertyValue){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",propertyKey));
				hashSet.add(DBUtil.setData(2,"String",propertyValue));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PROPERTYKEY ,hashSet);	
			beanContainer =DEPTIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_Ou = "SELECT * FROM DEPT WHERE ou =? and Property_Key=? ";
	/**
	 * 取得DEPT  BeanContainer ;
	 * @param Ou
	 * @param propertyKey
	 * @return
	 */
	public BeanContainer<String,Dept> getDept_OuProperty(String Ou ,String propertyKey){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",Ou));
				hashSet.add(DBUtil.setData(2,"String",propertyKey));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_Ou ,hashSet);	
			beanContainer =DEPTIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_VALUE = "SELECT * FROM DEPT WHERE PROPERTY_VALUE =? and Property_Key=? ";
	/**
	 * 取得DEPT  BeanContainer ;
	 * @param propertyValue
	 * @param propertyKey
	 * @return
	 */
	public BeanContainer<String,Dept> getDept_PropertyValue(String propertyValue ,String propertyKey){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",propertyValue));
				hashSet.add(DBUtil.setData(2,"String",propertyKey));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_VALUE ,hashSet);	
			beanContainer =DEPTIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM DEPT WHERE ou =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/** 
	 * 取得DEPT  PrimaryKey 回傳 DEPT ;
	 * @param bean 
	 * @return
	 */
	public Dept getDEPT_PK(Dept bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Dept retbean=new Dept();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =DEPTIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Dept> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	/**
	 * 寫入DEPT 
	 */
	public boolean insertDept(Dept bean){
		return insertDept(null,bean);
	}
	
	public boolean insertDept(Connection con,Dept bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO DEPT( ou  ,PROPERTY_KEY  ,PROPERTY_VALUE  ,PROPERTY_MEMO ,UPDATE_DATETIME ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
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

	//修改DEPT ;
	public boolean updateDept(Dept bean){
		return updateDept(null,bean);
	}
	
	public boolean updateDept(Connection con ,Dept bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE DEPT set ";
				sql+=" PROPERTY_MEMO =? ";
				sql+=" , UPDATE_DATETIME =? ";
				sql+=" , UPDATE_USER =? ";				
				sql+=" WHERE ou =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getPropertyMemo()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));				
				hashSet.add(DBUtil.setData(4,"String",bean.getOu()));
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
	 * 修改DEPT依照property模式(不同分行不可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateDeptProperty(Connection con ,Dept bean ){
		boolean isUpdate =false;
		//不同部門不可重複,所以要先刪除
		deleteDeptByOuKey(con ,bean.getOu(),bean.getPropertyKey());
		isUpdate=insertDept(con ,bean );
		return isUpdate;
	}	
	
	/**
	 * 修改DEPT依照property模式(不同分行可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateDeptPropertys(Connection con ,Dept bean ){
		boolean isUpdate =false;
		isUpdate=insertDept(con ,bean );
		return isUpdate;
	}	

	/**
	 * 依據傳入欄位.修改DEPT ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateDept(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE DEPT set ";
				sql+=sUpdateCode;
				sql+=" WHERE OU =? ";
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
	 * 刪除DEPT ;
	 * @param con
	 * @param Ou
	 * @param key
	 * @return
	 */
	public boolean deleteDeptByOuKey(Connection con ,String Ou ,String key){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPT ";
				sql+=" WHERE  ou =? ";
				sql+="   and  PROPERTY_KEY =? ";

				hashSet.add(DBUtil.setData(1,"String",Ou));
				hashSet.add(DBUtil.setData(2,"String",key));
				if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
				else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}	
	/**
	 * 刪除DEPT ;
	 * @param con
	 * @param propertyValue
	 * @param key
	 * @return
	 */
	public boolean deleteDeptByValue(Connection con ,String propertyValue ,String key){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPT ";
				sql+=" WHERE  property_Value =? ";
				sql+="   and  PROPERTY_KEY =? ";

				hashSet.add(DBUtil.setData(1,"String",propertyValue));
				hashSet.add(DBUtil.setData(2,"String",key));
				if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
				else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}		
	/**
	 * 刪除DEPT ;
	 * @param con
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean deleteDeptByKeyValue(Connection con ,String key ,String value){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPT ";
				sql+=" WHERE  PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",key));
				hashSet.add(DBUtil.setData(2,"String",value));
				if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
				else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}
	
	/**
	 * 刪除DEPT ;
	 * @param bean
	 * @return
	 */
	public boolean deleteDept(Dept bean){
		return deleteDept(null,bean);
	}
	
	public boolean deleteDept(Connection con ,Dept bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPT ";
				sql+=" WHERE OU =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
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

	private Dept setDEPT(Item item){
		Dept bean =new Dept();
			bean.setOu(item.getItemProperty("OU").getValue()+"");
			bean.setPropertyKey(item.getItemProperty("PROPERTY_KEY").getValue()+"");
			bean.setPropertyValue(item.getItemProperty("PROPERTY_VALUE").getValue()+"");
			bean.setPropertyMemo(item.getItemProperty("PROPERTY_MEMO").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Dept> DEPTIndexToBean(IndexedContainer container){
		BeanContainer<String,Dept> beanContainer =new BeanContainer<String,Dept>(Dept.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Dept bean =setDEPT(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
