package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.ScsbTitle;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class ScsbTitleService{
	private static Logger logger = Logger.getLogger(ScsbTitleService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public ScsbTitleService() {
	}

	public ScsbTitleService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM SCSBTITLE ";
	/**
 	 * 取得Title資料表中,所有資料
 	 * @return BeanContainer<String,Title>
 	 */	
	public BeanContainer<String,ScsbTitle> getTitle_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitle> beanContainer =new BeanContainer<String,ScsbTitle>(ScsbTitle.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =TitleIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM SCSBTITLE WHERE TITLE_NAME =? ";
	//取得Title  PrimaryKey ;
	public BeanContainer<String,ScsbTitle> getTitle_PKs(ScsbTitle bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitle> beanContainer =new BeanContainer<String,ScsbTitle>(ScsbTitle.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TitleIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM SCSBTITLE WHERE TITLE_NAME =? ";
	/** 
	 * 取得Title  PrimaryKey 回傳 Title ;
	 * @param bean 
	 * @return
	 */
	public ScsbTitle getTitle_PK(ScsbTitle bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,ScsbTitle> beanContainer =new BeanContainer<String,ScsbTitle>(ScsbTitle.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		ScsbTitle retbean=new ScsbTitle();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =TitleIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<ScsbTitle> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入TITLE ;
	public boolean insertTitle(ScsbTitle bean){
		return insertTitle(null,bean);
	}
	public boolean insertTitle(Connection con ,ScsbTitle bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO SCSBTITLE( TITLE_NAME ) ";
					sql+=" VALUES (  ? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改TITLE ;
	public boolean updateTitle(ScsbTitle bean){
		return updateTitle(null,bean);
	}
	public boolean updateTitle(Connection con ,ScsbTitle bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE SCSBTITLE set ";
				sql+=" WHERE TITLE_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));

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
	 * 依據傳入欄位.修改TITLE ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateTitle(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE SCSBTITLE set ";
				sql+=sUpdateCode;
				sql+=" WHERE TITLE_NAME =? ";
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

	//刪除TITLE ;
	public boolean deleteTitle(ScsbTitle bean){
		return deleteTitle(null,bean);
	}
	public boolean deleteTitle(Connection con ,ScsbTitle bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  SCSBTITLE ";
				sql+=" WHERE TITLE_NAME =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getTitleName()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private ScsbTitle setTitle(Item item){
		ScsbTitle bean =new ScsbTitle();
			bean.setTitleName(item.getItemProperty("TITLE_NAME").getValue()+"");

		return bean;
	}

	private BeanContainer<String,ScsbTitle> TitleIndexToBean(IndexedContainer container){
		BeanContainer<String,ScsbTitle> beanContainer =new BeanContainer<String,ScsbTitle>(ScsbTitle.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			ScsbTitle bean =setTitle(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
