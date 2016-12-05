package com.scsb.vaadin.ui.gc;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.scsb.util.IO;
import com.scsb.util.PropertiesUtil;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.OptionGroup;

/**
 * 重新載入記憶體
 * @author 3471
 *
 */
public class gc1050 extends ScsbGlobView {

	/**
	 * 重新載入記憶體-主畫面
	 */
	public gc1050() {
		
		
    	ServletContext sce = VaadinServlet.getCurrent().getServletContext();
		String path = sce.getRealPath("/");
		String systemFileProp = sce.getInitParameter("system-file")+"";
		String hashTablePropertyFile = sce.getInitParameter("hashtable-file")+"";
		
		Properties hashTableProp = PropertiesUtil.loadProperties(path+hashTablePropertyFile);
		Enumeration enumeration = hashTableProp.propertyNames();
		
		OptionGroup optionGroup =new OptionGroup();
		optionGroup.setMultiSelect(true);

		do{
			if(!enumeration.hasMoreElements())	break;
			String hashProp = (String)enumeration.nextElement()+"";
			if (hashProp.indexOf("TABLE") > -1){
				String table =hashTableProp.getProperty(hashProp);
				optionGroup.addItem(table);
				try {
					Class classTable = Class.forName(hashTableProp.getProperty(hashProp));
					Object objTable =classTable.getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
					classTable.getDeclaredMethod("Reload", new Class[0]).invoke(objTable, new Object[0]);
				}catch(Exception exception1){
					System.out.println(exception1);
				}
			}
		} while(true);
		optionGroup.setReadOnly(true);
		getContent().addComponent(optionGroup);
	}
	
}
