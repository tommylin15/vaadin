package com.scsb.vaadin.include;

import java.util.Iterator;
import java.util.Properties;

import com.scsb.domain.HashI18N;
import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

/**
 * SCSB-選單的快速查詢
 * @author 3471
 *
 */
@SuppressWarnings("serial")
public class QueryLayout extends ScsbGlob {
	
	private VerticalLayout g =new VerticalLayout();
	private String styleName="";
	
	public AbsoluteLayout absLayout =new AbsoluteLayout();
	
	public QueryLayout(VerticalLayout g ,String styleName) {
		this.g =g;
		this.styleName =styleName;
		Create();
	}

	
	public void Create() {
		absLayout.setWidth("150px");
		absLayout.setHeight("36px");
    	TextField text=new TextField();
    	text.setImmediate(true);
    	text.setWidth("90px");
    	text.setInputPrompt(messageProps.getProperty("FastQuery","快速檢索"));
    	//text.setIcon(FontAwesome.SEARCH);
    	text.addTextChangeListener(new TextChangeListener(){
			@Override
			public void textChange(TextChangeEvent event) {
				String sText =event.getText().trim();
				int iflag=0;
				for (Iterator<Component> iter=g.getComponentIterator();iter.hasNext();){
					Component comp =iter.next();
					comp.setVisible(true);
					if (!sText.equals("")){
						String compValue =comp.getCaption();
						if (compValue.toLowerCase().indexOf(sText.toLowerCase()) == -1){
							comp.setVisible(false);
						}else{
							iflag++;							
						}
					}else{
						iflag++;						
					}
					/*
			    	if (iflag % 2 == 1){
			    		comp.addStyleName(styleName);
			    	}else{
			    		comp.addStyleName("menuWhite");
			    	}		
			    	*/	
				}
			}
    		
    	});
    	
    	absLayout.addComponent(text,"right: 10px; top: 1px;");
       	Button queryBu =new Button(""); 
       	queryBu.setIcon(new ThemeResource("./images/Search16.png"));
       	queryBu.setId("queryBu");
       	queryBu.addStyleName(BaseTheme.BUTTON_LINK);    	
       	absLayout.addComponent(queryBu,"right: 80px; top: 1px;");
    }

}