package com.scsb.addons.gwt.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.ui.AbstractComponentContainerConnector;
import com.vaadin.shared.ui.Connect;

@Connect(com.scsb.addons.ui.HorizontalButtonGroup.class)
public class HorizontalButtonGroupConnector extends
        AbstractComponentContainerConnector {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HorizontalButtonGroupWidget theWidget;

    @Override
    public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
        if (getParent() == null) {
            // Component is removed, skip stuff to save user from JS exceptions
            // and some milliseconds of lost life
            return;
        }

        List<ComponentConnector> children = getChildComponents();
        HorizontalButtonGroupWidget widget = (HorizontalButtonGroupWidget) getWidget();
        widget.clear();
        for (ComponentConnector connector : children) {
            // TODO #13688
            ((HorizontalButtonGroupWidget) getWidget()).add(connector
                    .getWidget());
        }
    }

    @Override
    protected Widget createWidget() {
        theWidget = GWT.create(HorizontalButtonGroupWidget.class);
        return theWidget;
    }

    @Override
    public void updateCaption(ComponentConnector connector) {
    }

}
