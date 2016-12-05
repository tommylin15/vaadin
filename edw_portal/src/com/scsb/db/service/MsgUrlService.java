package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.MsgUrl;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class MsgUrlService{
	private static Logger logger = Logger.getLogger(MsgUrlService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool ="";
	public MsgUrlService() {
		this.dbPool="defaultPool";

		this.lang="tw";	

	}

	public MsgUrlService(String lang) {
		this.lang=lang;	
	}

public void setDbPool(String dbPool) {
		this.dbPool=dbPool;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM MSG_URL ";
	/**
 	 * 取得MsgUrl資料表中,所有資料
 	 * @return BeanContainer<String,MsgUrl>
 	 */	
	public BeanContainer<String,MsgUrl> getMsgUrl_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,MsgUrl> beanContainer =new BeanContainer<String,MsgUrl>(MsgUrl.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALL);	
			beanContainer =MsgUrlIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM MSG_URL WHERE MSG_URLX64 =? AND MSG_NO =? ";
	//取得MsgUrl  PrimaryKey ;
	public BeanContainer<String,MsgUrl> getMsgUrl_PKs(MsgUrl bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,MsgUrl> beanContainer =new BeanContainer<String,MsgUrl>(MsgUrl.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrlx64()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMsgNo()));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =MsgUrlIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM MSG_URL WHERE MSG_URLX64 =? AND MSG_NO =? ";
	/** 
	 * 取得MsgUrl  PrimaryKey 回傳 MsgUrl ;
	 * @param bean 
	 * @return
	 */
	public MsgUrl getMsgUrl_PK(MsgUrl bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,MsgUrl> beanContainer =new BeanContainer<String,MsgUrl>(MsgUrl.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		MsgUrl retbean=new MsgUrl();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrlx64()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMsgNo()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =MsgUrlIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<MsgUrl> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private String RETRIEVE_STMT_URLX64 = "SELECT * FROM MSG_URL WHERE MSG_URLX64 =?  ";
	/** 
	 * 取得MsgUrl  PrimaryKey 回傳 MsgUrl ;
	 * @param bean 
	 * @return
	 */
	public MsgUrl getMsgUrl_UrlX64(MsgUrl bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,MsgUrl> beanContainer =new BeanContainer<String,MsgUrl>(MsgUrl.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		MsgUrl retbean=new MsgUrl();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrlx64()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_URLX64 ,hashSet);	
				beanContainer =MsgUrlIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<MsgUrl> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
		dbResultSet = (new DBAction()).executeQuery(dbPool,sqlstmt);
		return dbResultSet;
	}		

	//寫入MSG_URL ;
	public boolean insertMsgUrl(MsgUrl bean){
		return insertMsgUrl(null,bean);
	}
	public boolean insertMsgUrl(Connection con ,MsgUrl bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO MSG_URL( MSG_URLX64  ,MSG_URL  ,MSG_SYS  ,MSG_NO ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrlx64()));
						hashSet.add(DBUtil.setData(2,"String",bean.getMsgUrl()));
						hashSet.add(DBUtil.setData(3,"String",bean.getMsgSys()));
						hashSet.add(DBUtil.setData(4,"String",bean.getMsgNo()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改MSG_URL ;
	public boolean updateMsgUrl(MsgUrl bean){
		return updateMsgUrl(null,bean);
	}
	public boolean updateMsgUrl(Connection con ,MsgUrl bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE MSG_URL set ";
				sql+=" MSG_URL =? ";
				sql+=", MSG_SYS =? ";
				sql+=" WHERE MSG_URLX64 =? ";
				sql+=" AND MSG_NO =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrl()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMsgSys()));
				hashSet.add(DBUtil.setData(3,"String",bean.getMsgUrlx64()));
				hashSet.add(DBUtil.setData(4,"String",bean.getMsgNo()));

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
	 * 依據傳入欄位.修改MSG_URL ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateMsgUrl(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE MSG_URL set ";
				sql+=sUpdateCode;
				sql+=" WHERE MSG_URLX64 =? ";
				sql+=" AND MSG_NO =? ";
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

	//刪除MSG_URL ;
	public boolean deleteMsgUrl(MsgUrl bean){
		return deleteMsgUrl(null,bean);
	}
	public boolean deleteMsgUrl(Connection con ,MsgUrl bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  MSG_URL ";
				sql+=" WHERE MSG_URLX64 =? ";
				sql+=" AND MSG_NO =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getMsgUrlx64()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMsgNo()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private MsgUrl setMsgUrl(Item item){
		MsgUrl bean =new MsgUrl();
			bean.setMsgUrlx64(item.getItemProperty("MSG_URLX64").getValue()+"");
			bean.setMsgUrl(item.getItemProperty("MSG_URL").getValue()+"");
			bean.setMsgSys(item.getItemProperty("MSG_SYS").getValue()+"");
			bean.setMsgNo(item.getItemProperty("MSG_NO").getValue()+"");

		return bean;
	}

	private BeanContainer<String,MsgUrl> MsgUrlIndexToBean(IndexedContainer container){
		BeanContainer<String,MsgUrl> beanContainer =new BeanContainer<String,MsgUrl>(MsgUrl.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			MsgUrl bean =setMsgUrl(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
