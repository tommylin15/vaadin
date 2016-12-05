package com.scsb.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.scsb.util.StrUtil;

public class ConnectionPool {
	private String jdbcDriverClass;
	private String jdbcURL;
	private String jdbcUserName;
	private String jdbcPassword;
	private int minimumConnections;
	private int maximumConnections;
	
	private  javax.sql.DataSource dataSource = null;
	private Logger log = Logger.getLogger(ConnectionPool.class);

	public String getJdbcDriverClass() {
		return jdbcDriverClass;
	}

	public String getJdbcURL() {
		return jdbcURL;
	}

	public String getJdbcUserName() {
		return jdbcUserName;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public int getMinimumConnections() {
		return minimumConnections;
	}

	public int getMaximumConnections() {
		return maximumConnections;
	}
	
	/**
	 * 使用Lookup方式.取得應用伺服器上的資料庫連線
	 * @throws NamingException 
	 */
	private void setDataSource(String dbType ,String lookupName) {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			if (dbType.equals("teradata")){
				System.out.println("java:comp/env/jdbc/"+lookupName);
				dataSource = (javax.sql.DataSource)initCtx.lookup("java:comp/env/jdbc/"+lookupName);				
			}else{
				System.out.println("java:/comp/env/"+lookupName);
				dataSource = (javax.sql.DataSource)initCtx.lookup("java:/comp/env/"+lookupName);
			}
			
			//Context envCtx = (Context) initCtx.lookup("java:comp/env");
			//dataSource = (javax.sql.DataSource)envCtx.lookup(lookupName);
		} catch (NamingException e) {
			log.error(StrUtil.convException(e));
		}
	}
	
	/**
	 * 使用DBCP方式.取得資料庫連線
	 * @param jdbcDriverClass
	 * @param jdbcURL
	 * @param jdbcUserName
	 * @param jdbcPassword
	 * @param minimumConnections
	 * @param maximumConnections
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public ConnectionPool(String dbType ,String lookupName) throws ClassNotFoundException, SQLException {
		// 20130329.G.直接改用lookup方式去取得連線
		setDataSource(dbType ,lookupName);
	}

	public synchronized Connection getConnection() {
		Connection result = null;
		try {
			result = dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e.toString());
		}
		return result;
	}
	
	public Connection getConnectionNoWait() {
		return getConnection();
	}

	public synchronized void releaseConnection(Connection connection) {		
		try {
			connection.close();
		} catch (SQLException e) {
			log.error(System.err);
		}
	}

	public synchronized void shutdown() {
		dataSource = null;
	}
}
