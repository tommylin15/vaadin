package com.scsb.vaadin.composite.manager;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ScsbBar extends AbsoluteLayout {
	
    private ScsbButton backButton = new ScsbButton("");
    private Component scsbBarComponent;
    private Component leftScsbBarComponent;
    
    private Button titleButton =new Button("");
    private VerticalLayout vLayout =new VerticalLayout();
    /**
     * Constructs a new scsbBar
     */
    public ScsbBar() {
    	titleButton.setWidth("-1px");
    	titleButton.addStyleName("large");
    	titleButton.addStyleName("borderless-colored");
    	
        backButton.setVisible(false);
        backButton.setIcon(FontAwesome.ARROW_CIRCLE_LEFT);
        backButton.addStyleName("small");
        backButton.addStyleName("borderless-colored");
        backButton.setWidth("-1px");
        backButton.addClickListener(new ClickListener() {
    		@Override
    		public void buttonClick(ClickEvent event) {
    			getScsbManager().navigateBack();
    		}
        });      
        
        setLeftComponent(backButton);
        setTitle();
        addComponent(vLayout, "left: 30%; right: 30%; top :4px");

        setWidth("100%");
        setHeight("40px");
    }
    
    public void setTitle() {
        vLayout.addComponent(titleButton);
        vLayout.setComponentAlignment(titleButton,Alignment.TOP_CENTER);
        vLayout.setSizeFull();
        vLayout.setSpacing(false);
        vLayout.setMargin(new MarginInfo(false,false,false,false));
    }    
    
    public ScsbManager getScsbManager() {
        Component p = getParent().getParent();
        if (p instanceof ScsbManager) {
            return (ScsbManager) p;
        }
        return null;
    }    
    
    public void setCaption(String caption){
    	titleButton.setCaption(caption);
    }
    
    public String getCaption(){
    		return  titleButton.getCaption();
    }    
    
    public void setLeftComponent(Component c) {
        if (leftScsbBarComponent != null) {
            removeComponent(leftScsbBarComponent);
        }
        if (c != null) {
            addComponent(c,"left: 5px; top: 6px;");
        	//addComponent(c);
        }
        leftScsbBarComponent = c;

       // markAsDirty();
    }

    public Component getLeftComponent() {
        return leftScsbBarComponent;
    }

    public void setRightComponent(Component c) {
        if (scsbBarComponent != null) {
            removeComponent(scsbBarComponent);
        }
        if (c != null) {
        	addComponent(c,"right: 5px; top: 4px;");
        	//addComponent(c);
        }
        scsbBarComponent = c;
        //markAsDirty();
    }

    public Component getRightComponent() {
        return scsbBarComponent;
    }

    public void setPreviousView(Component component) {
    	if (component !=null){
	        //if (getBackButton().getParent() != null) {
	            // only if the left component has not been changed
	            getBackButton().setTargetView(component);
	            getBackButton().setVisible(component != null);
	       // }
    	}else{
    		getBackButton().setVisible(component != null);
    	}
    }

    public Component getPreviousView() {   	
        return getBackButton().getTargetView();
    }

    /**
     * @return the back button
     */
    private ScsbButton getBackButton() {
        return backButton;
    }

}
