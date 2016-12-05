package com.scsb.domain;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.vaadin.server.VaadinServlet;

/**
 * 多國語系,須使用到
 * 	(hashSystem)
 * @author 3471
 *
 */
public class HashI18N{

	static Logger logger = Logger.getLogger(HashI18N.class.getName());
	protected static HashSystem    hashSystem    	= HashSystem.getInstance();
	protected static Properties systemProp = new Properties();
	
	private static Hashtable<String, Properties> table = new Hashtable<String, Properties>();
	private static HashI18N instance = new HashI18N();
	private String fullPath = null;

	public static HashI18N getInstance(){
		return instance;
	}

	private HashI18N(){
	}
	
	public void setFullPath(String fullPath){
		this.fullPath = fullPath;
	}

	public String getFullPath(){
		return fullPath;
	}
	/**
	 * 每當第一次使用時才會去載入該文檔,之後即存在記憶體中直到系統shutdown
	 * @param lang
	 * @param fileName
	 */
	public void loadProperties(String lang ,String fileName){
		ResourceBundle names = loadResourceBundle(fileName,lang);
		if (names != null){
			Enumeration<String> enuKeys =names.getKeys();
			Properties properties = new Properties();
			while(enuKeys.hasMoreElements()){
				String sKey =enuKeys.nextElement();
				properties.put(sKey, names.getString(sKey));
			}
			if(properties != null)	putProperties(lang ,fileName, properties);
		}
	}

	public void putProperties(String lang ,String fileName, Properties properties){
		 table.put(fileName+"_"+lang, properties);
	}

	public Properties getProperties(String lang, String fileName){
		Properties properties = (Properties)table.get(fileName+"_"+lang);
		if(properties == null){
			loadProperties(lang, fileName);
			properties = (Properties)table.get(fileName+"_"+lang);
		}
		return properties;
	}
	/**
	 * 採用i18n國際標準格式讀取 ??_tw.properties檔
	 * @param fileName
	 * @param lang
	 * @return
	 */
	public ResourceBundle loadResourceBundle(String fileName ,String lang) {
		Locale locale = new Locale(lang);
		if(locale == null) {
			locale = new Locale("tw");
		}		
		systemProp =hashSystem.getProperties();
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		String resourcePath = servletContext.getRealPath("/")+systemProp.getProperty("I18N_PATH").toString()+lang;	
		try {
			File resourceFile = new File(resourcePath);
			URL resourceUrl = resourceFile .toURI().toURL();
			URL[] urls = {resourceUrl };
			ClassLoader loader = new URLClassLoader(urls);
			ResourceBundle bundle = ResourceBundle.getBundle(fileName, locale, loader); 	
			return bundle;
		} catch (Exception e) {
			//e.printStackTrace();
			return null;
		} 
	}	

}
