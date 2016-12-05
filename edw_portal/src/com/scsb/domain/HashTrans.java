package com.scsb.domain;

import java.util.Hashtable;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.service.TransService;
import com.scsb.db.service.TransdService;
import com.vaadin.data.util.BeanContainer;

public class HashTrans{

	static Logger logger = Logger.getLogger(HashTrans.class.getName());
	
	private static Hashtable<String, Trans> transTable = new Hashtable<String, Trans>();
	private static Hashtable<String, Transd> transDTable = new Hashtable<String, Transd>();
	private static Hashtable<String, BeanContainer<String, Transd>> transDcTable = new Hashtable<String, BeanContainer<String, Transd>>();
	
	//營業單位可授權
	private static Hashtable<String, Trans> transPTable = new Hashtable<String, Trans>();
	private static Hashtable<String, Transd> transDPTable = new Hashtable<String, Transd>();
	//後勤單位可授權
	private static Hashtable<String, Trans> transLTable = new Hashtable<String, Trans>();
	private static Hashtable<String, Transd> transDLTable = new Hashtable<String, Transd>();
	
	private static HashTrans instance = new HashTrans();
	private BeanContainer<String,Trans> transContainer;
	
	public static HashTrans getInstance(){
		return instance;
	}

	private HashTrans(){
		this.transContainer = null;
		putTrans();
	}
	
	public void Reload(){
		this.transContainer = null;
		putTrans();
	}	

	public Trans getTrans(String s){
		Trans trans = null;
		Object obj = transTable.get(s);
		if(obj != null)
			trans = (Trans)obj;
		return trans;
	}

	public void putTrans(){
		TransService transSrv = new TransService("");
		this.transContainer = transSrv.getTrans_All();
		if(transContainer != null ){
			for(int i=0;i<transContainer.size();i++){
				Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
				transTable.put(bean.getGroupid(), bean);
				putTransD(bean.getGroupid() ,bean);
			}
		}
	}

	public BeanContainer<String,Trans> getAllTrans(){
		return this.transContainer;
	}
	
	public Hashtable<String, Trans> getDeptTrans(String sType){
		if (sType.equals("P")) 		return HashTrans.getTransPTable();
		if (sType.equals("L")) 		return HashTrans.getTransLTable();
		return null;
	}	
	
	

	public Transd getTransD(String programid){
		Transd transd = null;
		Object obj = transDTable.get(programid);
		if(obj != null)	transd = (Transd)obj;
		return transd;
	}

	public void putTransD(String groupId ,Trans trans){
		TransdService transDSrv = new TransdService("");
		BeanContainer<String,Transd> transDContainer = transDSrv.getTransd_GroupId(groupId);
		if(transDContainer != null){		
			transDcTable.put(groupId, transDContainer);
			for(int i=0;i<transDContainer.size();i++){
				Transd bean=transDContainer.getItem(transDContainer.getIdByIndex(i)).getBean();
				transDTable.put(bean.getProgramid(), bean);
				if (bean.getDeptactionmode().equals("Y")){
					getTransPTable().put(groupId, trans);
					getTransDPTable().put(bean.getProgramid(), bean);
					
					getTransLTable().put(groupId, trans);
					getTransDLTable().put(bean.getProgramid(), bean);
				}
				if (bean.getDeptactionmode().equals("P")){
					getTransPTable().put(groupId, trans);
					getTransDPTable().put(bean.getProgramid(), bean);
				}			
				if (bean.getDeptactionmode().equals("L")){
					getTransLTable().put(groupId, trans);
					getTransDLTable().put(bean.getProgramid(), bean);
				}					
			}
		}
	}

	public BeanContainer<String,Transd> getAllTransD(String groupId){
		BeanContainer<String,Transd> transDContainer = null;
		Object obj = transDcTable.get(groupId);
		if(obj != null)
			transDContainer = (BeanContainer<String,Transd>)obj;
		return transDContainer;
	}
	public Hashtable<String,Transd> getAllTransDP(String groupId){
		Trans transP = transPTable.get(groupId);
		Hashtable<String,Transd> transDP= new Hashtable<String,Transd>();
		BeanContainer<String,Transd> beanContainer =getAllTransD(groupId);
		for(int i=0;i<beanContainer.size();i++){
			Transd bean=beanContainer.getItem(beanContainer.getIdByIndex(i)).getBean();
			if (getTransDPTable().get(bean.getProgramid()) != null){
				transDP.put(bean.getProgramid(), bean);
			}
		}
		return transDP;
	}	
	public Hashtable<String,Transd> getAllTransDL(String groupId){
		Trans transL = transLTable.get(groupId);
		Hashtable<String,Transd> transDL= new Hashtable<String,Transd>();
		BeanContainer<String,Transd> beanContainer =getAllTransD(groupId);
		for(int i=0;i<beanContainer.size();i++){
			Transd bean=beanContainer.getItem(beanContainer.getIdByIndex(i)).getBean();
			if (getTransDLTable().get(bean.getProgramid()) != null){
				transDL.put(bean.getProgramid(), bean);
			}
		}
		return transDL;
	}

	public static Hashtable<String, Trans> getTransPTable() {
		return transPTable;
	}

	public static void setTransPTable(Hashtable<String, Trans> transPTable) {
		HashTrans.transPTable = transPTable;
	}

	public static Hashtable<String, Trans> getTransLTable() {
		return transLTable;
	}

	public static void setTransLTable(Hashtable<String, Trans> transLTable) {
		HashTrans.transLTable = transLTable;
	}

	public static Hashtable<String, Transd> getTransDLTable() {
		return transDLTable;
	}

	public static void setTransDLTable(Hashtable<String, Transd> transDLTable) {
		HashTrans.transDLTable = transDLTable;
	}

	public static Hashtable<String, Transd> getTransDPTable() {
		return transDPTable;
	}

	public static void setTransDPTable(Hashtable<String, Transd> transDPTable) {
		HashTrans.transDPTable = transDPTable;
	}	
}
