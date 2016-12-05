package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Users;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class UsersService{
	private static Logger logger = Logger.getLogger(UsersService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	public UsersService() {
	}

	public UsersService(String lang) {
		this.lang=lang;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM USERS ";
	/**
 	 * 取得Users資料表中,所有資料
 	 * @return BeanContainer<String,Users>
 	 */	
	public BeanContainer<String,Users> getUsers_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Users> beanContainer =new BeanContainer<String,Users>(Users.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_ALL);	
			beanContainer =UsersIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}
	
	//只有0000的單位人員
	private String RETRIEVE_STMT_BRH_USER1 = " select a.USERID ,a.USER_NAME  ,substr(a.deptid,1,2) as BRH_COD ,b.chin_al1 as BRH_NAME"
			+ "  from users a ,fnbct0 b ,ldapemp c "
			+ "  where substr(a.deptid,1,2) =b.brh_cod  and substr(a.deptid,3,4)='0000' "
			+ "    and a.userid=c.userid and c.RETIRED is null"
			+ "  order by BRH_COD ,USERID ";
	
	//只有0000的單位人員+不是AA
	private String RETRIEVE_STMT_BRH_USER11= " select a.USERID ,a.USER_NAME  ,substr(a.deptid,1,2) as BRH_COD ,b.chin_al1 as BRH_NAME"
			+ "  from users a ,fnbct0 b ,ldapemp c "
			+ "  where substr(a.deptid,1,2) =b.brh_cod  and substr(a.deptid,3,4)='0000' "
			+ "    and a.userid=c.userid and c.RETIRED is null"
			+ "    and a.title_name not like '%AA' "
			+ "  order by BRH_COD ,USERID ";	
	
	//全行人員
	private String RETRIEVE_STMT_BRH_USER2 = " select a.USERID ,a.USER_NAME  ,substr(a.deptid,1,2) as BRH_COD ,b.chin_al1 as BRH_NAME"
			+ "  from users a ,fnbct0 b ,ldapemp c "
			+ "  where substr(a.deptid,1,2) =b.brh_cod   "
			+ "    and a.userid=c.userid and c.RETIRED is null"
			+ "  order by BRH_COD ,USERID ";
	
	//全行人員+不是AA
	private String RETRIEVE_STMT_BRH_USER22 = " select a.USERID ,a.USER_NAME  ,substr(a.deptid,1,2) as BRH_COD ,b.chin_al1 as BRH_NAME"
			+ "  from users a ,fnbct0 b ,ldapemp c "
			+ "  where substr(a.deptid,1,2) =b.brh_cod   "
			+ "    and a.userid=c.userid and c.RETIRED is null"
			+ "    and a.title_name not like '%AA' "
			+ "  order by BRH_COD ,USERID ";	
	/**
 	 * 取得Users資料表中,單位名稱和使用者名稱
 	 * @return BeanContainer<String,Users>
 	 */	
	public IndexedContainer getUsers_Brhall(boolean is0000 ,boolean haveAA){
		IndexedContainer container = new IndexedContainer();
		this.ErrMsg="";
		try{
			if (is0000 & haveAA){
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_BRH_USER1);
			}else if (is0000 & !haveAA){
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_BRH_USER11);
			}else if (!is0000 & haveAA){
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_BRH_USER2);
			}else if (!is0000 & !haveAA){
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_BRH_USER22);
			}
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return container;
	}	
	
	private String RETRIEVE_STMT_DEPT = "SELECT * FROM USERS" +
			"	WHERE deptid in (select ou from LDAPOU where manacctno=?)" +
			"      or deptid in (select a.deptid from users a ,usersp b" +
			"						where a.userid=? and a.userid =b.userid" +
			"						  and b.property_key='ROLES' and b.property_value='A0500' " +
			"							)";
	/**
 	 * 取得Users資料表中,同一單位主管的USERS資料 ,另加一個特別的單位主管角色
 	 * @return BeanContainer<String,Users>
 	 */	
	public BeanContainer<String,Users> getDeptUsers(Users bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Users> beanContainer =new BeanContainer<String,Users>(Users.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			hashSet.add(DBUtil.setData(2,"String",bean.getUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_DEPT ,hashSet);	
			beanContainer =UsersIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}	

	private String RETRIEVE_STMT_PK = "SELECT * FROM USERS WHERE USERID =? ";
	/**
	 * 取得Users  PrimaryKey ;
	 * @param bean
	 * @return
	 */
	public BeanContainer<String,Users> getUsers_PKs(Users bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Users> beanContainer =new BeanContainer<String,Users>(Users.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =UsersIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM USERS WHERE USERID =? ";
	/** 
	 * 取得Users  PrimaryKey 回傳 Users ;
	 * @param bean 
	 * @return
	 */
	public Users getUsers_PK(Users bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,Users> beanContainer =new BeanContainer<String,Users>(Users.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		Users retbean=new Users();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
				container = DBUtil.ContainerQueryDB(RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =UsersIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<Users> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//寫入USERS ;
	public boolean insertUsers(Users bean){
		return insertUsers(null,bean);
	}
	public boolean insertUsers(Connection con ,Users bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO USERS( USERID  ,USER_NAME  ,USERLEVEL  ,TITLE_NAME  ,CARDTITLE " +
						 " ,BRANCHID  ,EMAIL ,DEPTID ,UPDATE_DATETIME ,UPDATE_USER ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,? ,? ,? ,?)";
						hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getUserName()));
						hashSet.add(DBUtil.setData(3,"String",bean.getUserlevel()));
						hashSet.add(DBUtil.setData(4,"String",bean.getTitleName()));
						hashSet.add(DBUtil.setData(5,"String",bean.getCardtitle()));
						hashSet.add(DBUtil.setData(6,"String",bean.getBranchid()));
						hashSet.add(DBUtil.setData(7,"String",bean.getEmail()));
						hashSet.add(DBUtil.setData(8,"String",bean.getDeptid()));
						hashSet.add(DBUtil.setData(9,"String",bean.getUpdateDatetime()));
						hashSet.add(DBUtil.setData(10,"String",bean.getUpdateUser()));
						
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改USERS ;
	public boolean updateUsers(Users bean){
		return updateUsers(null,bean);
	}
	public boolean updateUsers(Connection con ,Users bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE USERS set ";
				sql+=" USER_NAME =? ";
				sql+=", USERLEVEL =? ";
				sql+=", TITLE_NAME =? ";
				sql+=", CARDTITLE =? ";
				sql+=", BRANCHID =? ";
				sql+=", EMAIL =? ";
				sql+=", DEPTID =? ";
				sql+=", UPDATE_DATETIME =? ";
				sql+=", UPDATE_USER =? ";				
				sql+=" WHERE USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserName()));
				hashSet.add(DBUtil.setData(2,"String",bean.getUserlevel()));
				hashSet.add(DBUtil.setData(3,"String",bean.getTitleName()));
				hashSet.add(DBUtil.setData(4,"String",bean.getCardtitle()));
				hashSet.add(DBUtil.setData(5,"String",bean.getBranchid()));
				hashSet.add(DBUtil.setData(6,"String",bean.getEmail()));
				hashSet.add(DBUtil.setData(7,"String",bean.getDeptid()));
				hashSet.add(DBUtil.setData(8,"String",bean.getUpdateDatetime()));
				hashSet.add(DBUtil.setData(9,"String",bean.getUpdateUser()));
				hashSet.add(DBUtil.setData(10,"String",bean.getUserid()));

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
	 * 依據傳入欄位.修改USERS ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateUsers(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE USERS set ";
				sql+=sUpdateCode;
				sql+=" WHERE USERID =? ";
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

	//刪除USERS ;
	public boolean deleteUsers(Users bean){
		return deleteUsers(null,bean);
	}
	public boolean deleteUsers(Connection con ,Users bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  USERS ";
				sql+=" WHERE USERID =? ";

				hashSet.add(DBUtil.setData(1,"String",bean.getUserid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private Users setUsers(Item item){
		Users bean =new Users();
			bean.setUserid(item.getItemProperty("USERID").getValue()+"");
			bean.setUserName(item.getItemProperty("USER_NAME").getValue()+"");
			bean.setUserlevel(item.getItemProperty("USERLEVEL").getValue()+"");
			bean.setTitleName(item.getItemProperty("TITLE_NAME").getValue()+"");
			bean.setCardtitle(item.getItemProperty("CARDTITLE").getValue()+"");
			bean.setBranchid(item.getItemProperty("BRANCHID").getValue()+"");
			bean.setEmail(item.getItemProperty("EMAIL").getValue()+"");
			bean.setDeptid(item.getItemProperty("DEPTID").getValue()+"");
			bean.setUpdateDatetime(item.getItemProperty("UPDATE_DATETIME").getValue()+"");
			bean.setUpdateUser(item.getItemProperty("UPDATE_USER").getValue()+"");

		return bean;
	}

	private BeanContainer<String,Users> UsersIndexToBean(IndexedContainer container){
		BeanContainer<String,Users> beanContainer =new BeanContainer<String,Users>(Users.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			Users bean =setUsers(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
