package com.scsb.util;

import java.util.ArrayList;

import com.scsb.ldap.LDAPAdmin;

public class LdapChk {
	public LdapChk()	{
	}	
	public static void main(String[] args){
		boolean flag = false;
		String id = "26654";
		String passwd = "scsb26654";
String aaa="d:\\ldapParams.properties";
		LDAPAdmin ldapadmin = new LDAPAdmin(aaa);
		flag = ldapadmin.login();
		//System.out.println("admin login:"+(flag?"ok":"fail"));
		flag = ldapadmin.login(ldapadmin.getMemDN(id), passwd, "");
		//System.out.println(id+" dn:"+ldapadmin.getMemDN(id));
		System.out.println(id+" login:"+(flag?"ok":"fail"));		
	}
}
