package com.scsb.vaadin.chart;



    public interface ChartManager {  
        /** 
         * 圖表轉換器接口
         * @param chart    抽象對象 
         * @param product  圖表產品 
         * @return AbstractChart 
         */  
    	public AbstractChart convertChart(AbstractChart chart,String... product);
   
    }  