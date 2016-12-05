package com.scsb.vaadin.ui.rc;

import com.sas.net.connect.ConnectException;
import com.sas.net.connect.TelnetConnectClient;
import com.scsb.util.DBUtil;
import com.scsb.util.IO;
import com.scsb.vaadin.ui.export.ExcelExporter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

public class SasTable{
	
    HorizontalLayout controlBar = new HorizontalLayout();
    HorizontalLayout pageSize = new HorizontalLayout();
	 VerticalSplitPanel vSplitLayout =new VerticalSplitPanel();
    
	 Table table=new Table();
	 TextField currentPageTextField = new TextField();
	 Label pageInfo = new Label("目前筆數", ContentMode.HTML);
	 Label totalPagesLabel = new Label("頁數", ContentMode.HTML);
	 ExcelExporter exportXls = new ExcelExporter();
	 CheckBox haveLabel =new CheckBox();
	 
	 String saveFile="";
	IndexedContainer dataContainer = new IndexedContainer();
	int iTotalCount=0;
	int iTotalPage=0;
	int iNowPage=0;
	int iPage =25;
	String sasTable=""; 
	Connection connection;
    
    public SasTable(Connection connection ,String sasTable ,String saveFile){
    	this.saveFile   =saveFile;
    	this.sasTable=sasTable;
    	this.connection=connection;
        vSplitLayout.setLocked(false);
        vSplitLayout.setSplitPosition(40,Unit.PIXELS);// 100px :UNITS_PIXELS=0
        vSplitLayout.setFirstComponent(controlBar);	
        vSplitLayout.setSecondComponent(null);
        vSplitLayout.setImmediate(true);
        vSplitLayout.setSizeFull();
        
    	try {
    		//Connection con =getConnection();
    		IndexedContainer countContainer =DBUtil.ContainerQueryDB(this.connection ,"select count(*) as ICOUNT from "+sasTable+" ");
    		//con.close();
    		if (countContainer.size()>0){
    			iTotalCount =(int) ((Float.parseFloat((String)countContainer.getItem(countContainer.getIdByIndex(0)).getItemProperty("ICOUNT").getValue()))+0);
    			if (iTotalCount > 0){
    				iNowPage=1; 
    				setCurrentPage(1);
    		        createControls();    				
    			}
    		}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public HorizontalLayout createControls() {
        Label itemsPerPageLabel = new Label("每頁筆數:");
        final ComboBox itemsPerPageSelect = new ComboBox();

        itemsPerPageSelect.addItem("5");
        itemsPerPageSelect.addItem("10");
        itemsPerPageSelect.addItem("25");
        itemsPerPageSelect.addItem("50");
        itemsPerPageSelect.addItem("100");
        itemsPerPageSelect.addItem("500");
        itemsPerPageSelect.addItem("1000");
        itemsPerPageSelect.addItem("2000");        
        itemsPerPageSelect.setImmediate(true);
        itemsPerPageSelect.setNullSelectionAllowed(false);
        itemsPerPageSelect.setWidth("80px");
        itemsPerPageSelect.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (itemsPerPageSelect.getValue() != null){
					iPage=Integer.parseInt((String) itemsPerPageSelect.getValue());
					setCurrentPage(1);
				}
			}
    	});        

        itemsPerPageSelect.select("25");
        Label pageLabel = new Label("頁數", ContentMode.HTML);
        
        currentPageTextField.setValue(iNowPage+"");
        currentPageTextField.setConverter(Integer.class);
        Label separatorLabel = new Label("&nbsp;/&nbsp;", ContentMode.HTML);
       
