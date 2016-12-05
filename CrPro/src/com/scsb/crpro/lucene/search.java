package com.scsb.crpro.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FilterIndexReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.scsb.crpro.CrproLayout;
import com.scsb.domain.HashLucene;
import com.scsb.domain.HashSystem;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class search extends CrproLayout {
	protected static HashLucene hashLucene = HashLucene.getInstance();	
	protected static Properties luceneProps     =hashLucene.getProperties();
	
	protected IndexedContainer fileListContainer = new IndexedContainer();
	private HorizontalLayout topLayout =new HorizontalLayout();
	private TextField searchText =new TextField();
	private Button buSearch =new Button();
	private int iFolders =0;
	
	VerticalLayout mainBody = new VerticalLayout();
	public search() {
		create();
	}
	public void create(){
		this.removeAllComponents();
		this.setId("search");
        
		this.topLayout.addComponent(searchText);
		buSearch.setCaption("查詢");
		buSearch.setIcon(FontAwesome.SEARCH);
		this.topLayout.addComponent(buSearch);
		buSearch.addClickListener(new Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				getSearchList();
			}
			
		});
		//mainBody.setSizeFull();
	    mainBody.setId("mainBody");
		mainBody.addComponent(topLayout);
		mainBody.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
		mainBody.setExpandRatio(topLayout, 1.0f); 	    
		//mainBody.addComponent(pageTable);
		//mainBody.setComponentAlignment(pageTable, Alignment.TOP_CENTER);
		//mainBody.setExpandRatio(pageTable, 1.0f); 	    
			
		this.addComponent(mainBody);
		this.setSpacing(false);
		this.setMargin(false);
	}
	
	//執行檢索
	public void runSearch(){
		String indexPath =luceneProps.getProperty("index_dir");
		String field = "contents";
	    String queries = null;
	    int repeat = 0;
	    boolean raw = false;
	    String normsField = null;
	    int hitsPerPage = 1000;
	    
		IndexReader reader;
		try {
			reader = IndexReader.open(FSDirectory.open(new File(indexPath)), true);
			if (normsField != null)  reader = new OneNormsReader(reader, normsField);
		    Searcher searcher = new IndexSearcher(reader);
		    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);

		      BufferedReader in = null;
		      QueryParser parser = new QueryParser(field, analyzer);
	    	  String line =searchText.getValue();
		      if (line == null || line.length() ==-1 ) return;
		      line = line.trim();
		      if (line.length() == 0)   return;
		        
		      Query query = parser.parse(line);
		      //System.out.println("Searching for: " + query.toString(field));
		      if (repeat > 0) {                           // repeat & time as benchmark
		          Date start = new Date();
		          for (int i = 0; i < repeat; i++) {
		            searcher.search(query, null, 100);
		          }
		          Date end = new Date();
		          System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
		      }
			  // Collect enough docs to show 5 pages
			  TopScoreDocCollector collector = TopScoreDocCollector.create(5 * hitsPerPage, false);
			  searcher.search(query, collector);
			  ScoreDoc[] hits = collector.topDocs().scoreDocs;
			  
			  for (int i = 0; i < hits.length; i++) {
			    Document doc = searcher.doc(hits[i].doc);
			    String path = doc.get("path");
			    if (path != null) {			    	
			    	Item item=fileListContainer.addItem(i);
			    	//setFileToItem(new File(path) ,item);			    	
			    }
			  }//for
		      reader.close();
		      //setPageTable();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	//取得檔案列表
	public void getSearchList(){
		fileListContainer.removeAllItems();
		runSearch();
	}
	
	  /**
	   *使用的規範，從一個字段的所有字段。規範是讀入內存，
	   *使用的每場搜索文檔存儲一個字節。這可能會導致
	   *搜索的大集合了眾多領域跑出來的
	   *內存。如果所有的字段只包含一個令牌，然後將規範
	   *都是相同的，那麼單個規範載體可以被共享。 
	   **/
	  private static class OneNormsReader extends FilterIndexReader {
	    private String field;

	    public OneNormsReader(IndexReader in, String field) {
	      super(in);
	      this.field = field;
	    }

	    public byte[] norms(String field) throws IOException {
	      return in.norms(this.field);
	    }
	  }	
	
}