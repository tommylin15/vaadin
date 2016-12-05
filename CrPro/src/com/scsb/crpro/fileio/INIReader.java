package com.scsb.crpro.fileio;
//************************
//* tommy : 2012/11/30
//* 參數：ini's filename
//***********************
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;


public class INIReader {
    private boolean bOK=true; 
    @SuppressWarnings("unchecked")
	public  Hashtable htIni = new Hashtable();
    private int iCounter=0;
    static String NONE="@";

    private String sIniFile="";
    private File fIniFile;
    
    public boolean isOK(){
        return bOK;
    }

    @SuppressWarnings("unchecked")
    public INIReader(){
    }
    public void setINIFile(String sIniFile){
    	this.sIniFile=sIniFile;
    	this.fIniFile=new File(sIniFile);
    	ReaderToHashtable();
    }
    public void setINIFile(File IniFile){
    	this.fIniFile=IniFile;
    	ReaderToHashtable();
    } 
    public void setINIFile(InputStream is){
        try {
        	BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            ProFix(br);
            br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
    }    
	private void INIReader(File fIniFile) {
	}

	public void ReaderToHashtable(){
		if (!fIniFile.exists()){
            bOK=false;
		}else{
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fIniFile),"utf-8"));
                ProFix(br);
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

	public void ProFix(BufferedReader br){
		String sTemp="";
		String sProfix="";		
        try {
			while((sTemp=br.readLine()) !=null ){
			  	int iPos=0;
			  	sTemp=sTemp.trim();             	
			  	if (sTemp.length()>1 ){ //判斷是否是空行
					if (sTemp.substring(0,1)!="[" &&  (iPos=sTemp.indexOf("="))!=-1){    //為 Item
						htIni.put(sProfix+"@"+sTemp.substring(0,iPos).trim(),sTemp.substring(iPos+1,sTemp.length()).trim());
					}	
					else {  //為 Section
						sProfix=sTemp.substring(1,sTemp.length()-1).trim();                	
					}
				}
			    iCounter++;
			}
		} catch (IOException e) {
			 System.out.println(e.getMessage()+ "...");
		}		
	}
    public String Read(String sSection,String sItem){
    	 String sRet = (String)htIni.get(sSection+"@"+sItem);
    	 
    	 if (sRet != null)
    	 	return sRet;     	 
    	 else
    	 	return NONE;
    }
    public String ReadAll(){
    	String BR="\r\n";
    	HashMap<String, String> htData = new HashMap<String, String>();
    	String sall ="";
    	for (Enumeration<?> e = htIni.keys() ; e.hasMoreElements() ;) {
        	String key =e.nextElement()+"";
         	if (key.indexOf("@")>=0){
         		sall+=key.substring(key.indexOf("@")+1)+"="+htIni.get(key)+BR;
         	}else{
         		sall+=key+"="+htIni.get(key)+BR;
         	}
    	}
    	return sall;
   }    
    
	public HashMap<String, String> Read(String sSection){
		HashMap<String, String> htData = new HashMap<String, String>();
     for (Enumeration<?> e = htIni.keys() ; e.hasMoreElements() ;) {
    	String key =e.nextElement()+"";
     	if (key.indexOf(sSection)>=0){
     		htData.put(key.substring(key.indexOf("@")+1),(String)htIni.get(key));
     	}
     } 
     return htData;
   }    
}