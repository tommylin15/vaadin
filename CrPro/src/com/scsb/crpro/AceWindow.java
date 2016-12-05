package com.scsb.crpro;


import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;
import org.vaadin.aceeditor.AceTheme;

import com.scsb.util.IO;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

@SuppressWarnings("serial")
public class AceWindow extends Window{
	
	VerticalLayout vLayout =new VerticalLayout();
	//TextArea editor = new TextArea();
	AceEditor editor = new AceEditor();
	MenuBar menuSet =new MenuBar();	
	MenuBar.MenuItem downloadBu=null;
	MenuBar.MenuItem saveBu=null;
	com.scsb.util.DynamicStreamResource dsr =null;
	FileDownloader fileDownloader =null;
	String FielName=null;
	String FileString=null;
	String FileEncode=null;
	
    public AceWindow(String themePath  ,String FileCode ,String FielPath  ,String FielName ,String FileString ){
    	Create(themePath ,FileCode ,FielPath ,FielName ,FileString ,false);
    }
    public AceWindow(String themePath  ,String FileCode ,String FielPath ,String FielName ,final String FileString ,Boolean isEdit){
    	Create(themePath ,FileCode ,FielName ,FielPath ,FileString ,isEdit);
    }
	private void Create(String themePath ,String FileCode ,String FilePath ,String FielName, String FileString, Boolean isEdit) {
		this.FielName =FielName;
		this.FileString=FileString;
		this.FileEncode=FileCode;
    	this.setModal(true);
		this.setWidth("90%");
		this.setHeight("90%");


        this.setContent(vLayout);
        vLayout.setMargin(true);
        vLayout.setSpacing(true);
        vLayout.addComponent(menuSet);	

        editor.setImmediate(true);
        editor.setWordWrap(false);
        editor.setUseWorker(true);          
        editor.setThemePath(themePath);        
        editor.setModePath(themePath);
        editor.setWorkerPath(themePath);
        editor.setReadOnly(false);
		editor.setValue(FileString);
        editor.setReadOnly(true);
        editor.setMode(AceMode.text);
        if (FielName.indexOf(".java") > -1)	editor.setMode(AceMode.java);
        if (FielName.indexOf(".jsp") > -1)	editor.setMode(AceMode.jsp);        
        if (FielName.indexOf(".sas") > -1)	editor.setMode(AceMode.pascal);
        if (FielName.indexOf(".html") > -1)	editor.setMode(AceMode.html);
        if (FielName.indexOf(".htm") > -1)	editor.setMode(AceMode.html);
        if (FielName.indexOf(".sql") > -1)	editor.setMode(AceMode.sql);
        if (FielName.indexOf(".xml") > -1)	editor.setMode(AceMode.xml);
        if (FielName.indexOf(".json") > -1)	editor.setMode(AceMode.json);
        editor.setTheme(AceTheme.eclipse);
        editor.setWidth("100%");
		float iHeight =UI.getCurrent().getPage().getBrowserWindowHeight() *9 /10;
		if (iHeight  < 540){
			iHeight=540;
		} 
		float iBodyH =iHeight - 130;        
        editor.setHeight(iBodyH ,Unit.PIXELS);        
        vLayout.addComponent(editor);
        
        //menu bar
        if (isEdit){
        	editor.setReadOnly(false);
	        saveBu=menuSet.addItem("儲存檔案", null, saveFile);
	        saveBu.setIcon(FontAwesome.FILE);
	    	saveBu.setEnabled(false);
        }else{
        	editor.setReadOnly(true);
            downloadBu=menuSet.addItem("下載檔案", null, downloadFile);
            downloadBu.setIcon(FontAwesome.DOWNLOAD);
            DownloadFile();            
        }
        editor.addValueChangeListener(new ValueChangeListener(){
			public void valueChange(ValueChangeEvent event) {
				saveBu.setEnabled(true);
			}
        	
        });
    	UI.getCurrent().addWindow(this);		
	}
	
	   private Command downloadFile = new Command() {
			public void menuSelected(MenuItem selectedItem) {
				DownloadFile();
			}
	    };	
	    private Command saveFile = new Command() {
			public void menuSelected(MenuItem selectedItem) {
				SaveFile();
				saveBu.setEnabled(false);
			}
	    };	
	
	public void SaveFile(){
        //直接寫檔
       	IO.writeFile(FielName, editor.getValue() ,this.FileEncode);
	}	    
	public void DownloadFile(){
		dsr = new com.scsb.util.DynamicStreamResource(editor.getValue().getBytes() ,FielName ,"text/plain");
		fileDownloader = new FileDownloader(dsr);
		fileDownloader.extend(menuSet);
	}	
}