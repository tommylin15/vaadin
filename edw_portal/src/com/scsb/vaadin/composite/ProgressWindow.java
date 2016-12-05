package com.scsb.vaadin.composite;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ProgressWindow  {
	
	int MAX_PROGRESS=100;
	int NOW_PROGRESS=0;
	String caption="";
	Window progressWindow = new Window();
	ProgressBar progressBar = new ProgressBar();
	HorizontalLayout content = new HorizontalLayout();
	Label label =new Label();
	
	public ProgressWindow(){
		this("");
	}
	
	public ProgressWindow(String caption ){
		this.caption=caption;
		progressBar.setIndeterminate(true);
		
		
		content.addComponent(progressBar);
		content.addComponent(label);
		
	    progressWindow = new Window("", content);
	    progressWindow.setClosable(false);
	    progressWindow.setResizable(false);
	    progressWindow.center();
	    
	    //UI.getCurrent().setPollInterval(250);
	    UI.getCurrent().addWindow(progressWindow);
	  }
	
	public void setMaxProgress(int MAX_PROGRESS){
		this.MAX_PROGRESS=MAX_PROGRESS;
	}	
	
	public void setNowProgress(int NOW_PROGRESS){
		this.NOW_PROGRESS=NOW_PROGRESS;
		setCaption(caption+"("+this.NOW_PROGRESS+"/"+this.MAX_PROGRESS+")");
	}
	
	public void close(){
		progressWindow.close();
	}
	
	public void setCaption(String caption){
		label.setCaption(caption);
	}	
	
	public HorizontalLayout getContent(){
		return content;
	}
}
