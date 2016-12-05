package com.scsb.util;

import java.util.HashMap;
import java.util.Map;

public final class NamingService
{

	private static NamingService theObject = new NamingService();
	private Map nameValuePairs;

	public static NamingService getInstance()
	{
		return theObject;
	}

	public Object getAttribute(String s)
	{
		return nameValuePairs.get(s);
	}

	public void setAttribute(String s, Object obj)
	{
		if(nameValuePairs.get(s) == null)
			nameValuePairs.put(s, obj);
		else
			throw new IllegalArgumentException("");
	}

	public void removeAttribute(String s)
	{
		nameValuePairs.remove(s);
	}

	private NamingService()
	{
		nameValuePairs = new HashMap();
	}

}
