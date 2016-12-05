package com.scsb.vaadin.ui.popover;

import java.io.UnsupportedEncodingException;

import com.scsb.util.DynamicStreamResource;
import com.scsb.vaadin.composite.ScsbPopover;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class AcePopover extends ScsbPopover{
	//AceEditor editor = new AceEditor();
	String FielName="";
	Button button_Save = new Button();
	DynamicStreamResource dsr =null;
	FileDownloader fileDownloader =null;
	
	public AcePopover(String FielName ,String FileString){
		this.FielName =FielName;
		
		this.setTitle("<font color='red'><b>"+i18nProps.getProperty("title","檔案預覽")+"</b>&nbsp;</font>");
		popover.setWidth("90%");
		popover.setHeight("90%");

    	button_Save = new Button("下載檔案");
        button_Save.setStyleName("small default");
        button_Save.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {	
				DownloadFile();
			}
		});
		this.content.addComponent(button_Save);
		this.content.setComponentAlignment(button_Save,Alignment.TOP_CENTER);
		this.content.setExpandRatio(button_Save, 1.0f);         
        
        //editor.setValue(FileString);
       // editor.setImmediate(true);
       // editor.setReadOnly(true);
        //editor.setMode(AceMode.text);
       // if (FielName.indexOf(".sas") > -1)  editor.setMode(AceMode.sass);
       // if (FielName.indexOf(".java") > -1)  editor.setMode(AceMode.java);
      //  if (FielName.indexOf(".xml") > -1)  editor.setMode(AceMode.xml);
      //  if (FielName.indexOf(".log") > -1)  editor.setMode(AceMode.logiql);
        //editor.setTheme(AceTheme.xcode);
        //editor.setSizeFull();
       // editor.setWidth("100%");
		float iHeight =UI.getCurrent().getPage().getBrowserWindowHeight() *9 /10;
		if (iHeight  < 740){
			iHeight=740;
		} 
		float iBodyH =iHeight - 200;        
      //  editor.setHeight(iBodyH ,Unit.PIXELS);
		//this.content.addComponent(editor);
		//this.content.setComponentAlignment(editor,Alignment.TOP_CENTER);
		//this.content.setExpandRatio(editor, 1.0f);        
        
	}

	public void DownloadFile(){
		/*
		try {
			dsr = new DynamicStreamResource(editor.getValue().getBytes("UTF-8") ,FielName ,"text/plain");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		*/
		fileDownloader = new FileDownloader(dsr);
		fileDownloader.extend(button_Save);
	}	
}