package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.ActionItem;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class ActionItemService{
	private static Logger logger = Logger.getLogger(ActionItemService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public ActionItemService() {
	}

	public ActionItemService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM ACTION_ITEM ";
	/**
 	 * 取得ActionItem資料表中,所有資料
 	 * @return BeanContainer<String,ActionItem>
 	 */	
	public BeanContainer<String,ActionItem> getActionItem_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ActionItem> beanContainer =new BeanContainer<String,ActionItem>(ActionItem.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =ActionItemIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM ACTION_ITEM WHERE ACTION_CODE =? ";
	//取得ActionItem  PrimaryKey ;
	public BeanContainer<String,ActionItem> getActionItem_PKs(ActionItem bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ActionItem> beanContainer =new BeanContainer<String,ActionItem>(ActionItem.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =ActionItemIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM ACTION_ITEM WHERE ACTION_CODE =? ";
	/** 
	 * 取得ActionItem  PrimaryKey 回傳 ActionItem ;
	 * @param bean 
	 * @return
	 */
	public ActionItem getActionItem_PK(ActionItem bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ActionItem> beanContainer =new BeanContainer<String,ActionItem>(ActionItem.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		ActionItem retbean=new ActionItem();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =ActionItemIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<ActionItem> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入ACTION_ITEM ;
	public boolean insertActionItem(ActionItem bean){
		return insertActionItem(null,bean);
	}
	public boolean insertActionItem(Connection con ,ActionItem bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO ACTION_ITEM( ACTION_CODE  ,ACTION_NAME ) ";
					sql+=" VALUES (  ?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionName()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改ACTION_ITEM ;
	public boolean updateActionItem(ActionItem bean){
		return updateActionItem(null,bean);
	}
	public boolean updateActionItem(Connection con ,ActionItem bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE ACTION_ITEM set ";
				sql+=" ACTION_NAME =? ";
				sql+=" WHERE ACTION_CODE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionName()));
				hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));

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
	 * 依據傳入欄位.修改ACTION_ITEM ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateActionItem(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE ACTION_ITEM set ";
				sql+=sUpdateCode;
				sql+=" WHERE ACTION_CODE =? ";
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

	//刪除ACTION_ITEM ;
	public boolean deleteActionItem(ActionItem bean){
		return deleteActionItem(null,bean);
	}
	public boolean deleteActionItem(Connection con ,ActionItem bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  ACTION_ITEM ";
				sql+=" WHERE ACTION_CODE =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private ActionItem setActionItem(Item item){
		ActionItem bean =new ActionItem();
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setActionName(item.getItemProperty("ACTION_NAME").getValue()+"");

		return bean;
	}

	private BeanContainer<String,ActionItem> ActionItemIndexToBean(IndexedContainer container){
		BeanContainer<String,ActionItem> beanContainer =new BeanContainer<String,ActionItem>(ActionItem.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			ActionItem bean =setActionItem(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
