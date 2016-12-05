package com.scsb.scsbCode;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

/**
 * xml驗證 for xsd
 */
public class ValidateXML {

    private ValidateXML() {

    }

    public static boolean validateXml(String xsdPath, String xmlPath)   throws SAXException, IOException {
    	// 建立schema factory
    	SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    	//建立驗證檔案
    	File schemaFile = new File(xsdPath);
    	// 利用schema factory,接收驗證檔案
    	Schema schema = schemaFactory.newSchema(schemaFile);
    	//利用schemaFile進行驗證
    	Validator validator = schema.newValidator();
    	//要驗證的xml檔
    	Source source = new StreamSource(xmlPath);
    	//開始驗證
    	validator.validate(source);
    	return true;
    }
    
    public static void main(String[] args) {

    	 String xsdPath = "D:\\encrypt\\fatca\\FATCA XML Schema v1.1\\FatcaXML_v1.1.xsd";
    	 String xmlPath = "D:\\encrypt\\fatca\\SCSB\\sample-Corr.xml";

    	 try {
    	     if (ValidateXML.validateXml(xsdPath, xmlPath)) {
    	    	 System.out.println("驗證完成");
    	     }
    	 } catch (SAXException e) {
    	     System.out.println("驗證失敗");
    	     e.printStackTrace();
    	 } catch (IOException e) {
    	     System.out.println("驗證失敗");
    	     e.printStackTrace();
    	 }

   }    
}