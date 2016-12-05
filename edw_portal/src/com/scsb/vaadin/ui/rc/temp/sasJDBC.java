package com.scsb.vaadin.ui.rc.temp;

import com.sas.net.connect.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
 
public class sasJDBC {
	 public sasJDBC() {
	 }
	 static public void main(String[] argv){
		 sasJDBC sasjdbc =new sasJDBC();
		 try {
			Connection con  =sasjdbc.getConnection ();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       }
	 
		public static Connection getConnection () throws SQLException {
			Connection con = null;
			try{
				Class.forName("com.sas.net.sharenet.ShareNetDriver");
				String   DB   = "jdbc:sharenet://10.10.2.120:5566";
				con   =   DriverManager.getConnection(DB,"","");  
			} catch (Exception e) {
				e.getMessage();
		    }
			return con;
		}  		 
}