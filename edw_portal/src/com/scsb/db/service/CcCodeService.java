package com.scsb.db.service; 

import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.CcCode;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class CcCodeService{
	private static Logger logger = Logger.getLogger(CcCodeService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public CcCodeService() {
	}

	public CcCodeService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALLKIND = "SELECT distinct CODE_KIND ,CODE_MEMO FROM CC_CODE  ";
	/**
 	 * 取得CcCode資料表中,所有資料
 	 * @return BeanContainer<String,CcCode>
 	 */	
	public BeanContainer<String,CcCode> getCcCode_AllKind(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALLKIND);	
			beanContainer =CcCodeIndexToBean(container);
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_ALL = "SELECT * FROM CC_CODE ";
	/**
 	 * 取得CcCode資料表中,所有資料
 	 * @return BeanContainer<String,CcCode>
 	 */	
	public BeanContainer<String,CcCode> getCcCode_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =CcCodeIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}	

	private String RETRIEVE_STMT_PK = "SELECT * FROM CC_CODE WHERE CODE_KIND =? AND CODE_ID =? ";
	//取得CcCode  PrimaryKey ;
	public BeanContainer<String,CcCode> getCcCode_PKs(CcCode bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getCodeKind()));
				hashSet.add(DBUtil.setData(2,"String",bean.getCodeId()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =CcCodeIndexToBean(container);	
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
	 * 取得CC_Code where CODE_KIND ,Lang
	 * @param sWhere
	 * @return
	 */
	public BeanContainer<String ,CcCode> getCcCodeNoDel(String sWhere ){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			String[] sArr =new String[0];
			String sql="";
			
		    sql=" SELECT a.* FROM cc_code"+DBUtil.ConverLang4SQL(lang)+" a  " +
	    		"  WHERE a.del_flg='N' ";
		    sql+=sWhere;
		    sql+=" order by a.SORT_ORDER ,a.CODE_ID ";			    
			container = DBUtil.ContainerQueryDB(sql);
			beanContainer =CcCodeIndexToBean(container);
		} catch (RuntimeException se) {
			se.printStackTrace();	
			this.ErrMsg=se.getMessage()+"";	
		}catch (SQLException se) {
			se.printStackTrace();	
			this.ErrMsg=se.getMessage()+"";	
		}
		return beanContainer;
	}
	
	/**
	 * 取得CC_Code where CODE_KIND ,Lang
	 * @param sWhere
	 * @return
	 */
	public BeanContainer<String ,CcCode> getCcCodeAll(String sWhere){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			String[] sArr =new String[0];
			String sql="";
		    sql=" SELECT a.* FROM cc_code"+DBUtil.ConverLang4SQL(lang)+" a  " +
	    		"  WHERE a.del_flg='N' ";
		    sql+=sWhere;
		    sql+=" order by a.SORT_ORDER ,a.CODE_ID ";				
			container = DBUtil.ContainerQueryDB(sql);
			beanContainer =CcCodeIndexToBean(container);
		} catch (RuntimeException se) {
			se.printStackTrace();	
			this.ErrMsg=se.getMessage()+"";	
		}catch (SQLException se) {
			se.printStackTrace();	
			this.ErrMsg=se.getMessage()+"";	
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_PK2 = "SELECT * FROM CC_CODE WHERE CODE_KIND =? AND CODE_ID =? ";
	/** 
	 * 取得CcCode  PrimaryKey 回傳 CcCode ;
	 * @param bean 
	 * @return
	 */
	public CcCode getCcCode_PK(CcCode bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		CcCode retbean=new CcCode();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getCodeKind()));
				hashSet.add(DBUtil.setData(2,"String",bean.getCodeId()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =CcCodeIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<CcCode> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	private String RETRIEVE_STMT_KIND = "SELECT * FROM CC_CODE WHERE CODE_KIND =? and DEL_FLG='N'";
	/** 
	 * 取得CcCode  Kind 回傳 CcCode ;
	 * @param string 
	 * @return
	 */
	public BeanContainer<String ,CcCode> getCcCode_Kind(String code_Kind){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		if (code_Kind != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",code_Kind));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_KIND ,hashSet);	
				beanContainer =CcCodeIndexToBean(container);
			} catch (RuntimeException re) {
				logger.error(StrUtil.convException(re));	
				this.ErrMsg=re.toString();	
			}catch (SQLException se) {
				logger.error(StrUtil.convException(se));	
				this.ErrMsg=se.toString();	
			}
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_KIND2 = "SELECT * FROM CC_CODE WHERE CODE_KIND =? ";
	/** 
	 * 取得CcCode  Kind 回傳 CcCode ;
	 * @param string 
	 * @return
	 */
	public BeanContainer<String ,CcCode> getCcCode_Kind_All(String code_Kind){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		if (code_Kind != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",code_Kind));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_KIND2 ,hashSet);	
				beanContainer =CcCodeIndexToBean(container);
			} catch (RuntimeException re) {
				logger.error(StrUtil.convException(re));	
				this.ErrMsg=re.toString();	
			}catch (SQLException se) {
				logger.error(StrUtil.convException(se));	
				this.ErrMsg=se.toString();	
			}
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

	//寫入CC_CODE ;
	public boolean insertCcCode(CcCode bean){
		boolean isUpdate =false;
		CcCode selBean =this.getCcCode_PK(bean);
		this.ErrMsg="";
		if (selBean.getCodeId().equals("")){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				String sql = "INSERT INTO CC_CODE( CODE_KIND  ,CODE_ID  ,CODE_PARENT  ,CODE_NAME  ,DEL_FLG  ,SORT_ORDER  ,CODE_MEMO  ,UPDATE_DATETIME  ,UPDATE_USER) ";
						sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? )";
							hashSet.add(DBUtil.setData(1,"String",bean.getCodeKind()));
							hashSet.add(DBUtil.setData(2,"String",bean.getCodeId()));
							hashSet.add(DBUtil.setData(3,"String",bean.getCodeParent()));
							hashSet.add(DBUtil.setData(4,"String",bean.getCodeName()));
							hashSet.add(DBUtil.setData(5,"String",bean.getDelFlg()));
							hashSet.add(DBUtil.setData(6,"String",bean.getSortOrder()));
							hashSet.add(DBUtil.setData(7,"String",bean.getCodeMemo()));
							hashSet.add(DBUtil.setData(8,"String",bean.getUpdateDatetime()));
							hashSet.add(DBUtil.setData(9,"String",bean.getUpdateUser()));
				isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			} catch (RuntimeException re) {
				isUpdate=false;
				logger.error(StrUtil.convException(re));	
				this.ErrMsg=re.toString();
			}
		}else{
			this.ErrMsg="代碼重覆,煩請確認!!";
		}
		return isUpdate;
	}

	//修改CC_CODE ;
	public boolean updateCcCode(CcCode bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE CC_CODE set ";
				sql+=" CODE_PARENT =? ";
				sql+=", CODE_NAME =? ";
				sql+=", DEL_FLG =? ";
				sql+=", SORT_ORDER =? ";
				sql+=", CODE_MEMO =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";
				sql+=" WHERE CODE_KIND =? ";
				sql+=" AND CODE_ID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getCodeParent()));
				hashSet.add(DBUtil.setData(2,"String",bean.getCodeName()));
				hashSet.add(DBUtil.setData(3,"String",bean.getDelFlg()));
				hashSet.add(DBUtil.setData(4,"String",bean.getSortOrder()));
				hashSet.add(DBUtil.setData(5,"String",bean.getCodeMemo()));
				hashSet.add(DBUtil.setData(6,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(7,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(8,"String",bean.getCodeKind()));
				hashSet.add(DBUtil.setData(9,"String",bean.getCodeId()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	/**
	 * 依據傳入欄位.修改CC_CODE ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateCcCode(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE CC_CODE set ";
				sql+=sUpdateCode;
				sql+=" WHERE CODE_KIND =? ";
				sql+=" AND CODE_ID =? ";
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

	//刪除CC_CODE ;
	public boolean deleteCcCode(CcCode bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  CC_CODE ";
				sql+=" WHERE CODE_KIND =? ";
				sql+=" AND CODE_ID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getCodeKind()));
				hashSet.add(DBUtil.setData(2,"String",bean.getCodeId()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private CcCode setCcCode(Item item){
		CcCode bean =new CcCode();
			bean.setCodeKind(item.getItemProperty("CODE_KIND").getValue()+"");
			bean.setCodeId(item.getItemProperty("CODE_ID").getValue()+"");
			bean.setCodeParent(item.getItemProperty("CODE_PARENT").getValue()+"");
			bean.setCodeName(item.getItemProperty("CODE_NAME").getValue()+"");
			bean.setDelFlg(item.getItemProperty("DEL_FLG").getValue()+"");
			bean.setSortOrder(item.getItemProperty("SORT_ORDER").getValue()+"");
			bean.setCodeMemo(item.getItemProperty("CODE_MEMO").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");
		return bean;
	}

	private BeanContainer<String,CcCode> CcCodeIndexToBean(IndexedContainer container){
		BeanContainer<String,CcCode> beanContainer =new BeanContainer<String,CcCode>(CcCode.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			CcCode bean =setCcCode(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
