package com.scsb.vaadin.ui.vc;

import java.util.ArrayList;
import java.util.List;

import org.tepi.filtertable.FilterTable;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.vaadin.chart.highchart.HighChart;
import com.scsb.vaadin.chart.AbstractChart;
import com.scsb.vaadin.chart.DataSet;
import com.scsb.vaadin.chart.Point;
import com.scsb.vaadin.chart.Series;
import com.scsb.vaadin.chart.convertChart;
import com.scsb.vaadin.composite.ScsbGlob;

import com.vaadin.data.Item;
import com.vaadin.data.Container.Filterable;
import com.vaadin.ui.VerticalLayout;
/**
 * SCSB-ao kpi 圖表
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class vc0101ChartView extends ScsbGlob {
	public VerticalLayout vLayout =new VerticalLayout();
	//圖表按鈕
	List<Point> points = new ArrayList<Point>();
	List<Point> points2 = new ArrayList<Point>();
	
	String title="";
	
	public vc0101ChartView(String title ,FilterTable filterable) {
		this.title=title;
		for(java.util.Iterator<?> iter=filterable.getItemIds().iterator();iter.hasNext();){
			Object itemId =iter.next();
			Item item =filterable.getItem(itemId);
			item.getItemProperty("resp_no");
			points.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("amt01_92").getValue()+""));
			points2.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("qota_int").getValue()+""));    						
		}
		init();
	}
	
	public vc0101ChartView(Filterable filterable) {
		for(java.util.Iterator<?> iter=filterable.getItemIds().iterator();iter.hasNext();){
			Object itemId =iter.next();
			Item item =filterable.getItem(itemId);
			item.getItemProperty("resp_no");
			points.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("amt01_92").getValue()+""));
			points2.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("qota_int").getValue()+""));    						
		}
		init();
	}	
	
	public vc0101ChartView(JsonContainer jsonData){
		for(java.util.Iterator<?> iter=jsonData.getItemIds().iterator();iter.hasNext();){
			String itemId =(String)iter.next();
			Item item =jsonData.getItem(itemId);
			item.getItemProperty("resp_no");
			points.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("amt01_92").getValue()+""));
			points2.add(new Point(item.getItemProperty("resp_no").getValue()+"",item.getItemProperty("qota_int").getValue()+""));    						
		}	
		init();
    }
	private void init(){
    	loadI18N(lang, "vc0101Popover");		
		//this.content.setTitle("<font color='red'><b>"+title+i18nProps.getProperty("title","AO績效")+"</b>&nbsp;</font>");
		
		convertChart conChart =new convertChart();
		vLayout.removeAllComponents();
		//this.setSizeFull();
		//柱狀圖====================================================
		List<Series> series1 = new ArrayList<Series>();
		series1 = new ArrayList<Series>();
		series1.add(new Series("達成數",points));
		series1.add(new Series("預算數",points2));		
		DataSet dataSet1= new DataSet(series1);
		AbstractChart chart1 = new AbstractChart(dataSet1);
		chart1.setTitle("ao kpi 201406");
		chart1.setRangeAxis("貢獻度");
		chart1.setDomainAxis("企金AO");
		chart1.setChartType(1);//柱狀圖
		conChart.convertChart(chart1 ,"HighChart");
		//System.out.println(chart1.getHighChart().getHcjs());
		HighChart highChart =chart1.getHighChart();
		highChart.setWidth("100%");
		vLayout.addComponent(highChart);
	
		//圓餅圖====================================================
		/*
		List<Series> series2 = new ArrayList<Series>();
		series2 = new ArrayList<Series>();
		series2.add(new Series("達成數",points));
		DataSet dataSet2= new DataSet(series2);
		AbstractChart chart2 = new AbstractChart(dataSet2);
		chart2.setTitle("ao kpi 201406");
		chart2.setRangeAxis("貢獻度");
		chart2.setDomainAxis("企金AO");
		chart2.setChartType(7);//圓餅圖
		conChart.convertChart(chart2 ,"HighChart");
		//System.out.println(chart2.getHighChart().getHcjs());
		HighChart highChart2 =chart2.getHighChart();
		highChart2.setWidth("100%");
		this.addComponent(highChart2);
		*/
	}
}