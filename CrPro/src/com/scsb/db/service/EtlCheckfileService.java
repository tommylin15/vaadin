package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.EtlCheckfile;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class EtlCheckfileService{
	private static Logger logger = Logger.getLogger(EtlCheckfileService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool ="";
	public EtlCheckfileService() {
		this.dbPool="defaultPool";

		this.lang="tw";	

	}

	public EtlCheckfileService(String lang) {
		this.lang=lang;	
	}

public void setDbPool(String dbPool) {

		this.dbPool=dbPool;	

	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM ETL_CHECKFILE ";
	/**
 	 * 取得EtlCheckfile資料表中,所有資料
 	 * @return BeanContainer<String,EtlCheckfile>
 	 */	
	public BeanContainer<String,EtlCheckfile> getEtlCheckfile_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlCheckfile> beanContainer =new BeanContainer<String,EtlCheckfile>(EtlCheckfile.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALL);	
			beanContainer =EtlCheckfileIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM ETL_CHECKFILE WHERE FILENAME =? AND SHORT_FILENAME =? ";
	//取得EtlCheckfile  PrimaryKey ;
	public BeanContainer<String,EtlCheckfile> getEtlCheckfile_PKs(EtlCheckfile bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlCheckfile> beanContainer =new BeanContainer<String,EtlCheckfile>(EtlCheckfile.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			/*
			HashSet<Hashtable<String,Object>> hashDataTypeSet =new HashSet<Hashtable<String,Object>>();
			hashDataTypeSet.add("nowDate","");
			hashDataTypeSet.add("lastDate","");
			hashDataTypeSet.add("endDate","");
			*/
			
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getFilename()));
				hashSet.add(DBUtil.setData(2,"String",bean.getShortFilename()));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =EtlCheckfileIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ETL_CHECKFILE WHERE FILENAME =? AND SHORT_FILENAME =? ";
	/** 
	 * 取得EtlCheckfile  PrimaryKey 回傳 EtlCheckfile ;
	 * @param bean 
	 * @return
	 */
	public EtlCheckfile getEtlCheckfile_PK(EtlCheckfile bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,EtlCheckfile> beanContainer =new BeanContainer<String,EtlCheckfile>(EtlCheckfile.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		EtlCheckfile retbean=new EtlCheckfile();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getFilename()));
				hashSet.add(DBUtil.setData(2,"String",bean.getShortFilename()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =EtlCheckfileIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<EtlCheckfile> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
		dbResultSet = (new DBAction()).executeQuery(dbPool ,sqlstmt);
		return dbResultSet;
	}		

	//寫入ETL_CHECKFILE ;
	public boolean insertEtlCheckfile(EtlCheckfile bean){
		return insertEtlCheckfile(null,bean);
	}
	public boolean insertEtlCheckfile(Connection con ,EtlCheckfile bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO ETL_CHECKFILE( FILENAME  ,SHORT_FILENAME  ,PATH  ,TYPE  ,BUS_DATE  ,NOW_DATE  ,LAST_DATE  ,SIZE  ,EXTENSION  ,STATUS  ,END_DATE  ,UPDATE_MODE ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getFilename()));
						hashSet.add(DBUtil.setData(2,"String",bean.getShortFilename()));
						hashSet.add(DBUtil.setData(3,"String",bean.getPath()));
						hashSet.add(DBUtil.setData(4,"String",bean.getType()));
						hashSet.add(DBUtil.setData(5,"String",bean.getBusDate()));
						hashSet.add(DBUtil.setData(6,"String",bean.getNowDate()));
						hashSet.add(DBUtil.setData(7,"String",bean.getLastDate()));
						hashSet.add(DBUtil.setData(8,"long",bean.getSize()));
						hashSet.add(DBUtil.setData(9,"String",bean.getExtension()));
						hashSet.add(DBUtil.setData(10,"String",bean.getStatus()));
						hashSet.add(DBUtil.setData(11,"String",bean.getEndDate()));
						hashSet.add(DBUtil.setData(12,"String",bean.getUpdateMode()));
			if (con==null) isUpdate=DBUtil.UpdateDB(dbPool ,sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改ETL_CHECKFILE ;
	public boolean updateEtlCheckfile(EtlCheckfile bean){
		return updateEtlCheckfile(null,bean);
	}
	public boolean updateEtlCheckfile(Connection con ,EtlCheckfile bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE ETL_CHECKFILE set ";
				sql+=" PATH =? ";
				sql+=", TYPE =? ";
				sql+=", BUS_DATE =? ";
				sql+=", NOW_DATE =? ";
				sql+=", LAST_DATE =? ";
				sql+=", SIZE =? ";
				sql+=", EXTENSION =? ";
				sql+=", STATUS =? ";
				sql+=", END_DATE =? ";
				sql+=", UPDATE_MODE =? ";
				sql+=" WHERE FILENAME =? ";
				sql+=" AND SHORT_FILENAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getPath()));
				hashSet.add(DBUtil.setData(2,"String",bean.getType()));
				hashSet.add(DBUtil.setData(3,"String",bean.getBusDate()));
				hashSet.add(DBUtil.setData(4,"String",bean.getNowDate()));
				hashSet.add(DBUtil.setData(5,"String",bean.getLastDate()));
				hashSet.add(DBUtil.setData(6,"long",bean.getSize()));
				hashSet.add(DBUtil.setData(7,"String",bean.getExtension()));
				hashSet.add(DBUtil.setData(8,"String",bean.getStatus()));
				hashSet.add(DBUtil.setData(9,"String",bean.getEndDate()));
				hashSet.add(DBUtil.setData(10,"String",bean.getUpdateMode()));
				hashSet.add(DBUtil.setData(11,"String",bean.getFilename()));
				hashSet.add(DBUtil.setData(12,"String",bean.getShortFilename()));

			if (con==null) isUpdate=DBUtil.UpdateDB(dbPool ,sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	/**
	 * 改status
	 * @param con
	 * @param bean
	 * @return
	 */
	public boolean updateEtlCheckfileStatus(EtlCheckfile bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE ETL_CHECKFILE set ";
				sql+=" STATUS =? ";
				sql+=" WHERE FILENAME =? ";
				sql+=" AND SHORT_FILENAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getStatus()));
				hashSet.add(DBUtil.setData(2,"String",bean.getFilename()));
				hashSet.add(DBUtil.setData(3,"String",bean.getShortFilename()));
			    isUpdate=DBUtil.UpdateDB(dbPool ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}	
	
	/**
	 * 依據傳入欄位.修改ETL_CHECKFILE ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateEtlCheckfile(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE ETL_CHECKFILE set ";
				sql+=sUpdateCode;
				sql+=" WHERE FILENAME =? ";
				sql+=" AND SHORT_FILENAME =? ";
				hashSet.add(DBUtil.setData(i++,"String",keysValue[0]));
				hashSet.add(DBUtil.setData(i++,"String",keysValue[1]));
				isUpdate=DBUtil.UpdateDB(dbPool ,sql ,hashSet);
			} catch (RuntimeException se) {
				isUpdate=false;
				logger.error(StrUtil.convException(se)); 
				this.ErrMsg=se.toString(); 
			}
		}
		return isUpdate;
	}

	//刪除ETL_CHECKFILE ;
	public boolean deleteEtlCheckfile(EtlCheckfile bean){
		return deleteEtlCheckfile(null,bean);
	}
	public boolean deleteEtlCheckfile(Connection con ,EtlCheckfile bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ETL_CHECKFILE ";
				sql+=" WHERE FILENAME =? ";
				sql+=" AND SHORT_FILENAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getFilename()));
				hashSet.add(DBUtil.setData(2,"String",bean.getShortFilename()));
			if (con==null) isUpdate=DBUtil.UpdateDB(dbPool ,sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private EtlCheckfile setEtlCheckfile(Item item){
		EtlCheckfile bean =new EtlCheckfile();
			bean.setFilename(item.getItemProperty("FILENAME").getValue()+"");
			bean.setShortFilename(item.getItemProperty("SHORT_FILENAME").getValue()+"");
			bean.setPath(item.getItemProperty("PATH").getValue()+"");
			bean.setType(item.getItemProperty("TYPE").getValue()+"");
			bean.setBusDate(item.getItemProperty("BUS_DATE").getValue()+"");
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try {
				bean.setNowDate(format.format(format.parse(item.getItemProperty("NOW_DATE").getValue()+"")));
				bean.setLastDate(format.format(format.parse(item.getItemProperty("LAST_DATE").getValue()+"")));
				bean.setEndDate(format.format(format.parse(item.getItemProperty("LAST_DATE").getValue()+"")));
			} catch (ParseException e) {
			}
			long isize =0;
			try{
				isize =Long.parseLong(item.getItemProperty("SIZE").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setSize(isize);
			bean.setExtension(item.getItemProperty("EXTENSION").getValue()+"");
			bean.setStatus(item.getItemProperty("STATUS").getValue()+"");
			bean.setUpdateMode(item.getItemProperty("UPDATE_MODE").getValue()+"");

		return bean;
	}

	private BeanContainer<String,EtlCheckfile> EtlCheckfileIndexToBean(IndexedContainer container){
		BeanContainer<String,EtlCheckfile> beanContainer =new BeanContainer<String,EtlCheckfile>(EtlCheckfile.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			EtlCheckfile bean =setEtlCheckfile(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
