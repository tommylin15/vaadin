// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi 
// Source File Name:   Status.java

package com.scsb.util;

import java.util.*;

public class Status
{

	private List exceptions;

	public Status()
	{
		exceptions = new ArrayList();
	}

	public boolean isSuccessful()
	{
		return exceptions.size() == 0;
	}

	public void addException(Exception exception)
	{
		exceptions.add(exception);
	}

	public Iterator getExceptions()
	{
		return exceptions.iterator();
	}
}
