package com.scsb.schedule;

import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.domain.HashLucene;
import com.scsb.util.FileDocument;
import com.scsb.util.IO;
import com.vaadin.data.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LuceneIndex extends TimerTask{
	private static final long serialVersionUID = 1L;
	protected static HashLucene hashLucene = HashLucene.getInstance();
	protected static Properties luceneProps     =hashLucene.getProperties();

	public LuceneIndex(){
	}
	  
	public void run(){
		System.out.println("Lucene建立索引....begin");

		String jsonString =IO.read(System.getProperty("schedule.root")+luceneProps.getProperty("folder_json"));
		JsonContainer jsonData= JsonContainer.Factory.newInstance(jsonString);		
		System.out.println("jsonData:"+jsonData.size());		
	    Date start = new Date();
	    try {
	      File indexDir =new File(luceneProps.getProperty("index_dir"));
	      IndexWriter writer = new IndexWriter(FSDirectory.open(indexDir), new StandardAnalyzer(Version.LUCENE_CURRENT), true, IndexWriter.MaxFieldLength.LIMITED);
	      System.out.println("Indexing to directory '" +indexDir+ "'...");
	      for(int i=0;i<jsonData.size();i++){
        	Item item=jsonData.getItem(jsonData.getIdByIndex(i));
        	//String id=(String)item.getItemProperty("id").getValue();
        	String value=(String)item.getItemProperty("value").getValue();
        	String haveNext=(String)item.getItemProperty("haveNext").getValue();
        	indexDocs(writer, new File(value) ,haveNext ,true);
	      }	      
		  System.out.println("Optimizing...");
		  writer.optimize();
		  writer.close();
	
		  Date end = new Date();
		  System.out.println(end.getTime() - start.getTime() + " total milliseconds");
		  System.out.println("Lucene建立索引....end");
	
	    } catch (IOException e) {
	      System.out.println(" caught a " + e.getClass() +"\n with message: " + e.getMessage());
	    }		  
	  }

	  static void indexDocs(IndexWriter writer, File file ,String haveNext ,boolean isRoot) throws IOException {
	    // do not try to index files that cannot be read
	    if (file.canRead()) {
	      if (file.isDirectory()) {
	        String[] files = file.list();
	        // an IO error could occur
	        if (isRoot || haveNext.equals("Y")){
		        if (files != null) {
		          for (int i = 0; i < files.length; i++) {
		            indexDocs(writer, new File(file, files[i]) ,haveNext ,false);
		          }
		        }
		        //System.out.println("adding " + file +"\\*.*");
	        }
	      } else {
	        try {
	          writer.addDocument(FileDocument.Document(file));
	        }
	        // at least on windows, some temporary files raise this exception with an "access denied" message
	        // checking if the file can be read doesn't help
	        catch (FileNotFoundException fnfe) {
	          ;
	        }
	      }
	    }
	  }
	}