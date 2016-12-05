package com.scsb.crpro;

import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.domain.HashSystem;
import com.scsb.util.IO;
import com.vaadin.data.Item;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.OptionGroup;

/**
 * 取得 公用的群組元件
 */
public class UtilOptionGroup  {
	protected static HashSystem hashSystem = HashSystem.getInstance();
	protected static Properties systemProps     =hashSystem.getProperties();
	private Hashtable<String,String> hData =new Hashtable<String,String>();
	private OptionGroup optionGroup = new OptionGroup();
	public UtilOptionGroup(){
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		String jsonString =IO.read(servletContext.getRealPath("/")+systemProps.getProperty("extensionJson"));
		JsonContainer jsonData = JsonContainer.Factory.newInstance(jsonString);		
			
	    for (int i=0;i<jsonData.size();i++) {
			Item item = jsonData.getItem(jsonData.getIdByIndex(i));
			String CODE_ID =item.getItemProperty("extension").getValue()+"";			
			String CODE_NAME =item.getItemProperty("extension").getValue()+"";
			String isList =item.getItemProperty("isList").getValue()+"";
			String onlineEdit =item.getItemProperty("onlineEdit").getValue()+"";
			if (isList.equals("Y")){
				optionGroup.addItem(CODE_ID);
				optionGroup.setItemCaption(CODE_ID, CODE_NAME);
			}
            hData.put(CODE_ID ,onlineEdit);
	    }//for
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);
		optionGroup.addStyleName("horizontal");
		
	}
	
	/**
	 * 副檔名列表
	 * @return
	 */
	public OptionGroup getFileOptionGroup(){
		return optionGroup;
	}
	public Hashtable<String,String> getData(){
		return hData;
	}
	
}