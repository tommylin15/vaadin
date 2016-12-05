package com.scsb.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {

	public PropertiesUtil() {
	}

	public static Properties loadProperties(String s) {
		Properties properties;
		FileInputStream fileinputstream;
		properties = null;
		fileinputstream = null;
		try {
			File file = new File(s);
			if (file.exists()) {
				fileinputstream = new FileInputStream(file);
				properties = new Properties();
				properties.load(fileinputstream);
			}
		} catch (Exception exception1) {
			System.out.println(exception1);
		} finally {
			if (fileinputstream != null)
				try {
					fileinputstream.close();
				} catch (Exception exception) {
				}
		}
		return properties;
	}
}
