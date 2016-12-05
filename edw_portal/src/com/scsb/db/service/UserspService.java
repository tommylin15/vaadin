package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Dept;
import com.scsb.db.bean.Usersp;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class UserspService{
	private static Logger logger = Logger.getLogger(UserspService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public UserspService() {
	}

	public UserspService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM USERSP ";
	/**
 	 * 取得Usersp資料表中,所有資料
 	 * @return BeanContainer<String,Usersp>
 	 */	
	public BeanContainer<String,Usersp> getUsersp_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersp> beanContainer =new BeanContainer<String,Usersp>(Usersp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =UserspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_ALL_PROPERTY = ""
			/*個人屬性*/
			+ " 	select   PROPERTY_KEY ,PROPERTY_VALUE "
			+ "		from USERSP "
			+ "		where USERID =? "
			+ " union all "
			  /*個人所屬分行*/
			+ "	select  'ALLOW_BRH' as PROPERTY_KEY ,substr(deptid,1,2) as PROPERTY_VALUE	"
			+ "		from	users "
			+ "		where userid=? "
			/*所屬部門屬性*/
			+ " union all "
			+ "  select a.PROPERTY_KEY ,a.PROPERTY_VALUE	"
			+ "		from DEPT a  ,USERS b"
			+ "		where a.OU=b.DEPTID and b.USERID=? "
			/*所屬角色屬性*/
			+ " union all "
			+ " select a.PROPERTY_KEY ,a.PROPERTY_VALUE	"
			+ "		from ROLESP a ,USERSP b"
			+ "		where a.ROLESID=b.PROPERTY_VALUE  and b.PROPERTY_KEY='ROLES'	 and b.USERID =? "
			/*所屬職稱屬性*/
			+ " union all "
			+ " select  a.PROPERTY_KEY ,a.PROPERTY_VALUE	"
			+ "	from SCSBTITLEP a ,USERS b "
			+ "	where a.TITLE_NAME=b.TITLE_NAME and b.USERID =? "
				/*所屬工作內容屬性*/
			+ " union all "
			+ " select a.PROPERTY_KEY ,a.PROPERTY_VALUE	"
			+ "	FROM JOBP a"
			+ "	where (JOB_NAME in (select JOB1 from LDAPEMP  where USERID=?  ) "
			+ "   OR JOB_NAME in (select JOB2 from LDAPEMP  where USERID=?  ) "
			+ "   OR JOB_NAME in (select JOB3 from LDAPEMP  where USERID=?  ) "
			+ "    OR JOB_NAME in (select JOB4 from LDAPEMP  where USERID=?  ) "
			+ "    OR JOB_NAME in (select JOB5 from LDAPEMP  where USERID=?  ) "	    					    					    
			+ "   ) ";
	/**
	 * 取得Usersp  Property ;
	 * @param bean
	 * @return
	 */
	public HashSet<String> getUsers_Property(String users_id ,String propertyType){
		IndexedContainer container = new IndexedContainer();
		HashSet<String> hashProperty =new HashSet<String>();

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",users_id));
				hashSet.add(DBUtil.setData(2,"String",users_id));
				hashSet.add(DBUtil.setData(3,"String",users_id));
				hashSet.add(DBUtil.setData(4,"String",users_id));
				hashSet.add(DBUtil.setData(5,"String",users_id));
				hashSet.add(DBUtil.setData(6,"String",users_id));
				hashSet.add(DBUtil.setData(7,"String",users_id));
				hashSet.add(DBUtil.setData(8,"String",users_id));
				hashSet.add(DBUtil.setData(9,"String",users_id));
				hashSet.add(DBUtil.setData(10,"String",users_id));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL_PROPERTY ,hashSet);

			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				String propKey =item.getItemProperty("PROPERTY_KEY").getValue().toString();
				String propValue =item.getItemProperty("PROPERTY_VALUE").getValue().toString();
				if (propKey.equals(propertyType)){
					if (!hashProperty.equals(propValue)){
						hashProperty.add(propValue);
					}
				}
			}//for
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
			re.printStackTrace();
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
			se.printStackTrace();
		}
		return hashProperty;
	}
	
	private String RETRIEVE_STMT_PK = "SELECT * FROM USERSP WHERE USERID =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/**
	 * 取得Usersp  PrimaryKey ;
	 * @param bean
	 * @return
	 */
	public BeanContainer<String,Usersp> getUsersp_PKs(Usersp bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersp> beanContainer =new BeanContainer<String,Usersp>(Usersp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =UserspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	private String RETRIEVE_STMT_USERID = "SELECT * FROM USERSP WHERE USERID =? and Property_Key=? ";
	/**
	 * 取得Usersp  BeanContainer ;
	 * @param userId
	 * @param propertyKey
	 * @return
	 */
	public BeanContainer<String,Usersp> getUsersp_UserIdProperty(String userId ,String propertyKey){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersp> beanContainer =new BeanContainer<String,Usersp>(Usersp.class);
		beanContainer.setBeanIdProperty("beanKey");
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",userId));
				hashSet.add(DBUtil.setData(2,"String",propertyKey));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_USERID ,hashSet);	
			beanContainer =UserspIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	private String RETRIEVE_STMT_PK2 = "SELECT * FROM USERSP WHERE USERID =? AND PROPERTY_KEY =? AND PROPERTY_VALUE =? ";
	/** 
	 * 取得Usersp  PrimaryKey 回傳 Usersp ;
	 * @param bean 
	 * @return
	 */
	public Usersp getUsersp_PK(Usersp bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersp> beanContainer =new BeanContainer<String,Usersp>(Usersp.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Usersp retbean=new Usersp();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
				hashSet.add(DBUtil.setData(3,"String",bean.getPropertyValue()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =UserspIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Usersp> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
	 * 寫入USERSP ;
	 * @param bean
	 * @return
	 */
	public boolean insertUsersp(Usersp bean){
		return insertUsersp(null,bean);
	}
	public boolean insertUsersp(Connection con ,Usersp bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO USERSP( USERID  ,PROPERTY_KEY  ,PROPERTY_VALUE  ,PROPERTY_MEMO ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
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

	/**
	 * 修改usersp依照property模式(不同分行可重複)
	 * @param bean
	 * @return
	 */
	public boolean updateUserspPropertys(Connection con ,Usersp bean ){
		boolean isUpdate =false;
		isUpdate=insertUsersp(con ,bean );
		return isUpdate;
	}	

	/**
	 * 修改USERSP ;
	 * @param bean
	 * @return
	 */
	public boolean updateUsersp(Usersp bean){
		return updateUsersp(null,bean);
	}
	public boolean updateUsersp(Connection con ,Usersp bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE USERSP set ";
				sql+=" PROPERTY_MEMO =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE USERID =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getPropertyMemo()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getUserid()));
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
	 * 
	 * @param bean
	 * @return
	 */
	public boolean deleteUserspkey(Usersp bean){
		return deleteUsersp(null,bean);
	}
	public boolean deleteUserspkey(Connection con ,Usersp bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERSP ";
				sql+=" WHERE USERID =? ";
				sql+=" AND PROPERTY_KEY =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getPropertyKey()));
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
	 * 刪除USERSP ;
	 * @param bean
	 * @return
	 */
	public boolean deleteUsersp(Usersp bean){
		return deleteUsersp(null,bean);
	}
	public boolean deleteUsersp(Connection con ,Usersp bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERSP ";
				sql+=" WHERE USERID =? ";
				sql+=" AND PROPERTY_KEY =? ";
				sql+=" AND PROPERTY_VALUE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
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

	private Usersp setUsersp(Item item){
		Usersp bean =new Usersp();
			bean.setUserid(item.getItemProperty("USERID").getValue()+"");
			bean.setPropertyKey(item.getItemProperty("PROPERTY_KEY").getValue()+"");
			bean.setPropertyValue(item.getItemProperty("PROPERTY_VALUE").getValue()+"");
			bean.setPropertyMemo(item.getItemProperty("PROPERTY_MEMO").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");
		return bean;
	}

	private BeanContainer<String,Usersp> UserspIndexToBean(IndexedContainer container){
		BeanContainer<String,Usersp> beanContainer =new BeanContainer<String,Usersp>(Usersp.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Usersp bean =setUsersp(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
