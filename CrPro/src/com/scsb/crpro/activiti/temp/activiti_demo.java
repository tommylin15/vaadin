package com.scsb.crpro.activiti.temp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.TaskQuery;

public class activiti_demo {
	
	protected transient static RepositoryService repositoryService;
	
	 public activiti_demo() { }
	 
	 static public void main(String[] argv){
		 ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		 TaskService taskService = processEngine.getTaskService();
		 TaskQuery taskQuery =taskService.createTaskQuery();
		 List list =taskQuery.list();
		 System.out.println("task query :"+list.size());
		 
		 RepositoryService repositoryService = processEngine.getRepositoryService();
		 String barFileName = "d:/TommyProcess.bar";
		 ZipInputStream inputStream;
		try {
			inputStream = new ZipInputStream(new FileInputStream(barFileName));
			repositoryService.createDeployment().name("TommyProcess.bar").addZipInputStream(inputStream).deploy();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
}