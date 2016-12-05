package com.scsb.crpro.build.code;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import com.scsb.db.customfield.TextField4;
//import com.scsb.util.DBUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

public class ServiceCode {
	
	public String Code ="";
	
	String BR="\r\n";
	String serviceName="";
	String servicePackage="";
	String beanName="";
	String beanPackage="";
	String tableName="";
	String fileType ="";	
	IndexedContainer container;		
	
	String columnName="";
	int    columnSize=0;
	String fieldName ="";
	String fieldNameUp ="";
	String fieldType  ="";
	String DATA_TYPE ="";
	String DECIMAL_DIGITS ="";
	String PrimaryKey="";
	String BeanKey="";
	String LogKey="";
	String isQueryKey="";
	
	public ServiceCode(String serviceName ,String servicePackage 
			          ,String beanName ,String beanPackage
			          ,String tableName ,String fileType 		
			          ,IndexedContainer container){
		this.serviceName=serviceName;
		this.servicePackage=servicePackage;
		this.beanName=beanName;
		this.beanPackage=beanPackage;
		this.tableName=tableName;
		this.fileType=fileType;
		this.container=container;

		String sCode="";
		if (container.size() > 0){
			sCode+="package "+servicePackage+"; "+BR+BR;
			sCode+="import java.sql.Connection;"+BR;
			sCode+="import java.sql.SQLException;"+BR+BR;
			sCode+="import java.util.HashSet;"+BR;
			sCode+="import java.util.Hashtable;"+BR;
			sCode+="import java.util.HashMap;"+BR;
			sCode+="import java.util.Iterator;"+BR;
			sCode+="import java.util.Map.Entry;"+BR+BR;
			sCode+="import org.apache.log4j.Logger;"+BR+BR;
			sCode+="import "+beanPackage+"."+beanName+";"+BR;
			sCode+="import com.scsb.db.DBAction;"+BR;
			sCode+="import com.scsb.db.DBResultSet;"+BR;			
			sCode+="import com.scsb.util.DBUtil;"+BR;
			sCode+="import com.scsb.util.StrUtil;"+BR;			
			sCode+="import com.vaadin.data.Item;"+BR;
			sCode+="import com.vaadin.data.util.BeanContainer;"+BR;
			sCode+="import com.vaadin.data.util.BeanItem;"+BR;
			sCode+="import com.vaadin.data.util.IndexedContainer;"+BR+BR;			
			sCode+="public class "+serviceName+"{"+BR;
			sCode+="	private static Logger logger = Logger.getLogger("+serviceName+".class.getName());"+BR;
			sCode+="	public String ErrMsg =\"\";"+BR;
			sCode+="	private String lang =\"\";"+BR;
			sCode+="	private String dbPool =\"\";"+BR;
			sCode+="	public "+serviceName+"() {"+BR;
			sCode+="		this.dbPool=\"defaultPool\";"+BR+BR;
			sCode+="		this.lang=\"tw\";	"+BR+BR;
			sCode+="	}"+BR+BR;
			sCode+="	public "+serviceName+"(String lang) {"+BR;
			sCode+="		this.lang=lang;	"+BR;
			sCode+="	}"+BR+BR;
			sCode+="public void setDbPool(String dbPool) {"+BR;
			sCode+="		this.dbPool=dbPool;	"+BR;
			sCode+="	}"+BR+BR;
			sCode+=		getQueryAll()+BR;
			sCode+=		getQueryPK()+BR;
			sCode+=		getQueryPKretBean()+BR;
			sCode+=		getCaseSnQuery()+BR;
			sCode+=		getQueryValue()+BR;
			sCode+=		getQueryResultSet()+BR;
			sCode+=		getInsert()+BR;
			sCode+=		getUpdate()+BR;
			sCode+=		getUpdateFlds()+BR;			
			sCode+=		getDelete()+BR;
			sCode+=		getSetBean()+BR;
			sCode+="}"+BR;	
		}		
		this.Code=sCode;
	}
	
