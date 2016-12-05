package com.scsb.util;
//************************
//* tommy : 2012/9/30
//* 參數：ini's filename
//***********************
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
public class INIReader {
    private boolean bOK=true; 
    @SuppressWarnings("unchecked")
	private Hashtable htIni = new Hashtable();
    private int iCounter=0;
    static String NONE="@";


    public boolean isOK(){
        return bOK;
    }

    @SuppressWarnings("unchecked")
	public INIReader(String sIniFile){
		File fIniFile=new File(sIniFile);
		String sTemp="";
		String sProfix="";
		
		if (!fIniFile.exists()){
			//String Path = fIniFile.getPath();
            System.out.println("找不到 "+sIniFile + " 檔案，請檢查之！");
            bOK=false;
		}
        else{
            try{
                FileReader fr=new FileReader(fIniFile);
                BufferedReader br=new BufferedReader(fr);
                
                while((sTemp=br.readLine()) !=null ){
                  	int iPos=0;
                  	sTemp=sTemp.trim();             	
                  	if (sTemp.length()>1 ){ //判斷是否是空行
                		if (sTemp.substring(0,1)!="[" &&  (iPos=sTemp.indexOf("="))!=-1){    //為 Item
                			htIni.put(sProfix+"@"+sTemp.substring(0,iPos).trim(),sTemp.substring(iPos+1,sTemp.length()).trim());
                			//System.out.println(sProfix+sTemp.substring(0,iPos).trim()+":"+sTemp.substring(iPos+1,sTemp.length()).trim());
                		                		
                		}	
                		else {  //為 Section
                			sProfix=sTemp.substring(1,sTemp.length()-1).trim();                	
                		}
                	}
                    iCounter++;
                }
                br.close();
                fr.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage()+ "...");
            }
        }
    }

    public String Read(String sSection,String sItem){
    	 String sRet = (String)htIni.get(sSection+"@"+sItem);
    	 
    	 if (sRet != null)
    	 	return sRet;     	 
    	 else
    	 	return NONE;
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