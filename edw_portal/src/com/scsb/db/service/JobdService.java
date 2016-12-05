package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Jobd;
import com.scsb.db.bean.ScsbTitled;

import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class JobdService{
	private static Logger logger = Logger.getLogger(JobdService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public JobdService() {
	}

	public JobdService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM JOBD ";
	/**
 	 * 取得Jobd資料表中,所有資料
 	 * @return BeanContainer<String,Jobd>
 	 */	
	public BeanContainer<String,Jobd> getJobd_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Jobd> beanContainer =new BeanContainer<String,Jobd>(Jobd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =JobdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM JOBD WHERE PROGRAMID =? AND JOB_NAME =? ";
	//取得Jobd  PrimaryKey ;
	public BeanContainer<String,Jobd> getJobd_PKs(Jobd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Jobd> beanContainer =new BeanContainer<String,Jobd>(Jobd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getJobName()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =JobdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM JOBD WHERE PROGRAMID =? AND JOB_NAME =? ";
	/** 
	 * 取得Jobd  PrimaryKey 回傳 Jobd ;
	 * @param bean 
	 * @return
	 */
	public Jobd getJobd_PK(Jobd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Jobd> beanContainer =new BeanContainer<String,Jobd>(Jobd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Jobd retbean=new Jobd();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getJobName()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =JobdIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Jobd> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private String RETRIEVE_STMT_JOBNAME = "SELECT * FROM JOBD WHERE JOB_NAME =? ";
	//取得TITLENAME  PrimaryKey ;
	public BeanContainer<String,Jobd> getJobd_jobName(Jobd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Jobd> beanContainer =new BeanContainer<String,Jobd>(Jobd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getJobName()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_JOBNAME ,hashSet);	
			beanContainer =JobdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
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

	//寫入JOBD ;
	public boolean insertJobd(Jobd bean){
		return insertJobd(null,bean);
	}
	public boolean insertJobd(Connection con ,Jobd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO JOBD( PROGRAMID  ,ACTION_CODE  ,JOB_NAME  ,UPDATE_DATETIME  ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(3,"String",bean.getJobName()));
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
	 * 整批修改JOBD ;
	 * @param jobName
	 * @param groupId
	 * @param hashdata
	 * @return
	 */
	public boolean updateJobdByJobNameGroupId(String jobName ,String groupId,HashSet<Jobd> hashdata ){
		return updateJobdByJobNameGroupId(null,jobName,groupId,hashdata);
	}
	public boolean updateJobdByJobNameGroupId(Connection con,String jobName ,String groupId ,HashSet<Jobd> hashdata ){
		boolean isUpdate =false;
		this.ErrMsg="";
		isUpdate=deleteJobdByJobNameGroupId(con, jobName ,groupId);
		for(java.util.Iterator<Jobd> iter=hashdata.iterator();iter.hasNext();){
			isUpdate=insertJobd(con, iter.next());
			if (!isUpdate) break;
		}	
		return isUpdate;
	}		
	
	//修改JOBD ;
	public boolean updateJobd(Jobd bean){
		return updateJobd(null,bean);
	}
	public boolean updateJobd(Connection con ,Jobd bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE JOBD set ";
				sql+=" ACTION_CODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND JOB_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getJobName()));

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
	 * 依據傳入欄位.修改JOBD ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateJobd(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE JOBD set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND JOB_NAME =? ";
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
	 * 刪除JOBD ;
	 * @param jobName
	 * @param groupId
	 * @return
	 */
	public boolean deleteJobdByJobNameGroupId(String jobName ,String groupId){
		return deleteJobdByJobNameGroupId(null,jobName ,groupId);
	}
	public boolean deleteJobdByJobNameGroupId(Connection con ,String jobName ,String groupId){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  JOBD ";
				sql+=" WHERE  job_name =? " +
						" and programid in (select programid from transd where groupid=?) ";

				hashSet.add(DBUtil.setData(1,"String",jobName));
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
	
	//刪除JOBD ;
	public boolean deleteJobd(Jobd bean){
		return deleteJobd(null,bean);
	}
	public boolean deleteJobd(Connection con ,Jobd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  JOBD ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND JOB_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getJobName()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Jobd setJobd(Item item){
		Jobd bean =new Jobd();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setJobName(item.getItemProperty("JOB_NAME").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Jobd> JobdIndexToBean(IndexedContainer container){
		BeanContainer<String,Jobd> beanContainer =new BeanContainer<String,Jobd>(Jobd.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Jobd bean =setJobd(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
