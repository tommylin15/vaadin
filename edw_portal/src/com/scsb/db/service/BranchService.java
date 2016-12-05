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
import com.scsb.db.bean.Dept;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class BranchService{
	private static Logger logger = Logger.getLogger(BranchService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public BranchService() {
	}

	public BranchService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM Branch ";
	/**
 	 * 取得Branch資料表中,所有資料
 	 * @return BeanContainer<String,Branch>
 	 */	
	public BeanContainer<String,Branch> getBranch_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =BranchIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM Branch WHERE brh_cod =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/**
	 * 取得Branch  PrimaryKey ;
	 * @param bean
	 * @return
	 */
	public BeanContainer<String,Branch> getBranch_PKs(Branch bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =BranchIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_PROPERTYKEY = "SELECT * FROM Branch WHERE Property_Key=? and Property_Value=? ";
	/**
	 * 取得Branch  BeanContainer ;
	 * @param propertyKey
	 * @param propertyValue
	 * @return
	 */
	public BeanContainer<String,Branch> getbrhCod(String propertyKey ,String propertyValue){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",propertyKey));
				hashSet.add(DBUtil.setData(2,"String",propertyValue));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PROPERTYKEY ,hashSet);	
			beanContainer =BranchIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_brhCod = "SELECT * FROM Branch WHERE brh_cod =? and Property_Key=? ";
	/**
	 * 取得Branch  BeanContainer ;
	 * @param brhCod
	 * @param propertyKey
	 * @return
	 */
	public BeanContainer<String,Branch> getBranch_brhCodProperty(String brhCod ,String propertyKey){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",brhCod));
				hashSet.add(DBUtil.setData(2,"String",propertyKey));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_brhCod ,hashSet);	
			beanContainer =BranchIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM Branch WHERE brh_cod =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/** 
	 * 取得Branch  PrimaryKey 回傳 Branch ;
	 * @param bean 
	 * @return
	 */
	public Branch getBranch_PK(Branch bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Branch retbean=new Branch();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =BranchIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Branch> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
	 * 寫入Branch 
	 */
	public boolean insertBranch(Branch bean){
		return insertBranch(null,bean);
	}
	
	public boolean insertBranch(Connection con,Branch bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO Branch( brh_cod  ,PROPERTY_KEY  ,PROPERTY_VALUE  ,PROPERTY_MEMO" +
						 " ,UPDATE_DATETIME ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
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

	//修改Branch ;
	public boolean updateBranch(Branch bean){
		return updateBranch(null,bean);
	}
	
	public boolean updateBranch(Connection con ,Branch bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE Branch set ";
				sql+=" PROPERTY_MEMO =? ";
				sql+=" , UPDATE_DATETIME =? ";
				sql+=" , UPDATE_USER =? ";
				sql+=" WHERE brh_cod =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";
				hashSet.add(DBUtil.setData(1,"String",bean.getPropertyMemo()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getBrhCod()));
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
	 * 修改Branch依照property模式(不同分行不可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateBranchProperty(Connection con ,Branch bean ){
		boolean isUpdate =false;
		//不同部門不可重複,所以要先刪除
		deleteBranchBybrhCodKey(con ,bean.getBrhCod(),bean.getPropertyKey());
		isUpdate=insertBranch(con ,bean );
		return isUpdate;
	}	
	
	/**
	 * 修改Branch依照property模式(不同分行可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateBranchPropertys(Connection con ,Branch bean ){
		boolean isUpdate =false;
		isUpdate=insertBranch(con ,bean );
		return isUpdate;
	}	

	/**
	 * 依據傳入欄位.修改Branch ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateBranch(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE Branch set ";
				sql+=sUpdateCode;
				sql+=" WHERE brh_cod =? ";
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
	 * 刪除Branch ;
	 * @param con
	 * @param brhCod
	 * @param key
	 * @return
	 */
	public boolean deleteBranchBybrhCodKey(Connection con ,String brhCod ,String key){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  Branch ";
				sql+=" WHERE  brh_cod =? ";
				sql+="   and  PROPERTY_KEY =? ";

				hashSet.add(DBUtil.setData(1,"String",brhCod));
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
	 * 刪除Branch ;
	 * @param con
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean deleteBranchByKeyValue(Connection con ,String key ,String value){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  Branch ";
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
	 * 刪除Branch ;
	 * @param bean
	 * @return
	 */
	public boolean deleteBranch(Branch bean){
		return deleteBranch(null,bean);
	}
	
	public boolean deleteBranch(Connection con ,Branch bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  Branch ";
				sql+=" WHERE brh_cod =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getBrhCod()));
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

	private Branch setBranch(Item item){
		Branch bean =new Branch();
			bean.setBrhCod(item.getItemProperty("BRH_COD").getValue()+"");
			bean.setPropertyKey(item.getItemProperty("PROPERTY_KEY").getValue()+"");
			bean.setPropertyValue(item.getItemProperty("PROPERTY_VALUE").getValue()+"");
			bean.setPropertyMemo(item.getItemProperty("PROPERTY_MEMO").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Branch> BranchIndexToBean(IndexedContainer container){
		BeanContainer<String,Branch> beanContainer =new BeanContainer<String,Branch>(Branch.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Branch bean =setBranch(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
