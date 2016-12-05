package com.scsb.vaadin.composite.manager;

import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;

public class ScsbView extends CssLayout {
    private ScsbBar scsbBar = new ScsbBar();
    private Component mainComponent;

    public ScsbView() {
        this("");
    }
	
    public void setBarVisible(boolean noBar) {
    	getScsbBar().setVisible(noBar);
    }    
    
    public  ScsbView(String caption) {
        super.addComponent(getScsbBar());
        setCaption(caption);
    }
    
    /**
     * Sets the main content of the NavigationView. If null, an empty
     * {@link CssLayout} will be used.
     * 
     * @param c
     *            the component to set as the content
     */
    public void setContent(Component c) {
        if (mainComponent == c) {
            return;
        }
        if (mainComponent != null) {
        	super.removeComponent(mainComponent);
        }
        if (c == null) {
            c = new CssLayout();
        }
        super.addComponent(c);
        mainComponent = c;
    }

    /**
     * @return the content of the navigation view.
     */
    public Component getContent() {
        return mainComponent;
    }

    public void addComponent(Component c) {
        setContent(c);
    }    
    /**
     * The toolbar or content can be removed - other attempts will result in an
     * {@link IllegalArgumentException}. If the content is removed, an empty
     * {@link CssLayout} is set as the content.
     */
    @Override
    public void removeComponent(Component c) {
    	if (c == mainComponent) {
            setContent(null);
        } else {
            throw new IllegalArgumentException(
                    " Only the  main content can be removed");
        }
        //markAsDirty();
    }

    /**
     * Removes the current content (setting the content to an
     * empty {@link CssLayout}).
     */
    @Override
    public void removeAllComponents() {
        removeComponent(mainComponent);
    }

    /**
     * @return the {@link NavigationBar}
     */
    public ScsbBar getScsbBar() {
        return scsbBar;
    }

    /**
     * Sets the component in the navigation bar's right slot.
     * 
     * @param c
     *            the component to set in the right slot.
     */
    public void setRightComponent(Component c) {
    	getScsbBar().setRightComponent(c);
    }

    /**
     * @return the component in the right slot of the navigation bar or null if
     *         not set
     */
    public Component getRightComponent() {
        return getScsbBar().getRightComponent();
    }

    /**
     * Sets the component in the navigation bar's left slot. Most commonly this
     * component slot is automatically populated by the NavigationView (a back
     * button).
     * 
     * @param c
     *            the component to set in the left slot.
     */
    public void setLeftComponent(Component c) {
    	getScsbBar().setLeftComponent(c);
    }

    /**
     * @return the component in the left slot of the navigation bar or null if
     *         not set
     */
    public Component getLeftComponent() {
        return getScsbBar().getLeftComponent();
    }

    /**
     * @see NavigationBar#getPreviousView()
     */
    public Component getPreviousComponent() {
        return getScsbBar().getPreviousView();
    }

    /**
     * @see NavigationBar#setPreviousView(Component)
     */
    public void setPreviousComponent(Component component) {
    	getScsbBar().setPreviousView(component);
    }

    @Override
    public void setCaption(String caption) {
    	getScsbBar().setCaption(caption);
    }

    @Override
    public String getCaption() {
        return getScsbBar().getCaption();
    }

    /**
     * Called by {@link NavigationManager} when the view is about to become
     * visible.
     */
    protected void onBecomingVisible() {
        Component previousComponent = getPreviousComponent();
        if (previousComponent != null) {
            setPreviousComponent(previousComponent);
        }
    }

    /**
     * The main content  can be replaced - other attempts will
     * result in an {@link IllegalArgumentException}
     */
    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        if (mainComponent == oldComponent) {
            setContent(newComponent);
        } else {
            throw new IllegalArgumentException(
                    " Only the main content can be replaced");
        }
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        LinkedList<Component> linkedList = new LinkedList<Component>();
        if (scsbBar != null) {
            linkedList.add(scsbBar);
        }
        if (mainComponent != null) {
            linkedList.add(mainComponent);
        }
        return linkedList.iterator();
    }

    public ScsbManager getScsbManager() {
        Component p = getParent();
        if (p instanceof ScsbManager) {
            return (ScsbManager) p;
        }
        return null;
    }
}
