package com.scsb.db;

import java.io.ByteArrayInputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import oracle.xdb.XMLType;

import org.apache.log4j.Logger;

import com.scsb.util.NamingService;
import com.scsb.util.StrUtil;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

public class DBAction {
	public Connection connection = null;
	public ResultSet resultSet = null;
	public Statement statement = null;
	public PreparedStatement prepareStatement = null;
	public DatabaseMetaData metadata =null;
	
	static Logger logger = Logger.getLogger(DBAction.class.getName());
	public String ErrMsg = "";
	
	String defaultPool="defaultPool";
	/**
	 * 取得資料庫連線
	 */
	public void getConnect(){
		getConnect(defaultPool);
	}
	public void getConnect(String connectionPoolName){
		try {	
			if (connectionPoolName == null){
				getConnect();
			}else if (connectionPoolName.equals("")){
				getConnect();
			}else{
				NamingService nameSvc = NamingService.getInstance();
				ConnectionPool connectionpool = (ConnectionPool)nameSvc.getAttribute(connectionPoolName);
				connection = connectionpool.getConnection();
			}
		} catch (NullPointerException e) {
			logger.info("無法從DBCP取得連線.改為直接連線(測試使用)");			
			connection = getConnDirect();
		} 
	}
	
	/**
	 * 切斷資料庫連線
	 */
	public void disConnect() {
		try {
			if(resultSet != null) {
		        try { 
		        	resultSet.close(); 
		        	//logger.debug("關閉 resultSet");
		        } catch (SQLException se) {
		        	logger.error(se.toString()); 
		        }
		     }
		     if(statement != null) {
		    	 try { 
		    		 statement.close(); 
		    		 //logger.debug("關閉 statement");
		    	 } catch (SQLException se) {
		    		 logger.error(se.toString()); 
		    	 }
		     }
		     if(prepareStatement != null) {
		    	 try { 
		    		 prepareStatement.close(); 
		    		 //logger.debug("關閉 prepareStatement");
		    	 } catch (SQLException se) {
		    		 logger.error(se.toString()); 
		    	 }
		     }
		     if (getConnection() != null && !getConnection().isClosed()) {
				getConnection().close();
				//logger.debug("釋放JDBC連線!");
		     } else {
				logger.error("釋放JDBC連線時,發現原本就不存在連線!");
		     }
		} catch (Exception e) {
			logger.error("釋放JDBC連線失敗!");
			logger.error(e.toString());
		}
	}

	/**
	 * 取得連線
	 */
	private Connection getConnection() {
		return connection;
	}
	
	/**
	 * 執行查詢SQL
	 * @param sqlstmt
	 * @return
	 */
	public DBResultSet executeQuery(String sqlstmt) {
		return executeQuery(defaultPool, sqlstmt) ;
	}
	public DBResultSet executeQuery(String dbPool,String sqlstmt) {
		DBResultSet result = null;
		try {
			getConnect(dbPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				ErrMsg = "";
				statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);

				logger.debug("SQL = " + sqlstmt);
				resultSet = statement.executeQuery(sqlstmt);
				//logger.debug("執行成功");
				result = new DBResultSet(resultSet);

				resultSet.close();
				statement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			disConnect();
		}

		return result;
	}
	
