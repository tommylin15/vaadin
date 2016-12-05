package com.scsb.crpro.explorer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import com.scsb.domain.HashSystem;

public class FileData {
	protected static HashSystem hashSystem   = HashSystem.getInstance();
	protected static Properties systemProps  = hashSystem.getProperties();
	
    public File m_file;
    public String m_path;
    public String m_name;
    public String m_type;
    public String m_date;
    public String m_size;
    public String m_isLock;
    public boolean isEdit;
    public long i_size;
    
	public FileData(File file_in, boolean isParent ,Hashtable<String,String> hData) {
		m_isLock="Y";
		isEdit=false;
	    if (isParent == false) {
	        m_file = file_in;
	        m_path = file_in.getPath();
	        m_name = m_file.getName().toLowerCase();
	        m_type = (m_file.isDirectory() ? "檔案資料夾" : "檔案");
	        m_size=(m_file.length() /1024)+"KB";
	        i_size=(m_file.length() /1024);
	        Date d = new Date(m_file.lastModified());
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd aa KK:mm:ss");
	        m_date = sdf.format(d);
	        
			String m_lastname =m_name;
			if (m_lastname.lastIndexOf(".") > -1)
				m_lastname =m_lastname.substring(m_lastname.lastIndexOf("."));
			//(副檔名在選擇範圍內而且size<預設值 ) or 副檔名為log
			if (
					( i_size < Long.parseLong(systemProps.getProperty("downloadSize"))
					  && hData.get(m_lastname) != null
					 ) 
			  || m_lastname.indexOf(".log") > -1){	 
				m_isLock="N";
			}else{
				m_isLock="Y";
			}
			if (hData.get(m_lastname) != null){
				if (hData.get(m_lastname).equals("Y")){
					isEdit=true;
				}
			}
	    }
	
	    if (isParent == true) {
	        m_file = file_in;
	        m_name = "..";
	        m_type = "回上一層目錄";
	        m_date = "";
	    }
	}
}