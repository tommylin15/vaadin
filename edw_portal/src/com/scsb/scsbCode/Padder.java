// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Padder.java

package com.scsb.scsbCode;

import java.io.Serializable;

public class Padder
	implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1576433116803585345L;
	public static final int LEFT = -1;
	public static final int CENTER = 0;
	public static final int RIGHT = 1;
	private String source;
	private int wholelength;
	private int align;
	private String replacement;

	public Padder(String s, int i, int j, String s1)
	{
		source = s;
		wholelength = i;
		align = j;
		replacement = s1;
	}

	public void setSource(String s)
	{
		source = s;
	}

	public void setWholelength(int i)
	{
		wholelength = i;
	}

	public void setAlign(int i)
	{
		align = i;
	}

	public void setReplacement(String s)
	{
		replacement = s;
	}

	public String getOutput()
	{
		String s = "";
		byte abyte0[] = source.getBytes();
		if(wholelength < abyte0.length)
		{
			if(align == -1 || align == 0)
				s = new String(abyte0, 0, wholelength);
			else
			if(align == 1)
				s = new String(abyte0, abyte0.length - wholelength, wholelength);
		} else
		if(wholelength > abyte0.length)
		{
			String s1 = "";
			for(int i = 0; i < wholelength - abyte0.length; i++)
				s1 = (new StringBuilder()).append(s1).append(replacement).toString();

			if(align == -1)
				s = (new StringBuilder()).append(source).append(s1).toString();
			else
			if(align == 0)
			{
				int j = s1.length() / 2;
				if(j == 0)
					s = (new StringBuilder()).append(source).append(s1).toString();
				else
					s = (new StringBuilder()).append(s1.substring(0, j)).append(source).append(s1.substring(j)).toString();
			} else
			if(align == 1)
				s = (new StringBuilder()).append(s1).append(source).toString();
		} else
		{
			s = source;
		}
		return s;
	}
}
