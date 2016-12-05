package com.scsb.schedule;

import com.scsb.crpro.MessageBox;
import com.scsb.crpro.MessageBox.ButtonType;
import com.scsb.crpro.MessageBox.EventListener;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import myschedule.quartz.extra.SchedulerTemplate;
import myschedule.web.MySchedule;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**s
 * JobsRunningContent provides a table view for all the current running jobs in scheduler. Note that typical job in
 * scheduler should complete quickly and will not linger in this view. However if user have some long running job,
 * or one that stuck, then they may use this view to verify or even interrupt the job.
 *
 * User: Zemian Deng
 * Date: 6/1/13
 */
public class JobsRunningContent extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsRunningContent.class);
    MySchedule mySchedule = MySchedule.getInstance();
    String schedulerSettingsName;
    HorizontalLayout toolbar;
    HorizontalLayout tableRowActionButtonsGroup;
    Table table;
    String selectedTriggerKeyName;
    private ScheduleLayout scheduleLayout;

    public JobsRunningContent(ScheduleLayout sLayout ,String schedulerSettingsName) {
        this.schedulerSettingsName = schedulerSettingsName;
        this.scheduleLayout=sLayout;
        initToolbar();
        initJobsTable();
    }

    private void initToolbar() {
        toolbar = new HorizontalLayout();
        addComponent(toolbar);

        toolbar.addComponent(createRefreshButton());

        tableRowActionButtonsGroup = new HorizontalLayout();
        toolbar.addComponent(tableRowActionButtonsGroup);

        tableRowActionButtonsGroup.addComponent(createViewDetailsButton());
        tableRowActionButtonsGroup.addComponent(createInterruptButton());

        disableToolbarIfNeeded();
    }

    private void disableToolbarIfNeeded() {
        if (selectedTriggerKeyName == null) {
            tableRowActionButtonsGroup.setEnabled(false);
        } else {
            tableRowActionButtonsGroup.setEnabled(true);
        }
    }

    private Button createRefreshButton() {
        Button button = new Button("Refresh");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                reloadTableContent();
            }
        });
        return button;
    }

    private Button createViewDetailsButton() {
        Button button = new Button("View Details");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                showJobsWithTriggersWindow();
            }
        });
        return button;
    }

    private Button createInterruptButton() {
        Button button = new Button("Interrupt");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	new MessageBox("作業資訊",
                        MessageBox.Icon.QUESTION,
                        "Are you sure to interrupt this job?",
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
        			).show(	
                        new EventListener() {
        	                public void buttonClicked(ButtonType buttonType) {
        	                    if (buttonType == MessageBox.ButtonType.YES) {
                                    TriggerKey triggerKey = getSelectedTriggerKey();
                                    SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
                                    Trigger trigger = scheduler.getTrigger(triggerKey);
                                    try {
                                        scheduler.interrupt(trigger.getJobKey());
                                        scheduleLayout.loadSchedulerScreen(schedulerSettingsName);
                                    } catch (RuntimeException e) {
                                    	UI.getCurrent().addWindow(new ErrorWindow(e));
                                    }
        	               		}
        	               	}
        	            }
                    );            	
            }
        });
        return button;
    }

    private void initJobsTable() {
        table = new Table();
        addComponent(table);

        table.setSizeFull();
        table.setImmediate(true);
        table.setSelectable(true);

        Object defaultValue = null; // Not used.
        table.addContainerProperty("Trigger", String.class, defaultValue);
        table.addContainerProperty("JobDetail", String.class, defaultValue);
        table.addContainerProperty("Type", String.class, defaultValue);
        table.addContainerProperty("Next Run", String.class, defaultValue);
        table.addContainerProperty("Last Run", String.class, defaultValue);

        // Selectable handler
        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                selectedTriggerKeyName = (String) event.getProperty().getValue();
                disableToolbarIfNeeded();
            }
        });

        // Double click handler - drill down to trigger/job details
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    selectedTriggerKeyName = (String) event.getItemId();
                    showJobsWithTriggersWindow();
                }
            }
        });

        reloadTableContent();
    }

    private void reloadTableContent() {
        table.removeAllItems();
        // Fill table data
        LOGGER.debug("Loading current running jobs from scheduler {}", schedulerSettingsName);
        MySchedule mySchedule = MySchedule.getInstance();
        SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
        List<JobExecutionContext> jobs = scheduler.getCurrentlyExecutingJobs();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (JobExecutionContext job : jobs) {
            Trigger trigger = job.getTrigger();
            TriggerKey triggerKey = trigger.getKey();
            JobKey jobKey = trigger.getJobKey();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Date nextFireTime = trigger.getNextFireTime();
            Date previousFireTime = trigger.getPreviousFireTime();
            String triggerKeyName = triggerKey.getName() + "/" + triggerKey.getGroup();
            Object[] row = new Object[]{
                    triggerKeyName,
                    jobKey.getName() + "/" + jobKey.getGroup(),
                    trigger.getClass().getSimpleName() + "/" + jobDetail.getJobClass().getSimpleName(),
                    (nextFireTime == null) ? "" : df.format(nextFireTime),
                    (previousFireTime == null) ? "" : df.format(previousFireTime)
            };
            table.addItem(row, triggerKeyName);
        }
    }

    private void showJobsWithTriggersWindow() {
        TriggerKey triggerKey = getSelectedTriggerKey();
        JobsWithTriggersWindow window = new JobsWithTriggersWindow(schedulerSettingsName, triggerKey);
        UI.getCurrent().addWindow(window);
    }

    private TriggerKey getSelectedTriggerKey() {
        String[] names = StringUtils.split(selectedTriggerKeyName, "/");
        if (names.length != 2)
            throw new RuntimeException("Unable to retrieve trigger: invalid trigger name/group format used.");

        TriggerKey triggerKey = new TriggerKey(names[0], names[1]);
        return triggerKey;
    }
}