	/**
	 * 執行查詢SQL.使用帶參數方式
	 * @param sqlstmt
	 * @param parameterValues
	 * @return
	 */
	public DBResultSet executeQuery(String sqlstmt, String[] parameterValues) {
		return executeQuery(defaultPool ,sqlstmt, parameterValues);
	}
	public DBResultSet executeQuery(String dbPool ,String sqlstmt, String[] parameterValues) {
		DBResultSet result = null;
		try {
			getConnect(dbPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				ErrMsg = "";
				prepareStatement = getConnection().prepareStatement(sqlstmt,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				logger.debug("SQL = " + sqlstmt);				
				if (parameterValues!=null) {
					for (int i=0; i<parameterValues.length; i++){
						prepareStatement.setString(1+i, parameterValues[i]);	
					}
					logger.debug("參數值:"+ StrUtil.convAryToStr(parameterValues, ","));
				}
				resultSet = prepareStatement.executeQuery();
				//logger.debug("執行成功");
				result = new DBResultSet(resultSet);

				resultSet.close();
				prepareStatement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			disConnect();
		}
		return result;
	}
	
	/**
	 * 執行查詢SQL.使用帶參數方式,並指定參數類型
	 * @param sqlstmt
	 * @param datatypes
	 * @param parameterValues
	 * @return
	 */
	public DBResultSet executeQuery(String sqlstmt,String[] datatypes, Object[] parameterValues) {
		return executeQuery(defaultPool, sqlstmt, datatypes,  parameterValues);
	}
	
	public DBResultSet executeQuery(String defaultPool ,String sqlstmt,String[] datatypes, Object[] parameterValues) {
		DBResultSet result = null;
		try {
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				ErrMsg = "";
				prepareStatement = getConnection().prepareStatement(sqlstmt,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				logger.debug("SQL = " + sqlstmt);
				if (parameterValues!=null) {
					for (int i=0; i < parameterValues.length; i++){
						if (datatypes[i].equals("INTEGER")) {
							if (parameterValues[i]!=null) {
								prepareStatement.setNull(i+1, java.sql.Types.INTEGER);							
							} else {
								int tmpInt = (Integer)parameterValues[i];
								prepareStatement.setInt(i+1, tmpInt);
							}
						} else if (datatypes[i].equals("FLOAT")) {
							if (parameterValues[i]!=null) {
								prepareStatement.setNull(i+1, java.sql.Types.FLOAT);							
							} else {
								float tmpInt = (Float)parameterValues[i];
								prepareStatement.setFloat(i+1, tmpInt);
							}
						} else if (datatypes[i].equals("XMLTYPE")) {
							try {
								XMLType poXML = XMLType.createXML(getConnection(), (String)parameterValues[i]);
								prepareStatement.setObject(i+1, poXML);
							} catch (Exception e){
								logger.error(e.toString());
							}						
						} else if (datatypes[i].equals("LONG RAW")) {
							ByteArrayInputStream in = new ByteArrayInputStream(((String)parameterValues[i]).getBytes());
							prepareStatement.setBinaryStream(i+1, in, in.available());
						} else { 
							if (parameterValues[i]!=null) 
								prepareStatement.setNull(i+1, java.sql.Types.VARCHAR);
							else 
								prepareStatement.setString(i+1, (String)parameterValues[i]);
						}
					}
					logger.debug("參數值:"+ StrUtil.convAryToStr(parameterValues, ","));
				}
				
				resultSet = prepareStatement.executeQuery(sqlstmt);
				//logger.debug("執行成功");
				result = new DBResultSet(resultSet);

				resultSet.close();
				prepareStatement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			disConnect();
		}
		return result;
	}

	/**
	 * 若Query後的資料值僅有一筆且僅一個資料值,為減少程式碼撰寫.可直接使用
	 * @param sqlstmt
	 * @param parameterValues
	 * @return
	 */
	public String getQueryRtn(String sqlstmt, String[] parameterValues) {
		String result = "";
		ErrMsg = "";
		DBResultSet rs = executeQuery(sqlstmt, parameterValues);
		if (rs.getSize()>0) {
			result = rs.getPositionData(0, 0);	
		} 
		return result;
	}
	
	/**
	 * 若Query後的資料值僅有一筆資料值,為減少程式碼撰寫.可直接使用
	 * @param sqlstmt
	 * @param parameterValues SQL條件變數
	 * @param fldsNumber 回傳欄位數
	 * @return
	 */
	public String[]  getQueryRtn(String executeQuery,String sqlstmt, String[] parameterValues, int fldsNumber) {
		return getQueryRtn(defaultPool, executeQuery, sqlstmt,  parameterValues,  fldsNumber);
	}
	
	public String[] getQueryRtn(String defaultPool,String executeQuery,String sqlstmt, String[] parameterValues, int fldsNumber) {
		String[] result = new String[parameterValues.length];
		ErrMsg = "";
		DBResultSet rs = executeQuery(defaultPool ,sqlstmt, parameterValues);
		if (rs.getSize()>0) {
			if (fldsNumber>0) {
				for (int i=0; i<fldsNumber; i++) {
					result[i] = rs.getPositionData(0, i);	
				}				
			} 
		}
		return result;
	}	
	
	/**
	 * 若Query後的資料值僅有一筆資料值,為減少程式碼撰寫.可直接使用
	 * @param sqlstmt
	 * @param parameterValues 
	 * @param getFldsName 回傳欄位名稱
	 * @return
	 */
	public String[] getQueryRtn(String sqlstmt, String[] parameterValues, String[] getFldsName) {
		return getQueryRtn(defaultPool , sqlstmt,  parameterValues,  getFldsName) ;
	}
	public String[] getQueryRtn(String defaultPool ,String sqlstmt, String[] parameterValues, String[] getFldsName) {
		String[] result = new String[parameterValues.length];
		ErrMsg = "";
		DBResultSet rs = executeQuery(defaultPool ,sqlstmt, parameterValues);
		if (rs.getSize()>0) {
			if (getFldsName!=null) {
				for (int i=0; i<getFldsName.length; i++) {
					result[i] = rs.getPositionData(0, rs.getTitleIndex(getFldsName[i]));	
				}				
			} 
		}
		return result;
	}

	

	/**
	 * SQL作用為COUNT個數
	 * @param sqlstmt
	 * @return
	 */
	public int executeQueryCount(String sqlstmt) {
		return executeQueryCount(defaultPool, sqlstmt);
	}
	public int executeQueryCount(String defaultPool ,String sqlstmt) {
		int count = 0;
		try {
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				ErrMsg = "";
				statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);

				logger.debug("SQL = " + sqlstmt);
				resultSet = statement.executeQuery(sqlstmt);
				logger.debug( "執行成功");
				count = resultSet.getInt(0);

				resultSet.close();
				statement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			disConnect();
		}

		return count;
	}

	
	public int executeUpdate(String sqlstmt) {
		return executeUpdate(defaultPool ,sqlstmt);
	}
	public int executeUpdate(String defaultPool ,String sqlstmt) {
		int result = -9999;

		try {
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				getConnection().setAutoCommit(false);
				ErrMsg = "";
				statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
				logger.debug("SQL = " + sqlstmt);
				result = statement.executeUpdate(sqlstmt);
				logger.debug("執行結束(回傳代碼:" + result + ")");

				getConnection().commit();
				getConnection().setAutoCommit(true);
				statement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				logger.error(e.toString());
			}
		} finally {
			disConnect();
		}

		return result;
	}
	
	
	public int executeUpdate(String sqlstmt, String[] parameterValues) {
		return executeUpdate(defaultPool , sqlstmt,  parameterValues);
	}
	public int executeUpdate(String defaultPool ,String sqlstmt, String[] parameterValues) {
		int result = -9999;

		try {
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				getConnection().setAutoCommit(false);
				ErrMsg = "";
				prepareStatement = getConnection().prepareStatement(sqlstmt, ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
				logger.debug("SQL = " + sqlstmt);
				if (parameterValues!=null) {
					for (int i=0; i<parameterValues.length; i++){
						prepareStatement.setString(1+i, parameterValues[i]);	
					}
					logger.debug("參數值:"+ StrUtil.convAryToStr(parameterValues, ","));					
				}
				result = prepareStatement.executeUpdate();
				logger.debug("執行結束(回傳代碼:" + result + ")");

				getConnection().commit();
				getConnection().setAutoCommit(true);
				prepareStatement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
			ErrMsg = e.getMessage();
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				logger.error(e.toString());
			}
		} finally {
			disConnect();
		}

		return result;
	}
	
	public int executeUpdate(String sqlstmt, String[] datatypes, String[] parameterValues) {
		return executeUpdate(defaultPool, sqlstmt,  datatypes,  parameterValues) ;
	}
	public int executeUpdate(String defaultPool ,String sqlstmt, String[] datatypes, String[] parameterValues) {
		int result = -9999;

		try {
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				getConnection().setAutoCommit(false);
				ErrMsg = "";
				prepareStatement = getConnection().prepareStatement(sqlstmt, ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
				logger.debug("SQL = " + sqlstmt);
				if (parameterValues!=null){
					for (int i=0; i < parameterValues.length; i++){
						if (datatypes[i].equals("INTEGER")) {
							if (StrUtil.isNull(parameterValues[i])) {
								prepareStatement.setNull(i+1, java.sql.Types.INTEGER);							
							} else {
								int tmpInt = Integer.parseInt(parameterValues[i]);
								prepareStatement.setInt(i+1, tmpInt);
							}
						} else if (datatypes[i].equals("XMLTYPE")) {
							try {
								XMLType poXML = XMLType.createXML(getConnection(), parameterValues[i]);
								prepareStatement.setObject(i+1, poXML);
							} catch (Exception e){
								logger.error(e.toString());
							}						
						} else if (datatypes[i].equals("LONG RAW")) {
							ByteArrayInputStream in = new ByteArrayInputStream(parameterValues[i].getBytes());
							prepareStatement.setBinaryStream(i+1, in, in.available());
						} else { 
							if (StrUtil.isNull(parameterValues[i])) 
								prepareStatement.setNull(i+1, java.sql.Types.VARCHAR);
							else 
								prepareStatement.setString(i+1, parameterValues[i]);
						}
					}
					logger.debug("參數值:"+ StrUtil.convAryToStr(parameterValues, ","));
				}
				result = prepareStatement.executeUpdate();
				logger.debug("執行結束(回傳代碼:" + result + ")");

				getConnection().commit();
				getConnection().setAutoCommit(true);
				prepareStatement.close();
			}
		} catch (Exception e) {
			logger.error(e.toString());
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				logger.error(e.toString());
			}
		} finally {
			disConnect();
		}

		return result;
	}	

