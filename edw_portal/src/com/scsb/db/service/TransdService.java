package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Transd;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class TransdService{
	private static Logger logger = Logger.getLogger(TransdService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public TransdService() {
		this.lang="";	
	}	
	public TransdService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM TRANSD order by SORT_KEY ";
	/**
 	 * 取得Transd資料表中,所有資料
 	 * @return BeanContainer<String,Transd>
 	 */	
	public BeanContainer<String,Transd> getTransd_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =TransdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	
	/** 
	 * 取得Transd  by action 回傳 HashSet ;
	 * @param Hashtable<String,String>(key=programid) 
	 * @return HashSet<String>
	 */
	public BeanContainer<String,Transd> getTransd_ACTION(String groupid){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			String RETRIEVE_STMT_ACTION = "SELECT * FROM TRANSD WHERE GROUPID =? order by SORT_KEY ";
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",groupid));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_GROUPID ,hashSet);	
			beanContainer =TransdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	
	
	private String RETRIEVE_STMT_GROUPID = "SELECT * FROM TRANSD WHERE GROUPID =? order by SORT_KEY ";
	/** 
	 * 取得Transd  by groupid 回傳 BeanContainer ;
	 * @param bean 
	 * @return BeanContainer<String,Transd>
	 */
	public BeanContainer<String,Transd> getTransd_GroupId(String groupid){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			//如果groupid沒有"_"表示查全部子選單的項目
			if (groupid.indexOf("_")==-1){
				RETRIEVE_STMT_GROUPID=" select * from transd where substr(groupid,1,2)=? order by SORT_KEY";
			}
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",groupid));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_GROUPID ,hashSet);	
			beanContainer =TransdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_PK = "SELECT * FROM TRANSD WHERE PROGRAMID =? AND GROUPID =? ";
	/** 
	 * 取得Transd  PrimaryKey 回傳 BeanContainer ;
	 * @param bean 
	 * @return BeanContainer<String,Transd>
	 */	
	public BeanContainer<String,Transd> getTransd_PKs(Transd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Transd retBean =new Transd();
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getGroupid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =TransdIndexToBean(container);
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}	

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM TRANSD WHERE PROGRAMID =? AND GROUPID =? ";
	/** 
	 * 取得Transd  PrimaryKey 回傳 Transd ;
	 * @param bean 
	 * @return
	 */
	public Transd getTransd_PK(Transd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Transd retbean=new Transd();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getGroupid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =TransdIndexToBean(container);
				if (beanContainer.size()>0){
					BeanItem<Transd> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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
	
	private String RETRIEVE_STMT_SORT = "SELECT max(SORT_KEY)+1 as SORT_KEY FROM TRANSD ";
	/** 
	 * 取得Transd  目前排序最大值 回傳 sort_key+1 ;
	 * @param bean 
	 * @return
	 */
	public String getTransd_MaxSortKey(){
		IndexedContainer container = new IndexedContainer();
		this.ErrMsg="";
		String sortKey="";
			try{
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_SORT);
				Item item=container.getItem(container.getIdByIndex(0));
				sortKey =item.getItemProperty("SORT_KEY").getValue()+"";
			} catch (RuntimeException re) {
				logger.error(StrUtil.convException(re));	
				this.ErrMsg=re.toString();	
			}catch (SQLException se) {
				logger.error(StrUtil.convException(se));	
				this.ErrMsg=se.toString();	
			}
		return sortKey;
	}	

	//取得Transd  PrimaryKey 回傳 Transd ;
	public Transd getBeanByKeys(String[] keys){
		Transd qryBean = new Transd();
		// TODO 確認是否產生 setFileNameUp(keys[i]) 程式碼
		qryBean.setProgramid(keys[0]);
		qryBean.setGroupid(keys[1]);
        Transd bean = new Transd();
        bean = getTransd_PK(qryBean);
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

	//寫入TRANSD ;
	public boolean insertTransd(Transd bean){
		return insertTransd(null,bean);
	}
	public boolean insertTransd(Connection con ,Transd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO TRANSD( PROGRAMID  ,PROGRAMNAME  ,PROGRAMMODE  ,MENUHIDE  ,CONFIRMMODE  ,SETMODE  " +
						 ",AUTHORIZEMODE  ,REPORTMODE  ,BROWSEMODE  ,DOWNLOADMODE  ,GROUPID  ,PROGRAMTYPE  ,ACTION_CODE ,DEPTACTIONMODE" +
						 ",SORT_KEY ,UPDATE_DATETIME ,UPDATE_USER) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,? ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getProgramname()));
						hashSet.add(DBUtil.setData(3,"String",bean.getProgrammode()));
						hashSet.add(DBUtil.setData(4,"String",bean.getMenuhide()));
						hashSet.add(DBUtil.setData(5,"String",bean.getConfirmmode()));
						hashSet.add(DBUtil.setData(6,"String",bean.getSetmode()));
						hashSet.add(DBUtil.setData(7,"String",bean.getAuthorizemode()));
						hashSet.add(DBUtil.setData(8,"String",bean.getReportmode()));
						hashSet.add(DBUtil.setData(9,"String",bean.getBrowsemode()));
						hashSet.add(DBUtil.setData(10,"String",bean.getDownloadmode()));
						hashSet.add(DBUtil.setData(11,"String",bean.getGroupid()));
						hashSet.add(DBUtil.setData(12,"String",bean.getProgramtype()));
						hashSet.add(DBUtil.setData(13,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(14,"String",bean.getDeptactionmode()));
						hashSet.add(DBUtil.setData(15,"String",bean.getSortKey()));
						hashSet.add(DBUtil.setData(16,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(17,"String",bean.getUpdateUser()));
						
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改TRANSD ;
	public boolean updateTransd(Transd bean){
		return updateTransd(null,bean);
	}	
	public boolean updateTransd(Connection con ,Transd bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE TRANSD set ";
				sql+=" PROGRAMNAME =? ";
				sql+=", PROGRAMMODE =? ";
				sql+=", MENUHIDE =? ";
				sql+=", CONFIRMMODE =? ";
				sql+=", SETMODE =? ";
				sql+=", AUTHORIZEMODE =? ";
				sql+=", REPORTMODE =? ";
				sql+=", BROWSEMODE =? ";
				sql+=", DOWNLOADMODE =? ";
				sql+=", PROGRAMTYPE =? ";
				sql+=", ACTION_CODE =? ";
				sql+=", DEPTACTIONMODE =? ";
				sql+=", SORT_KEY =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND GROUPID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramname()));
				hashSet.add(DBUtil.setData(2,"String",bean.getProgrammode()));
				hashSet.add(DBUtil.setData(3,"String",bean.getMenuhide()));
				hashSet.add(DBUtil.setData(4,"String",bean.getConfirmmode()));
				hashSet.add(DBUtil.setData(5,"String",bean.getSetmode()));
				hashSet.add(DBUtil.setData(6,"String",bean.getAuthorizemode()));
				hashSet.add(DBUtil.setData(7,"String",bean.getReportmode()));
				hashSet.add(DBUtil.setData(8,"String",bean.getBrowsemode()));
				hashSet.add(DBUtil.setData(9,"String",bean.getDownloadmode()));
				hashSet.add(DBUtil.setData(10,"String",bean.getProgramtype()));
				hashSet.add(DBUtil.setData(11,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(12,"String",bean.getDeptactionmode()));
				hashSet.add(DBUtil.setData(13,"String",bean.getSortKey()));
				hashSet.add(DBUtil.setData(14,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(15,"String",bean.getUpdateUser()));				
				hashSet.add(DBUtil.setData(16,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(17,"String",bean.getGroupid()));

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
	 * 依據傳入欄位.修改TRANSD ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateTransd(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE TRANSD set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND GROUPID =? ";
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

	//刪除TRANSD ;
	public boolean deleteTransd(Transd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  TRANSD ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND GROUPID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getGroupid()));
			isUpdate=DBUtil.UpdateDB(sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Transd setTransd(Item item){
		Transd bean =new Transd();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setProgramname(item.getItemProperty("PROGRAMNAME").getValue()+"");
			bean.setProgrammode(item.getItemProperty("PROGRAMMODE").getValue()+"");
			bean.setMenuhide(item.getItemProperty("MENUHIDE").getValue()+"");
			bean.setConfirmmode(item.getItemProperty("CONFIRMMODE").getValue()+"");
			bean.setSetmode(item.getItemProperty("SETMODE").getValue()+"");
			bean.setAuthorizemode(item.getItemProperty("AUTHORIZEMODE").getValue()+"");
			bean.setReportmode(item.getItemProperty("REPORTMODE").getValue()+"");
			bean.setBrowsemode(item.getItemProperty("BROWSEMODE").getValue()+"");
			bean.setDownloadmode(item.getItemProperty("DOWNLOADMODE").getValue()+"");
			bean.setGroupid(item.getItemProperty("GROUPID").getValue()+"");
			bean.setProgramtype(item.getItemProperty("PROGRAMTYPE").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setDeptactionmode(item.getItemProperty("DEPTACTIONMODE").getValue()+"");			
			bean.setSortKey(item.getItemProperty("SORT_KEY").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Transd> TransdIndexToBean(IndexedContainer container){
		BeanContainer<String,Transd> beanContainer =new BeanContainer<String,Transd>(Transd.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Transd bean =setTransd(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
