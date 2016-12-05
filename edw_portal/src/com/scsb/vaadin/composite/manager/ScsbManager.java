package com.scsb.vaadin.composite.manager;

import java.util.Stack;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ScsbManager extends VerticalLayout {
    private Connector previousComponent;
    //private Connector nextComponent;
    private Connector currentComponent;
	
    private Stack<Component> viewStack = new Stack<Component>();
    private boolean maintainBreadcrumb = true;

    public ScsbManager() {
        //setSizeFull();
    }

    public ScsbManager(Component c) {
        this();
        navigateTo(c);
    }

    public Stack<Component> getViewStack() {
        return viewStack;
    }

    public void navigateTo(Component newComponent) {
        if (newComponent == null) {
            throw new UnsupportedOperationException(
                    "Some component must always be visible");
        } else if (newComponent == getCurrentComponent()) {
            return;
        } else if (getPreviousComponent() == newComponent) {
            navigateBack();
            return;
        }
        
        if (getPreviousComponent() != null) {
            if (isMaintainBreadcrumb()) {
            	getViewStack().push(getPreviousComponent());
            }
        }
        Component curComponent =getCurrentComponent();
        setCurrentComponent(newComponent);
        setPreviousComponent(curComponent);
        
        //showViewStack();   
        
    }
    
    public void showViewStack(){
		System.out.println("getViewStack push=============="+viewStack.size());
		for (java.util.Iterator<Component> iter =viewStack.iterator();iter.hasNext();){
			Component comp =iter.next();
			System.out.println("getViewStack push:"+comp.getCaption());
		}  
    }

    public void navigateBack() {
    	//沒有上一步就停止
        if (getPreviousComponent() == null) {
            return;
        }
        //回到上一步
        setCurrentComponent(getPreviousComponent());
        
        if (isMaintainBreadcrumb()) {
            setPreviousComponent(getViewStack().isEmpty() ? null : getViewStack().pop());
        } else {
            setPreviousComponent(null);
        }

        if (getCurrentComponent() ==getPreviousComponent() ){
        	setPreviousComponent(null);
        }
        //showViewStack();     
    }

    public void setCurrentComponent(Component newCurrentComponent) {
        if (getCurrentComponent() != newCurrentComponent) {
	        if (getCurrentComponent() != null) {
	            removeComponent(getCurrentComponent());
	        }
            this.currentComponent = newCurrentComponent;
            addComponent(newCurrentComponent);
        }
    }

    public Component getCurrentComponent() {
        return (Component) currentComponent;
    }

    public void setPreviousComponent(Component newPreviousComponent) {
    	if (newPreviousComponent != null)  {
            if (getPreviousComponent() != null  & getPreviousComponent() != getCurrentComponent()) {
                removeComponent(getPreviousComponent());
            }
            this.previousComponent = newPreviousComponent;
            
            ScsbView view = (ScsbView) getCurrentComponent();
            view.setPreviousComponent(newPreviousComponent);

        }else{
        	ScsbView view = (ScsbView) getCurrentComponent();
        	if (view != null)    	view.setPreviousComponent(null);
        }
    }

    public Component getPreviousComponent() {
        return (Component) previousComponent;
    }

    public boolean isMaintainBreadcrumb() {
        return maintainBreadcrumb;
    }

    public void setMaintainBreadcrumb(boolean maintainBreadcrumb) {
        this.maintainBreadcrumb = maintainBreadcrumb;
    }

}
