package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Ldapou;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class LdapouService{
	private static Logger logger = Logger.getLogger(LdapouService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public LdapouService() {
	}

	public LdapouService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM LDAPOU ";
	/**
 	 * 取得SasLdapou資料表中,所有資料
 	 * @return BeanContainer<String,SasLdapou>
 	 */	
	public BeanContainer<String,Ldapou> getSasLdapou_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Ldapou> beanContainer =new BeanContainer<String,Ldapou>(Ldapou.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =SasLdapouIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM LDAPOU WHERE OU =? ";
	//取得SasLdapou  PrimaryKey ;
	public BeanContainer<String,Ldapou> getSasLdapou_PKs(Ldapou bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Ldapou> beanContainer =new BeanContainer<String,Ldapou>(Ldapou.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =SasLdapouIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM LDAPOU WHERE OU =? ";
	/** 
	 * 取得SasLdapou  PrimaryKey 回傳 SasLdapou ;
	 * @param bean 
	 * @return
	 */
	public Ldapou getSasLdapou_PK(Ldapou bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Ldapou> beanContainer =new BeanContainer<String,Ldapou>(Ldapou.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Ldapou retbean=new Ldapou();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =SasLdapouIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Ldapou> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入LDAPOU ;
	public boolean insertSasLdapou(Ldapou bean){
		return insertSasLdapou(null,bean);
	}
	public boolean insertSasLdapou(Connection con ,Ldapou bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO LDAPOU( OU  ,DESCRIPTION  ,MAIL  ,MANACCTNO  ,OBSERVER  ,OU_6DIGIT  ,ID ,PARENTOU ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  )";
						hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
						hashSet.add(DBUtil.setData(2,"String",bean.getDescription()));
						hashSet.add(DBUtil.setData(3,"String",bean.getMail()));
						hashSet.add(DBUtil.setData(4,"String",bean.getManacctno()));
						hashSet.add(DBUtil.setData(5,"String",bean.getObserver()));
						hashSet.add(DBUtil.setData(6,"String",bean.getOu6digit()));
						hashSet.add(DBUtil.setData(7,"String",bean.getId()));
						hashSet.add(DBUtil.setData(8,"String",bean.getParentou()));

			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改LDAPOU ;
	public boolean updateSasLdapou(Ldapou bean){
		return updateSasLdapou(null,bean);
	}
	public boolean updateSasLdapou(Connection con ,Ldapou bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE LDAPOU set ";
				sql+=", DESCRIPTION =? ";
				sql+=", MAIL =? ";
				sql+=", MANACCTNO =? ";
				sql+=", OBSERVER =? ";
				sql+=", OU_6DIGIT =? ";
				sql+=", ID =? ";
				sql+=", PARENTOU =? ";
				sql+=" WHERE OU =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getDescription()));
				hashSet.add(DBUtil.setData(2,"String",bean.getMail()));
				hashSet.add(DBUtil.setData(3,"String",bean.getManacctno()));
				hashSet.add(DBUtil.setData(4,"String",bean.getObserver()));
				hashSet.add(DBUtil.setData(5,"String",bean.getOu6digit()));
				hashSet.add(DBUtil.setData(6,"String",bean.getId()));
				hashSet.add(DBUtil.setData(7,"String",bean.getParentou()));
				hashSet.add(DBUtil.setData(8,"String",bean.getOu()));

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
	 * 依據傳入欄位.修改LDAPOU ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateSasLdapou(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE LDAPOU set ";
				sql+=sUpdateCode;
				sql+=" WHERE OU =? ";
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

	//刪除LDAPOU ;
	public boolean deleteSasLdapou(Ldapou bean){
		return deleteSasLdapou(null,bean);
	}
	public boolean deleteSasLdapou(Connection con ,Ldapou bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  LDAPOU ";
				sql+=" WHERE OU =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Ldapou setSasLdapou(Item item){
		Ldapou bean =new Ldapou();
			bean.setOu(item.getItemProperty("OU").getValue()+"");
			bean.setDescription(item.getItemProperty("DESCRIPTION").getValue()+"");
			bean.setMail(item.getItemProperty("MAIL").getValue()+"");
			bean.setManacctno(item.getItemProperty("MANACCTNO").getValue()+"");
			bean.setObserver(item.getItemProperty("OBSERVER").getValue()+"");
			bean.setOu6digit(item.getItemProperty("OU_6DIGIT").getValue()+"");
			bean.setId(item.getItemProperty("ID").getValue()+"");
			bean.setParentou(item.getItemProperty("PARENTOU").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Ldapou> SasLdapouIndexToBean(IndexedContainer container){
		BeanContainer<String,Ldapou> beanContainer =new BeanContainer<String,Ldapou>(Ldapou.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Ldapou bean =setSasLdapou(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
