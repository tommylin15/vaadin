package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Roles;
import com.scsb.db.bean.Rolesd;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.scsb.vaadin.custfield.OptionButtonField;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class RolesService{
	private static Logger logger = Logger.getLogger(RolesService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public RolesService() {
	}

	public RolesService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM ROLES ";
	/**
 	 * 取得Roles資料表中,所有資料
 	 * @return BeanContainer<String,Roles>
 	 */	
	public BeanContainer<String,Roles> getRoles_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Roles> beanContainer =new BeanContainer<String,Roles>(Roles.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =RolesIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM ROLES WHERE ROLEID =? ";
	//取得Roles  PrimaryKey ;
	public BeanContainer<String,Roles> getRoles_PKs(Roles bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Roles> beanContainer =new BeanContainer<String,Roles>(Roles.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getRoleid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =RolesIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ROLES WHERE ROLEID =? ";
	/** 
	 * 取得Roles  PrimaryKey 回傳 Roles ;
	 * @param bean 
	 * @return
	 */
	public Roles getRoles_PK(Roles bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Roles> beanContainer =new BeanContainer<String,Roles>(Roles.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Roles retbean=new Roles();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getRoleid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =RolesIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Roles> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入ROLES ;
	public boolean insertRoles(Roles bean){
		return insertRoles(null,bean);
	}
	public boolean insertRoles(Connection con ,Roles bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO ROLES( ROLEID  ,ROLENAME  ,LOGINPROGRAMID  ,LOGINDEFAULTLANGUAGE  ,CREATETELLER  ,CREATEDATE  ,CREATETIME  ,UPDATE_DATETIME ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getRoleid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getRolename()));
						hashSet.add(DBUtil.setData(3,"String",bean.getLoginprogramid()));
						hashSet.add(DBUtil.setData(4,"String",bean.getLogindefaultlanguage()));
						hashSet.add(DBUtil.setData(5,"String",bean.getCreateteller()));
						hashSet.add(DBUtil.setData(6,"String",bean.getCreatedate()));
						hashSet.add(DBUtil.setData(7,"String",bean.getCreatetime()));
						hashSet.add(DBUtil.setData(8,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(9,"String",bean.getUpdateUser()));
						
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改ROLES ;
	public boolean updateRoles(Roles bean ){
		return updateRoles(null,bean );
	}
	public boolean updateRoles(Connection con ,Roles bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE ROLES set ";
				sql+=" ROLENAME =? ";
				sql+=", LOGINPROGRAMID =? ";
				sql+=", LOGINDEFAULTLANGUAGE =? ";
				sql+=", CREATETELLER =? ";
				sql+=", CREATEDATE =? ";
				sql+=", CREATETIME =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE ROLEID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getRolename()));
				hashSet.add(DBUtil.setData(2,"String",bean.getLoginprogramid()));
				hashSet.add(DBUtil.setData(3,"String",bean.getLogindefaultlanguage()));
				hashSet.add(DBUtil.setData(4,"String",bean.getCreateteller()));
				hashSet.add(DBUtil.setData(5,"String",bean.getCreatedate()));
				hashSet.add(DBUtil.setData(6,"String",bean.getCreatetime()));
				hashSet.add(DBUtil.setData(7,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(8,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(9,"String",bean.getRoleid()));

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
	 * 依據傳入欄位.修改ROLES ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateRoles(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE ROLES set ";
				sql+=sUpdateCode;
				sql+=" WHERE ROLEID =? ";
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

	//刪除ROLES ;
	public boolean deleteRoles(Roles bean){
		return deleteRoles(null,bean);
	}
	public boolean deleteRoles(Connection con ,Roles bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			//要先清除明細
			RolesdService rolesdSrv =new RolesdService();
			Rolesd rolesdBean=new Rolesd();
			rolesdBean.setRoleid(bean.getRoleid());
			rolesdSrv.deleteRolesd(con ,rolesdBean);
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ROLES ";
				sql+=" WHERE ROLEID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getRoleid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Roles setRoles(Item item){
		Roles bean =new Roles();
			bean.setRoleid(item.getItemProperty("ROLEID").getValue()+"");
			bean.setRolename(item.getItemProperty("ROLENAME").getValue()+"");
			bean.setLoginprogramid(item.getItemProperty("LOGINPROGRAMID").getValue()+"");
			bean.setLogindefaultlanguage(item.getItemProperty("LOGINDEFAULTLANGUAGE").getValue()+"");
			bean.setCreateteller(item.getItemProperty("CREATETELLER").getValue()+"");
			bean.setCreatedate(item.getItemProperty("CREATEDATE").getValue()+"");
			bean.setCreatetime(item.getItemProperty("CREATETIME").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Roles> RolesIndexToBean(IndexedContainer container){
		BeanContainer<String,Roles> beanContainer =new BeanContainer<String,Roles>(Roles.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Roles bean =setRoles(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
