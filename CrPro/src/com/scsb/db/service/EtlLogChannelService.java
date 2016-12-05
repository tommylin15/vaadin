package com.scsb.db.service; 

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import com.scsb.db.bean.EtlLogChannel;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class EtlLogChannelService{
	private static Logger logger = Logger.getLogger(EtlLogChannelService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool="";
	public EtlLogChannelService() {
		this.dbPool="defaultPool";	
		this.lang="tw";
	}
	public void setDbPool(String dbPool) {
		this.dbPool=dbPool;	
	}	

	public EtlLogChannelService(String lang) {
		this.lang=lang;	
	}
	
	private String RETRIEVE_STMT_ALLROOT ="SELECT a.* ,b.status " +
			" FROM ETL_LOG_CHANNEL a " +
			" left join (select x.channel_id ,x.status	from ETL_LOG_JOB x " +
			" 	 union " +
			" 	 select y.channel_id ,y.status	from ETL_LOG_TRANS y " +
			"	) b " +
			"	on a.channel_id=b.channel_id " +
			" where a.channel_id=a.root_channel_id " +
			" 	and a.id_batch >-1" +
			" order by log_date desc ";

	/**
 	 * 取得EtlLogChannel資料表中,全部的root channel id(外加status)
 	 * @return BeanContainer<String,EtlLogChannel>
 	 */	
	public BeanContainer<String,EtlLogChannel> getEtlLogChannelByAllRoot(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{			
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALLROOT );	
			beanContainer =EtlLogChannelIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_RUNROOT ="SELECT a.* ,b.status " +
			" FROM ETL_LOG_CHANNEL a " +
			" left join (select x.channel_id ,x.status	from ETL_LOG_JOB x " +
			" 	 union " +
			" 	 select y.channel_id ,y.status	from ETL_LOG_TRANS y " +
			"	) b " +
			"	on a.channel_id=b.channel_id " +
			" where a.channel_id=a.root_channel_id " +
			" 	and a.id_batch >-1" +
			"  and a.id_batch in (select id_batch from etl_log_channel where  OBJECT_NAME ='Table exists') "+
			"  order by log_date desc ";

	/**
 	 * 取得EtlLogChannel資料表中,全部的root channel id(外加status)
 	 * @return BeanContainer<String,EtlLogChannel>
 	 */	
	public BeanContainer<String,EtlLogChannel> getEtlLogChannelByRunRoot(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{			
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_RUNROOT );	
			beanContainer =EtlLogChannelIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_ROOT ="SELECT a.* ,b.status " +
	" FROM ETL_LOG_CHANNEL a " +
	" left join (select x.channel_id ,x.status	from ETL_LOG_JOB x " +
	" 	 union " +
	" 	 select y.channel_id ,y.status	from ETL_LOG_TRANS y " +
	"	)  b " +
	"	on a.channel_id=b.channel_id " +
	" where a.root_channel_id=? " +
	" 	and a.id_batch >-1" +
	"   and a.logging_object_type <>'DATABASE' " +
	" order by log_date ";
	/**
 	 * 取得EtlLogChannel資料表中,單一root channel id 向下所有資料
 	 * @return BeanContainer<String,EtlLogChannel>
 	 */	
	public BeanContainer<String,EtlLogChannel> getEtlLogChannelByRoot(String root_channel_id){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			hashSet.add(DBUtil.setData(1,"String",root_channel_id));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ROOT ,hashSet);	
			beanContainer =EtlLogChannelIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_ALL = "SELECT * FROM ETL_LOG_CHANNEL ";
	/**
 	 * 取得EtlLogChannel資料表中,所有資料
 	 * @return BeanContainer<String,EtlLogChannel>
 	 */	
	public BeanContainer<String,EtlLogChannel> getEtlLogChannel_All(String poolName ){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(poolName ,RETRIEVE_STMT_ALL);	
			beanContainer =EtlLogChannelIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM ETL_LOG_CHANNEL WHERE CHANNEL_ID =? ";
	//取得EtlLogChannel  PrimaryKey ;
	public BeanContainer<String,EtlLogChannel> getEtlLogChannel_PKs(EtlLogChannel bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getChannelId()));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =EtlLogChannelIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ETL_LOG_CHANNEL WHERE CHANNEL_ID =? ";
	/** 
	 * 取得EtlLogChannel  PrimaryKey 回傳 EtlLogChannel ;
	 * @param bean 
	 * @return
	 */
	public EtlLogChannel getEtlLogChannel_PK(EtlLogChannel bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		EtlLogChannel retbean=new EtlLogChannel();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getChannelId()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =EtlLogChannelIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<EtlLogChannel> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private EtlLogChannel setEtlLogChannel(Item item){
		EtlLogChannel bean =new EtlLogChannel();
			bean.setIdBatch(item.getItemProperty("ID_BATCH").getValue()+"");
			bean.setChannelId(item.getItemProperty("CHANNEL_ID").getValue()+"");
			bean.setLogDate(item.getItemProperty("LOG_DATE").getValue()+"");
			bean.setLoggingObjectType(item.getItemProperty("LOGGING_OBJECT_TYPE").getValue()+"");
			bean.setObjectName(item.getItemProperty("OBJECT_NAME").getValue()+"");
			bean.setObjectCopy(item.getItemProperty("OBJECT_COPY").getValue()+"");
			bean.setRepositoryDirectory(item.getItemProperty("REPOSITORY_DIRECTORY").getValue()+"");
			bean.setFilename(item.getItemProperty("FILENAME").getValue()+"");
			bean.setObjectId(item.getItemProperty("OBJECT_ID").getValue()+"");
			bean.setObjectRevision(item.getItemProperty("OBJECT_REVISION").getValue()+"");
			bean.setParentChannelId(item.getItemProperty("PARENT_CHANNEL_ID").getValue()+"");
			bean.setRootChannelId(item.getItemProperty("ROOT_CHANNEL_ID").getValue()+"");
			//外加的欄位
			if (item.getItemProperty("STATUS") != null){
				bean.setStatus(item.getItemProperty("STATUS").getValue()+"");
			}

		return bean;
	}

	private BeanContainer<String,EtlLogChannel> EtlLogChannelIndexToBean(IndexedContainer container){
		BeanContainer<String,EtlLogChannel> beanContainer =new BeanContainer<String,EtlLogChannel>(EtlLogChannel.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			EtlLogChannel bean =setEtlLogChannel(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}