	/**
	 * 連續執行多個SQL命令
	 * @param sqlstmt
	 * @return
	 */
	public int executeMultiUpdate(String[] sqlstmt) {
		return executeMultiUpdate(defaultPool ,sqlstmt);
	}
	public int executeMultiUpdate(String defaultPool ,String[] sqlstmt) {
		int result = 0;

		try {
			getConnect(defaultPool);
			if (getConnection() != null && !getConnection().isClosed()) {
				getConnection().setAutoCommit(false);
				ErrMsg = "";
				Statement statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
				int current = 0;
				for (int i = 0; i < sqlstmt.length; i++) {
					logger.debug( "SQL = " + sqlstmt[i]);
					current = statement.executeUpdate(sqlstmt[i]);
					logger.debug("執行結束(回傳代碼:" + current + ")");
					result += current;
				}
				logger.debug("執行完畢(總共:" + current + "筆SQL命令)");

				getConnection().commit();
				getConnection().setAutoCommit(true);
				statement.close();
			}
		} catch (Exception e) {
			result = -9999;
			logger.error(e.toString());
			try {
				getConnection().rollback();
			} catch (SQLException e1) {
				logger.error(e1);
			}
		} finally {
			disConnect();
		}
		return result;
	}
	
	/**
	 * 執行資料庫 Store Procedure.回傳值均已String處理
	 * @param sStoreProcedureName 預儲程式名稱
	 * @param sInValues String[]{傳入參數數值1,傳入參數數值2..}
	 * @param iOutNumbers 傳出參數數量
	 * @return
	 */
	public String[] executeSP(String sStoreProcedureName, String[] sInValues, int iOutNumbers){
		return executeSP(defaultPool , sStoreProcedureName,  sInValues,  iOutNumbers);
	}
	public String[] executeSP(String defaultPool ,String sStoreProcedureName, String[] sInValues, int iOutNumbers){
		String[] result = new String[iOutNumbers];		
	    try {	    	
			getConnect(defaultPool);			
			if (getConnection() != null && !getConnection().isClosed()) {
				ErrMsg = "";
				int iInNumber = sInValues.length;
				int iOutNumber = iOutNumbers; 
				String sqlstmt_question = StrUtil.repeat("?", iInNumber+iOutNumber, true);				
				CallableStatement cstmt = getConnection().prepareCall("{call "+sStoreProcedureName+"("+sqlstmt_question+")}");
				for (int i=1; i<=sInValues.length; i++){
					cstmt.setString(i, "");
				}				
				for (int o=1; o<=iOutNumbers; o++){					
					cstmt.registerOutParameter(iInNumber+o, Types.VARCHAR);
				}
				cstmt.executeUpdate();
				
				for (int o=1; o<=iOutNumbers; o++){
					result[o] = cstmt.getString(iInNumber+o);
				}
			}
	    } catch (SQLException sqle) {
	    	ErrMsg = sqle.getMessage();
	    	logger.error("異常:"+sqle.getMessage());
	    }
	    return result;
	}

