package com.scsb.util;

import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
public class LogWriter{
	protected String filename;
	protected File file;
	protected FileWriter fWriter;
	protected PrintWriter pWriter;
	protected static String hour;
	protected static String min;
	protected static String sec;
	
	public LogWriter(String filename) throws IOException{
        this.filename = filename;
        file = new File(filename);
    }
	public synchronized void append(String log ){
		append(log ,false);
	}
	public synchronized void append(String log ,boolean HaveTime) {
        fWriter = null;
        pWriter = null;
        
        try{
        	if (HaveTime){
        		log=GetDate()+" "+GetTime(":")+"=="+log;
        	}
            fWriter = new FileWriter(file, true);
            pWriter = new PrintWriter(fWriter);
            // append to log file
            pWriter.println(log);
            // output it to console
            System.out.println(log);
        }catch (IOException e){
            System.out.println ("Error Writing log file: " + e);
        }finally{
            try{
                if (pWriter != null){
                    pWriter.close();
                }
                if (fWriter != null){
                    fWriter.close();
                }
            }catch (IOException e){}
        }
    }
	public long getLength(){
        return file.length();
    }
	public void append(Object obj) throws IOException{
        append(obj.toString());
    }
	public static String GetDate()  throws IOException  {
		String date=GetDate("YYYYMMDD","");
		return date;
	}

	public static String GetDate(String gg)  throws IOException  {
		String date=GetDate(gg,"");
		return date;
	}	
	public static String GetDate(String gg,String gg1)  throws IOException  {
		Calendar rightNow = Calendar.getInstance();
		String year = (rightNow.get(Calendar.YEAR))+"";
		String  month = (rightNow.get(Calendar.MONTH)+1)+"";
		if(month.trim().length()<2)
			month="0"+month;
		String  date = (rightNow.get(Calendar.DATE))+"";
		if(date.trim().length()<2)
			date="0"+date;
		hour= (rightNow.get(Calendar.HOUR))+"";
		min = (rightNow.get(Calendar.MINUTE))+"";
		sec = (rightNow.get(Calendar.SECOND))+"";
		String day=year+month+date;
		gg=gg.trim().toUpperCase();
		if(gg.equals("YYMMDD")){
			day=(Integer.parseInt(day.substring(0,4))-1911)+gg1+day.substring(4,6)+gg1+day.substring(6,8);
		}else if(gg.equals("MMDDYY")){
			day=day.substring(4,6)+gg1+day.substring(6,8)+gg1+(Integer.parseInt(day.substring(0,4))-1911);
		}else if(gg.equals("MMDDYYYY")){
			day=day.substring(4,6)+gg1+day.substring(6,8)+gg1+day.substring(0,4);
		}else if(gg.equals("DDMMYYYY")){
			day=day.substring(6,8)+gg1+day.substring(4,6)+gg1+day.substring(0,4);
		}else if(gg.equals("DDMMYY")){
			day=(Integer.parseInt(day.substring(0,4))-1911)+gg1+day.substring(2,4)+gg1+day.substring(0,2);
		}else{
			day=day.substring(0,4)+gg1+day.substring(4,6)+gg1+day.substring(6,8);
		}
		return day;
	}
	public static String GetTime() {
		String date=GetTime("","");
		return date;
	}

	public static String GetTime(String gg) {
		String date=GetTime(gg,"");
		return date;
	}	
	public static String GetTime(String gg,String kk) {
		Calendar rightNow = Calendar.getInstance();
		String hour="";
		if (kk.equals("1")){
			hour = (rightNow.get(Calendar.HOUR_OF_DAY))+"";
		}else{
			hour = (rightNow.get(Calendar.HOUR))+"";
		}
		if(hour.trim().length()==1)
			hour="0"+hour;
		String  min = (rightNow.get(Calendar.MINUTE))+"";
		if(min.trim().length()==1)
			min="0"+min;
		String  sec = (rightNow.get(Calendar.SECOND))+"";
		if(sec.trim().length()==1)
			sec="0"+sec;
		String time=hour+gg+min+gg+sec;
		return time;
	}	
}