package com.scsb.vaadin.chart;

import java.io.Serializable;

import com.scsb.vaadin.chart.highchart.HighChart;

public class BaseChartObject implements Serializable,Cloneable{  
    private String xml;  
    private String json;  
    private HighChart highChart;
    
    public void setHighChart(HighChart highChart){
    	this.highChart=highChart;
    }
    
    public HighChart getHighChart(){
    	return this.highChart;
    }    
} 