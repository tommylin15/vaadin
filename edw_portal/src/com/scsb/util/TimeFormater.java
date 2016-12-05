package com.scsb.util;

import java.io.Serializable;

public class TimeFormater
	implements Serializable
{

	public static final int NORMAL = 0;
	public static final int COLOR = 1;
	private long timeMillis;
	private String format;
	private int style;

	public TimeFormater()
	{
		format = "hh:mm:ss:ddd";
		style = 0;
	}

	public TimeFormater(long l, String s, int i)
	{
		format = "hh:mm:ss:ddd";
		style = 0;
		timeMillis = l;
		format = s;
		style = i;
	}

	public void setTimeMillis(long l)
	{
		timeMillis = l;
	}

	public void setFormat(String s)
	{
		format = s;
	}

	public void setStyle(int i)
	{
		style = i;
	}

	public String getOutput()
	{
		boolean flag = format.toUpperCase().indexOf("TW") != -1;
		String s = Math.round((timeMillis / 0x36ee80L) % 24L) <= 0 ? "" : (new StringBuilder()).append(Math.round((timeMillis / 0x36ee80L) % 24L)).append("").toString();
		if(style == 1)
			s = s.length() <= 0 ? "" : (new StringBuilder()).append("<font color=black>").append(s).append("</font>").toString();
		String s1 = Math.round(((timeMillis / 1000L) * 60L) % 60L) <= 0 && s.length() <= 0 ? "" : (new StringBuilder()).append(Math.round(((timeMillis / 1000L) * 60L) % 60L)).append("").toString();
		if(style == 1)
			s1 = s1.length() <= 0 ? "" : (new StringBuilder()).append("<font color=blue>").append(s1).append("</font>").toString();
		String s2 = Math.round((timeMillis / 1000L) % 60L) <= 0 && s1.length() <= 0 ? "" : (new StringBuilder()).append(Math.round((timeMillis / 1000L) % 60L)).append("").toString();
		if(style == 1)
			s2 = s2.length() <= 0 ? "" : (new StringBuilder()).append("<font color=red>").append(s2).append("</font>").toString();
		String s3 = Math.round(timeMillis % 1000L) <= 0 && s2.length() <= 0 ? "" : (new StringBuilder()).append(Math.round(timeMillis % 1000L)).append("").toString();
		if(style == 1)
			s3 = s3.length() <= 0 ? "" : (new StringBuilder()).append("<font color=gray>").append(s3).append("</font>").toString();
		if(flag)
		{
			s = s.length() <= 0 ? "" : (new StringBuilder()).append(s).append("時").toString();
			s1 = s1.length() <= 0 ? "" : (new StringBuilder()).append(s1).append("分").toString();
			s2 = s2.length() <= 0 ? "" : (new StringBuilder()).append(s2).append("秒").toString();
			s3 = s3.length() <= 0 ? "" : (new StringBuilder()).append(s3).append("毫秒").toString();
		} else
		{
			s = s.length() <= 0 ? "" : (new StringBuilder()).append(s).append(":").toString();
			s1 = s1.length() <= 0 ? "" : (new StringBuilder()).append(s1).append(":").toString();
			s2 = s2.length() <= 0 ? "" : (new StringBuilder()).append(s2).append(":").toString();
			s3 = s3.length() <= 0 ? "" : s3;
		}
		String s4 = "";
		if(format.indexOf("hh") != -1)
			s4 = (new StringBuilder()).append(s4).append(s).toString();
		if(format.indexOf("mm") != -1)
			s4 = (new StringBuilder()).append(s4).append(s1).toString();
		if(format.indexOf("ss") != -1)
			s4 = (new StringBuilder()).append(s4).append(s2).toString();
		if(format.indexOf("ddd") != -1)
			s4 = (new StringBuilder()).append(s4).append(s3).toString();
		return s4;
	}
}
