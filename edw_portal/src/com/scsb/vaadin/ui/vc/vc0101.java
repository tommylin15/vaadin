package com.scsb.vaadin.ui.vc;


import java.util.Properties;

import org.tepi.filtertable.FilterTable;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.db.bean.Fnbct0;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.util.GetUrlDoc;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.property.GoogleMapView;
import com.scsb.vaadin.server.SasJsonService;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.tapio.googlemaps.client.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
//import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
//import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;

/**
 * Google Maps Test
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class vc0101 extends ScsbGlobView {
	/**
	 * Google Maps-主畫面
	 */
	public vc0101() {
		create();
	}
	public void create(){
		
		Fnbct0Service fnbct0Srv =new Fnbct0Service();
		BeanContainer<String,Fnbct0> fnbct0Container =fnbct0Srv.getSasFnbct0_All();
		
		Fnbct0 sasFnbct0=fnbct0Container.getItem("01").getBean();
		
		GoogleMapView googleMapView =new GoogleMapView(sasFnbct0.getChinAl1() ,
				sasFnbct0.getChinAd1().trim()+sasFnbct0.getChinAd2().trim() ,
				14 ,sasFnbct0.getBrhCod() ,true);
		googleMapView.getView().setWidth("100%");
		for(java.util.Iterator<String> iter =fnbct0Container.getItemIds().iterator();iter.hasNext();){
			String itemId= iter.next();
			Fnbct0 fnbct0= fnbct0Container.getItem(itemId).getBean();
			GoogleMapMarker googleMapMarker =googleMapView.addMarker(fnbct0.getBrhCod()+fnbct0.getChinAl1(), fnbct0.getChinAd1().trim()+fnbct0.getChinAd2().trim(), false ,"");
			//GoogleMapInfoWindow googleMapInfoWindow = new GoogleMapInfoWindow("分行:"+fnbct0.getChinAl1(), googleMapMarker);
		}//for
		final CssLayout consoleLayout = new CssLayout();
		getContent().addComponent(consoleLayout);
		
		googleMapView.getGoogleMap().addMarkerClickListener(
				new MarkerClickListener(){
					@Override
					public void markerClicked(GoogleMapMarker clickedMarker) {
						String brh_cod=clickedMarker.getCaption().substring(0,2);
//System.out.println("brh_cod:"+brh_cod);						
						SasJsonService sasJson =new SasJsonService();
						sasJson.setSasCgiBroker(systemProps.getProperty("_SasCgi"));
						   Properties pp =new Properties();
						   pp.put("_service", "default4");
						   pp.put("_program", "ibspgm.json_test2.sas");
						   pp.put("xpgn"    , "noCheck");
						   pp.put("brh_cod"    , brh_cod);
						String jsonString =sasJson.getSasJson(pp);
//System.out.println("jsonString:"+jsonString);						
						JsonContainer jsonData = JsonContainer.Factory.newInstance(jsonString);
						FilterTable    pageTable     = new FilterTable();
						pageTable.setContainerDataSource(jsonData);
						vc0101Popover scsbPopover = new vc0101Popover(clickedMarker.getCaption() ,pageTable);
						scsbPopover.popover.setWidth("90%");
						scsbPopover.popover.setHeight("90%");
						UI.getCurrent().addWindow(scsbPopover.popover);
					}
				}
		);
		/*
		googleMapView.getGoogleMap().addInfoWindowClosedListener(new InfoWindowClosedListener() {
			@Override
			public void infoWindowClosed(GoogleMapInfoWindow window) {
			}
		});
		*/
		
		getContent().addComponent(googleMapView.getGoogleMap());
	}
	
}
