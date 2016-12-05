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
import myschedule.web.SchedulerSettings;
import myschedule.web.SchedulerStatus;

import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * UI screen for displaying a dashboard of all schedulers in a table view. This is the default main screen for the
 * MySchedule application. User should see all the configured scheduler in this screen with their status and actions.
 * This screen should also have a tableRowActionButtonsGroup to allow user to add new scheduler config entry into this scheduler table.
 */
public class DashboardScreen extends VerticalLayout {
    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardScreen.class);
    private static final long serialVersionUID = 1L;
    private HorizontalLayout toolbar;
    private SchedulerButtonGroup schedulerButtonGroup;
    private Table table;
    private MySchedule mySchedule = MySchedule.getInstance();
    private String selectedSettingsName;
    private Button viewDetailsButton;
    private ScheduleLayout scheduleLayout;

    public DashboardScreen(ScheduleLayout sLayout) {
        initToolbar();
        initSchedulersTable();
        this.scheduleLayout=sLayout;
    }

    private void initToolbar() {
        toolbar = new HorizontalLayout();
        addComponent(toolbar);

        viewDetailsButton = createViewDetailsButton();
        toolbar.addComponent(viewDetailsButton);

        schedulerButtonGroup = new SchedulerButtonGroup();
        toolbar.addComponent(schedulerButtonGroup);
    }

    private Button createViewDetailsButton() {
        final Button button = new Button("排程明細");
        button.setEnabled(false);

        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                if (selectedSettingsName != null)
                	scheduleLayout.loadSchedulerScreen(selectedSettingsName);
                else
                    button.setEnabled(false);
            }
        });

        return button;
    }

    private void initSchedulersTable() {
        table = new Table();
        addComponent(table);

        table.setSizeFull();
        table.setSelectable(true);
        table.setImmediate(true);

        Object defaultValue = null; // Not used.
        table.addContainerProperty("Scheduler", String.class, defaultValue);
        table.addContainerProperty("Config ID", String.class, defaultValue);
        table.addContainerProperty("Status", String.class, defaultValue);
        table.addContainerProperty("Job Counts", Integer.class, defaultValue);

        // Fill table data
        List<String> names = mySchedule.getSchedulerSettingsNames();
        for (String settingsName : names) {
            LOGGER.debug("Adding scheduler settings {} to dashboard.", settingsName);
            SchedulerSettings settings = mySchedule.getSchedulerSettings(settingsName);
            SchedulerTemplate scheduler = mySchedule.getScheduler(settingsName);
            SchedulerStatus status = MySchedule.getSchedulerStatus(scheduler);
            Integer jobCount = 0;
            String schedulerName = settings.getSchedulerFullName();

            if (status == SchedulerStatus.RUNNING || status == SchedulerStatus.STANDBY) {
                jobCount = scheduler.getAllTriggers().size();
            }

            Object[] row = new Object[]{
                    schedulerName,
                    settingsName,
                    status.toString(),
                    jobCount
            };
            table.addItem(row, settingsName);
        }

        // Selectable action
        table.addValueChangeListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                String settingsName = (String) event.getProperty().getValue();
                schedulerButtonGroup.updateSelectedSettingsName(settingsName);
            }
        });

        // Double click drill down action - show scheduler screen (jobs)
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    String settingsName = (String) event.getItemId();
                    SchedulerTemplate scheduler = mySchedule.getScheduler(settingsName);
                    SchedulerStatus status = MySchedule.getSchedulerStatus(scheduler);
                    if (status != SchedulerStatus.SHUTDOWN)
                    	scheduleLayout.loadSchedulerScreen(settingsName);
                }
            }
        });
    }

    class SchedulerButtonGroup extends HorizontalLayout {
        Button init = createInitButton();
        Button start = createStartButton();
        Button standby = createStandbyButton();
        Button shutdown = createShutdownButton();
        Button edit = createEditButton();
        Button delete = createDeleteButton();

        public SchedulerButtonGroup() {
            addComponent(init);
            addComponent(start);
            addComponent(standby);
            addComponent(shutdown);
            addComponent(delete);
            addComponent(edit);
            addComponent(createNewButton());

            updateSelectedSettingsName(null);
        }

        void updateSelectedSettingsName(String settingsName) {
            selectedSettingsName = settingsName;
            if (settingsName == null) {
                disableButtons(viewDetailsButton, delete, init, start, standby, shutdown, edit);
            } else {
                enableButtons(viewDetailsButton, edit, delete);

                SchedulerTemplate scheduler = mySchedule.getScheduler(settingsName);
                SchedulerStatus status = MySchedule.getSchedulerStatus(scheduler);

                if (status == SchedulerStatus.RUNNING) {
                    disableButtons(init, start);
                    enableButtons(standby, shutdown);
                } else if (status == SchedulerStatus.SHUTDOWN) {
                    enableButtons(init);
                    disableButtons(viewDetailsButton, start, standby, shutdown);
                } else if (status == SchedulerStatus.STANDBY) {
                    disableButtons(init, standby);
                    enableButtons(start, shutdown);
                }
            }
        }

        private void enableButtons(Button... buttons) {
            for (Button b : buttons)
                b.setEnabled(true);
        }

        private void disableButtons(Button... buttons) {
            for (Button b : buttons)
                b.setEnabled(false);
        }

        private Button createNewButton() {
            Button button = new Button("New");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    UI.getCurrent().addWindow(new NewSchedulerWindow(scheduleLayout));
                }
            });
            return button;
        }

        private Button createEditButton() {
            Button button = new Button("Edit");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    UI.getCurrent().addWindow(new EditSchedulerWindow(scheduleLayout,selectedSettingsName));
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
                            "Are you sure to delete?",
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
            			).show(	
                            new EventListener() {
            	                public void buttonClicked(ButtonType buttonType) {
            	                    if (buttonType == MessageBox.ButtonType.YES) {
            	                    	mySchedule.deleteSchedulerSettings(selectedSettingsName);
                                        table.removeItem(selectedSettingsName);
                                        scheduleLayout.loadDashboardScreen();
            	               		}
            	               	}
            	            }
                        );                	
                }
            });
            return button;
        }

        private Button createInitButton() {
            Button button = new Button("Init");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        SchedulerSettings settings = mySchedule.getSchedulerSettings(selectedSettingsName);
                        mySchedule.createScheduler(settings);
                    } catch (Exception e) {
                        UI.getCurrent().addWindow(new ErrorWindow(e));
                    }
                    scheduleLayout.loadDashboardScreen(); // Now refresh the dashboard for the updated scheduler.
                }
            });
            return button;
        }

        private Button createStartButton() {
            Button button = new Button("Start");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                    try {
                        mySchedule.getScheduler(selectedSettingsName).start();
                    } catch (RuntimeException e) {
                        UI.getCurrent().addWindow(new ErrorWindow(e));
                    }
                    scheduleLayout.loadDashboardScreen();
                }
            });
            return button;
        }

        private Button createStandbyButton() {
            Button button = new Button("Standby");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                	new MessageBox("作業資訊",
                            MessageBox.Icon.QUESTION,
                            "Are you sure to standby?",
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
            			).show(	
                            new EventListener() {
            	                public void buttonClicked(ButtonType buttonType) {
            	                    if (buttonType == MessageBox.ButtonType.YES) {
                                        try {
                                            mySchedule.getScheduler(selectedSettingsName).standby();
                                        } catch (RuntimeException e) {
                                            UI.getCurrent().addWindow(new ErrorWindow(e));
                                        }
                                        scheduleLayout.loadDashboardScreen();
            	               		}
            	               	}
            	            }
                        );                	
                }
            });
            return button;
        }

        private Button createShutdownButton() {
            Button button = new Button("Shutdown");
            button.addClickListener(new Button.ClickListener() {
                public void buttonClick(Button.ClickEvent event) {
                	new MessageBox("作業資訊",
                            MessageBox.Icon.QUESTION,
                            "Are you sure to shutdown?",
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "確定"),
                            new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "取消")
            			).show(	
                            new EventListener() {
            	                public void buttonClicked(ButtonType buttonType) {
            	                    if (buttonType == MessageBox.ButtonType.YES) {
                                        try {
                                            mySchedule.shutdownScheduler(selectedSettingsName);
                                            scheduleLayout.loadDashboardScreen();
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
    }
}
