package com.scsb.vaadin.chart;

import java.util.List;

import org.vaadin.teemu.jsoncontainer.JsonContainer;



public class DataSet extends BaseChartObject {  
	//資料源，一個series對應一组List資料  
    private List<Series> series;  
    //下層操作  
    private List<Action> actions;  
    //自定屬性  
    private List<Attribute> attributes;
    
    public DataSet(List<Series> series){
    	this.series=series;
    }
    
    public void clearSeries(){
    	series.clear();
    }
    public void addSeries(Series s){
    	series.add(s);
    }
    
    public List<Series> getSeries(){
    	return series;
    }    
} 
