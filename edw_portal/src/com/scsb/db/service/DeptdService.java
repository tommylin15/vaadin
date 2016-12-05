package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Deptd;
import com.scsb.db.bean.ScsbTitled;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class DeptdService{
	private static Logger logger = Logger.getLogger(DeptdService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public DeptdService() {
	}

	public DeptdService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM DEPTD ";
	/**
 	 * 取得Deptd資料表中,所有資料
 	 * @return BeanContainer<String,Deptd>
 	 */	
	public BeanContainer<String,Deptd> getDeptd_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Deptd> beanContainer =new BeanContainer<String,Deptd>(Deptd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =DeptdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK = "SELECT * FROM DEPTD WHERE PROGRAMID =? AND OU =? ";
	//取得Deptd  PrimaryKey ;
	public BeanContainer<String,Deptd> getDeptd_PKs(Deptd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Deptd> beanContainer =new BeanContainer<String,Deptd>(Deptd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getOu()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =DeptdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_DEPTID = "SELECT * FROM DEPTD WHERE ou =? ";
	//取得ou  PrimaryKey ;
	public BeanContainer<String,Deptd> getDeptd_deptId(Deptd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Deptd> beanContainer =new BeanContainer<String,Deptd>(Deptd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getOu()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_DEPTID ,hashSet);	
			beanContainer =DeptdIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}		

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM DEPTD WHERE PROGRAMID =? AND OU =? ";
	/** 
	 * 取得Deptd  PrimaryKey 回傳 Deptd ;
	 * @param bean 
	 * @return
	 */
	public Deptd getDeptd_PK(Deptd bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Deptd> beanContainer =new BeanContainer<String,Deptd>(Deptd.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Deptd retbean=new Deptd();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getOu()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =DeptdIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Deptd> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入DEPTD ;
	public boolean insertDeptd(Deptd bean){
		return insertDeptd(null,bean);
	}
	public boolean insertDeptd(Connection con ,Deptd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO DEPTD( PROGRAMID  ,ACTION_CODE  ,OU  ,UPDATE_DATETIME  ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getActionCode()));
						hashSet.add(DBUtil.setData(3,"String",bean.getOu()));
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
	 * 整批修改DEPTD ;
	 * @param deptid
	 * @param groupId
	 * @param hashdata
	 * @return
	 */
	public boolean updateDeptdByDeptIdGroupId(String deptId ,String groupId,HashSet<Deptd> hashdata ){
		return updateDeptdByDeptIdGroupId(null,deptId,groupId,hashdata);
	}
	public boolean updateDeptdByDeptIdGroupId(Connection con,String titleName ,String groupId ,HashSet<Deptd> hashdata ){
		boolean isUpdate =false;
		this.ErrMsg="";
		isUpdate=deleteDeptdByDeptIdGroupId(con, titleName ,groupId);
		for(java.util.Iterator<Deptd> iter=hashdata.iterator();iter.hasNext();){
			isUpdate=insertDeptd(con, iter.next());
			if (!isUpdate) break;
		}	
		return isUpdate;
	}		
	
	//修改DEPTD ;
	public boolean updateDeptd(Deptd bean){
		return updateDeptd(null,bean);
	}
	public boolean updateDeptd(Connection con ,Deptd bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE DEPTD set ";
				sql+=" ACTION_CODE =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND OU =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getActionCode()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(3,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(4,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(5,"String",bean.getOu()));

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
	 * 依據傳入欄位.修改DEPTD ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateDeptd(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE DEPTD set ";
				sql+=sUpdateCode;
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND OU =? ";
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
	 * 刪除DEPTD ;
	 * @param deptId
	 * @param groupId
	 * @return
	 */
	public boolean deleteDeptdByDeptIdGroupId(String deptId ,String groupId){
		return deleteDeptdByDeptIdGroupId(null,deptId ,groupId);
	}
	public boolean deleteDeptdByDeptIdGroupId(Connection con ,String deptId ,String groupId){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPTD ";
				sql+=" WHERE  ou =? " +
						" and programid in (select programid from transd where groupid=?) ";

				hashSet.add(DBUtil.setData(1,"String",deptId));
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

	//刪除DEPTD ;
	public boolean deleteDeptd(Deptd bean){
		return deleteDeptd(null,bean);
	}
	public boolean deleteDeptd(Connection con ,Deptd bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  DEPTD ";
				sql+=" WHERE PROGRAMID =? ";
				sql+=" AND OU =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getProgramid()));
				hashSet.add(DBUtil.setData(2,"String",bean.getOu()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Deptd setDeptd(Item item){
		Deptd bean =new Deptd();
			bean.setProgramid(item.getItemProperty("PROGRAMID").getValue()+"");
			bean.setActionCode(item.getItemProperty("ACTION_CODE").getValue()+"");
			bean.setOu(item.getItemProperty("OU").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Deptd> DeptdIndexToBean(IndexedContainer container){
		BeanContainer<String,Deptd> beanContainer =new BeanContainer<String,Deptd>(Deptd.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Deptd bean =setDeptd(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
