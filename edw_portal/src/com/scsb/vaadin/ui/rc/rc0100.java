package com.scsb.vaadin.ui.rc;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.vaadin.aceeditor.AceEditor;

import com.sas.net.connect.ConnectException;
import com.sas.net.connect.TelnetConnectClient;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.composite.ScsbPage;
import com.scsb.vaadin.composite.ScsbToolbar;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * sas client 模組
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class rc0100 extends ScsbGlobView {
	ScsbToolbar		  toolBar		=new ScsbToolbar();
	Accordion ac = new Accordion();
	TabSheet     mainTab    =new TabSheet();	
	 
	AceEditor editor = new AceEditor();
	AceEditor logList = new AceEditor();
	Properties info = new Properties();
	TelnetConnectClient client;
	
	java.sql.Connection jdbcConnection = null;
	String xworkPath="";

	ScsbPage    pagePanel     = new ScsbPage();
	/**
	 * 主畫面
	 */
	public rc0100() {
		
		info.setProperty("prompt1", "Username:");
		info.setProperty("response1",i18nProps.getProperty("username","sasweb") );
		info.setProperty("prompt2", "Password:");
		info.setProperty("response2", i18nProps.getProperty("password","sasweb"));
		info.setProperty("prompt3", "Hello>");
		info.setProperty("response3", "sas -nosyntaxcheck  "+i18nProps.getProperty("sasv9_cfg","")+"");
		info.setProperty("sasPortTag", "PORT=");
		
		try {
			client=new TelnetConnectClient(info);
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		create();
	}
	
	public void create(){
		//更新
		toolBar.setRefreshButtonVisible(true);
		toolBar.refreshBu.addClickListener(refreshBulistener);
		//連線
		toolBar.setConnectButtonVisible(true);
		toolBar.connectBu.setImmediate(true);
		toolBar.connectBu.addClickListener(connectBulistener);
		toolBar.connectBu.setIcon(FontAwesome.CAR);
		toolBar.connectBu.setDescription("未連線");
		toolBar.connectBu.addStyleName("danger");

		//執行
		toolBar.setRunButtonVisible(true);
		toolBar.runBu.setImmediate(true);
		toolBar.runBu.addClickListener(runBulistener);
		toolBar.runBu.setIcon(FontAwesome.SEND);
		toolBar.runBu.setDescription("執行");
		toolBar.runBu.addStyleName("friendly");
		toolBar.runBu.setEnabled(false);
		

		HorizontalSplitPanel hSplitLayout =new HorizontalSplitPanel();
		VerticalSplitPanel vSplitLayout =new VerticalSplitPanel();
		
       
        ac.setCaption("SAS環境");
        ac.setSizeFull();
		
        editor.setSizeFull();
        editor.setImmediate(true);

        
        logList.setSizeFull();
        logList.setReadOnly(true);
        logList.setImmediate(true);
        
	    mainTab.setImmediate(true);
	    mainTab.setSizeFull();
		Tab tabEdit =mainTab.addTab(editor);
		tabEdit.setIcon(FontAwesome.EDIT);
		tabEdit.setCaption(i18nProps.getProperty("editor","編輯器"));
		Tab tabLog =mainTab.addTab(logList);
		tabLog.setIcon(FontAwesome.HISTORY);		
		tabLog.setCaption(i18nProps.getProperty("loger","日誌"));
		
		hSplitLayout.setLocked(false);
		hSplitLayout.setSplitPosition(200,Unit.PIXELS);// 100px :UNITS_PIXELS=0
		hSplitLayout.setFirstComponent(ac);	
		hSplitLayout.setSecondComponent(mainTab);
		// screenHeight -toolbar
		int screenHeight =this._screenHeight -34;
		hSplitLayout.setHeight(screenHeight+"px");		
		hSplitLayout.setWidth("100%");		
		
		getContent().addComponent(toolBar.getContent());
		getContent().addComponent(hSplitLayout);
		getContent().setSpacing(false);
	}
	
	/**
	 *  Listener for 更新動作
	 * */	
	ClickListener refreshBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {	
			setSasLib();
		}		
	};		
	
	/**
	 * Listener for 執行動作
	 */
	ClickListener runBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {	
            try {
                int ver1=client.getMajorSasVersion();
                int ver2=client.getMinorSasVersion();
                String driver=client.getDriverName();
                
                event.getButton().removeStyleName("friendly");
				event.getButton().addStyleName("primary");
				event.getButton().setDescription("正在執行中");                
				event.getButton().setEnabled(false);
				
                client.clearLogLines();
                if (editor.getValue() != null){
	                if (editor.getValue().length() > 0 & checkConnection()){
	                	/*把程式裡的註解區塊先拿掉*/
		           		 String sTemp=editor.getValue();
		        		 String sflag1="/*";
		        		 String sflag2="*/";
		        		 int iflag1 =sTemp.indexOf(sflag1);
		        		 int iflag2 =sTemp.indexOf(sflag2);
		        		 while (iflag1 > -1 & iflag2 > -1 & iflag1 < iflag2){
		        			 sTemp =sTemp.substring(0 ,iflag1)+sTemp.substring(iflag2+sflag2.length());
		        			 
		        			 iflag1 =sTemp.indexOf(sflag1);
		        			 iflag2 =sTemp.indexOf(sflag2);
		        		 }
		        		 System.out.println("getTextTransportFormat"+client.getTextTransportFormat());
		                //client.rsubmit(sTemp);
		        		 client.rsubmit(editor.getValue() );
		                event.getButton().removeStyleName("primary");
						event.getButton().addStyleName("friendly");
						event.getButton().setDescription("執行");                
						event.getButton().setEnabled(true);                
		                
		                //String lines=client.getEditLines();
		                String logs=client.getLogLines();
		                //String list=client.getListLines();                
		                client.clearEditLines();
		                client.clearListLines();		
		                client.clearListLines();
		
		                //System.out.println(list);
		                logList.setReadOnly(false);
		                logList.setValue(logs);
		                logList.setReadOnly(true);
		                
		                setSasLib();
		                mainTab.setSelectedTab(logList);
		                
	                }
                }
	        } catch (ConnectException e) {
	                e.printStackTrace();
	        }			
		}		
	};	
	
	/**
	 * Listener for 連線動作
	 */
	ClickListener connectBulistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {	
			
	        try {
				if (client.isConnected()){
					client.disconnect();
					setSasLib();
				}else{
					//client=new TelnetConnectClient(info);
					client.connect("10.10.2.120",23);
					jdbcConnection = client.getSharenet();
					String logs=client.getLogLines();
	                logList.setReadOnly(false);
	                logList.setValue(logs);
	                logList.setReadOnly(true);				
					mainTab.setSelectedTab(logList);					
					setSasLib();
				}

			} catch (ConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}		
	};		
	
	/**
	 * Listener for 讀檔動作
	 */
	ClickListener getDataListener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			if (checkConnection()){
				String buId=event.getButton().getId();
				String libname=buId.substring(0,buId.indexOf("."));
				String tableName=buId.substring(buId.indexOf(".")+1);
				String dataTableName=libname+"."+tableName;

				String saveFile=i18nProps.getProperty("savePath","")+"/"+users.getUserid()+"_"+DateUtil.getDTS()+".xls";
				SasTable table =new SasTable(jdbcConnection ,dataTableName ,saveFile);
		        
				Tab tabQuery =mainTab.addTab(table.vSplitLayout);
				tabQuery.setIcon(FontAwesome.SEARCH);
				tabQuery.setCaption("檢視:"+buId);	
				tabQuery.setClosable(true);
				mainTab.setSelectedTab(tabQuery);
			}
		}
	};
	/**
	 * check connection
	 */
	
	public boolean checkConnection(){
		if (!client.isConnected()){
			toolBar.connectBu.removeStyleName("friendly");
			toolBar.connectBu.addStyleName("danger");
			toolBar.connectBu.setDescription("未連線");
			toolBar.runBu.setEnabled(false);
			return false;
		}else{
			toolBar.connectBu.removeStyleName("danger");
			toolBar.connectBu.addStyleName("friendly");
			toolBar.connectBu.setDescription("連線中");			
			toolBar.runBu.setEnabled(true);			
			return true;	
		}
		
	}
	/**
	 * 建立sas libname Accordion
	 */
	public void setSasLib(){
		boolean isCon=checkConnection();
		if (isCon){
	          try {
				ac.removeAllComponents();
				//先加一個work libname
				/*
				VerticalLayout workContent =setSasDataList("WORK");
				Tab myTab=ac.addTab(workContent , "WORK");
				myTab.setIcon(FontAwesome.FOLDER);
				*/
				//再補其它的libname
	        	Statement statement = jdbcConnection.createStatement();
	            String query = new String(" select distinct libname  from dictionary.tables   "
	            		+ " where libname in ("+i18nProps.getProperty("libname_list"+users.getDeptid(),"'WORK'") +")"
	            		+ " order libname");            
	            ResultSet resultset = statement.executeQuery(query);
	            while (resultset.next()){
	            	String libname=resultset.getString(1).trim();
	            	VerticalLayout libContent =setSasDataList(libname);
	            	Tab libTab=ac.addTab(libContent , libname);
	            	libTab.setIcon(FontAwesome.FOLDER);
	            	libTab.setStyleName("scsbBg5");
	            }//while
	            resultset.close();
	            statement.close();
	          } catch (java.sql.SQLException e) {
	          }			
			
			String logs=client.getLogLines();		
		}else{
			ac.removeAllComponents();			
		}
	}
	
	/**
	 * 取得sas libaname的 data list
	 * @param libname
	 * @return
	 */
	private VerticalLayout setSasDataList(String libname){
        String queryData = new String("select memname ,memlabel from dictionary.tables " +
                "where libname ='" +libname + "' order memname" );
        
		VerticalLayout vContent = new VerticalLayout();
		int iFlag=0;        
        Statement statement;
		try {
			statement = jdbcConnection.createStatement();
	        ResultSet rs = statement.executeQuery(queryData);
	        while (rs.next()) {
				if (vContent != null){
						iFlag++;
						String filename =rs.getString(1);
						String filelabel =rs.getString(2);
						Button button =new Button(filename);
						button.setDescription(filelabel);
						button.setWidth("100%");
						
				    	if (iFlag % 2 == 1){
				    		button.addStyleName("scsbBg2");
				    		button.addStyleName("textLeft");
				    	}else{
				    		button.addStyleName("scsbBg3");
				    		button.addStyleName("textLeft");
				    	}
				    	button.setIcon(FontAwesome.TABLE);
						button.setId(libname.trim()+"."+filename.trim());
						button.addClickListener(getDataListener);
						vContent.addComponent(button);
				}//if		        	
		     }//while		
		     rs.close();	
	     } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vContent;
	}
}