        currentPageTextField.setImmediate(true);
        currentPageTextField.addValueChangeListener(new ValueChangeListener() {
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                if (currentPageTextField.isValid()       && currentPageTextField.getValue() != null) {
                    int page = Integer.valueOf(String.valueOf(currentPageTextField.getValue()));
                    setCurrentPage(page);
                }
            }
        });
        pageLabel.setWidth(null);
        currentPageTextField.setWidth("20px");
        separatorLabel.setWidth(null);
        totalPagesLabel.setWidth(null);


        final Button first = new Button("<<", new ClickListener() {
            public void buttonClick(ClickEvent event) {
                setCurrentPage(1);
            }
        });
        final Button previous = new Button("<", new ClickListener() {
            public void buttonClick(ClickEvent event) {
                previousPage();
            }
        });
        final Button next = new Button(">", new ClickListener() {
            public void buttonClick(ClickEvent event) {
                nextPage();
            }
        });
        final Button last = new Button(">>", new ClickListener() {
            public void buttonClick(ClickEvent event) {
                setCurrentPage(iTotalPage);
            }
        });
        first.addStyleName("link");
        previous.addStyleName("link");
        next.addStyleName("link");
        last.addStyleName("link");

        exportXls.addStyleName("icon-only");
        exportXls.setIcon(new ThemeResource("./images/excel.png"));
        exportXls.setDescription("Export to Excel");
        exportXls.setTableToBeExported(table);
        exportXls.addClickListener(excellistener);
        
   	 	haveLabel.setDescription("是否顯示LABEL");
   		haveLabel.addStyleName("icon-only");
   		haveLabel.setIcon(FontAwesome.NAVICON);
   	 	haveLabel.addValueChangeListener(labelListener);       
        
        pageSize.addComponent(itemsPerPageLabel);
        pageSize.addComponent(itemsPerPageSelect);
        pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(itemsPerPageSelect,Alignment.MIDDLE_LEFT);
        pageSize.setSpacing(true);
        pageSize.addComponent(first);
        pageSize.addComponent(previous);
        pageSize.addComponent(pageLabel);
        pageSize.addComponent(currentPageTextField);
        pageSize.addComponent(separatorLabel);
        pageSize.addComponent(totalPagesLabel);
        pageSize.addComponent(next);
        pageSize.addComponent(last);
        pageSize.addComponent(pageInfo);
   	 	pageSize.addComponent(haveLabel);        
        pageSize.addComponent(exportXls);
        
        pageSize.setComponentAlignment(haveLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(currentPageTextField, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(separatorLabel,Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(totalPagesLabel, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(pageInfo, Alignment.MIDDLE_LEFT);
        pageSize.setComponentAlignment(exportXls, Alignment.MIDDLE_LEFT);
        
        controlBar.addComponent(pageSize);
        controlBar.setWidth("100%");
        controlBar.setExpandRatio(pageSize, 1);
        return controlBar;
    }

    ClickListener excellistener = new ClickListener() {
		@Override
		public void buttonClick(ClickEvent event) {
			//System.out.println("buttonClick.....");
			InputStream is = exportXls.getStream();
			//留存記錄
			try {
				InputStreamReader inputStramReader = new InputStreamReader(is);
				BufferedReader bufferedReader = new BufferedReader(inputStramReader);
				int ch;
				StringBuffer buffer = new StringBuffer();
				while ((ch = bufferedReader.read()) > -1) {
					buffer.append((char) ch);
				}
				bufferedReader.close();
				inputStramReader.close();
				is.close();
				
				IO.writeFile(saveFile,buffer.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
    	
    };
    
    ValueChangeListener labelListener = new ValueChangeListener() {
		@Override
		public void valueChange(ValueChangeEvent event) {
			try {
				if (haveLabel.getValue()){
					//取得label
					Hashtable<String, String> hLabel;
					hLabel = DBUtil.getTableLabel(connection ,"select * from " +sasTable+"(obs=1)",false);
					for(java.util.Iterator<String> iter=hLabel.keySet().iterator();iter.hasNext();){
						String key=iter.next();
						table.setColumnHeader( key, hLabel.get(key));
					}				
				}else{
					//取得label
					Hashtable<String,String> hLabel =DBUtil.getTableLabel(connection ,"select * from " +sasTable+"(obs=1)",false);
					for(java.util.Iterator<String> iter=hLabel.keySet().iterator();iter.hasNext();){
						String key=iter.next();
						table.setColumnHeader( key, key);
					}				
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
	};	
    
    public void nextPage() {
    	if ((iNowPage+1) <=iTotalPage){
    		iNowPage++;
    		currentPageTextField.setValue(iNowPage+"");
    		getData();
    	}
    }

    public void previousPage() {
    	if ((iNowPage-1) >= 1){
    		iNowPage--;
    		currentPageTextField.setValue(iNowPage+"");
    		getData();
    	}
    }

    public void setCurrentPage(int page) {
		if (iTotalCount % iPage == 0){
			iTotalPage =iTotalCount / iPage ;
		}else{
			iTotalPage =iTotalCount / iPage +1;
		}    	
    	if (page >= 1 &  page <=iTotalPage){
	    	iNowPage=page;
	    	getData();
    	}
    }
	 
    private void getData(){
		try {
			int firstObs=(iNowPage-1)*iPage+1;
			int obs=iNowPage*(iPage);
			//Connection con =getConnection();
			dataContainer = DBUtil.ContainerQueryDB(connection, " select * from "+sasTable+"(firstobs="+firstObs+" obs="+obs+")");
			//con.close();
			pageInfo.setValue("目前筆數:"+firstObs+"-"+obs+" ，總筆數:"+iTotalCount);
			totalPagesLabel.setValue(iTotalPage+"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		table.setContainerDataSource(dataContainer);
		table.setImmediate(true);
		exportXls.setTableToBeExported(table);		
		vSplitLayout.setSecondComponent(table);    
    }
	/**
	 * 連線至sas share server (JDBC)
	 * */
    /*
	public Connection getConnection () throws SQLException {
		Connection con = null;
		try{
			
			
			Class.forName("com.sas.net.sharenet.ShareNetDriver");
			String   DB   = "jdbc:sharenet://"+i18nProps.getProperty("sas_server_ip","sastesting")+":"
										+i18nProps.getProperty("sas_server_share_port","5566")+"";
			con   =   DriverManager.getConnection(DB
																						,i18nProps.getProperty("username","")
																						,i18nProps.getProperty("password",""));
		} catch (Exception e) {
			e.getMessage();
	    }
		return con;
	}  	 
	*/
}