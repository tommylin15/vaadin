package com.scsb.crpro;

import java.sql.SQLException;

import com.scsb.domain.HashSystem;
import com.scsb.util.DBUtil;
import com.scsb.util.Status;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.UI;

public class DBService{
	
	public Status status =new Status();
	public DBService(){	}
	//取得TableList
	public IndexedContainer getTableList(String dbPool ,String dbOwner){
		IndexedContainer container = new IndexedContainer();
		try{
			container = DBUtil.getTableList(dbPool ,dbOwner);
		} catch (RuntimeException se) {
			// TODO Auto-generated catch block
			se.printStackTrace();		
			status.addException(new Exception(se.getMessage()));			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return container;
	}
	public IndexedContainer getFieldList(String dbPool ,String dbOwner ,String tablename ,IndexedContainer container){
		try{
			container.removeAllItems();
			container = DBUtil.getColName(dbPool ,dbOwner ,tablename ,container);
		} catch (RuntimeException se) {
			// TODO Auto-generated catch block
			se.printStackTrace();		
			status.addException(new Exception(se.getMessage()));			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return container;
	}
}