	//建立查詢語法 for all
	public String getQueryAll(){
		String sCode ="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
			}	
			sCode+="	private String RETRIEVE_STMT_ALL = \"SELECT * FROM "+tableName+" \";"+BR;			
			sCode+="	/**"+BR;	
			sCode+=" 	 * 取得"+ beanName +"資料表中,所有資料"+BR;	
			sCode+=" 	 * @return BeanContainer<String,"+beanName+">"+BR;	
			sCode+=" 	 */	"+BR;			
			sCode+="	public BeanContainer<String,"+beanName+"> get"+beanName+"_All(){"+BR;
			sCode+="		IndexedContainer container = new IndexedContainer();"+BR;
			sCode+="		BeanContainer<String ,"+beanName+"> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+".class);"+BR;
			sCode+="		beanContainer.setBeanIdProperty(\"beanKey\");"+BR+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;	
			sCode+="			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_ALL);	"+BR;			
			sCode+="			beanContainer ="+beanName+"IndexToBean(container);	"+BR;			
			sCode+="		} catch (RuntimeException re) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;		
			sCode+="			this.ErrMsg=re.getMessage()+\"\";	"+BR;			
			sCode+="		}catch (SQLException se) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(se));	"+BR;
			sCode+="			this.ErrMsg=se.toString()+\"\";	"+BR;
			sCode+="		}"+BR;	
			sCode+="		return beanContainer;"+BR;	
			sCode+="	}"+BR;					
		}
		return sCode;
	}
	
	//建立查詢語法 for pk
	public String getQueryPK(){
		String sCode ="";
		String sPKCode="";
		String sSetData="";
		if (container.size() > 0){
			//處理 PrimaryKey
			int iIndex=0;
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) >= 0){
		        	if (sPKCode.length()==0) 
		        		sPKCode+=" WHERE "+columnName+" =?";
		        	else 
		        		sPKCode+=" AND "+columnName+" =?";
			        //setData
					iIndex++;
		        	sSetData+="				hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        	
		        }
			}			
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
			}
			sCode+="	private String RETRIEVE_STMT_PK = \"SELECT * FROM "+tableName+sPKCode+" \";"+BR;			
			sCode+="	//取得"+beanName+"  PrimaryKey ;"+BR;
			sCode+="	public BeanContainer<String,"+beanName+"> get"+beanName+"_PKs("+beanName+" bean){"+BR;
			sCode+="		IndexedContainer container = new IndexedContainer();"+BR;
			sCode+="		BeanContainer<String ,"+beanName+"> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+".class);"+BR;
			sCode+="		beanContainer.setBeanIdProperty(\"beanKey\");"+BR+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;
			sCode+="			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+=				sSetData;
			sCode+="			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK ,hashSet);	"+BR;			
			sCode+="			beanContainer ="+beanName+"IndexToBean(container);	"+BR;			
			sCode+="		} catch (RuntimeException re) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;		
			sCode+="			this.ErrMsg=re.toString(); "+BR;			
			sCode+="		}catch (SQLException se) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(se));	"+BR;
			sCode+="			this.ErrMsg=se.toString(); "+BR;	
			sCode+="		}"+BR;			
			sCode+="		return beanContainer;"+BR;
			sCode+="	}"+BR;					
		}
		return sCode;
	}
	
	//建立查詢語法 for pk return Bean
	public String getQueryPKretBean(){
		String sCode ="";
		String sPKCode="";
		String sSetData="";
		if (container.size() > 0){
			//處理 PrimaryKey
			int iIndex=0;
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) >= 0){
		        	if (sPKCode.length()==0)
		        		sPKCode+=" WHERE "+columnName+" =?";
		        	else 
		        		sPKCode+=" AND "+columnName+" =?";
			        //setData
					iIndex++;
		        	sSetData+="				hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        	
		        }
			}			
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
			}
			sCode+="	private String RETRIEVE_STMT_PK2 = \"SELECT * FROM "+tableName+sPKCode+" \";"+BR;			
			sCode+="	/** "+BR;
			sCode+="	 * 取得"+beanName+"  PrimaryKey 回傳 "+beanName+" ;"+BR;
			sCode+="	 * @param bean "+BR;
			sCode+="	 * @return"+BR;
			sCode+="	 */"+BR;
			sCode+="	public "+beanName+" get"+beanName+"_PK("+beanName+" bean){"+BR;
			sCode+="		IndexedContainer container = new IndexedContainer();"+BR;
			sCode+="		BeanContainer<String ,"+beanName+"> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+".class);"+BR;
			sCode+="		beanContainer.setBeanIdProperty(\"beanKey\");"+BR+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		"+beanName+" retbean=new "+beanName+"();"+BR;
			sCode+="		if (bean != null){"+BR;			
			sCode+="			try{"+BR;
			sCode+="				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+=					sSetData;
			sCode+="				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_PK2 ,hashSet);	"+BR;			
			sCode+="				beanContainer ="+beanName+"IndexToBean(container);	"+BR;
			sCode+="				if (beanContainer.size()>0){"+BR;
			sCode+="					BeanItem<"+beanName+"> beanitem=beanContainer.getItem(beanContainer.getIdByIndex(0));"+BR;
			sCode+="					retbean = beanitem.getBean();"+BR;
			sCode+="				}"+BR;			
			sCode+="			} catch (RuntimeException re) {"+BR;	
			sCode+="				logger.error(StrUtil.convException(re));	"+BR;		
			sCode+="				this.ErrMsg=re.toString();	"+BR;			
			sCode+="			}catch (SQLException se) {"+BR;	
			sCode+="				logger.error(StrUtil.convException(se));	"+BR;
			sCode+="				this.ErrMsg=se.toString();	"+BR;	
			sCode+="			}"+BR;	
			sCode+="		}"+BR;			
			sCode+="		return retbean;"+BR;
			sCode+="	}"+BR;					
		}
		return sCode;
	}
	
	//建立查詢語法 for CaseSnQuery
	public String getCaseSnQuery(){
		String sCode ="";
		String sQueryCode="";		
		sCode+="		/**"+BR;
		sCode+="		 * 取得CmCaseMem  CaseSn ;"+BR;
		sCode+="		 * @param bean"+BR;
		sCode+="		 * @return"+BR;
		sCode+="		 */"+BR;
		sCode+="		public BeanContainer<String,CmCaseMem> getCmCaseMem_caseSn(String caseSn){"+BR;
		sCode+="			String RETRIEVE_STMT_QUERY = \"SELECT * FROM "+tableName+" where CASE_SN =?\";"+BR;
		sCode+="			IndexedContainer container = new IndexedContainer();"+BR;
		sCode+="			BeanContainer<String ,CmCaseMem> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+");"+BR;
		sCode+="			beanContainer.setBeanIdProperty(\"beanKey\");"+BR;
		sCode+="			this.ErrMsg=\"\";"+BR;
		sCode+="			try{"+BR;
		sCode+="				HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
		sCode+="				hashSet.add(DBUtil.setData(1,\"String\",caseSn));"+BR;
		sCode+="				container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_QUERY ,hashSet);"+BR;
		sCode+="				beanContainer ="+beanName+"IndexToBean(container);	"+BR;
		sCode+="			} catch (RuntimeException re) {"+BR;
		sCode+="				logger.error(StrUtil.convException(re));	"+BR;
		sCode+="				this.ErrMsg=re.toString();	"+BR;
		sCode+="			}catch (SQLException se) {"+BR;
		sCode+="				logger.error(StrUtil.convException(se));	"+BR;
		sCode+="				this.ErrMsg=se.toString();	"+BR;
		sCode+="			}"+BR;
		sCode+="			return beanContainer;"+BR;
		sCode+="		}"+BR;
		if (fileType.equals("ONE_CASE")) return sCode;
		else return "";		
	}
	
	//建立查詢語法 for Query
	public String getQueryValue(){
		String sCode ="";
		String sQueryCode="";
		boolean haveQueryKey =false;
		if (container.size() > 0){
			//處理 Query
			sQueryCode+="				int ihashCount=0;"+BR;
			sQueryCode+="				boolean haveWhere=false;"+BR;
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (isQueryKey.indexOf(columnName) >= 0){
		        	sQueryCode+="				if (!(bean.get"+fieldNameUp+"()+\"\").equals(\"\") && !(bean.get"+fieldNameUp+"()+\"\").equals(\"null\")){ "+BR;
		        	sQueryCode+="					ihashCount++;"+BR;
		        	sQueryCode+="					hashSet.add(DBUtil.setData(ihashCount,\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;
		        	sQueryCode+="					if (haveWhere){"+BR;
	        		sQueryCode+="						RETRIEVE_STMT_QUERY += \" AND "+columnName+" =? \"; "+BR;
			        sQueryCode+="					}else{"+BR;
		        	sQueryCode+="						RETRIEVE_STMT_QUERY += \" WHERE "+columnName+" =? \"; "+BR;
		        	sQueryCode+="					}"+BR;
		        	sQueryCode+="					haveWhere=true;"+BR;
		        	sQueryCode+="				}"+BR;
		        	haveQueryKey=true;		        			        	
		        }
			}
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
			}
			sCode+="	//取得"+beanName+"  QueryKey ;"+BR;
			sCode+="	public BeanContainer<String,"+beanName+"> get"+beanName+"_Query("+beanName+" bean){"+BR;
			sCode+="		String RETRIEVE_STMT_QUERY = \"SELECT * FROM "+tableName+"\";"+BR;						
			sCode+="		IndexedContainer container = new IndexedContainer();"+BR;
			sCode+="		BeanContainer<String ,"+beanName+"> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+".class);"+BR;
			sCode+="		beanContainer.setBeanIdProperty(\"beanKey\");"+BR+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;
			sCode+="			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+=				sQueryCode;
			sCode+="			container = DBUtil.ContainerQueryDB(dbPool ,RETRIEVE_STMT_QUERY ,hashSet);	"+BR;			
			sCode+="			beanContainer ="+beanName+"IndexToBean(container);	"+BR;			
			sCode+="		} catch (RuntimeException re) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;		
			sCode+="			this.ErrMsg=re.toString();	"+BR;			
			sCode+="		}catch (SQLException se) {"+BR;	
			sCode+="			logger.error(StrUtil.convException(se));	"+BR;
			sCode+="			this.ErrMsg=se.toString();	"+BR;		
			sCode+="		}"+BR;			
			sCode+="		return beanContainer;"+BR;
			sCode+="	}"+BR;					
		}
		if (haveQueryKey) return sCode;
		else return "";
	}	
	
	/**
	 * 產生getBeansByCond()程式碼
	 * @return
	 */
	public String getQueryResultSet(){
		String sCode ="";
		sCode+="	/**												"+BR;
		sCode+="	 * 輸入查詢條件欄位名稱.與條件值,取回DBResultSet	"+BR;
		sCode+="	 * @param flds {欄位名稱1,欄位名稱2..}			"+BR;
		sCode+="	 * @param values {條件值1,條件值2..}				"+BR;
		sCode+="	 * @return										"+BR;
		sCode+="	 */												"+BR;
		sCode+="	public DBResultSet getBeansByCond(String[] flds, String[] values){ "+BR;
		sCode+="		DBResultSet dbResultSet = null; "+BR;
		sCode+="		String conditionSQL = \"\"; "+BR;
		sCode+="		if (flds.length != values.length) {"+BR;
		sCode+="			this.ErrMsg=\"傳入參數個數不匹配.請重新確認.\" +"+BR;
		sCode+="							\"欄位:\"+StrUtil.convAryToStr(flds, \",\")+"+BR;
		sCode+="							\"資料:\"+StrUtil.convAryToStr(values, \",\");"+BR;		
		sCode+="			logger.fatal(this.ErrMsg);"+BR;
		sCode+="		} else {"+BR;
		sCode+="			String tmpSQL = \"\";"+BR;
		sCode+="			for (int i=0; i<flds.length; i++){"+BR;
		sCode+="				tmpSQL = flds[i].toUpperCase() + \"='\" + values[i] + \"'\";"+BR;
		sCode+="				if (i==0) {"+BR;
		sCode+="					conditionSQL = \"WHERE \" + tmpSQL ;"+BR;
		sCode+="				} else {"+BR;
		sCode+="					conditionSQL = conditionSQL + \" AND \" + tmpSQL; "+BR;
		sCode+="				}"+BR;
		sCode+="			}"+BR;
		sCode+="		}		"+BR;
		sCode+="		String sqlstmt = RETRIEVE_STMT_ALL.concat(conditionSQL);"+BR;
		sCode+="		dbResultSet = (new DBAction()).executeQuery(dbPool,sqlstmt);"+BR;
		sCode+="		return dbResultSet;"+BR;
		sCode+="	}		"+BR;
		return sCode;
	}		
	
	//建立Insert語法
	public String getInsert(){
		String sCode ="";
		String sFieldCode="";
		String sValue="";
		String sValueCode="";
		String sSetData="";
		if (container.size() > 0){
			int iIndex=0;
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        //Field
		        if (sFieldCode.length() > 0) sFieldCode+=" ,"+columnName+" ";
		        else sFieldCode+=" "+columnName+" ";
		        //Value
		        sValue="?";
		        if (sValueCode.length() > 0) sValueCode+=" ,"+sValue+" ";
		        else sValueCode+=" "+sValue+" ";		        
		        //setData
				iIndex++;
	        	sSetData+="						hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        
			}
			sCode+="	//寫入"+tableName+" ;"+BR;
			sCode+="	public boolean insert"+beanName+"("+beanName+" bean){"+BR;
			sCode+="		return insert"+beanName+"(null,bean);"+BR;
			sCode+="	}"+BR;
			sCode+="	public boolean insert"+beanName+"(Connection con ,"+beanName+" bean){"+BR;
			sCode+="		boolean isUpdate =false;"+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;
			sCode+="			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+="			String sql = \"INSERT INTO "+tableName+"("+sFieldCode+") \";"+BR;
			sCode+="					sql+=\" VALUES ( "+sValueCode+")\";"+BR;
			sCode+=						sSetData;
			sCode+="			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);"+BR;
			sCode+="			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);"+BR;
			sCode+="		} catch (RuntimeException re) {"+BR;
			sCode+="			isUpdate=false;"+BR;
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;
			sCode+="			this.ErrMsg=re.toString();"+BR;
			sCode+="		}"+BR;
			sCode+="		return isUpdate;"+BR;
			sCode+="	}"+BR;		
		}
		return sCode;
	}
	
	//建立Update語法
	public String getUpdate(){
		String sCode ="";
		String sUpdateCode="";
		String sWhereCode="";
		String sSetData="";
		if (container.size() > 0){
			int iIndex=0;
			//先處理 update value
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) < 0){
			        if (!sUpdateCode.trim().equals("")) sUpdateCode+="				sql+=\", ";
			        else sUpdateCode+="				sql+=\" ";
			        sUpdateCode+=""+columnName+" =? \";"+BR;
			        //setData
					iIndex++;
		        	sSetData+="				hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        
		        }
			}
			//再處理 where key
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) >= 0){
		        	if (sWhereCode.length()==0)
		        		sWhereCode+="				sql+=\" WHERE "+columnName+" =? \";"+BR;
		        	else 	
		        		sWhereCode+="				sql+=\" AND "+columnName+" =? \";"+BR;
			        //setData
					iIndex++;
		        	sSetData+="				hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        	
		        }
			}
			sCode+="	//修改"+tableName+" ;"+BR;
			sCode+="	public boolean update"+beanName+"("+beanName+" bean){"+BR;
			sCode+="		return update"+beanName+"(null,bean);"+BR;
			sCode+="	}"+BR;			
			sCode+="	public boolean update"+beanName+"(Connection con ,"+beanName+" bean ){"+BR;
			sCode+="		boolean isUpdate =false;"+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;
			sCode+="			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;									
			sCode+="			String sql = \"UPDATE "+tableName+" set \";"+BR;
			sCode+=				sUpdateCode;
			sCode+=				sWhereCode+BR;
			sCode+=				sSetData+BR;
			sCode+="			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);"+BR;
			sCode+="			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);"+BR;
			sCode+="		} catch (RuntimeException re) {"+BR;
			sCode+="			isUpdate=false;"+BR;
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;
			sCode+="			this.ErrMsg=re.toString();"+BR;
			sCode+="		}"+BR;
			sCode+="		return isUpdate;"+BR;
			sCode+="	}"+BR;		
		}
		return sCode;
	}
	
	public String getUpdateFlds(){
		String sCode ="";
		String sSetData="";
		if (container.size() > 0){
			int iIndex=0;
			//處理 where key
			String sWhereCode="";			
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) >= 0){
		        	if (sWhereCode.length()==0)
		        		sWhereCode+="				sql+=\" WHERE "+columnName+" =? \";"+BR;
		        	else 
		        		sWhereCode+="				sql+=\" AND "+columnName+" =? \";"+BR;
		        	sSetData+="				hashSet.add(DBUtil.setData(i++,\""+fieldType+"\",keysValue["+(iIndex++)+"]));"+BR;					
		        }
			}
			sCode+="	/**"+BR;
			sCode+="	 * 依據傳入欄位.修改"+tableName+" ;"+BR;
			sCode+="	 * @param hmData <欄位名稱,[欄位型態(字串),欄位值(object)]>"+BR;
			sCode+="	 * @param keysValue PK欄位值"+BR;
			sCode+="	 * @return"+BR;
			sCode+="	 **/"+BR;
			sCode+="	public boolean update"+beanName+"(HashMap<String, Object> hmData, String[] keysValue ){"+BR;
			sCode+="		boolean isUpdate =false;"+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+="		if (hmData.size() > 0){"+BR;
			sCode+="			Iterator it = hmData.entrySet().iterator();"+BR;
			sCode+="			String sUpdateCode = \"\";"+BR;
			sCode+="			int i=1;"+BR;
			sCode+="			while (it.hasNext()) {"+BR;
			sCode+="				Entry entry = (Entry)it.next();"+BR;
			sCode+="				if (sUpdateCode==null || sUpdateCode.length()==0) "+BR;
			sCode+="					sUpdateCode = entry.getKey().toString().toUpperCase() + \" = ?\";"+BR;
			sCode+="				else "+BR;			
			sCode+="					sUpdateCode = sUpdateCode + \",\" + entry.getKey().toString().toUpperCase() + \" = ?\";"+BR;			
			sCode+="				Object[] value = (Object[])entry.getValue();"+BR;
			sCode+="				if (value[0]==null || ((String)value[0]).length()==0)"+BR;
			sCode+="					hashSet.add(DBUtil.setData(i,\"String\",value[1]));"+BR;
			sCode+="				else "+BR;
			sCode+="					hashSet.add(DBUtil.setData(i,(String)value[0],value[1]));"+BR;
			sCode+="				i++;"+BR;
			sCode+="			}"+BR;
			sCode+="			try{"+BR;
			sCode+="				String sql = \"UPDATE "+tableName+" set \";"+BR;
			sCode+="				sql+=sUpdateCode;"+BR;
			sCode+=					sWhereCode;
			sCode+=					sSetData;
			sCode+="				isUpdate=DBUtil.UpdateDB(sql ,hashSet);"+BR;
			sCode+="			} catch (RuntimeException se) {"+BR;
			sCode+="				isUpdate=false;"+BR;
			sCode+="				logger.error(StrUtil.convException(se)); "+BR;
			sCode+="				this.ErrMsg=se.toString(); "+BR;
			sCode+="			}"+BR;
			sCode+="		}"+BR;
			sCode+="		return isUpdate;"+BR;
			sCode+="	}"+BR;
		}
		return sCode;
	}	
	
	
	//建立delete語法
	public String getDelete(){
		String sCode ="";
		String sSetData="";
		String sWhereCode="";
		if (container.size() > 0){
			int iIndex=0;
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (PrimaryKey.indexOf(columnName) >= 0){
		        	if (sWhereCode.length()==0)
		        		sWhereCode+="				sql+=\" WHERE "+columnName+" =? \";"+BR;
		        	else 
		        		sWhereCode+="				sql+=\" AND "+columnName+" =? \";"+BR;
			        //setData
					iIndex++;
		        	sSetData+="				hashSet.add(DBUtil.setData("+(iIndex)+",\""+fieldType+"\",bean.get"+fieldNameUp+"()));"+BR;		        	
		        }
			}			
			sCode+="	//刪除"+tableName+" ;"+BR;
			sCode+="	public boolean delete"+beanName+"("+beanName+" bean){"+BR;
			sCode+="		return delete"+beanName+"(null,bean);"+BR;
			sCode+="	}"+BR;			
			sCode+="	public boolean delete"+beanName+"(Connection con ,"+beanName+" bean){"+BR;
			sCode+="		boolean isUpdate =false;"+BR;
			sCode+="		this.ErrMsg=\"\";"+BR;
			sCode+="		try{"+BR;
			sCode+="			HashSet<Hashtable<String,Object>> hashSet =new HashSet<Hashtable<String,Object>>();"+BR;
			sCode+="			String sql = \"DELETE from  "+tableName+" \";"+BR;
			sCode+=						sWhereCode+BR;
			sCode+=						sSetData;
			sCode+="			if (con==null) isUpdate=DBUtil.UpdateDB(sql ,hashSet);"+BR;
			sCode+="			else isUpdate=DBUtil.UpdateDB(con ,sql ,hashSet);"+BR;
			sCode+="		} catch (RuntimeException re) {"+BR;
			sCode+="			isUpdate=false;"+BR;
			sCode+="			logger.error(StrUtil.convException(re));	"+BR;
			sCode+="			this.ErrMsg=re.toString()+\"\";"+BR;
			sCode+="		}"+BR;
			sCode+="		return isUpdate;"+BR;
			sCode+="	}"+BR;		
		}
		return sCode;
	}	
	
	//建立Bean轉換語法
	public String getSetBean(){
		String sCode ="";
		String sBeanCode="";
		String BeanKey="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (fieldType.equals("String")){
		        	sBeanCode+="			bean.set"+fieldNameUp+"(item.getItemProperty(\""+columnName.toUpperCase()+"\").getValue()+\"\");"+BR;
		        }else if (fieldType.equals("int")){
		        	sBeanCode+="			int i"+fieldName+" =0;"+BR;
		        	sBeanCode+="			try{"+BR;
		        	sBeanCode+="				i"+fieldName+" =Integer.parseInt(item.getItemProperty(\""+columnName.toUpperCase()+"\").getValue()+\"\");"+BR;
		        	sBeanCode+="			} catch (NumberFormatException se) {"+BR;
		        	sBeanCode+="			}"+BR;
		        	sBeanCode+="			bean.set"+fieldNameUp+"(i"+fieldName+");"+BR;
		        }else if (fieldType.equals("long")){
		        	sBeanCode+="			long i"+fieldName+" =0;"+BR;
		        	sBeanCode+="			try{"+BR;
		        	sBeanCode+="				i"+fieldName+" =Long.parseLong(item.getItemProperty(\""+columnName.toUpperCase()+"\").getValue()+\"\");"+BR;
		        	sBeanCode+="			} catch (NumberFormatException se) {"+BR;
		        	sBeanCode+="			}"+BR;
		        	sBeanCode+="			bean.set"+fieldNameUp+"(i"+fieldName+");"+BR;
		        }else if (fieldType.equals("float")){
		        	sBeanCode+="			float i"+fieldName+" =0;"+BR;
		        	sBeanCode+="			try{"+BR;
		        	sBeanCode+="				i"+fieldName+" =Float.parseFloat(item.getItemProperty(\""+columnName.toUpperCase()+"\").getValue()+\"\");"+BR;
		        	sBeanCode+="			} catch (NumberFormatException se) {"+BR;
		        	sBeanCode+="			}"+BR;
		        	sBeanCode+="			bean.set"+fieldNameUp+"(i"+fieldName+");"+BR;
		        }else if (fieldType.equals("double")){
		        	sBeanCode+="			double i"+fieldName+" =0;"+BR;
		        	sBeanCode+="			try{"+BR;
		        	sBeanCode+="				i"+fieldName+" =Double.parseDouble(item.getItemProperty(\""+columnName.toUpperCase()+"\").getValue()+\"\");"+BR;
		        	sBeanCode+="			} catch (NumberFormatException se) {"+BR;
		        	sBeanCode+="			}"+BR;
		        	sBeanCode+="			bean.set"+fieldNameUp+"(i"+fieldName+");"+BR;
		        }
			}
			sCode+="	private "+beanName+" set"+beanName+"(Item item){"+BR;
			sCode+="		"+beanName+" bean =new "+beanName+"();"+BR;
			sCode+=			sBeanCode+BR;
			sCode+="		return bean;"+BR;
			sCode+="	}"+BR+BR;		         
			sCode+="	private BeanContainer<String,"+beanName+"> "+beanName+"IndexToBean(IndexedContainer container){"+BR;	
			sCode+="		BeanContainer<String,"+beanName+"> beanContainer =new BeanContainer<String,"+beanName+">("+beanName+".class);"+BR;	
			sCode+="		beanContainer.setBeanIdProperty(\"beanKey\");"+BR;	
			sCode+="		for(int i=0;i<container.size();i++){"+BR;	
			sCode+="			Item item=container.getItem(container.getIdByIndex(i));"+BR;	
			sCode+="			"+beanName+" bean =set"+beanName+"(item);"+BR;	
			sCode+="			beanContainer.addBean(bean);"+BR;	
			sCode+="		}"+BR;	
			sCode+="		return beanContainer;"+BR;	
			sCode+="	}"+BR;
		}
		return sCode;
	}
	
	@SuppressWarnings("unchecked")
	void setItemValue(Item item){
		this.columnName =(((TextField)item.getItemProperty("COLUMN_NAME").getValue()).getValue()+"").trim();
		this.fieldName =(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
		this.fieldNameUp =fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		this.fieldType  ="String";
		this.DATA_TYPE =(((TextField)item.getItemProperty("DATA_TYPE").getValue()).getValue()+"").trim();
		this.DECIMAL_DIGITS =(((TextField)item.getItemProperty("DECIMAL_DIGITS").getValue()).getValue()+"").trim();
		this.PrimaryKey="";
		this.isQueryKey="";
		this.columnSize =Integer.parseInt((((TextField)item.getItemProperty("COLUMN_SIZE").getValue()).getValue()+"").trim());
		if (DATA_TYPE.equals("NUMBER") && DECIMAL_DIGITS.equals("0") ) fieldType="int";
		if (DATA_TYPE.equals("NUMBER") && !DECIMAL_DIGITS.equals("0") ) fieldType="float";
		if (DATA_TYPE.equals("NUMBER") && DECIMAL_DIGITS.equals("0") && columnSize >=10 ) fieldType="long";
		if (DATA_TYPE.equals("NUMBER") && !DECIMAL_DIGITS.equals("0") && columnSize >=10 ) fieldType="double";
		
    	//拆key
		Collection<String> itemArrayList =new HashSet<String>();
		OptionGroup keyList =(OptionGroup)item.getItemProperty("KEY").getValue();
		itemArrayList =(Collection<String>)keyList.getValue();				
    	Iterator itr = itemArrayList.iterator();				
        while (itr.hasNext()) {
        	String value=(String) itr.next();
        	if (value.equals("PrimaryKey")){
        		PrimaryKey+=columnName+",";
        		BeanKey+=fieldName+",";
        	}
        	if (value.equals("LogKey")) LogKey+=fieldName+",";
        }
        
        //isQueryKey
        /*
        TextField4 queryForm =(TextField4)item.getItemProperty("QueryForm").getValue();
        Hashtable<String, String> map =(Hashtable<String, String>)queryForm.getValue();
        if (map.get("CHECK").equals("true")) this.isQueryKey+=columnName+",";
        */
	}
}