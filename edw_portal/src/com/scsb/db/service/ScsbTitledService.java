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
import com.scsb.db.bean.ScsbTitled;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class ScsbTitledService{
	private static Logger logger = Logger.getLogger(ScsbTitledService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public ScsbTitledService() {
	}

	public ScsbTitledService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM SCSBTITLED ";
	/**
 	 * 取得SCSBTitled資料表中,所有資料
 	 * @return BeanContainer<String,Titled>
 	 */	
	public BeanContainer<String,ScsbTitled> getTitled_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitled> beanContainer =new BeanContainer<String,ScsbTitled>(ScsbTitled.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =TitledIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM TITLED WHERE PROGRAMID =? AND TITLE_NAME =? ";
	//取得Titled  PrimaryKey ;
	public BeanContainer<String,ScsbTitled> getTitled_PKs(ScsbTitled bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitled> beanContainer =new BeanContainer<String,ScsbTitled>(ScsbTitled.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getTitleName()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TitledIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_TITLENAME = "SELECT * FROM SCSBTITLED WHERE TITLE_NAME =? ";
	//取得TITLENAME  PrimaryKey ;
	public BeanContainer<String,ScsbTitled> getTitled_titleName(ScsbTitled bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitled> beanContainer =new BeanContainer<String,ScsbTitled>(ScsbTitled.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_TITLENAME ,hashSet);	
			beanContainer =TitledIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	
	private String RETRIEVE_STMT_PK2 = "SELECT * FROM SCSBTITLED WHERE PROGRAMID =? AND TITLE_NAME =? ";
	/** 
	 * 取得Titled  PrimaryKey 回傳 Titled ;
	 * @param bean 
	 * @return
	 */
	public ScsbTitled getTitled_PK(ScsbTitled bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitled> beanContainer =new BeanContainer<String,ScsbTitled>(ScsbTitled.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		ScsbTitled retbean=new ScsbTitled();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getTitleName()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =TitledIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<ScsbTitled> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入TITLED ;
	public boolean insertTitled(ScsbTitled bean){
		return insertTitled(null,bean);
	}
	public boolean insertTitled(Connection con ,ScsbTitled bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO SCSBTITLED( PROGRAMID  ,ACTION_CODE  ,TITLE_NAME  ,UPDATE_DATETIME  ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(3,"String",bean.getTitleName()));
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
	 * 整批修改SCSBTITLED ;
	 * @param titleName
	 * @param groupId
	 * @param hashdata
	 * @return
	 */
	public boolean updateTitledByTitleNameGroupId(String titleName ,String groupId,HashSet<ScsbTitled> hashdata ){
		return updateTitledByTitleNameGroupId(null,titleName,groupId,hashdata);
	}
	public boolean updateTitledByTitleNameGroupId(Connection con,String titleName ,String groupId ,HashSet<ScsbTitled> hashdata ){
		boolean isUpdate =false;
		this.ErrMsg="";
		isUpdate=deleteTitledByTitleNameGroupId(con, titleName ,groupId);
		for(java.util.Iterator<ScsbTitled> iter=hashdata.iterator();iter.hasNext();){
			isUpdate=insertTitled(con, iter.next());
			if (!isUpdate) break;
		}	
		return isUpdate;
	}	
	
	//修改TITLED ;
	public boolean updateTitled(ScsbTitled bean){
		return updateTitled(null,bean);
	}
	public boolean updateTitled(Connection con ,ScsbTitled bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE SCSBTITLED set ";
				sql+=" ACTION_CODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND TITLE_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getTitleName()));

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
	 * 依據傳入欄位.修改TITLED ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateTitled(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE SCSBTITLED set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND TITLE_NAME =? ";
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
	 * 刪除TITLED ;
	 * @param titleName
	 * @param groupId
	 * @return
	 */
	public boolean deleteTitledByTitleNameGroupId(String titleName ,String groupId){
		return deleteTitledByTitleNameGroupId(null,titleName ,groupId);
	}
	public boolean deleteTitledByTitleNameGroupId(Connection con ,String titleName ,String groupId){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  SCSBTITLED ";
				sql+=" WHERE  title_name =? " +
						" and programid in (select programid from transd where groupid=?) ";

				hashSet.add(DBUtil.setData(1,"String",titleName));
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

	//刪除TITLED ;
	public boolean deleteTitled(ScsbTitled bean){
		return deleteTitled(null,bean);
	}
	public boolean deleteTitled(Connection con ,ScsbTitled bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  SCSBTITLED ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND TITLE_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getTitleName()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private ScsbTitled setTitled(Item item){
		ScsbTitled bean =new ScsbTitled();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setTitleName(item.getItemProperty("TITLE_NAME").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,ScsbTitled> TitledIndexToBean(IndexedContainer container){
		BeanContainer<String,ScsbTitled> beanContainer =new BeanContainer<String,ScsbTitled>(ScsbTitled.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			ScsbTitled bean =setTitled(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
