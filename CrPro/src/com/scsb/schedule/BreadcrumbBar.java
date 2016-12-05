package com.scsb.schedule;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

/**
 * A navigation bar for the main application.
 */
public class BreadcrumbBar extends HorizontalLayout {
    private static final long serialVersionUID = 1L;
    private Button schedulerCrumb;
    private ScheduleLayout scheduleLayout;

    public BreadcrumbBar(ScheduleLayout sLayout) {
    	this.scheduleLayout=sLayout;
        Button dashboardButton = new Button("Schedulers Dashboard");
        dashboardButton.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	scheduleLayout.loadDashboardScreen();
            }
        });
        addComponent(dashboardButton);
    }

    public void addSchedulerCrumb(String schedulerFullName, final String schedulerSettingsName) {
        if (schedulerCrumb != null)
            removeSchedulerCrumb();

        schedulerCrumb = new Button(schedulerFullName);
        schedulerCrumb.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	scheduleLayout.loadSchedulerScreen(schedulerSettingsName);
            }
        });
        addComponent(schedulerCrumb);
    }

    public void removeSchedulerCrumb() {
        if (schedulerCrumb == null)
            return;

        removeComponent(schedulerCrumb);
        schedulerCrumb = null;
    }
}