	/**
	 * 取得實際執行SQL
	 * @param sqlstmt
	 * @param params
	 * @return
	 */
	public String getPreparedSQL(String sqlstmt, Object[] params){
		int paramNum = 0;
		if (params != null) 
			paramNum = params.length;
		if (paramNum < 1) return sqlstmt;
		StringBuffer returnSQL= new StringBuffer();
		String[] subSQL = sqlstmt.split("\\?");
		for (int i=0; i<paramNum; i++){
			if (params[i] instanceof Date){
				returnSQL.append(subSQL[i]).append(" '").append((java.sql.Date)params[i]).append("' ");
			} else {
				returnSQL.append(subSQL[i]).append(" '").append(params[i]).append("' ");
			}
		}
		if (subSQL.length > params.length) {
			returnSQL.append(subSQL[subSQL.length-1]);
		}
		logger.error("執行SQL:"+returnSQL.toString());
		return returnSQL.toString();
	}
	
	/**
	 * 設定傳入參數
	 * @param pstmt
	 * @param params
	 * @throws SQLException 
	 */
	private void setPrams(PreparedStatement pstmt, Object[] params) throws SQLException{
		if (params!=null) {
			for (int i=0, paramNum = params.length; i<paramNum; i++){
				try {
					if (params[i]!=null && params[i] instanceof java.util.Date){
						pstmt.setTimestamp(i+1, dateToSqlDate((java.sql.Date)params[i]));
					} else {
						pstmt.setObject(i+1, params[i]);						
					}
				} catch (SQLException e){
					throw e;
				}
			}
		}
	}
	
