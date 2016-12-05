package com.scsb.crpro.im;

import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;


public class OpenFireManager{
	
	public OpenFireManager(){
		
	}
    public static void main(String[] args) {
    	try {
			java.sql.Connection con=DbConnectionManager.getConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//WebManager webManager=org.jivesoftware.util.WebManager;
    	//SessionManager sessionManager = webManager.getSessionManager();
    	//PresenceManager presenceManager = webManager.getPresenceManager();
    	//UserManager userManager = webManager.getUserManager();
    	//String serverDomainName =XMPPServer.getInstance().getServerInfo().getXMPPDomain();     	
    }
}    