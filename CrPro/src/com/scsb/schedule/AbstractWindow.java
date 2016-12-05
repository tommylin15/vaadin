package com.scsb.schedule;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import myschedule.web.MySchedule;

/**
 * A base class to provide a popup UI window on an existing screen. It setup screen to fill up 90% by 90% on center of
 * the screen. It also provide a default vertical layout for subclass to add content components.
 * <p/>
 * This base class also provide and setup MySchedule and MyScheduleUi access.
 */
public class AbstractWindow extends Window {
    MySchedule mySchedule = MySchedule.getInstance();
    VerticalLayout content;

    public AbstractWindow() {
        initContent();
    }

    protected void initContent() {
        // Give default Window position and size
        setWidth("80%");
        setHeight("98%");
        center();

        // Prepare content container
        content = new VerticalLayout();
        content.setMargin(true);
        setContent(content);
    }
}
