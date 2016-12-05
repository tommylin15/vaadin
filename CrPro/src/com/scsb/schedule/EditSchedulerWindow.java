package com.scsb.schedule;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A popup UI window to display a text editor to create a scheduler by a Quartz config properties content.
 */
public class EditSchedulerWindow extends EditorWindow {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditSchedulerWindow.class);
    private static final long serialVersionUID = 1L;
    private String schedulerSettingsName;
    private ScheduleLayout scheduleLayout;

    public EditSchedulerWindow(ScheduleLayout sLayout, String schedulerSettingsName) {
        this.scheduleLayout = sLayout;
        this.schedulerSettingsName = schedulerSettingsName;
        initEditorControls();
    }

    private void initEditorControls() {
        setCaption("Editing Scheduler Config ID: " + schedulerSettingsName);

        String editText = mySchedule.getSchedulerSettingsConfig(schedulerSettingsName);
        editor.setValue(editText);

        Button button = new Button("Save and Restart Scheduler");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                String configText = editor.getValue();
                LOGGER.debug("Updating scheduler settings {}.", schedulerSettingsName);
                try {
                    mySchedule.updateSchedulerSettings(schedulerSettingsName, configText);
                    close(); // This is a popup, so close it self upon completion.
                } catch (Exception e) {
                    UI.getCurrent().addWindow(new ErrorWindow(e));
                }
                scheduleLayout.loadDashboardScreen(); // Now refresh the dashboard for the updated scheduler.
            }
        });
        content.addComponent(button);
    }
}
