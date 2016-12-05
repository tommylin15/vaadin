package com.scsb.vaadin.chart;

import java.awt.Color;
import java.awt.Label;
import java.util.List;

    public class Point extends BaseChartObject{  
        private String id;  
        private String name;  
        private String value;  
        //點大小 
        private String size;  
        //颜色  
        private Color color;  
        // 是否被選中  
        private Boolean selected;  
        // 是否允許被選中  
        private Boolean allowSelect;  
        //圓餅圖是否突出  
        private Boolean pieExploded;  
        private Label label;  
        //private TooltipInfo toolTip;  
        //private Marker marker;  
        private List<Action> actions;  
        private List<Attribute> attributes;  
       // private Animation animation;  
        private List<Label> extraLabel;  
       // private List<TooltipInfo> extraToolTip;      
        //private List<Marker> extraMarker;
        
        public Point(String name,String value){
        	this.name=name;
        	this.value=value;
        }
        public Point(String id ,String name,String value){
        	this.id=id;
        	this.name=name;
        	this.value=value;
        }
        
        public void setName(String name){
        	this.name=name;
        }
        
        public String getName(){
        	return this.name;
        }    
        
        public void setValue(String value){
        	this.value=value;
        }
        
        public String getValue(){
        	return this.value;
        }  
    }  