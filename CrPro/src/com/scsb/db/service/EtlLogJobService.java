package com.scsb.db.service; 

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.scsb.db.bean.EtlLogJob;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class EtlLogJobService{
	private static Logger logger = Logger.getLogger(EtlLogJobService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool="";	
	public EtlLogJobService() {
	}

	public EtlLogJobService(String lang) {
		this.lang=lang;	
	}
	
	public void setDbPool(String dbPool) {
		this.dbPool=dbPool;	
	}		

	private String RETRIEVE_STMT_ALL = "SELECT * FROM ETL_LOG_JOB ";
	/**
 	 * 取得EtlLogJob資料表中,所有資料
 	 * @return BeanContainer<String,EtlLogJob>
 	 */	
	public BeanContainer<String,EtlLogJob> getEtlLogJob_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogJob> beanContainer =new BeanContainer<String,EtlLogJob>(EtlLogJob.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool,RETRIEVE_STMT_ALL);	
			beanContainer =EtlLogJobIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM ETL_LOG_JOB WHERE CHANNEL_ID =? ";
	//取得EtlLogJob  PrimaryKey ;
	public BeanContainer<String,EtlLogJob> getEtlLogJob_PKs(EtlLogJob bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogJob> beanContainer =new BeanContainer<String,EtlLogJob>(EtlLogJob.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getChannelId()));
			container = DBUtil.ContainerQueryDB(dbPool,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =EtlLogJobIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_PK3 = "SELECT * FROM ETL_LOG_JOB WHERE CHANNEL_ID =? ";
	//取得EtlLogJob  PrimaryKey ;
	public BeanContainer<String,EtlLogJob> getEtlLogJob_PKs(String channelId){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogJob> beanContainer =new BeanContainer<String,EtlLogJob>(EtlLogJob.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",channelId));
			container = DBUtil.ContainerQueryDB(dbPool,RETRIEVE_STMT_PK3 ,hashSet);	
			beanContainer =EtlLogJobIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ETL_LOG_JOB WHERE CHANNEL_ID =? ";
	/** 
	 * 取得EtlLogJob  PrimaryKey 回傳 EtlLogJob ;
	 * @param bean 
	 * @return
	 */
	public EtlLogJob getEtlLogJob_PK(EtlLogJob bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogJob> beanContainer =new BeanContainer<String,EtlLogJob>(EtlLogJob.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		EtlLogJob retbean=new EtlLogJob();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getChannelId()));
				container = DBUtil.ContainerQueryDB(dbPool,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =EtlLogJobIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<EtlLogJob> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private EtlLogJob setEtlLogJob(Item item){
		EtlLogJob bean =new EtlLogJob();
			bean.setIdJob(item.getItemProperty("ID_JOB").getValue()+"");
			bean.setChannelId(item.getItemProperty("CHANNEL_ID").getValue()+"");
			bean.setJobname(item.getItemProperty("JOBNAME").getValue()+"");
			bean.setStatus(item.getItemProperty("STATUS").getValue()+"");
			bean.setLinesRead(item.getItemProperty("LINES_READ").getValue()+"");
			bean.setLinesWritten(item.getItemProperty("LINES_WRITTEN").getValue()+"");
			bean.setLinesUpdated(item.getItemProperty("LINES_UPDATED").getValue()+"");
			bean.setLinesInput(item.getItemProperty("LINES_INPUT").getValue()+"");
			bean.setLinesOutput(item.getItemProperty("LINES_OUTPUT").getValue()+"");
			bean.setLinesRejected(item.getItemProperty("LINES_REJECTED").getValue()+"");
			bean.setErrors(item.getItemProperty("ERRORS").getValue()+"");
			bean.setStartdate(item.getItemProperty("STARTDATE").getValue()+"");
			bean.setEnddate(item.getItemProperty("ENDDATE").getValue()+"");
			bean.setLogdate(item.getItemProperty("LOGDATE").getValue()+"");
			bean.setDepdate(item.getItemProperty("DEPDATE").getValue()+"");
			bean.setReplaydate(item.getItemProperty("REPLAYDATE").getValue()+"");
			bean.setLogField(item.getItemProperty("LOG_FIELD").getValue()+"");
			bean.setStartJobEntry(item.getItemProperty("START_JOB_ENTRY").getValue()+"");
			bean.setClient(item.getItemProperty("CLIENT").getValue()+"");

		return bean;
	}

	private BeanContainer<String,EtlLogJob> EtlLogJobIndexToBean(IndexedContainer container){
		BeanContainer<String,EtlLogJob> beanContainer =new BeanContainer<String,EtlLogJob>(EtlLogJob.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			EtlLogJob bean =setEtlLogJob(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
