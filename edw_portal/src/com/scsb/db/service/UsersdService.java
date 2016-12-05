package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Rolesd;
import com.scsb.db.bean.Transp;
import com.scsb.db.bean.Users;
import com.scsb.db.bean.Usersd;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class UsersdService{
	private static Logger logger = Logger.getLogger(UsersdService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public UsersdService() {
	}

	public UsersdService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM USERSD ";
	/**
 	 * 取得Usersd資料表中,所有資料
 	 * @return BeanContainer<String,Usersd>
 	 */	
	public BeanContainer<String,Usersd> getUsersd_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =UsersdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_ID = "SELECT * FROM USERSD where USERID=? ";
	/**
 	 * 取得Usersd資料表中,所有資料
 	 * @return BeanContainer<String,Usersd>
 	 */	
	public BeanContainer<String,Usersd> getUsersd_ID(Usersd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ID ,hashSet);	
			beanContainer =UsersdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_PK = "SELECT * FROM USERSD WHERE PROGRAMID =? AND USERID =? ";
	/**
	 * 取得Usersd  PrimaryKey ;
	 * @param bean
	 * @return
	 */
	public BeanContainer<String,Usersd> getUsersd_PKs(Usersd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =UsersdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM USERSD WHERE PROGRAMID =? AND USERID =? ";
	/** 
	 * 取得Usersd  PrimaryKey 回傳 Usersd ;
	 * @param bean 
	 * @return
	 */
	public Usersd getUsersd_PK(Usersd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Usersd retbean=new Usersd();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUserid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =UsersdIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Usersd> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private String RETRIEVE_STMT_USER_ACTION =
		//個人
		" SELECT programid ,action_code  FROM USERSD WHERE USERID =?  "+
		" 			and PROGRAMID not in (select PROGRAMID from transd where PROGRAMMODE='N')" +
		" union" +
		//角色+全行通用(A0000)
		" SELECT PROGRAMID ,ACTION_CODE   " +
		"   FROM ROLESD " +
		"	where (ROLEID in (select PROPERTY_VALUE from USERSP  " +
		"							where PROPERTY_KEY='ROLES' and USERID=? )" +
		"      								OR ROLEID ='A0000'"+
		" 	)		and PROGRAMID not in (select PROGRAMID from transd where PROGRAMMODE='N')" +
		//職稱
		" union" +
		" SELECT PROGRAMID ,ACTION_CODE   " +
		"   FROM SCSBTITLED " +
		"	where TITLE_NAME in (select TITLE_NAME from USERS  where USERID=? )"+
		" 			and PROGRAMID not in (select PROGRAMID from transd where PROGRAMMODE='N')" +
		//工作內容
		" union" +
		" SELECT PROGRAMID ,ACTION_CODE   " +
		"   FROM JOBD " +
		"	where (JOB_NAME in (select JOB1 from LDAPEMP  where USERID=? )"+
		"	   OR JOB_NAME in (select JOB2 from LDAPEMP  where USERID=? )"+
		"	   OR JOB_NAME in (select JOB3 from LDAPEMP  where USERID=? )"+
		"	   OR JOB_NAME in (select JOB4 from LDAPEMP  where USERID=? )"+
		"	   OR JOB_NAME in (select JOB5 from LDAPEMP  where USERID=? )"+
		" 		)	and PROGRAMID not in (select PROGRAMID from transd where PROGRAMMODE='N')" +
		//單位
		" union" +
		" SELECT PROGRAMID ,ACTION_CODE  " +
		"   FROM DEPTD " +
		"	where OU in (select DEPTID from USERS  where USERID=? )"+
		" 			and PROGRAMID not in (select PROGRAMID from transd where PROGRAMMODE='N')";

	public Hashtable<String ,String> getUsers_Action(String userId){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");
		
		this.ErrMsg="";
		Hashtable<String ,String> hashData =new Hashtable<String ,String>();
		try{
			UsersService usersSrv =new UsersService();
			Users users =new Users();
			users.setUserid(userId);
			String userDept =usersSrv.getUsers_PK(users).getDeptid();			
			TranspService transpSrv =new TranspService();			
			BeanContainer<String,Transp> transpContainer =transpSrv.getTransp_PKs("MASKDEPT");
			
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			hashSet.add(DBUtil.setData(1,"String",userId));
			hashSet.add(DBUtil.setData(2,"String",userId));
			hashSet.add(DBUtil.setData(3,"String",userId));
			hashSet.add(DBUtil.setData(4,"String",userId));
			hashSet.add(DBUtil.setData(5,"String",userId));
			hashSet.add(DBUtil.setData(6,"String",userId));
			hashSet.add(DBUtil.setData(7,"String",userId));
			hashSet.add(DBUtil.setData(8,"String",userId));
			hashSet.add(DBUtil.setData(9,"String",userId));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_USER_ACTION ,hashSet);			
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				Usersd bean =setUsersd(item);
				String key =bean.getProgramid();
				//加判斷若有限定部門的,不合格者要排除
				boolean IsCheck =true;
				for(int x=0;x<transpContainer.size();x++){
					Transp transpBean=transpContainer.getItem(transpContainer.getIdByIndex(x)).getBean();
					if (transpBean.getProgramid().equals(key)){
						if (!transpBean.getPropertyValue().equals(userDept)){
							IsCheck=false;
							break;
						}
					}
				}
				//再判斷若自己部門也能使用者要重新加回
				for(int x=0;x<transpContainer.size();x++){
					Transp transpBean=transpContainer.getItem(transpContainer.getIdByIndex(x)).getBean();
					if (transpBean.getProgramid().equals(key)){
						if (transpBean.getPropertyValue().equals(userDept)){
							IsCheck=true;
							break;
						}
					}
				}				
				if (IsCheck){
					if (hashData.get(key) == null)	hashData.put(key, bean.getActionCode());
					else hashData.put(key, hashData.get(key)+bean.getActionCode());
				}
			}
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString();	
		}
		return hashData;
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
	 * 寫入USERSD ;
	 * @param bean
	 * @return
	 */
	public boolean insertUsersd(Usersd bean){
		return insertUsersd(null,bean);
	}
	public boolean insertUsersd(Connection con ,Usersd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO USERSD( PROGRAMID  ,ACTION_CODE  ,USERID ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(3,"String",bean.getUserid()));
						hashSet.add(DBUtil.setData(4,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(5,"String",bean.getUpdateUser()));
						
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
	 * 整批修改USERSD ;
	 * @param userId
	 * @param groupId
	 * @param hashdata
	 * @return
	 */
	public boolean updateUsersdByUseridGroupId(String userId ,String groupId,HashSet<Usersd> hashdata ){
		return updateUsersdByUseridGroupId(null,userId,groupId,hashdata);
	}
	public boolean updateUsersdByUseridGroupId(Connection con,String userId ,String groupId ,HashSet<Usersd> hashdata ){
		boolean isUpdate =false;
		this.ErrMsg="";
		isUpdate=deleteUsersdByUseridGroupId(con, userId ,groupId);
		for(java.util.Iterator<Usersd> iter=hashdata.iterator();iter.hasNext();){
			isUpdate=insertUsersd(con, iter.next());
			if (!isUpdate) break;
		}	
		return isUpdate;
	}	
	
	/**
	 * 修改USERSD ;
	 * @param bean
	 * @return
	 */
	public boolean updateUsersd(Usersd bean){
		return updateUsersd(null,bean);
	}
	public boolean updateUsersd(Connection con ,Usersd bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE USERSD set ";
				sql+=" ACTION_CODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));				;
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getUserid()));

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
	 * 依據傳入欄位.修改USERSD ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateUsersd(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE USERSD set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND USERID =? ";
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
	 * 刪除USERD ;
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean deleteUsersdByUseridGroupId(String userId ,String groupId){
		return deleteUsersdByUseridGroupId(null,userId ,groupId);
	}
	public boolean deleteUsersdByUseridGroupId(Connection con ,String userId ,String groupId){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERSD ";
				sql+=" WHERE  USERID =? " +
						" and programid in (select programid from transd where groupid=?) ";

				hashSet.add(DBUtil.setData(1,"String",userId));
				hashSet.add(DBUtil.setData(2,"String",groupId));
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
	 * 刪除USERSD ;
	 * @param bean
	 * @return
	 */
	public boolean deleteUsersd(Usersd bean){
		return deleteUsersd(null,bean);
	}
	public boolean deleteUsersd(Connection con ,Usersd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERSD ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUserid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Usersd setUsersd(Item item){
		Usersd bean =new Usersd();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setUserid(item.getItemProperty("USERID").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Usersd> UsersdIndexToBean(IndexedContainer container){
		BeanContainer<String,Usersd> beanContainer =new BeanContainer<String,Usersd>(Usersd.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Usersd bean =setUsersd(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
