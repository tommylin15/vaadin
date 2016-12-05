package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Usersa;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class UsersaService{
	private static Logger logger = Logger.getLogger(UsersaService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public UsersaService() {
	}

	public UsersaService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM USERSA ";
	/**
 	 * 取得Usersa資料表中,所有資料
 	 * @return BeanContainer<String,Usersa>
 	 */	
	public BeanContainer<String,Usersa> getUsersa_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersa> beanContainer =new BeanContainer<String,Usersa>(Usersa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =UsersaIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM USERSA WHERE USERID =? ";
	//取得Usersa  PrimaryKey ;
	public BeanContainer<String,Usersa> getUsersa_PKs(Usersa bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersa> beanContainer =new BeanContainer<String,Usersa>(Usersa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =UsersaIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM USERSA WHERE USERID =? ";
	/** 
	 * 取得Usersa  PrimaryKey 回傳 Usersa ;
	 * @param bean 
	 * @return
	 */
	public Usersa getUsersa_PK(Usersa bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersa> beanContainer =new BeanContainer<String,Usersa>(Usersa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Usersa retbean=new Usersa();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =UsersaIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Usersa> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入USERSA ;
	public boolean insertUsersa(Usersa bean){
		return insertUsersa(null,bean);
	}
	public boolean insertUsersa(Connection con ,Usersa bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO USERSA( USERID  ,USERPWD  ,MUSTCHANGE  ,ACCOUNTMODE  ,POLICYID  ,ERRORTIMES  ,REMOTEADDR  " +
						 ",RECENTLOGINTIME  ,CREATETELLER  ,CREATEDATE  ,CREATETIME  ,BINARYPASSWORD  ,LASTLOGOUTTIME  ,REPEATLOGIN" +
						 ",UPDATE_DATETIME ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getUserPwd()));
						hashSet.add(DBUtil.setData(3,"String",bean.getMustchange()));
						hashSet.add(DBUtil.setData(4,"String",bean.getAccountmode()));
						hashSet.add(DBUtil.setData(5,"String",bean.getPolicyid()));
						hashSet.add(DBUtil.setData(6,"double",bean.getErrortimes()));
						hashSet.add(DBUtil.setData(7,"String",bean.getRemoteaddr()));
						hashSet.add(DBUtil.setData(8,"String",bean.getRecentlogintime()));
						hashSet.add(DBUtil.setData(9,"String",bean.getCreateteller()));
						hashSet.add(DBUtil.setData(10,"String",bean.getCreatedate()));
						hashSet.add(DBUtil.setData(11,"String",bean.getCreatetime()));
						hashSet.add(DBUtil.setData(12,"String",bean.getBinarypassword()));
						hashSet.add(DBUtil.setData(13,"String",bean.getLastlogouttime()));
						hashSet.add(DBUtil.setData(14,"String",bean.getRepeatlogin()));
						hashSet.add(DBUtil.setData(15,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(16,"String",bean.getUpdateUser()));
						
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改USERSA ;
	public boolean updateUsersa(Usersa bean){
		return updateUsersa(null,bean);
	}
	public boolean updateUsersa(Connection con ,Usersa bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE USERSA set ";
				sql+=" USERPWD =? ";
				sql+=", MUSTCHANGE =? ";
				sql+=", ACCOUNTMODE =? ";
				sql+=", POLICYID =? ";
				sql+=", ERRORTIMES =? ";
				sql+=", REMOTEADDR =? ";
				sql+=", RECENTLOGINTIME =? ";
				sql+=", CREATETELLER =? ";
				sql+=", CREATEDATE =? ";
				sql+=", CREATETIME =? ";
				sql+=", BINARYPASSWORD =? ";
				sql+=", LASTLOGOUTTIME =? ";
				sql+=", REPEATLOGIN =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserPwd()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMustchange()));
				hashSet.add(DBUtil.setData(3,"String",bean.getAccountmode()));
				hashSet.add(DBUtil.setData(4,"String",bean.getPolicyid()));
				hashSet.add(DBUtil.setData(5,"double",bean.getErrortimes()));
				hashSet.add(DBUtil.setData(6,"String",bean.getRemoteaddr()));
				hashSet.add(DBUtil.setData(7,"String",bean.getRecentlogintime()));
				hashSet.add(DBUtil.setData(8,"String",bean.getCreateteller()));
				hashSet.add(DBUtil.setData(9,"String",bean.getCreatedate()));
				hashSet.add(DBUtil.setData(10,"String",bean.getCreatetime()));
				hashSet.add(DBUtil.setData(11,"String",bean.getBinarypassword()));
				hashSet.add(DBUtil.setData(12,"String",bean.getLastlogouttime()));
				hashSet.add(DBUtil.setData(13,"String",bean.getRepeatlogin()));
				hashSet.add(DBUtil.setData(14,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(15,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(16,"String",bean.getUserid()));

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
	 * 依據傳入欄位.修改USERSA ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateUsersa(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE USERSA set ";
				sql+=sUpdateCode;
				sql+=" WHERE USERID =? ";
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

	//刪除USERSA ;
	public boolean deleteUsersa(Usersa bean){
		return deleteUsersa(null,bean);
	}
	public boolean deleteUsersa(Connection con ,Usersa bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERSA ";
				sql+=" WHERE USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Usersa setUsersa(Item item){
		Usersa bean =new Usersa();
			bean.setUserid(item.getItemProperty("USERID").getValue()+"");
			bean.setUserPwd(item.getItemProperty("USERPWD").getValue()+"");
			bean.setMustchange(item.getItemProperty("MUSTCHANGE").getValue()+"");
			bean.setAccountmode(item.getItemProperty("ACCOUNTMODE").getValue()+"");
			bean.setPolicyid(item.getItemProperty("POLICYID").getValue()+"");
			double ierrortimes =0;
			try{
				ierrortimes =Double.parseDouble(item.getItemProperty("ERRORTIMES").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setErrortimes(ierrortimes);
			bean.setRemoteaddr(item.getItemProperty("REMOTEADDR").getValue()+"");
			bean.setRecentlogintime(item.getItemProperty("RECENTLOGINTIME").getValue()+"");
			bean.setCreateteller(item.getItemProperty("CREATETELLER").getValue()+"");
			bean.setCreatedate(item.getItemProperty("CREATEDATE").getValue()+"");
			bean.setCreatetime(item.getItemProperty("CREATETIME").getValue()+"");
			bean.setBinarypassword(item.getItemProperty("BINARYPASSWORD").getValue()+"");
			bean.setLastlogouttime(item.getItemProperty("LASTLOGOUTTIME").getValue()+"");
			bean.setRepeatlogin(item.getItemProperty("REPEATLOGIN").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Usersa> UsersaIndexToBean(IndexedContainer container){
		BeanContainer<String,Usersa> beanContainer =new BeanContainer<String,Usersa>(Usersa.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Usersa bean =setUsersa(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
