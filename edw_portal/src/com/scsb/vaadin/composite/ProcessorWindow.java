package com.scsb.vaadin.composite;

import java.util.Random;

import com.scsb.vaadin.composite.ProcessorWindow.Processor.ProgressListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class ProcessorWindow  {
	
	int MAX_PROGRESS=100;
	int NOW_PROGRESS=0;
	Window progressWindow = new Window();
	ProgressBar progressBar = new ProgressBar();
	
	public ProcessorWindow(String caption ){
        progressBar.setWidth(400, Unit.PIXELS);
			
		HorizontalLayout content = new HorizontalLayout();
		content.addComponent(progressBar);
	    progressWindow = new Window("", content);
	    progressWindow.setClosable(false);
	    progressWindow.setResizable(false);
	    progressWindow.center();
		    
        new Thread(new Runnable() {
		          public void run() {
		            new Processor(new ProgressListener() {
		              public void onProgress(final long progress) {
		                UI.getCurrent().access(new Runnable() {
		                  public void run() {
		                    // 0 .. 1
		                    final float progressBarValue = (float) progress / Processor.MAX_PROGRESS;
		                    progressBar.setValue(progressBarValue);
		                    if (progress == Processor.MAX_PROGRESS) {
		                      UI.getCurrent().setPollInterval(-1);
		                      UI.getCurrent().removeWindow(progressWindow);
		                    }
		                  }
		                });
		              }
		            }).run();
		          }
		        }).start();		    
		    
		    UI.getCurrent().setPollInterval(250);
		    UI.getCurrent().addWindow(progressWindow);
  }
  
	  static class Processor {
		    interface ProgressListener {
		      void onProgress(long progress);
		    }
		    private static final int MAX_PROGRESS = 100;
		    private final ProgressListener progressListener;
		    Processor(final ProgressListener progressListener) {
		      this.progressListener = progressListener;
		    }
		    void run() {
		      for (int i = 0; i <= MAX_PROGRESS; i++) {
		        progressListener.onProgress(i);
		        try {
		          Thread.sleep(100 + new Random().nextInt(250));
		        } catch (final InterruptedException e) {
		          break;
		        }
		      }
		    }
		  }
}