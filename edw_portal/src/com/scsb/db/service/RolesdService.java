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
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class RolesdService{
	private static Logger logger = Logger.getLogger(RolesdService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public RolesdService() {
	}

	public RolesdService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM ROLESD ";
	/**
 	 * 取得Rolesd資料表中,所有資料
 	 * @return BeanContainer<String,Rolesd>
 	 */	
	public BeanContainer<String,Rolesd> getRolesd_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Rolesd> beanContainer =new BeanContainer<String,Rolesd>(Rolesd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =RolesdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_ROLEID = "SELECT * FROM ROLESD WHERE ROLEID =? ";
	//取得Rolesd  PrimaryKey ;
	public BeanContainer<String,Rolesd> getRolesd_id(Rolesd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Rolesd> beanContainer =new BeanContainer<String,Rolesd>(Rolesd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getRoleid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ROLEID ,hashSet);	
			beanContainer =RolesdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_PK = "SELECT * FROM ROLESD WHERE PROGRAMID =? AND ROLEID =? ";
	//取得Rolesd  PrimaryKey ;
	public BeanContainer<String,Rolesd> getRolesd_PKs(Rolesd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Rolesd> beanContainer =new BeanContainer<String,Rolesd>(Rolesd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getRoleid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =RolesdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ROLESD WHERE PROGRAMID =? AND ROLEID =? ";
	/** 
	 * 取得Rolesd  PrimaryKey 回傳 Rolesd ;
	 * @param bean 
	 * @return
	 */
	public Rolesd getRolesd_PK(Rolesd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Rolesd> beanContainer =new BeanContainer<String,Rolesd>(Rolesd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Rolesd retbean=new Rolesd();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getRoleid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =RolesdIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Rolesd> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
	 * 寫入ROLESD ;
	 * @param bean
	 * @return
	 */
	public boolean insertRolesd(Rolesd bean){
		return insertRolesd(null,bean);
	}
	public boolean insertRolesd(Connection con ,Rolesd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO ROLESD( PROGRAMID  ,ACTION_CODE  ,ROLEID ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(3,"String",bean.getRoleid()));
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
	 * 整批修改ROLESD ;
	 * @param roleId
	 * @param groupId
	 * @param hashdata
	 * @return
	 */
	public boolean updateRolesdByRoleidGroupId(String roleId ,String groupId,HashSet<Rolesd> hashdata ){
		return updateRolesdByRoleidGroupId(null,roleId,groupId,hashdata);
	}
	public boolean updateRolesdByRoleidGroupId(Connection con,String roleId ,String groupId ,HashSet<Rolesd> hashdata ){
		boolean isUpdate =false;
		this.ErrMsg="";
		isUpdate=deleteRolesdByRoleidGroupId(con, roleId ,groupId);
		for(java.util.Iterator<Rolesd> iter=hashdata.iterator();iter.hasNext();){
			isUpdate=insertRolesd(con, iter.next());
			//break;
			if (!isUpdate) break;
		}	
		
		return isUpdate;
	}	
	
	/**
	 * 修改ROLESD ;
	 * @param bean
	 * @return
	 */
	public boolean updateRolesd(Rolesd bean){
		return updateRolesd(null,bean);
	}
	public boolean updateRolesd(Connection con ,Rolesd bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE ROLESD set ";
				sql+=" ACTION_CODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND ROLEID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getRoleid()));

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
	 * 依據傳入欄位.修改ROLESD ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateRolesd(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE ROLESD set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND ROLEID =? ";
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
	 * 刪除ROLESD ;
	 * @param rolesId
	 * @param groupId
	 * @return
	 */
	public boolean deleteRolesdByRoleidGroupId(String rolesId ,String groupId){
		return deleteRolesdByRoleidGroupId(null,rolesId ,groupId);
	}
	public boolean deleteRolesdByRoleidGroupId(Connection con ,String rolesId ,String groupId){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ROLESD ";
				sql+=" WHERE  ROLEID =? " +
						" and programid in (select programid from transd where groupid=?) ";

				hashSet.add(DBUtil.setData(1,"String",rolesId));
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
	 * 刪除BY PROGRAMID
	 * @param bean
	 * @return
	 */
	public boolean deleteRolesdByProgramid(Rolesd bean){
		return deleteRolesdByProgramid(null,bean);
	}
	public boolean deleteRolesdByProgramid(Connection con ,Rolesd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ROLESD ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND ROLEID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getRoleid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}	
	//刪除
	public boolean deleteRolesd(Rolesd bean){
		return deleteRolesd(null,bean);
	}
	public boolean deleteRolesd(Connection con ,Rolesd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ROLESD ";
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

	private Rolesd setRolesd(Item item){
		Rolesd bean =new Rolesd();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setRoleid(item.getItemProperty("ROLEID").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Rolesd> RolesdIndexToBean(IndexedContainer container){
		BeanContainer<String,Rolesd> beanContainer =new BeanContainer<String,Rolesd>(Rolesd.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Rolesd bean =setRolesd(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
