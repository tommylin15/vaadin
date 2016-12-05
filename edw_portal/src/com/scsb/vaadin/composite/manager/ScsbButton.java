package com.scsb.vaadin.composite.manager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public class ScsbButton extends Button {

    private String targetViewCaption;
    private Component targetView;
    
    public ScsbButton(String caption) {
        setCaption(caption);
    }

    public ScsbButton(Component targetView) {
        this(targetView.getCaption());
        setTargetView(targetView);
    }

    public void setTargetView(Component targetView) {   	
		if (targetView != null){    	
			this.setCaption(targetView.getCaption());
		}else{
			this.setVisible(true);
			this.setCaption("");
		}
        this.targetView = targetView;
    }    
    
    public ScsbManager getScsbManager() {
        Component p = getParent();
        while (p != null && !(p instanceof ScsbManager)) {
            p = p.getParent();
        }
        return (ScsbManager) p;
    }

     public Component getTargetView() {
    	 return (Component) targetView;
    }

    public String getTargetViewCaption() {
        return targetViewCaption;
    }

    public void setTargetViewCaption(String targetViewCaption) {
    	this.targetViewCaption = targetViewCaption;
    }

}
