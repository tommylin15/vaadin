package com.scsb.vaadin.chart;

import java.awt.Color;
import java.awt.Label;
import java.util.List;


public class Series extends BaseChartObject{  
    private List<Point> pointList;  
    private String id;  
    private String name;  
    //private ChartEnums.ChartTypeEnum type;  
    private Color color;  
    //柱狀圖的不同顯示效果  
    //private ChartEnums.ShapeType shapeType;  
    //多維度使用，對應extra yAxis的name，表示該series屬於該維度  
    private String yAxis;  
    //圓餅圖點擊後是否突出顯示  
    private Boolean pieExploded;  
    private Label label;  
    //private TooltipInfo toolTip;  
    //private Marker marker;  
    private List<Action> actions;  
    private List<Attribute> attributes;  
    //private Animation animation;  
    private List<Label> extraLabel;  
    //private List<TooltipInfo> extraToolTip;  
    //private List<Marker> extraMarker;  
    
    public Series(List<Point> points){
    	pointList =points;
    }    
    public Series(String id ,List<Point> points){
    	this.id=id;
    	this.name=id;
    	this.pointList =points;
    }
    public Series(String id ,String name,List<Point> points){
    	this.id=id;
    	this.name=name;
    	this.pointList =points;
    }    
    
    public void setPointList(List<Point> points){
    	pointList =points;
    }
    
    public List<Point> getPointList(){
    	return pointList;
    }  
    
    public void setName(String name){
    	this.name =name;
    }
    
    public String getName(){
    	return this.name;
    } 
    public void setId(String id){
    	this.id =id;
    }
    
    public String getId(){
    	return this.id;
    }
    
}  