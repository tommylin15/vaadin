package com.scsb.schedule;

import com.scsb.crpro.CrproLayout;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import myschedule.web.MySchedule;

public class ScheduleLayout extends CrproLayout {
    private static final long serialVersionUID = 1L;
    private VerticalLayout content;
    private BreadcrumbBar breadcrumbBar;
    private Component currentScreen;
    private HorizontalLayout currentScreenWrapper;

    public ScheduleLayout() {
    	this.removeAllComponents();
        // Create components
        content = new VerticalLayout();
        breadcrumbBar = new BreadcrumbBar(this);
        currentScreen = new DashboardScreen(this);

        //HorizontalLayout headerContent = createHeader();
        HorizontalLayout footerContent = creatFooter();

        // We needed a wrapper to remain in place so when refreshing currentScreen, the footer won't get messed up.
        currentScreenWrapper = new HorizontalLayout();
        currentScreenWrapper.setSizeFull();
        currentScreenWrapper.addComponent(currentScreen);

        // Setup content
        content.setImmediate(true);
        content.setMargin(true);
        //content.addComponent(headerContent);
        //content.setComponentAlignment(headerContent, Alignment.MIDDLE_CENTER);
        content.addComponent(breadcrumbBar);
        content.addComponent(currentScreenWrapper);
        content.addComponent(footerContent);
        content.setComponentAlignment(footerContent, Alignment.BOTTOM_RIGHT);
        this.addComponent(content);
    }

    private HorizontalLayout createHeader() {
        Label headerLabel = new Label("SCSB 排程管理 - Quartz Scheduler Manager", ContentMode.HTML);
        HorizontalLayout result = new HorizontalLayout();
        result.addComponent(headerLabel);
        return result;
    }

    private HorizontalLayout creatFooter() {
        String myScheduleAppName = "myschedule";
        MySchedule mySchedule = MySchedule.getInstance();
        String version = mySchedule.getMyScheduleVersion();
        if (!version.equals(""))
            myScheduleAppName += "-" + version;

        String quartzAppName = "quartz";
        version = mySchedule.getQuartzVersion();
        if (!version.equals(""))
            quartzAppName += "-" + version;

        String poweredByText = "Powered by (S.C.S.B.)" + myScheduleAppName + " with " + quartzAppName;
        Label poweredByLabel = new Label(poweredByText, ContentMode.PREFORMATTED);

        HorizontalLayout result = new HorizontalLayout();
        result.addComponent(poweredByLabel);
        return result;
    }

    void loadSchedulerScreen(String schedulerSettingsName) {
        currentScreenWrapper.removeComponent(currentScreen);
        currentScreen = new SchedulerScreen(this,schedulerSettingsName);
        currentScreenWrapper.addComponent(currentScreen);
        
        MySchedule mySchedule = MySchedule.getInstance();
        String schedulerFullName = mySchedule.getSchedulerSettings(schedulerSettingsName).getSchedulerFullName();
        breadcrumbBar.addSchedulerCrumb(schedulerFullName, schedulerSettingsName);
    }

    void loadDashboardScreen() {
        currentScreenWrapper.removeComponent(currentScreen);
        currentScreen = new DashboardScreen(this);
        currentScreenWrapper.addComponent(currentScreen);
        
        breadcrumbBar.removeSchedulerCrumb();
    }
}
