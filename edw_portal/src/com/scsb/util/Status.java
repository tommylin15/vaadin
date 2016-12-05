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