	private Timestamp dateToSqlDate(java.util.Date dt){
		Timestamp ts = new Timestamp(dt.getTime());
		return ts;
	}
	
	/**
	 * 將DBResultSet資料.轉為Bean
	 * @param dbRS
	 * @param seqno
	 * @return
	 */
	public Item convRowToItem(DBResultSet dbRS, int seqno){
		Item item;
		int numCols = 0;
		numCols = dbRS.getColumnSize();
		IndexedContainer      container  = new IndexedContainer();
		Object[] oRow =new Object[numCols];
		for (int i=0; i<numCols; i++){
			String fieldName = dbRS.getTitle(i).toUpperCase();
			container.addContainerProperty(fieldName, String.class,  null);
    		oRow[i] =fieldName;			
		}
		
		item =container.addItem(0);			
		for(int i = 1; i <= numCols; i++){
			String sField = StrUtil.objToStr(dbRS.getPositionData(seqno, i-1));
			item.getItemProperty(oRow[i-1]).setValue(sField);
		}
		
		return item;
	}	
	public Connection getConnDirect(String type ,String ip ,String port ,String sid ,String username ,String password) {
		String driver = "";
		String url = "";
		if (type.equals("OracleDB")){
			driver = "oracle.jdbc.driver.OracleDriver";
			url ="jdbc:oracle:thin:@"+ip+":"+port+":"+sid;
		}
		if (type.equals("MariaDB")){
			driver = "com.mysql.jdbc.Driver";
			url="jdbc:mysql://"+ip+":"+port+":"+sid;
		}		
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 測試使用
	 * @return
	 */
	public Connection getConnDirect() {
		String driver = "oracle.jdbc.driver.OracleDriver";
		String username = "vaadin";
		String password = "vaadin";	
		String url = "jdbc:oracle:thin:@10.10.2.101:1521:fntsdb";
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}	
}
