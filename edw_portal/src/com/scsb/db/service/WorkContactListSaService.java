package com.scsb.db.service; 

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.scsb.db.bean.WorkContactListSa;
import com.scsb.db.DBAction;
import com.scsb.db.DBResultSet;
import com.scsb.util.DBUtil;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;

public class WorkContactListSaService{
	private static Logger logger = Logger.getLogger(WorkContactListSaService.class.getName());
	public String ErrMsg ="";
	private String lang ="";
	private String dbPool ="";
	public WorkContactListSaService() {
		this.dbPool="defaultPool";
		this.lang="tw";	
	}

	public WorkContactListSaService(String lang) {
		this.lang=lang;	
	}

	public void setDbPool(String dbPool) {
		this.dbPool=dbPool;	
	}

	private String RETRIEVE_STMT_ALL = "SELECT * FROM WORK_CONTACT_LIST_SA ";
	/**
 	 * 取得WorkContactListSa資料表中,所有資料
 	 * @return BeanContainer<String,WorkContactListSa>
 	 */	
	public BeanContainer<String,WorkContactListSa> getWorkContactListSa_All(){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,WorkContactListSa> beanContainer =new BeanContainer<String,WorkContactListSa>(WorkContactListSa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALL);	
			beanContainer =WorkContactListSaIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return beanContainer;
	}
	
