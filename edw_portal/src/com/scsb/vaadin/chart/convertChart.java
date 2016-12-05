package com.scsb.vaadin.chart;

import java.awt.Font;
import org.apache.commons.lang3.ArrayUtils;

import com.scsb.vaadin.chart.highchart.HighChart;

public class convertChart{  

	public convertChart(){}
    public AbstractChart convertChart(AbstractChart chart,String... product) {  
        if(ArrayUtils.isEmpty(product)){  
            product = new String[]{"HighChart"};  
        }  
        for (String string : product) {  
            if("HighChart".equals(string)){//基于HighChart的轉換器  
                chart.setHighChart(this.convert2HighChart(chart));  
            }  
        }  
        return chart;  
    }  	
    
    private HighChart convert2HighChart(AbstractChart chart){
    	
    	HighChart highChart =new HighChart();
    	//標題
    	String sTitle="";
    	//圖形
    	String sChart="";
    	//資料內容
    	String sSeries="";
    	//Y項目
    	String syAxis="";
    	//X項目
    	String sxAxis="";    	
    	
    	//柱狀圖===============================================================================
    	if (chart.getChartType()==1){
    		sChart=" chart: { type: 'column'} ";
    		sTitle=" title: { text: '"+chart.getTitle().trim()+"' }";
    		syAxis=" yAxis: { title: { text: '"+chart.getRangeAxis()+"' } }";
            //DataSet轉換為HighChart
        	sSeries+=" series: [ ";
        	int i=0;
    		for (Series serise : chart.getDataSet().getSeries()) {
    			String name =serise.getName();
    			if (i > 0){
    				sSeries+=",";
    			}
    			sSeries+=" {name: '"+name.trim()+"', data: [";
    			int j=0;
    			for (Point point : serise.getPointList()){
    				if (j > 0){
    					sSeries+=",";
    					if (i==0) sxAxis+=",";
    				}else{
    					if (i==0){
    						sxAxis+="xAxis: { title: { text:'"+chart.getDomainAxis()+"' } , categories: [";
    					}
    				}
    				if (i==0) sxAxis+="'"+point.getName()+"'";
    				sSeries+=point.getValue();
    				j++;
    			}
    			sSeries+="]}";
    			if (i==0) sxAxis+="]}";
    			i++;
    		}
    		sSeries+=" ] ";
    	//圓餅圖=============================================================================
    	}else if (chart.getChartType()==7){
    		sChart=" chart: { type: 'pie' ,options3d: { enabled: true, alpha: 45, beta: 0 }} ";
    		sTitle=" title: { text: '"+chart.getTitle().trim()+"' }";
            //DataSet轉換為HighChart
        	sSeries+=" series: [{ type: 'pie',name: '"+chart.getRangeAxis()+"' ";
        	int i=0;
    		for (Series serise : chart.getDataSet().getSeries()) {
    			String name =serise.getName();
    			if (i > 0){
    				sSeries+=",";
    			}
    			sSeries+=" , data:[";
    			int j=0;
    			for (Point point : serise.getPointList()){
    				if (j > 0){
    					sSeries+=",";
    				}
    				sSeries+="['"+point.getName()+"',"+point.getValue()+"]";
    				j++;
    			}
    			sSeries+="]}";
    			i++;
    		}
    		sSeries+=" ] ";
    	}

		
		//組合script
    	String sHcjs="";
    	if (sHcjs.trim().length()>0 && sChart.trim().length() > 0) sHcjs+=",";
    	sHcjs+=sChart;
    	if (sHcjs.trim().length()>0 && sTitle.trim().length() > 0) sHcjs+=",";
    	sHcjs+=sTitle;
    	if (sHcjs.trim().length()>0 && syAxis.trim().length() > 0) sHcjs+=",";
    	sHcjs+=syAxis;
    	if (sHcjs.trim().length()>0 && sxAxis.trim().length() > 0) sHcjs+=",";
    	sHcjs+=sxAxis;
    	if (sHcjs.trim().length()>0 && sSeries.trim().length() > 0) sHcjs+=",";
    	sHcjs+=sSeries;
    	sHcjs=" var options = {"+sHcjs+"};";
		highChart.setHcjs(sHcjs);
        return highChart;
    }     
}