package com.scsb.vaadin.property;

import java.util.Properties;

import com.scsb.db.bean.GoogleLatlon;
import com.scsb.db.service.GoogleLatlonService;
import com.scsb.util.DateUtil;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.scsb.vaadin.server.LongLatService;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.LatLon;
//import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * GoogleMap 地圖
 * @author 3471
 *
 */
public class GoogleMapView extends ScsbGlobView {
	
	private GoogleMap googleMap;
    private final String apiKey = "";
	/**
	 * GoogleMap 地圖
	 * @param title
	 * @param addr
	 */
    public GoogleMapView(String title ,Properties pp) {
    	getView().setSizeFull();
    	getView().setCaption(title);
		String addr =pp.getProperty("GOOGLE_MAP_ADDR");
		LatLon latlon=getLatLon(addr);
        createGoogleMap(latlon, 15, apiKey);
        addMarker(title, latlon, false ,null);
	}
    
    public GoogleMapView(LatLon latlon ,int zoom,String apiKey) {
    	createGoogleMap(latlon ,zoom ,apiKey );
    }    
    public GoogleMapView(String title ,String addr ,int zoom,String apiKey) {
		LatLon latlon=getLatLon(addr);
        createGoogleMap(latlon, zoom, apiKey);
        addMarker(title, latlon, false ,null);
    }
    public GoogleMapView(String title ,String addr ,int zoom,String apiKey ,boolean noMaker) {
		LatLon latlon=getLatLon(addr);
        createGoogleMap(latlon, zoom, apiKey);
        if (!noMaker){
        	addMarker(title, latlon, false ,null);
        }
    }
    
    /**
     * 建立GoogleMap
     * @param latlon
     * @param zoom
     * @param apiKey
     */
    public void createGoogleMap(LatLon latlon ,int zoom,String apiKey){
    	//AIzaSyAz0J9lIObLIFZoeuZdcMM1F_vNJ4E0c6s tommy lin 的 gwt map v3 api key
    	this.googleMap =new GoogleMap(latlon, zoom,  "");
    	this.googleMap.setSizeFull();
    	this.googleMap.setMaxZoom(16);
    	this.googleMap.setMinZoom(4);
    }
    
    /**
     * 在地圖上標記位置
     * @param title
     * @param latlon
     * @param draggable
     * @param iconUrl
     */
    public GoogleMapMarker addMarker(String title ,String addr, boolean draggable, String iconUrl){
		LatLon latlon=getLatLon(addr);
		return addMarker(title, latlon, draggable ,iconUrl);
    }
    /**
     * 在地圖上標記位置
     * @param title
     * @param latlon
     * @param draggable
     * @param iconUrl
     */
    public GoogleMapMarker addMarker(String title ,LatLon latlon, boolean draggable, String iconUrl){
        GoogleMapMarker googleMapMarker = new GoogleMapMarker(title, latlon, draggable ,iconUrl);
    	addMarker(googleMapMarker);
        return googleMapMarker;
    }

    public void addMarker(GoogleMapMarker googleMapMaker){
    	this.googleMap.addMarker(googleMapMaker);
    }

    
    
    /**
     * 取得地址經緯度 
     * @param addr
     * @return LatLon
     */
    public LatLon getLatLon(String addr){
        LongLatService tDirectionService = new LongLatService();
        
        //先去db 查經緯度,查無資料再上google 查
        GoogleLatlonService googlelatlonSrv =new GoogleLatlonService();
        GoogleLatlon bean =new GoogleLatlon();
        bean.setAddr(addr);
        GoogleLatlon dataBean =googlelatlonSrv.getGoogleLatlon_PK(bean);
        LatLon latlon=new LatLon();
        if (dataBean.getAddr().equals("")){
        	//在google 查到後就存到db裡
        	latlon= tDirectionService.getLongitudeLatitude(addr);
        	bean.setLat(latlon.getLat()+"");
        	bean.setLon(latlon.getLon()+"");
        	bean.setUpdateDatetime(DateUtil.getDTS());
        	bean.setUpdateUser(users.getUserid()); 					        	
        	googlelatlonSrv.insertGoogleLatlon(bean);
        }else{
        	latlon.setLat(Double.parseDouble(dataBean.getLat()));
        	latlon.setLon(Double.parseDouble(dataBean.getLon()));
        } 
        return latlon;
    }
    
    public GoogleMap getGoogleMap(){
    	return googleMap;
    }
}