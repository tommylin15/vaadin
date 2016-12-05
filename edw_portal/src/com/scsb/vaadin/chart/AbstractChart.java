package com.scsb.vaadin.chart;

import java.awt.Label;

import org.vaadin.teemu.jsoncontainer.JsonContainer;


public class AbstractChart extends BaseChartObject{  
	//基本設定-統計圖類型  
	private ChartTypeEnum chartType;  
    //資料源
    private DataSet dataSet;  
    //說明  
    private Label label;
    //標題
    private String title;
    //區域軸線名稱
    private String domainAxis;
    //範圍軸線名稱
    private String rangeAxis;
    //產出的統計圖
    private Object obj;
    
    public AbstractChart(DataSet dataSet){
    	this.dataSet=dataSet;
    }
    
    public void setChartType(int i){
    	chartType =ChartTypeEnum.getChartType(i);
    }
    
    public int getChartType(){
    	return chartType.getCode();
    }  
    
    public DataSet getDataSet(){
    	return dataSet;
    }  
    
    public void setTitle(String title){
    	this.title=title;
    }  
    
    public String getTitle(){
    	return title;
    }     
    
    public void setDomainAxis(String domainAxis){
    	this.domainAxis=domainAxis;
    }  
    
    public String getDomainAxis(){
    	return domainAxis;
    }   
    public void setRangeAxis(String rangeAxis){
    	this.rangeAxis=rangeAxis;
    }  
    
    public String getRangeAxis(){
    	return rangeAxis;
    }     
}