	private String RETRIEVE_STMT_MAXID = "SELECT max(ITEMID) as ID FROM WORK_CONTACT_LIST_SA ";
	/**
 	 * 取得WorkContactListSa資料表中,最大流水號+1
 	 * @return BeanContainer<String,WorkContactListSa>
 	 */	
	public int getWorkContactListSa_maxid(){
		IndexedContainer container = new IndexedContainer();
		int imaxId=1;
		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_MAXID);
			if (container.size() > 0){
				imaxId =Integer.parseInt(container.getItem(container.getIdByIndex(0)).getItemProperty("ID").getValue().toString());
			}
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return imaxId;
	}

	
	private String RETRIEVE_STMT_INVDATE = "SELECT distinct invdate_str ,invdate_end"
			+ " FROM WORK_CONTACT_LIST_SA "
			+ " order by invdate_str ";
	/**
 	 * 取得WorkContactListSa資料表中,全部的期間帶
 	 * @return BeanContainer<String,WorkContactListSa>
 	 */	
	public Hashtable<String ,String> getWorkContactListSa_invdate(){
		IndexedContainer container = new IndexedContainer();
		Hashtable<String ,String> inv =new Hashtable<String ,String>();
		int imaxId=1;
		this.ErrMsg="";
		try{
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_INVDATE);
			if (container.size() > 0){
				for(Iterator iter=container.getItemIds().iterator() ;iter.hasNext();){
					Item item=container.getItem(iter.next());
					String invStr=item.getItemProperty("INVDATE_STR").getValue().toString();
					String invEnd=item.getItemProperty("INVDATE_END").getValue().toString();
					inv.put(invStr, invEnd);
				}
			}
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.getMessage()+"";	
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString()+"";	
		}
		return inv;
	}	
	
	private String RETRIEVE_STMT_PK = "SELECT * FROM WORK_CONTACT_LIST_SA WHERE ITEMID =? ";
	//取得WorkContactListSa  PrimaryKey ;
	public BeanContainer<String,WorkContactListSa> getWorkContactListSa_PKs(WorkContactListSa bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,WorkContactListSa> beanContainer =new BeanContainer<String,WorkContactListSa>(WorkContactListSa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"double",bean.getItemid()));
			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	
			beanContainer =WorkContactListSaIndexToBean(container);	
		} catch (RuntimeException re) {
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString(); 
		}catch (SQLException se) {
			logger.error(StrUtil.convException(se));	
			this.ErrMsg=se.toString(); 
		}
		return beanContainer;
	}

	private String RETRIEVE_STMT_PK2 = "SELECT * FROM WORK_CONTACT_LIST_SA WHERE ITEMID =? ";
	/** 
	 * 取得WorkContactListSa  PrimaryKey 回傳 WorkContactListSa ;
	 * @param bean 
	 * @return
	 */
	public WorkContactListSa getWorkContactListSa_PK(WorkContactListSa bean){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,WorkContactListSa> beanContainer =new BeanContainer<String,WorkContactListSa>(WorkContactListSa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		WorkContactListSa retbean=new WorkContactListSa();
		if (bean != null){
			try{
				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
				hashSet.add(DBUtil.setData(1,"double",bean.getItemid()));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	
				beanContainer =WorkContactListSaIndexToBean(container);	
				if (beanContainer.size()>0){
					BeanItem<WorkContactListSa> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));
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

	//包含check
	private String RETRIEVE_STMT_BRH_AREA1 = ""
			+ "select distinct a.*  from work_contact_list_sa	 a"
			+ "    ,(  select distinct cust_idn , max(crd_datetime) as crd_datetime"
			+ "			     from work_contact_list_sa"
			+ "			     	 where INVDATE_STR=?"
			+ "		 					 and ischeck not in('D') "
			+ "				      group by cust_idn	  )	b"
			+ "  		where a.INVDATE_STR=? and a.show_brh=?"
			+ "			 and a.ischeck not in('D')  "
			+ "		 	and a.cust_idn =b.cust_idn and a.crd_datetime =b.crd_datetime"
			+ "   order by a.cust_idn ";	
	//不含check
	private String RETRIEVE_STMT_BRH_AREA2 = ""
			+ "select distinct a.*  from work_contact_list_sa	 a"
			+ "    ,(  select distinct cust_idn , max(crd_datetime) as crd_datetime"
			+ "			     from work_contact_list_sa"
			+ "			     	 where INVDATE_STR=?"
			+ "		 					 and ischeck not in('D') "
			+ "				      group by cust_idn	  )	b"			
			+ "  		where a.INVDATE_STR=? and a.show_brh=?"
			+ "			 and a.ischeck not in('D','N')   "
			+ "		 	 and a.cust_idn =b.cust_idn and a.crd_datetime =b.crd_datetime"
			+ "   order by a.cust_idn ";
	//僅限異動check 中
	private String RETRIEVE_STMT_BRH_AREA3 = ""
			+ "	select *  from work_contact_list_sa	"
			+ "  		where INVDATE_STR=? and show_brh=?"
			+ "			 and ischeck in('N')	  "
			+ "   order by cust_idn ";	
	//未撤件
	private String RETRIEVE_STMT_BRH_AREA4 = ""
			+ "select distinct a.*  from work_contact_list_sa	 a"
			+ "    ,(  select distinct cust_idn , max(crd_datetime) as crd_datetime"
			+ "			     from work_contact_list_sa"
			+ "			     	 where INVDATE_STR=?"
			+ "		 					and ischeck in('Y')	and (reset not in ('Y','N') or reset is null)"
			+ "				      group by cust_idn	  )	b"
			+ " 		where a.INVDATE_STR=?   and a.show_brh=?"
			+ "		 and a.ischeck  in('Y')	and (a.reset not in ('Y','N') or a.reset is null)"
			+ "		 and a.cust_idn =b.cust_idn and a.crd_datetime =b.crd_datetime"
			+ "  order by a.cust_idn";	
	//僅限 已撤件
	private String RETRIEVE_STMT_BRH_AREA5 = ""
			+ "	select *  from work_contact_list_sa	"
			+ "  		where INVDATE_STR=? and show_brh=?"
			+ "			 and reset in ('Y')	  "
			+ "   order by cust_idn ";	
	//僅限 撤件check 中
	private String RETRIEVE_STMT_BRH_AREA6 = ""
			+ "	select *  from work_contact_list_sa	"
			+ "  		where INVDATE_STR=? and show_brh=? "
			+ "			 and reset in ('N')	  "
			+ "   order by cust_idn ";		
	
	//僅限 新承作且ao code為空值者(未認列)
	private String RETRIEVE_STMT_BRH_AREA7 = ""
			+ "	select a.*  from work_contact_list_sa a	"
			+ "    ,(  select distinct cust_idn , max(crd_datetime) as crd_datetime"
			+ "			     from work_contact_list_sa"
			+ "			     	 where INVDATE_STR=?"
			+ "		 					 and ischeck not in('D') "
			+ "				      group by cust_idn	  )	b"		
			+ "  		where a.INVDATE_STR=? and a.show_brh=?"
			+ "			 and (a.ao_code is null	or  a.ao_code='') "
			+ "		 	 and a.cust_idn =b.cust_idn and a.crd_datetime =b.crd_datetime"
			+ "   order by a.cust_idn ";	
	//不含check 及空白ao_code(已認列)
	private String RETRIEVE_STMT_BRH_AREA8 = ""
			+ "select distinct a.*  from work_contact_list_sa	 a"
			+ "    ,(  select distinct cust_idn , max(crd_datetime) as crd_datetime"
			+ "			     from work_contact_list_sa"
			+ "			     	 where INVDATE_STR=?"
			+ "		 					 and ischeck not in('D') "
			+ "				      group by cust_idn	  )	b"			
			+ "  		where a.INVDATE_STR=? and a.show_brh=?"
			+ "			 and a.ischeck not in('D','N')  and a.ao_code is not null  "
			+ "		 	 and a.cust_idn =b.cust_idn and a.crd_datetime =b.crd_datetime"
			+ "   order by a.cust_idn ";	
	/**
	 * 取得同一期最新認列的名單資料 ;
	 * @param brh_cod
	 * @param invdate_str
	 * @param iCheckType :1=包含check ,2=不含check ,3=僅限check 中
	 * @return
	 */
	public BeanContainer<String,WorkContactListSa> getWorkContactListSa_brh_area(String brh_cod ,String invdate_str ,String checkType){
		IndexedContainer container = new IndexedContainer();
		BeanContainer<String ,WorkContactListSa> beanContainer =new BeanContainer<String,WorkContactListSa>(WorkContactListSa.class);
		beanContainer.setBeanIdProperty("beanKey");

		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			if (checkType.equals("2")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",invdate_str));		
				hashSet.add(DBUtil.setData(3,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA2 ,hashSet);
			}else if (checkType.equals("3")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA3 ,hashSet);
			}else if (checkType.equals("4")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",invdate_str));		
				hashSet.add(DBUtil.setData(3,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA4 ,hashSet);
			}else if (checkType.equals("5")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA5 ,hashSet);
			}else if (checkType.equals("6")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA6 ,hashSet);
			}else if (checkType.equals("7")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",invdate_str));
				hashSet.add(DBUtil.setData(3,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA7 ,hashSet);
			}else if (checkType.equals("8")){
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",invdate_str));
				hashSet.add(DBUtil.setData(3,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA8 ,hashSet);
			}else {
				hashSet.add(DBUtil.setData(1,"String",invdate_str));
				hashSet.add(DBUtil.setData(2,"String",invdate_str));		
				hashSet.add(DBUtil.setData(3,"String",brh_cod));
				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_BRH_AREA1 ,hashSet);
			}
			beanContainer =WorkContactListSaIndexToBean(container);	
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

	//寫入WORK_CONTACT_LIST_SA ;
	public boolean insertWorkContactListSa(WorkContactListSa bean){
		return insertWorkContactListSa(null,bean);
	}
	public boolean insertWorkContactListSa(Connection con ,WorkContactListSa bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "INSERT INTO WORK_CONTACT_LIST_SA( ITEMID  ,CUST_IDN  ,MEET_TIMES  ,INVDATE_STR  ,INVDATE_END  ,AO_CODE  ,LIST_TYPE  ,BRH_COD  ,CHIN_ACT  ,TEL1  ,TEL2  ,DATADATE  ,TITLE  ,DB  ,CLASS  ,INVDATE_SDAY  ,INVDATE_EDAY  ,BDAY  ,CARD_FLAG  ,MOBIL_PH  ,AO_FLAG  ,MGR_EMP  ,RESP_NO  ,PAO_ID  ,CRD_USER   ,ISCHECK  ,CHANGE_FLAG  ,ACU_AVG  ,TOGETHERID  ,RESET  ,SHOW_BRH ) ";
					sql+=" VALUES (  ?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?  ,?   ,?  ,?  ,?  ,?  ,?  ,? )";
						hashSet.add(DBUtil.setData(1,"double",bean.getItemid()));
						hashSet.add(DBUtil.setData(2,"String",bean.getCustIdn()));
						hashSet.add(DBUtil.setData(3,"double",bean.getMeetTimes()));
						hashSet.add(DBUtil.setData(4,"String",bean.getInvdateStr()));
						hashSet.add(DBUtil.setData(5,"String",bean.getInvdateEnd()));
						hashSet.add(DBUtil.setData(6,"String",bean.getAoCode()));
						hashSet.add(DBUtil.setData(7,"String",bean.getListType()));
						hashSet.add(DBUtil.setData(8,"String",bean.getBrhCod()));
						hashSet.add(DBUtil.setData(9,"String",bean.getChinAct()));
						hashSet.add(DBUtil.setData(10,"String",bean.getTel1()));
						hashSet.add(DBUtil.setData(11,"String",bean.getTel2()));
						hashSet.add(DBUtil.setData(12,"String",bean.getDatadate()));
						hashSet.add(DBUtil.setData(13,"String",bean.getTitle()));
						hashSet.add(DBUtil.setData(14,"String",bean.getDb()));
						hashSet.add(DBUtil.setData(15,"String",bean.get_Class()));
						hashSet.add(DBUtil.setData(16,"String",bean.getInvdateSday()));
						hashSet.add(DBUtil.setData(17,"String",bean.getInvdateEday()));
						hashSet.add(DBUtil.setData(18,"String",bean.getBday()));
						hashSet.add(DBUtil.setData(19,"String",bean.getCardFlag()));
						hashSet.add(DBUtil.setData(20,"String",bean.getMobilPh()));
						hashSet.add(DBUtil.setData(21,"String",bean.getAoFlag()));
						hashSet.add(DBUtil.setData(22,"String",bean.getMgrEmp()));
						hashSet.add(DBUtil.setData(23,"String",bean.getRespNo()));
						hashSet.add(DBUtil.setData(24,"String",bean.getPaoId()));
						hashSet.add(DBUtil.setData(25,"String",bean.getCrdUser()));
						//hashSet.add(DBUtil.setData(26,"String",bean.getCrdDatetime()));
						hashSet.add(DBUtil.setData(26,"String",bean.getIscheck()));
						hashSet.add(DBUtil.setData(27,"String",bean.getChangeFlag()));
						hashSet.add(DBUtil.setData(28,"double",bean.getAcuAvg()));
						hashSet.add(DBUtil.setData(29,"int",bean.getTogetherid()));
						hashSet.add(DBUtil.setData(30,"String",bean.getReset()));
						hashSet.add(DBUtil.setData(31,"String",bean.getShowBrh()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString();
		}
		return isUpdate;
	}

	//修改WORK_CONTACT_LIST_SA ;
	public boolean updateWorkContactListSa(WorkContactListSa bean){
		return updateWorkContactListSa(null,bean);
	}
	public boolean updateWorkContactListSa(Connection con ,WorkContactListSa bean ){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "UPDATE WORK_CONTACT_LIST_SA set ";
				sql+="  CRD_USER =? ";
				sql+=", CRD_DATETIME =sysdate ";
				sql+=", ISCHECK =? ";
				sql+=", RESET =? ";
				sql+=" WHERE ITEMID =? ";
				hashSet.add(DBUtil.setData(1,"String",bean.getCrdUser()));
				hashSet.add(DBUtil.setData(2,"String",bean.getIscheck()));
				hashSet.add(DBUtil.setData(3,"String",bean.getReset()));
				hashSet.add(DBUtil.setData(4,"double",bean.getItemid()));

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
	 * 依據傳入欄位.修改WORK_CONTACT_LIST_SA ;
	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>
	 * @param keysValue PK欄位值
	 * @return
	 **/
	public boolean updateWorkContactListSa(HashMap<String, Object> hmData, String[] keysValue ){
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
				String sql = "UPDATE WORK_CONTACT_LIST_SA set ";
				sql+=sUpdateCode;
				sql+=" WHERE ITEMID =? ";
				hashSet.add(DBUtil.setData(i++,"double",keysValue[0]));
				isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			} catch (RuntimeException se) {
				isUpdate=false;
				logger.error(StrUtil.convException(se)); 
				this.ErrMsg=se.toString(); 
			}
		}
		return isUpdate;
	}

	//刪除WORK_CONTACT_LIST_SA ;
	public boolean deleteWorkContactListSa(WorkContactListSa bean){
		return deleteWorkContactListSa(null,bean);
	}
	public boolean deleteWorkContactListSa(Connection con ,WorkContactListSa bean){
		boolean isUpdate =false;
		this.ErrMsg="";
		try{
			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();
			String sql = "DELETE from  WORK_CONTACT_LIST_SA ";
				sql+=" WHERE ITEMID =? ";

				hashSet.add(DBUtil.setData(1,"double",bean.getItemid()));
			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);
			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);
		} catch (RuntimeException re) {
			isUpdate=false;
			logger.error(StrUtil.convException(re));	
			this.ErrMsg=re.toString()+"";
		}
		return isUpdate;
	}

	private WorkContactListSa setWorkContactListSa(Item item){
		WorkContactListSa bean =new WorkContactListSa();
			double iitemid =0;
			try{
				iitemid =Double.parseDouble(item.getItemProperty("ITEMID").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setItemid(iitemid);
			bean.setCustIdn(item.getItemProperty("CUST_IDN").getValue()+"");
			double imeetTimes =0;
			try{
				imeetTimes =Double.parseDouble(item.getItemProperty("MEET_TIMES").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setMeetTimes(imeetTimes);
			bean.setInvdateStr(item.getItemProperty("INVDATE_STR").getValue()+"");
			bean.setInvdateEnd(item.getItemProperty("INVDATE_END").getValue()+"");
			bean.setAoCode(item.getItemProperty("AO_CODE").getValue()+"");
			bean.setListType(item.getItemProperty("LIST_TYPE").getValue()+"");
			bean.setBrhCod(item.getItemProperty("BRH_COD").getValue()+"");
			bean.setChinAct(item.getItemProperty("CHIN_ACT").getValue()+"");
			bean.setTel1(item.getItemProperty("TEL1").getValue()+"");
			bean.setTel2(item.getItemProperty("TEL2").getValue()+"");
			bean.setDatadate(item.getItemProperty("DATADATE").getValue()+"");
			bean.setTitle(item.getItemProperty("TITLE").getValue()+"");
			bean.setDb(item.getItemProperty("DB").getValue()+"");
			bean.set_Class(item.getItemProperty("CLASS").getValue()+"");
			bean.setInvdateSday(item.getItemProperty("INVDATE_SDAY").getValue()+"");
			bean.setInvdateEday(item.getItemProperty("INVDATE_EDAY").getValue()+"");
			bean.setBday(item.getItemProperty("BDAY").getValue()+"");
			bean.setCardFlag(item.getItemProperty("CARD_FLAG").getValue()+"");
			bean.setMobilPh(item.getItemProperty("MOBIL_PH").getValue()+"");
			bean.setAoFlag(item.getItemProperty("AO_FLAG").getValue()+"");
			bean.setMgrEmp(item.getItemProperty("MGR_EMP").getValue()+"");
			bean.setRespNo(item.getItemProperty("RESP_NO").getValue()+"");
			bean.setPaoId(item.getItemProperty("PAO_ID").getValue()+"");
			bean.setCrdUser(item.getItemProperty("CRD_USER").getValue()+"");
			bean.setCrdDatetime(item.getItemProperty("CRD_DATETIME").getValue()+"");
			bean.setIscheck(item.getItemProperty("ISCHECK").getValue()+"");
			bean.setChangeFlag(item.getItemProperty("CHANGE_FLAG").getValue()+"");
			double iacuAvg =0;
			try{
				iacuAvg =Double.parseDouble(item.getItemProperty("ACU_AVG").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setAcuAvg(iacuAvg);
			int itogetherid =0;
			try{
				itogetherid =Integer.parseInt(item.getItemProperty("TOGETHERID").getValue()+"");
			} catch (NumberFormatException se) {
			}
			bean.setTogetherid(itogetherid);
			bean.setReset(item.getItemProperty("RESET").getValue()+"");
			bean.setShowBrh(item.getItemProperty("SHOW_BRH").getValue()+"");

		return bean;
	}

	private BeanContainer<String,WorkContactListSa> WorkContactListSaIndexToBean(IndexedContainer container){
		BeanContainer<String,WorkContactListSa> beanContainer =new BeanContainer<String,WorkContactListSa>(WorkContactListSa.class);
		beanContainer.setBeanIdProperty("beanKey");
		for(int i=0;i<container.size();i++){
			Item item=container.getItem(container.getIdByIndex(i));
			WorkContactListSa bean =setWorkContactListSa(item);
			beanContainer.addBean(bean);
		}
		return beanContainer;
	}

}
