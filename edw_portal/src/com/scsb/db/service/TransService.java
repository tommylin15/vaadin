package com.scsb.db.service; 

import java.sql.SQLException;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Trans;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class TransService{
	private static Logger logger = Logger.getLogger(TransService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public TransService() {
	}	
	public TransService(String lang) {
		this.lang=lang;	
	}

	/**
 	 * 取得Trans資料表中,所有資料
 	 * @return BeanContainer<String,Trans>
 	 */	
	private String RETRIEVE_STMT = "SELECT * FROM TRANS ";
	public BeanContainer<String,Trans> getTrans(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Trans> beanContainer =new BeanContainer<String,Trans>(Trans.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT);	
			beanContainer =TransIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;		
	}
	
	private String RETRIEVE_STMT_ALL = "SELECT * FROM TRANS WHERE GROUPMODE='Y' ";
	/**
 	 * 取得Trans資料表中,所有資料
 	 * @return BeanContainer<String,Trans>
 	 */	
	public BeanContainer<String,Trans> getTrans_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Trans> beanContainer =new BeanContainer<String,Trans>(Trans.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =TransIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM TRANS WHERE GROUPID =? ";
	//取得Trans  PrimaryKey ;
	public Trans getTrans_PKs(String groupId){
		Trans bean =new Trans();
		bean.setGroupid(groupId);
		BeanContainer<String,Trans> beanContainer = getTrans_PKs(bean);
		if (beanContainer.size() > 0) bean =beanContainer.getItem(beanContainer.getIdByIndex(0)).getBean();
		return bean;
	}
	public BeanContainer<String,Trans> getTrans_PKs(Trans bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Trans> beanContainer =new BeanContainer<String,Trans>(Trans.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getGroupid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TransIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM TRANS WHERE GROUPID =? ";
	/** 
	 * 取得Trans  PrimaryKey 回傳 Trans ;
	 * @param bean 
	 * @return
	 */
	public Trans getTrans_PK(Trans bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Trans> beanContainer =new BeanContainer<String,Trans>(Trans.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Trans retbean=new Trans();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getGroupid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =TransIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Trans> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//取得Trans  PrimaryKey 回傳 Trans ;
	public Trans getBeanByKeys(String[] keys){
		Trans qryBean = new Trans();
		// TODO 確認是否產生 setFileNameUp(keys[i]) 程式碼
		qryBean.setGroupid(keys[0]);
        Trans bean = new Trans();
        bean = getTrans_PK(qryBean);
		return bean;
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

	//寫入TRANS ;
	public boolean insertTrans(Trans bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO TRANS( GROUPID  ,GROUPNAME  ,GROUPTYPE  ,GROUPMODE ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,?  ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getGroupid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getGroupname()));
						hashSet.add(DBUtil.setData(3,"String",bean.getGrouptype()));
						hashSet.add(DBUtil.setData(4,"String",bean.getGroupmode()));
						hashSet.add(DBUtil.setData(5,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(6,"String",bean.getUpdateUser()));						
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改TRANS ;
	public boolean updateTrans(Trans bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE TRANS set ";
				sql+=" GROUPNAME =? ";
				sql+=", GROUPTYPE =? ";
				sql+=", GROUPMODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE GROUPID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getGroupname()));
				hashSet.add(DBUtil.setData(2,"String",bean.getGrouptype()));
				hashSet.add(DBUtil.setData(3,"String",bean.getGroupmode()));
				hashSet.add(DBUtil.setData(4,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(5,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(6,"String",bean.getGroupid()));

			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	/**
	 * 依據傳入欄位.修改TRANS ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateTrans(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE TRANS set ";
				sql+=sUpdateCode;
				sql+=" WHERE GROUPID =? ";
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

	//刪除TRANS ;
	public boolean deleteTrans(Trans bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  TRANS ";
				sql+=" WHERE GROUPID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getGroupid()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Trans setTrans(Item item){
		Trans bean =new Trans();
			bean.setGroupid(item.getItemProperty("GROUPID").getValue()+"");
			bean.setGroupname(item.getItemProperty("GROUPNAME").getValue()+"");
			bean.setGrouptype(item.getItemProperty("GROUPTYPE").getValue()+"");
			bean.setGroupmode(item.getItemProperty("GROUPMODE").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Trans> TransIndexToBean(IndexedContainer container){
		BeanContainer<String,Trans> beanContainer =new BeanContainer<String,Trans>(Trans.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Trans bean =setTrans(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
