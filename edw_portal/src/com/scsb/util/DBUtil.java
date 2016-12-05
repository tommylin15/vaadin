package com.scsb.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.scsb.db.customfield.PropertyField;
import com.scsb.db.ConnNotAvailException;
import com.scsb.db.ConnectionPool;
import com.scsb.db.DBAction;
import com.scsb.db.ShuttingDownException;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

/**
 * 資料庫相關設定 連結 DBAction
 * @author :Tommy Lin
 * @version : 3.0
 */
public class DBUtil {
	private static Logger logger = Logger.getLogger(DBUtil.class.getName());
	
	/**
	 * 至資料庫取得label
	 * @param con
	 * @param SQL
	 * @param ConverField
	 * @return
	 * @throws SQLException
	 */
	public synchronized static <T> Hashtable<String,String> getTableLabel(Connection con ,String SQL ,boolean ConverField) throws SQLException{
		DBAction dbAction =new DBAction();
	    dbAction.getConnect();
	    Connection connection=dbAction.connection;
	    Hashtable<String,String> hLabel =new Hashtable<String,String>();
	    if (con !=null) connection=con;
        try {
        	// Create SQL SELECT statement
        	dbAction.prepareStatement = connection.prepareStatement(SQL); 	       	
	       // Initialize statement and execute the query
			dbAction.resultSet = dbAction.prepareStatement.executeQuery();	       

        	int numCols=0;        	
			numCols = dbAction.resultSet.getMetaData().getColumnCount();
			Object[] oRow =new Object[numCols];
			
	    	for (int i=1 ;i<=numCols ;i++){
	    		String fieldName=dbAction.resultSet.getMetaData().getColumnName(i);
	    		String filedLabel = dbAction.resultSet.getMetaData().getColumnLabel(i)+"";
	    		if (filedLabel.equals("") || filedLabel.equals("null") || filedLabel.equals("NULL") ) filedLabel=fieldName;	    		
	    		//欄位要轉換者
	    		if (ConverField){
	    			fieldName=replaceFormatName(fieldName);
	    		}else{
	    			fieldName=fieldName.toUpperCase();
	    		}
	    		oRow[i-1] =fieldName;
	    		hLabel.put(fieldName, filedLabel);
	    	}//for
	 	   // Handle any SQL errors			
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally {
	    	dbAction.disConnect();	    	
        }
        return hLabel;
    }
	
	
	/**
	 * 至資料庫查詢資料
	 * @param SQL
	 * @return IndexedContainer
	 * @throws SQLException
	 * @param <T>
	 * @param SQL
	 * @param ConverField  :欄位名稱是否要格式化
	 * @return IndexedContainer
	 * @throws SQLException
	 */
	public synchronized static IndexedContainer ContainerQueryDB(Connection con ,String SQL) throws SQLException{
		return ContainerQueryDB(con ,SQL ,new HashSet<Hashtable<String,Object>>());
	}
	public synchronized static IndexedContainer ContainerQueryDB(Connection con  ,String SQL ,HashSet<Hashtable<String,Object>> hashSet) throws SQLException{
		return ContainerQueryDB(con ,SQL ,hashSet ,false);
	}	
	
	public synchronized static <T> IndexedContainer ContainerQueryDB(Connection con ,String SQL ,HashSet<Hashtable<String,Object>> hashSet ,boolean ConverField) throws SQLException{
		DBAction dbAction =new DBAction();
		 Connection connection=null;
		 String dbProd ="";
	    if (con !=null){
	    	connection=con;
	    	dbProd=con.getMetaData().getDatabaseProductName();
	    }else{
		    dbAction.getConnect();
		    connection=dbAction.connection;	    	
	    }
		IndexedContainer      container  = new IndexedContainer();
		
        try {
        	// Create SQL SELECT statement
        	dbAction.prepareStatement = connection.prepareStatement(SQL); 	       	
			for(Iterator<Hashtable<String,Object>> iterator = hashSet.iterator(); iterator.hasNext(); ){
				Hashtable<String,Object> hashData = (Hashtable<String,Object>)iterator.next();
				int iIndex =Integer.parseInt(hashData.get("DATA_INDEX")+"");
				if (hashData.get("DATA_TYPE").equals("int")){
					int  iValue =Integer.parseInt(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setInt(iIndex, iValue);					
				}else if (hashData.get("DATA_TYPE").equals("long")){
					long  lValue =Long.parseLong(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setLong(iIndex, lValue);					
				}else if (hashData.get("DATA_TYPE").equals("float")){
					float  fValue =Float.parseFloat(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setFloat(iIndex, fValue);					
				}else if (hashData.get("DATA_TYPE").equals("double")){
					double  dValue =Double.parseDouble(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setDouble(iIndex, dValue);					
				}else{					
					dbAction.prepareStatement.setString(iIndex, hashData.get("VALUE").toString());
				}
			};			
	       // Initialize statement and execute the query
			dbAction.resultSet = dbAction.prepareStatement.executeQuery();	       

        	int numCols=0;        	
			numCols = dbAction.resultSet.getMetaData().getColumnCount();
			Object[] oRow =new Object[numCols];
			Hashtable<String,String> hLabel =new Hashtable<String,String>();
			
	    	for (int i=1 ;i<=numCols ;i++){
	    		String fieldName=dbAction.resultSet.getMetaData().getColumnName(i);
	    		String filedLabel = dbAction.resultSet.getMetaData().getColumnLabel(i)+"";
	    		if (filedLabel.equals("") || filedLabel.equals("null") || filedLabel.equals("NULL") ) filedLabel=fieldName;	    		
	    		//欄位要轉換者
	    		if (ConverField){
	    			fieldName=replaceFormatName(fieldName);
	    		}else{
	    			fieldName=fieldName.toUpperCase();
	    		}
	    		container.addContainerProperty(fieldName, String.class,  null);
	    		oRow[i-1] =fieldName;
	    		hLabel.put(fieldName, filedLabel);
	    	}
    		//container..addContainerProperty("LABEL", Hashtable.class, hLabel);
	    	
        	int iRows=0;
			while (dbAction.resultSet.next()){
				iRows++;
				Item item =container.addItem(iRows);
				
				for(int i = 1; i <= numCols; i++){
					String sField=(""+dbAction.resultSet.getString(i)).trim();
					if(sField.equals("null") || sField.equals("NULL")) sField="";
					item.getItemProperty(oRow[i-1]).setValue(sField);
				}
			}//while			
			return container;
	   // Handle any SQL errors			
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally {
	    	//如果是sas connection 直接關掉,不使用pool	    	
	    	if (!dbProd.equals("SAS")){
	    		dbAction.disConnect();
	    	}
	    }
	}		
	
	public synchronized static <T> IndexedContainer ContainerQueryDB(String SQL) throws SQLException{
		return ContainerQueryDB(SQL ,new HashSet<Hashtable<String,Object>>());
	}
	
	public synchronized static IndexedContainer ContainerQueryDB(String SQL ,HashSet<Hashtable<String,Object>> hashSet) throws SQLException{
		return ContainerQueryDB("" ,SQL ,hashSet ,false);
	}	

	public synchronized static IndexedContainer ContainerQueryDB(String DBPoolName ,String SQL) throws SQLException{
		return ContainerQueryDB(DBPoolName ,SQL  ,new HashSet<Hashtable<String,Object>>() ,false);
	}		

	public synchronized static IndexedContainer ContainerQueryDB(String DBPoolName ,String SQL  ,HashSet<Hashtable<String,Object>> hashSet) throws SQLException{
		return ContainerQueryDB(DBPoolName ,SQL  ,hashSet ,false);
	}		

	
	public synchronized static <T> IndexedContainer ContainerQueryDB(String DBPoolName ,String SQL ,HashSet<Hashtable<String,Object>> hashSet ,boolean ConverField) throws SQLException{
		DBAction dbAction =new DBAction();
		dbAction.getConnect(DBPoolName);
	    Connection connection=dbAction.connection;

		IndexedContainer      container  = new IndexedContainer();
		
        try {
        	// Create SQL SELECT statement
        	dbAction.prepareStatement = connection.prepareStatement(SQL); 	       	
			for(Iterator<Hashtable<String,Object>> iterator = hashSet.iterator(); iterator.hasNext(); ){
				Hashtable<String,Object> hashData = (Hashtable<String,Object>)iterator.next();
				int iIndex =Integer.parseInt(hashData.get("DATA_INDEX")+"");
				if (hashData.get("DATA_TYPE").equals("int")){
					int  iValue =Integer.parseInt(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setInt(iIndex, iValue);					
				}else if (hashData.get("DATA_TYPE").equals("long")){
					long  lValue =Long.parseLong(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setLong(iIndex, lValue);					
				}else if (hashData.get("DATA_TYPE").equals("float")){
					float  fValue =Float.parseFloat(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setFloat(iIndex, fValue);					
				}else if (hashData.get("DATA_TYPE").equals("double")){
					double  dValue =Double.parseDouble(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setDouble(iIndex, dValue);					
				}else{					
					dbAction.prepareStatement.setString(iIndex, hashData.get("VALUE").toString());
				}
			};			
	       // Initialize statement and execute the query
			dbAction.resultSet = dbAction.prepareStatement.executeQuery();	       

        	int numCols=0;        	
			numCols = dbAction.resultSet.getMetaData().getColumnCount();
			Object[] oRow =new Object[numCols];
			Hashtable<String,String> hLabel =new Hashtable<String,String>();
			
	    	for (int i=1 ;i<=numCols ;i++){
	    		String fieldName=dbAction.resultSet.getMetaData().getColumnName(i);
	    		String filedLabel = dbAction.resultSet.getMetaData().getColumnLabel(i)+"";
	    		if (filedLabel.equals("") || filedLabel.equals("null") || filedLabel.equals("NULL") ) filedLabel=fieldName;	    		
	    		//欄位要轉換者
	    		if (ConverField){
	    			fieldName=replaceFormatName(fieldName);
	    		}else{
	    			fieldName=fieldName.toUpperCase();
	    		}
	    		container.addContainerProperty(fieldName, String.class,  null);
	    		oRow[i-1] =fieldName;
	    		hLabel.put(fieldName, filedLabel);
	    	}
    		//container..addContainerProperty("LABEL", Hashtable.class, hLabel);
	    	
        	int iRows=0;
			while (dbAction.resultSet.next()){
				iRows++;
				Item item =container.addItem(iRows);
				
				for(int i = 1; i <= numCols; i++){
					String sField=(""+dbAction.resultSet.getString(i)).trim();
					if(sField.equals("null") || sField.equals("NULL")) sField="";
					item.getItemProperty(oRow[i-1]).setValue(sField);
				}
			}//while			
			return container;
	   // Handle any SQL errors			
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally {
	    	dbAction.disConnect();
	    }
	}	
	/**
	 * 代碼檔有使用多語系時,對照的view規格
	 * @param lang
	 * @return
	 */
	public static String ConverLang4SQL(String lang){
		if (lang.equals("en")) lang="_v_en";
		else if (lang.equals("cn")) lang="_v_en";
		else if (lang.equals("tw")) lang="";
		return lang;
	}
	/**
	 * 資料庫Table的欄位格式化
	 * @param FieldName
	 * @return
	 */
	private static String replaceFormatName(String FieldName){
		String FormatName=FieldName;
		FormatName =FormatName.toLowerCase();
		while(true){
			if (FormatName.indexOf("_") >= 0){
				FormatName = FormatName.substring(0,FormatName.indexOf("_"))
				          +FormatName.substring(FormatName.indexOf("_")+1,FormatName.indexOf("_")+2).toUpperCase()
				          +FormatName.substring(FormatName.indexOf("_")+2);
			}else{
				break;
			}
		}
		return FormatName;
	}	
	/**
	 * 至資料庫執行:新增,修改,刪除資料的動作
	 * @param SQL
	 * @return boolean
	 */
	public synchronized static boolean UpdateDB(String SQL){
		return UpdateDB("",SQL ,new HashSet<Hashtable<String,Object>>());
	}	
	
	public synchronized static boolean UpdateDB(String DBPoolName ,String SQL){
		return UpdateDB(DBPoolName,SQL ,new HashSet<Hashtable<String,Object>>());
	}

	public synchronized static boolean UpdateDB(String SQL,HashSet<Hashtable<String,Object>> hashSet){
		return UpdateDB("",SQL ,hashSet);
	}

	public synchronized static boolean UpdateDB(String DBPoolName ,String SQL,HashSet<Hashtable<String,Object>> hashSet){
		DBAction dbAction =new DBAction();
	    dbAction.getConnect(DBPoolName);
		boolean retFlag=false;
		try{
			dbAction.prepareStatement= dbAction.connection.prepareStatement(SQL);
			for(Iterator<Hashtable<String,Object>> iterator = hashSet.iterator(); iterator.hasNext(); ){
				Hashtable<String,Object> hashData = (Hashtable<String,Object>)iterator.next();
				int iIndex =Integer.parseInt(hashData.get("DATA_INDEX")+"");
				if (hashData.get("DATA_TYPE").equals("int")){
					int  iValue =Integer.parseInt(hashData.get("VALUE").toString());
					dbAction.prepareStatement.setInt(iIndex, iValue);					
				}else if (hashData.get("DATA_TYPE").equals("float")){
						float  fValue =Float.parseFloat(hashData.get("VALUE").toString());
						dbAction.prepareStatement.setFloat(iIndex, fValue);					
				}else{
					dbAction.prepareStatement.setString(iIndex, hashData.get("VALUE").toString());
				}
			};
			dbAction.prepareStatement.executeUpdate();
			retFlag= true;
		  } catch (SQLException se) {
			  logger.error(StrUtil.convException(se));
		      throw new RuntimeException("A database error occured. " + se.getMessage());
		  // Handle no available connection
		  } catch (ConnNotAvailException cnae) {
			  logger.error(StrUtil.convException(cnae));
			  throw new RuntimeException("The server is busy, please try again later.");
		  // Handle server shutting down
		  } catch (ShuttingDownException sde) {
			  logger.error(StrUtil.convException(sde));
			  throw new RuntimeException("The server is being shutdown.");
		  // Clean up JDBC resources
		  } finally {
				dbAction.disConnect();
		  }	    
	    return retFlag;
	}

	public synchronized static boolean UpdateCLOB(Connection connection ,String table ,String where ,String field ,String value){
		boolean retFlag=false;
		PreparedStatement preparedstatement=null;
		try{
			connection.setAutoCommit(false);
			Statement stmt=null;
			stmt=connection.createStatement();
			ResultSet rs = stmt.executeQuery(" select "+field+" from "+table+" where "+where +" FOR UPDATE ");
			while (rs.next()) {
				oracle.sql.CLOB clob = (oracle.sql.CLOB)rs.getClob(field);
				java.io.BufferedWriter outbu = new java.io.BufferedWriter(clob.getCharacterOutputStream());
				outbu.write(value);
				outbu.close();
			}			
			connection.commit();
			retFlag= true;
			return retFlag;
		  } catch (IOException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
			}
			retFlag=false;
			e.printStackTrace();
		  } catch (SQLException se) {
		      throw new RuntimeException("A database error occured. " + se.getMessage());
		  // Handle no available connection
		  } catch (ConnNotAvailException cnae) {
		    throw new RuntimeException("The server is busy, please try again later.");
		  // Handle server shutting down
		  } catch (ShuttingDownException sde) {
		    throw new RuntimeException("The server is being shutdown.");
		  // Clean up JDBC resources
		  } finally {
			 try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
			}
		     if(connection != null) {
		    	 try { 
		    		 connection.close(); 
		    	 } catch (SQLException se) {
		    	 }
		     }			  
		  }
		return retFlag;
	}	

	/**
	 * 至資料庫執行:新增,修改,刪除資料的動作 
	 * @param connection
	 * @param SQL
	 * @param hashSet
	 * @return
	 */
	public synchronized static boolean UpdateDB(Connection connection ,String SQL){
		return UpdateDB(connection ,SQL,new HashSet<Hashtable<String,Object>>());
	}
	
	public synchronized static boolean UpdateDB(Connection connection ,String SQL,HashSet<Hashtable<String,Object>> hashSet){
		boolean retFlag=false;
		PreparedStatement preparedstatement=null;
		try{
			preparedstatement = connection.prepareStatement(SQL);
			Object[] params= new Object[hashSet.size()];
			for(Iterator<Hashtable<String,Object>> iterator = hashSet.iterator(); iterator.hasNext(); ){
				Hashtable<String,Object> hashData = (Hashtable<String,Object>)iterator.next();
				int iIndex =Integer.parseInt(hashData.get("DATA_INDEX")+"");
				String hData =hashData.get("VALUE").toString();
				params[iIndex-1] = hashData.get("VALUE");
				if (hashData.get("DATA_TYPE").equals("int")){
					if (hData == null) hData="0";
					if (hData.equals("")) hData="0";
					int  iValue =Integer.parseInt(hData);
					preparedstatement.setInt(iIndex, iValue);
				}else if (hashData.get("DATA_TYPE").equals("long")){
					if (hData == null) hData="0";
					if (hData.equals("")) hData="0";
					long  lValue =Long.parseLong(hData);
					preparedstatement.setLong(iIndex, lValue);										
				}else if (hashData.get("DATA_TYPE").equals("float")){
					if (hData == null) hData="0";
					if (hData.equals("")) hData="0";
					float  fValue =Float.parseFloat(hData);
					preparedstatement.setFloat(iIndex, fValue);					
				}else if (hashData.get("DATA_TYPE").equals("double")){				
					if (hData == null ) hData="0";
					if (hData.equals("")) hData="0";
					double  dValue =Double.parseDouble(hData);
					preparedstatement.setDouble(iIndex, dValue);
				}else if (hashData.get("DATA_TYPE").equals("String")){
					if (hData == null) hData="";
					preparedstatement.setString(iIndex, hData);					
				}else if (hashData.get("DATA_TYPE").equals("Clob")){
					if (hData == null) hData="";
					Reader clobReader = new StringReader(hData);
					preparedstatement.setCharacterStream(iIndex ,clobReader, hData.length());
				}else{
					if (hData == null) hData="";
					preparedstatement.setString(iIndex, hData);
				}
			};			
			(new DBAction()).getPreparedSQL(SQL, params);
			preparedstatement.executeUpdate();
			retFlag= true;
			return retFlag;
		  } catch (SQLException se) {
		      throw new RuntimeException("A database error occured. " + se.getMessage());
		  // Handle no available connection
		  } catch (ConnNotAvailException cnae) {
		    throw new RuntimeException("The server is busy, please try again later.");
		  // Handle server shutting down
		  } catch (ShuttingDownException sde) {
		    throw new RuntimeException("The server is being shutdown.");
		  // Clean up JDBC resources
		  } finally {
		     if(preparedstatement != null) {
		    	 try { 
		    		 preparedstatement.close(); 
		    		 //logger.debug("關閉 prepareStatement");
		    	 } catch (SQLException se) {
		    	 }
		     }			  
		  }
	}
	
	public static Hashtable<String,Object> setData(int sno ,String type ,Object value){
		Hashtable<String,Object> hashData =new Hashtable<String,Object>();
		hashData.put("DATA_INDEX", sno);
		hashData.put("DATA_TYPE", type);
		hashData.put("VALUE", value);
		return hashData;
	}	
	
	/**
	 * 至資料庫取得編號最大值
	 * @param table_name
	 * @param field_name
	 * @param size
	 * @param sql[][] where條件 {field,value}
	 * @return
	 * @throws IOException
	 */
	public static String GetNo(String table_name,String field_name,int size,String[][] sql) throws IOException  {
		return GetNo(table_name,field_name,"",size,sql);
	}
	/**
	 * 
	 * @param table_name
	 * @param field_name
	 * @param fieldTB 流水號開頭字(如:欄位編號採 A000001 , 則 fieldTB:'A')
	 * @param size
	 * @param arr[][]where條件 {field,value}
	 * @return
	 * @throws IOException
	 */
	public static String GetNo(String table_name,String field_name,String fieldTB,int size,String[][] arr) throws IOException  {
		String sql=MakeSQLStr(arr);	
		int ee=0;
		String sql1="select "+field_name.trim()+" as SNO from "+table_name.trim()+" where 1=1 "+sql;
		try {
			IndexedContainer container=ContainerQueryDB(sql1);
			String SU="1";
			if(container.getItemIds().size()>0){
				sql="select max("+field_name.trim()+") as SNO from "+table_name.trim()+" where 1=1 "+sql;
				IndexedContainer container2 = ContainerQueryDB(sql);			
				if(container2.getItemIds().size() > 0){
					Item item =(Item)container2.getItem(1);
					String sno = item.getItemProperty("SNO")+"";
					ee=(Integer.parseInt(sno.trim().substring(fieldTB.trim().length(),sno.trim().length()))+1);
					SU=ee+"";
					SU=SU.trim();
				}
			}
			for(int a=SU.length();(size-fieldTB.trim().length())>a;a++)	SU="0"+SU;
			return fieldTB.trim()+SU;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		
	}
	/**
	 * 產生SQL語法字串
	 * @param arr
	 * @return String
	 * @throws IOException
	 */
	public static String MakeSQLStr(String[][] arr) throws IOException{
		String sql="";
		int a=0,ccc=arr.length;
		try{
			sql = arr[0][0].trim();
		}catch(ArrayIndexOutOfBoundsException e){
			ccc=0;
		}			
		sql="";
		for (a=0;a<ccc;a++){
			if ((arr[a][0].equals("c")) || (arr[a][0].equals("C"))){
				if((arr[a].length<4) || (arr[a].length>4)){
						System.out.println("字元參數數量錯誤!!");
						sql="";
						return sql;
				 	}
				if (!arr[a][3].trim().equals("")){
					if(arr[a][2].trim().equals("=")){
						sql+=" and "+arr[a][1].trim()+" = '"+arr[a][3].trim()+"' ";
					}else if((arr[a][2].trim().equals("like")) || (arr[a][2].trim().equals("LIKE"))){
						sql+=" and "+arr[a][1].trim()+" LIKE '%"+arr[a][3].trim()+"%' ";
					}else{
						System.out.println("=,LIKE判斷符號錯誤!!");
						sql="";
						return sql;
				 	}
				}
			}else if ((arr[a][0].equals("f")) || (arr[a][0].equals("F"))){
				if((arr[a].length<4) || (arr[a].length>4)){
						System.out.println("欄位參數數量錯誤!!");
						sql="";
						return sql;
				 	}
				sql+=" and "+arr[a][1].trim()+" = "+arr[a][3].trim()+" ";
			}else if ((arr[a][0].equals("n")) || (arr[a][0].equals("N"))){
				if((arr[a].length<4) || (arr[a].length>4)){
						System.out.println("數值參數數量錯誤!!");
						sql="";
						return sql;
				}
				int ret;
				if (!arr[a][3].trim().equals("")){
					if (arr[a].length==4){
						try{
							ret = Integer.parseInt(arr[a][3].trim());
						}catch(Exception e){
							System.out.println("arr[3]["+a+"]輸入非數值之字串!!");
							sql="";
							return sql;
						}			
						if((arr[a][2].trim().equals("=")) || (arr[a][2].trim().equals("<=")) || (arr[a][2].trim().equals(">=")) || (arr[a][2].trim().equals(">")) || (arr[a][2].trim().equals("<"))){
							sql+=" and "+arr[a][1].trim()+" "+arr[a][2].trim()+" "+arr[a][3].trim()+" ";
						}else{
							System.out.println(">,<,>=,<=判斷符號錯誤!!");
							sql="";
							return sql;
						}
					}
				}else{
					System.out.println("數值參數數量錯誤!!");
					sql="";
					return sql;
				}
			}else if((arr[a][0].equals("d")) || (arr[a][0].equals("D"))){
				int ret;
				String dateG="";
				if (arr[a].length==5){
					if(!((arr[a][4].trim().equals("yyyy/mm/dd")) || (arr[a][4].trim().equals("yy/mm/dd")) || (arr[a][4].trim().equals("dd/mm/yyyy")) || (arr[a][4].trim().equals("dd/mm/yy")) || (arr[a][4].trim().equals("yyyymmdd")) || (arr[a][4].trim().equals("yymmdd")) || (arr[a][4].trim().equals("ddmmyy")) || (arr[a][4].trim().equals("ddmmyyyy")) || (arr[a][4].trim().equals("YYYY/MM/DD")) || (arr[a][4].trim().equals("YY/MM/DD")) || (arr[a][4].trim().equals("DD/MM/YYYY")) || (arr[a][4].trim().equals("DD/MM/YY")) || (arr[a][4].trim().equals("YYYYMMDD")) || (arr[4][5].trim().equals("YYMMDD")) || (arr[a][4].trim().equals("DDMMYY")) || (arr[a][4].trim().equals("DDMMYYYY")))){
						System.out.println("此種規格此Function不支援!!");
						sql="";
						return sql;
					}
					dateG=arr[a][4].trim();
				}else if(arr[a].length==5){
					dateG="yyyymmdd";
				}else{
					System.out.println("日期參數數量錯誤!!");
					sql="";
					return sql;
				}
				if (!arr[a][3].trim().equals("")){
					if((arr[a][2].trim().equals("=")) || (arr[a][2].trim().equals("<=")) || (arr[a][2].trim().equals(">=")) || (arr[a][2].trim().equals(">")) || (arr[a][2].trim().equals("<"))){
						sql+=" and "+arr[a][1].trim()+" "+arr[a][2].trim()+" '"+arr[a][3].trim()+"' ";
					}else{
						System.out.println(">,<,>=,<=日期判斷符號錯誤!!");
						sql="";
						return sql;
					}
				}			
			}	
		}	
		return sql;
	}
	
	/**
	 * 至資料庫執行:取得欄位名稱的動作
	 * @param tableContainer
	 * @param tableName
	 * @return IndexedContainer
	 * @throws SQLException
	 */
	public synchronized static IndexedContainer getColName(IndexedContainer   tableContainer ,String tableName) throws SQLException{
		DBAction dbAction =new DBAction();
	    dbAction.getConnect();
		try{
			dbAction.metadata= dbAction.connection.getMetaData();

			dbAction.resultSet = dbAction.metadata.getColumns(null, null, tableName.toUpperCase(), null);

			//ResultSet columnSet = metadata.getColumns(null, "OPS$AIMSADM", tableName.toUpperCase(), "CODE");
			while(dbAction.resultSet.next()){  				
				String Field_Name=(dbAction.resultSet.getString("COLUMN_NAME")+"").toUpperCase();
				String Field_Memo=dbAction.resultSet.getString("REMARKS")+"";
				if (Field_Memo.trim().equals("null")) Field_Memo=Field_Name;
				Item logItem =tableContainer.addItem(Field_Name);
		    	logItem.getItemProperty("Field_Name").setValue(Field_Name);
		    	logItem.getItemProperty("Field_Memo").setValue(Field_Memo);
			}
			return tableContainer;
	   // Handle any SQL errors
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally { 
	    	dbAction.disConnect();
	    }
	}
	
	/*****************************************************************************************************************
	至資料庫執行:取得Table名稱的動作,帶參數模式:<br>
			con			:	Connection物件<br>
			metadata	:	DatabaseMetaData物件<br>
	******************************************************************************************************************/
	public synchronized static IndexedContainer getTableList(String dbPool ,String dbOwner) throws SQLException{
		
		DBAction dbAction =new DBAction();
		dbAction.getConnect(dbPool);
	    Connection connection=dbAction.connection;

        // Handle any SQL errors
 	    //Table ht =new Table();
       	
	    PreparedStatement retrieveall_stmt = null;
	    ResultSet results = null;	   
	    DatabaseMetaData metadata =null;
	    IndexedContainer      container  = new IndexedContainer();
		try{
			metadata= connection.getMetaData();
			//ResultSet tableSet = metadata.getTables(null, dbOwner, null, new String[]{"TABLE","VIEW"});
			ResultSet tableSet = metadata.getTables("","%","%",new String[]{"TABLE","VIEW"});
			//ResultSet columnSet = metadata.getColumns(null, "OPS$AIMSADM", tableName.toUpperCase(), "CODE");
			container.addContainerProperty("TABLE_NAME", String.class,  null);
			
        	int iRows=0;			
			while(tableSet.next()){
				Item item =container.addItem(iRows);
				String TABLE_NAME=(tableSet.getString("TABLE_NAME")+"").toUpperCase();
				item.getItemProperty("TABLE_NAME").setValue(TABLE_NAME);
				iRows++;
			}
			return container;
	   // Handle any SQL errors
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally {   	
	    	 dbAction.disConnect();
	    }
	}	
	
	/*****************************************************************************************************************
	至資料庫執行:取得欄位名稱的動作,帶參數模式:<br>
			con			:	Connection物件<br>
			metadata	:	DatabaseMetaData物件<br>
	******************************************************************************************************************/
	public synchronized static IndexedContainer getColName(String dbPool ,String dbOwner ,String tableName ,IndexedContainer container) throws SQLException{
		DBAction dbAction =new DBAction();
		dbAction.getConnect(dbPool);
	    Connection connection=dbAction.connection;

	    PreparedStatement retrieveall_stmt = null;
	    ResultSet results = null;	   
	    DatabaseMetaData metadata =null;
		try{
			metadata= connection.getMetaData();
			System.out.println("dbPool:"+dbPool);
			System.out.println("dbOwner:"+dbOwner);
			System.out.println("tableName:"+tableName);
			//ResultSet columnSet = metadata.getColumns(connection.getCatalog(), dbOwner, tableName.toUpperCase(), "");
			ResultSet columnSet = metadata.getColumns(null, "%", tableName.toUpperCase(), "%");
			ResultSet pkeySet =metadata.getPrimaryKeys(null, "%", tableName.toUpperCase());
			
			/*
			 * 
			TABLE_CAT String => table catalog (may be null) 
			TABLE_SCHEM String => table schema (may be null) 
			TABLE_NAME String => table name 
			COLUMN_NAME String => column name 
			DATA_TYPE int => SQL type from java.sql.Types 
			TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified 
			COLUMN_SIZE int => column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision. 
			BUFFER_LENGTH is not used. 
			DECIMAL_DIGITS int => the number of fractional digits 
			NUM_PREC_RADIX int => Radix (typically either 10 or 2) 
			NULLABLE int => is NULL allowed. 
			columnNoNulls - might not allow NULL values 
			columnNullable - definitely allows NULL values 
			columnNullableUnknown - nullability unknown 
			REMARKS String => comment describing column (may be null) 
			COLUMN_DEF String => default value (may be null) 
			SQL_DATA_TYPE int => unused 
			SQL_DATETIME_SUB int => unused 
			CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column 
			ORDINAL_POSITION int => index of column in table (starting at 1) 
			IS_NULLABLE String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows. 
			SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) 
			SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
			SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF) 
			SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
			 * 
			 * */		
			int iFlag=0;
			while(columnSet.next()){
				iFlag++;
				TextField COLUMN_NAME =new TextField();
				COLUMN_NAME.setValue((columnSet.getString("COLUMN_NAME")+"").toUpperCase());
				COLUMN_NAME.setReadOnly(true);
				
				TextField BEAN_NAME =new TextField();
				BEAN_NAME.setValue(replaceBeanName(COLUMN_NAME.getValue().toString()+""));
				
				TextField REMARKS =new TextField();
				REMARKS.setValue(columnSet.getString("REMARKS")+"");
				
				TextField sData_Type =new TextField();
				sData_Type.setValue("VARCHAR2");
				int DATA_TYPE=columnSet.getInt("DATA_TYPE");
				if (DATA_TYPE==1) sData_Type.setValue("CHAR");
				if (DATA_TYPE==3) sData_Type.setValue("NUMBER");
				sData_Type.setReadOnly(true);

				TextField COLUMN_SIZE =new TextField();
				COLUMN_SIZE.setValue(columnSet.getInt("COLUMN_SIZE")+"");
				COLUMN_SIZE.setReadOnly(true);
				
				TextField DECIMAL_DIGITS =new TextField();
				DECIMAL_DIGITS.setValue(columnSet.getString("DECIMAL_DIGITS")+"");
				DECIMAL_DIGITS.setReadOnly(true);
				
				Item logItem =container.addItem(COLUMN_NAME.getValue().toString()+"");
		    	logItem.getItemProperty("COLUMN_NAME").setValue(COLUMN_NAME);
		    	logItem.getItemProperty("BEAN_NAME").setValue(BEAN_NAME);
		    	logItem.getItemProperty("REMARKS").setValue(REMARKS);
		    	logItem.getItemProperty("DATA_TYPE").setValue(sData_Type);    	
		    	logItem.getItemProperty("COLUMN_SIZE").setValue(COLUMN_SIZE);
		    	logItem.getItemProperty("DECIMAL_DIGITS").setValue(DECIMAL_DIGITS);		    	
		    	//logItem.getItemProperty("QueryForm").setValue(new TextField4());
		    	//logItem.getItemProperty("DataForm").setValue(new TextField4());
		    	logItem.getItemProperty("QueryField").setValue(new PropertyField(logItem).getValue());
		    	//logItem.getItemProperty("DataField").setValue(new PropertyField(logItem));
		    	
				OptionGroup KEY =new OptionGroup("",Arrays.asList(new String[]{"LogKey","PrimaryKey"}));
				KEY.setMultiSelect(true);
		    	logItem.getItemProperty("KEY").setValue(KEY);
		    	
			}
			return container;
	   // Handle any SQL errors
	    } catch (SQLException se) {
	      throw new RuntimeException("A database error occured. " + se.getMessage());
	
	    // Handle no available connection
	    } catch (ConnNotAvailException cnae) {
	      throw new RuntimeException("The server is busy, please try again later.");
	
	    // Handle server shutting down
	    } catch (ShuttingDownException sde) {
	      throw new RuntimeException("The server is being shutdown.");
	
	    // Clean up JDBC resources
	    } finally {   	
	    	 dbAction.disConnect();
	    }
	}
	
	//replace 
	static String replaceBeanName(String Field_Name){
		String BeanName=Field_Name;
		BeanName =BeanName.toLowerCase();
		while(true){
			if (BeanName.indexOf("_") >= 0){
				BeanName = BeanName.substring(0,BeanName.indexOf("_"))
				          +BeanName.substring(BeanName.indexOf("_")+1,BeanName.indexOf("_")+2).toUpperCase()
				          +BeanName.substring(BeanName.indexOf("_")+2);
			}else{
				break;
			}
		}
		return BeanName;
	}	
	
	
}