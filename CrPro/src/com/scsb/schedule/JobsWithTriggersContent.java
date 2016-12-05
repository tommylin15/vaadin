package com.scsb.schedule;

import com.scsb.crpro.MessageBox;
import com.scsb.crpro.MessageBox.ButtonType;
import com.scsb.crpro.MessageBox.EventListener;
import com.vaadin.data.Item;
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
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**s
 * JobsWithTriggersContents provide a table view for all JobDetails that have triggers associated.
 * User: Zemian Deng
 * Date: 6/1/13
 */
public class JobsWithTriggersContent extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobsWithTriggersContent.class);
    MySchedule mySchedule = MySchedule.getInstance();
    String schedulerSettingsName;
    HorizontalLayout toolbar;
    HorizontalLayout tableRowActionButtonsGroup;
    Table table;
    String selectedTriggerKeyName;
    Button pauseOrResumeButton;

    public JobsWithTriggersContent(String schedulerSettingsName) {
        this.schedulerSettingsName = schedulerSettingsName;
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
        tableRowActionButtonsGroup.addComponent(createDeleteButton());
        tableRowActionButtonsGroup.addComponent(createRunItNowButton());

        pauseOrResumeButton = createPauseOrResumeButton();
        tableRowActionButtonsGroup.addComponent(pauseOrResumeButton);

        disableToolbarIfNeeded(Trigger.TriggerState.NORMAL.toString());
    }

    private void disableToolbarIfNeeded(String triggerStateName) {
        if (selectedTriggerKeyName == null) {
            tableRowActionButtonsGroup.setEnabled(false);
        } else {
            // Check and ensure Pause/Resume button has the right label.
            Trigger.TriggerState triggerState = Trigger.TriggerState.valueOf(triggerStateName);
            if (triggerState == Trigger.TriggerState.PAUSED)
                pauseOrResumeButton.setCaption("Resume");
            else
                pauseOrResumeButton.setCaption("Pause");
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

    private Button createDeleteButton() {
        Button button = new Button("Delete");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
            	new MessageBox("作業資訊",
                        MessageBox.Icon.QUESTION,
                        "Are you sure to delete trigger?",
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
        			).show(	
                        new EventListener() {
        	                public void buttonClicked(ButtonType buttonType) {
        	                    if (buttonType == MessageBox.ButtonType.YES) {
                                    TriggerKey triggerKey = getSelectedTriggerKey();
                                    SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
                                    scheduler.unscheduleJob(triggerKey);

                                    reloadTableContent();

        	               		}
        	               	}
        	            }
                    );
            }
        });
        return button;
    }

    private Button createRunItNowButton() {
        Button button = new Button("Run It Now");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
    			new MessageBox("作業資訊",
                        MessageBox.Icon.QUESTION,
                        "Are you sure to run it now?",
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
        			).show(	
                        new EventListener() {
        	                public void buttonClicked(ButtonType buttonType) {
        	                    if (buttonType == MessageBox.ButtonType.YES) {
                                    TriggerKey triggerKey = getSelectedTriggerKey();
                                    SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
                                    Trigger trigger = scheduler.getTrigger(triggerKey);
                                    scheduler.triggerJob(trigger.getJobKey());

                                    reloadTableContent();
        	               		}
        	               	}
        	            }
                    );            	
            }
        });
        return button;
    }

    private Button createPauseOrResumeButton() {
        Button button = new Button("Pause");
        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                final String caption = event.getButton().getCaption();
                String msg = caption.equals("Pause") ?
                        "Are you sure to pause it now?" :
                        "Are you sure to resume it now?";
    			new MessageBox("作業資訊",
                        MessageBox.Icon.QUESTION,
                        msg,
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
        			).show(	
                        new EventListener() {
        	                public void buttonClicked(ButtonType buttonType) {
        	                    if (buttonType == MessageBox.ButtonType.YES) {
                                    TriggerKey triggerKey = getSelectedTriggerKey();
                                    SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
                                    if (caption.equals("Pause")) {
                                        scheduler.pauseTrigger(triggerKey);
                                    } else {
                                        scheduler.resumeTrigger(triggerKey);
                                    }
                                    reloadTableContent();
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
        table.addContainerProperty("Status", String.class, defaultValue);

        // Selectable handler
        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                selectedTriggerKeyName = (String) event.getProperty().getValue();
                Item item = table.getItem(selectedTriggerKeyName);
                if (item != null) {
                    String triggerStateName = (String)item.getItemProperty("Status").getValue();
                    disableToolbarIfNeeded(triggerStateName);
                }
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

        // Fill table data
        reloadTableContent();
    }

    private void reloadTableContent() {
        table.removeAllItems();
        LOGGER.debug("Loading triggers from scheduler {}", schedulerSettingsName);
        MySchedule mySchedule = MySchedule.getInstance();
        SchedulerTemplate scheduler = mySchedule.getScheduler(schedulerSettingsName);
        List<Trigger> triggers = scheduler.getAllTriggers();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Trigger trigger : triggers) {
            TriggerKey triggerKey = trigger.getKey();
            JobKey jobKey = trigger.getJobKey();
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            Date nextFireTime = trigger.getNextFireTime();
            Date previousFireTime = trigger.getPreviousFireTime();
            String triggerKeyName = triggerKey.getName() + "/" + triggerKey.getGroup();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
            Object[] row = new Object[]{
                    triggerKeyName,
                    jobKey.getName() + "/" + jobKey.getGroup(),
                    trigger.getClass().getSimpleName() + "/" + jobDetail.getJobClass().getSimpleName(),
                    (nextFireTime == null) ? "" : df.format(nextFireTime),
                    (previousFireTime == null) ? "" : df.format(previousFireTime),
                    triggerState.toString()
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
