package com.scsb.vaadin.chart.highchart;

import java.util.HashMap;
import java.util.Map;

	public enum ChartTypeEnum{
        column(1,"column","柱狀圖"),  
        bar(2,"bar","長條圖"),  
        line_vertical(3,"line_vertical","折線圖-直"),  
        line_horizontal(4,"line_horizontal","折線圖-水平"),  
        spline_vertical(5,"spline_vertical","曲線圖-直"),  
        spline_horizontal(6,"spline_horizontal","曲線圖-水平"),  
        pie(7,"pie","圓餅圖");  
        
        private int code;
        private String label;
        private String description;

        /**
         * A mapping between the integer code and its corresponding Status to facilitate lookup by code.
         */
        private static Map<Integer, ChartTypeEnum> chartTypeMapping;

        private ChartTypeEnum(int code, String label, String description) {
            this.code = code;
            this.label = label;
            this.description = description;
        }  
        public static ChartTypeEnum getChartType(int i) {
            if (chartTypeMapping == null) {
                initMapping();
            }
            return chartTypeMapping.get(i);
        }  
        private static void initMapping() {
        	chartTypeMapping = new HashMap<Integer, ChartTypeEnum>();
            for (ChartTypeEnum s : values()) {
            	chartTypeMapping.put(s.code, s);
            }
        }

        public int getCode() {
            return code;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(code);
            return sb.toString();
        }

        public static void main(String[] args) {
            System.out.println(ChartTypeEnum.column);
            System.out.println(ChartTypeEnum.getChartType(1));
        }        
	}