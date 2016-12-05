package com.scsb.crpro.explorer;

import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.tepi.filtertable.FilterTable;
import org.tepi.filtertable.FilterTreeTable;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.crpro.AceWindow;
import com.scsb.crpro.CrproLayout;
import com.scsb.crpro.UtilOptionGroup;
import com.scsb.crpro.fileio.DynamicStreamResource;
import com.scsb.util.DateUtil;
import com.scsb.util.IO;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.FilesystemContainer;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SuppressWarnings("serial")
public class explorer extends CrproLayout {

	private String nowPath;
	private String backupPath;
	private NativeSelect pathChoo = new NativeSelect();
	
	
	protected IndexedContainer fileListContainer = new IndexedContainer();
	private FilterTreeTable foldertable = new FilterTreeTable();
	private IndexedContainer folderContainer   = new IndexedContainer();
	
	protected FilterTable      pageTable         = new FilterTable();
	protected UtilOptionGroup uog =new UtilOptionGroup();
	protected Hashtable<String,String> hData =new Hashtable<String,String>();
	
	private JsonContainer jsonData=null;
	
	private Label showPath = new Label();
	private int iFolders =0;
	private Upload upLoadFile = new Upload("",null);
	
	DynamicStreamResource dsr =null;
	FileDownloader fileDownloader =null;
	
	AbsoluteLayout topLayout = new AbsoluteLayout();
	HorizontalSplitPanel mainBody = new HorizontalSplitPanel();
	OptionGroup extension =new OptionGroup();
	public explorer() {
		this.hData = uog.getData();
		
		this.fileListContainer.addContainerProperty("isLock",Button.class, new Button());		
		this.fileListContainer.addContainerProperty("m_name",String.class, "");
		this.fileListContainer.addContainerProperty("m_date",String.class, "");
		this.fileListContainer.addContainerProperty("m_size",String.class, "");
		this.fileListContainer.addContainerProperty("m_path",String.class, "");
		this.fileListContainer.addContainerProperty("file",FileData.class, "");
		setPageTable();
				
		create();		
	}
	public void create(){
		this.removeAllComponents();
		
		this.setId("explorer");
		
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
    	String jsonString =IO.read(servletContext.getRealPath("/")+systemProps.getProperty("folderJson"));
		jsonData= JsonContainer.Factory.newInstance(jsonString);		
		
		pathChoo.setImmediate(true);
        pathChoo.setWidth("300px");
        pathChoo.setNullSelectionAllowed(false);
        for(int i=0;i<jsonData.size();i++){
        	Item item=jsonData.getItem(jsonData.getIdByIndex(i));
        	String id=(String)item.getItemProperty("id").getValue();
        	String value=(String)item.getItemProperty("value").getValue();
            pathChoo.addItem(value);
            pathChoo.setItemCaption(value ,id);
            if (i==0)  pathChoo.setValue(value);
        }
        pathChoo.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				nowPath=(String)event.getProperty().getValue();
				getFolder();
			}
        });
		this.nowPath =(String)pathChoo.getValue();
		
		this.extension =uog.getFileOptionGroup();
		
		extension.addValueChangeListener(new ValueChangeListener(){
			public void valueChange(ValueChangeEvent event) {
				getFileList(nowPath);
	        	//System.out.println(extension.getValue());				
			}
			
		});
		
		this.showPath.setImmediate(true);		
		
		
	    //上傳檔案
	    class FileUploader implements Receiver{
	    	public File file;
	    	
	        public OutputStream receiveUpload(String filename, String mimeType) {
	        	FileOutputStream fos = null;
                // Open the file for writing.
                file = new File(nowPath + filename);
                //檢查是否需備份
                String haveBackup =systemProps.getProperty("haveBackup");
                if (haveBackup.equals("Y")){
                	String backupRoot=systemProps.getProperty("backupPath");
                	String oldFile =nowPath+filename;
                	String newFile =backupRoot+backupPath+filename+"."+DateUtil.getDTS()+"_"+_User;
                	//先建資料夾
                	IO.mkDir(backupRoot+backupPath);
                	//再複製檔案
                	IO.copyFile(oldFile, newFile);
                }
                try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	        	
	            //os.reset(); // If re-uploading
	            return fos;
	         }
	     };
	    final FileUploader receiver = new FileUploader(); 
	    upLoadFile.setReceiver(receiver);
	    upLoadFile.setButtonCaption("上傳檔案");
	    upLoadFile.setImmediate(true);
	    upLoadFile.addSucceededListener(new SucceededListener() {
            public void uploadSucceeded(SucceededEvent event) {
            	getFileList(nowPath);
            	receiver.file=null;
            }
        });		
	    
	    
		topLayout.setWidth("100%");
        topLayout.setHeight("60px");
        topLayout.setImmediate(true);
        topLayout.addComponent(pathChoo,"left: 1px; top: 10px;");
        topLayout.addComponent(extension,"left: 310px; top: 2px;");
        topLayout.addComponent(showPath,"left: 310px; top: 60px;");
        if (this._hashUserAction.get("explorerUpLoad").equals("Y")){
        	topLayout.addComponent(upLoadFile,"right: 10px; top: 10px;");
        }
        this.addComponent(topLayout);		
		
		folderContainer.addContainerProperty("m_name",String.class, "");
		folderContainer.addContainerProperty("FileData",FileData.class, null);
        
		foldertable.setSizeFull();
		foldertable.setFilterBarVisible(true);
		foldertable.setSelectable(true);
		foldertable.setImmediate(true);
		foldertable.setMultiSelect(true);
		foldertable.setColumnCollapsingAllowed(true);
		foldertable.setColumnReorderingAllowed(true);  
		
		foldertable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			public void itemClick(ItemClickEvent event) {				
				Item item =folderContainer.getItem(event.getItemId());
				String sName=(String)item.getItemProperty("m_name").getValue();
				if (sName.equals("..")){
					if (nowPath.lastIndexOf("/") > -1) nowPath =nowPath.substring(0,nowPath.lastIndexOf("/"));
					if (nowPath.lastIndexOf("/") > -1) nowPath =nowPath.substring(0,nowPath.lastIndexOf("/")+1);
				}else{
					nowPath+=sName+"/";
				}
				getFolder();
		    }
		});	
		foldertable.setStyleName("filtertable");
		
		//mainBody.setSizeFull();
	    mainBody.setLocked(false);
	    mainBody.setId("mainBody");
	    mainBody.setSplitPosition(300,Unit.PIXELS);// 100px :UNITS_PIXELS=0
	    getFolder();
		mainBody.setFirstComponent(foldertable);
		mainBody.setSecondComponent(pageTable);
			
		this.addComponent(mainBody);
		this.setSpacing(false);
		this.setMargin(false);
	}
	
	//取得檔案列表
	public void getFileList(String filePath){
		fileListContainer.removeAllItems();
		File f = new File(filePath);
		File[] files = f.listFiles(textFilter);
		
		if (files != null){
	        for (int i = 0; i < files.length; i++) {
	            if (!files[i].isDirectory()) {
	            	Item item=fileListContainer.addItem(i);
	            	setFileToItem(files[i] ,item);
	        	}
	        }
	        setPageTable();
		}else{
		}
	}
	
	FilenameFilter textFilter = new FilenameFilter() {
		public boolean accept(File file ,String name) {
			Collection<String> coll = (Collection<String>)extension.getValue();
			Iterator<String> itr = coll.iterator();
	        while (itr.hasNext()) {	
	        	String stext=itr.next();
	        	if (stext.indexOf(".*") > -1 ) return true;
			    if (name.toLowerCase().endsWith(stext)){
			    	return true;
			    }
			} 				
		    return false;
		}	
	};	
	
	public void getFolder(){
		if (!nowPath.substring(nowPath.length()-1, nowPath.length()).equals("/")){
			nowPath=nowPath+File.separator;
		}
		backupPath=nowPath.replace((String) pathChoo.getValue(), pathChoo.getItemCaption(pathChoo.getValue())+"/");
		showPath.setCaption(backupPath);		
		//tree.setParent("Branch 1", "Root");
		folderContainer.removeAllItems();
		iFolders=0;
		
		File f = new File(nowPath);
		File[] files = f.listFiles();
		
		if (!nowPath.equals(pathChoo.getValue())){
			FileData folder=new FileData(null,true ,null );
        	Item item=folderContainer.addItem(iFolders++);
        	item.getItemProperty("m_name").setValue(folder.m_name);
        	item.getItemProperty("FileData").setValue(folder);
		}
		
		if (files != null){
			//資料夾
			for (int i = 0; i < files.length; i++) {
	            if (files[i].isDirectory()) {
	            	FileData folder=new FileData(files[i],false ,hData);            	
	            	Item item=folderContainer.addItem(iFolders++);	            	
	            	item.getItemProperty("m_name").setValue(folder.m_name);
	            	item.getItemProperty("FileData").setValue(folder);
	            }
	        }
			foldertable.setContainerDataSource(folderContainer);
			foldertable.setColumnHeader("m_name"  ,"資料夾名稱" );
			foldertable.setVisibleColumns(new Object[]{"m_name"});
			getFileList(nowPath);			
		}
	}
	public void setFileToItem(File file ,Item item){
    	FileData filedata=new FileData(file,false ,hData);
    	Button buLock =new Button();
    	if (filedata.m_isLock.equals("Y"))
    		buLock.setIcon(FontAwesome.LOCK);
    	else
    		buLock.setIcon(FontAwesome.UNLOCK);	            	
    	item.getItemProperty("isLock").setValue(buLock);
    	item.getItemProperty("m_name").setValue(filedata.m_name);
    	item.getItemProperty("m_date").setValue(filedata.m_date);
    	item.getItemProperty("m_path").setValue(filedata.m_path);
    	item.getItemProperty("m_size").setValue(filedata.m_size);
    	item.getItemProperty("file").setValue(filedata);
	}
	
	/**
	 * 設定PageTable
	 */
	public void setPageTable(){
        pageTable.setFilterBarVisible(true);
        pageTable.setId("ccCodeTable");
        pageTable.setStyleName("filtertable");
        pageTable.setSelectable(true);
        pageTable.setImmediate(true); 
        pageTable.setSortEnabled(true);
        pageTable.setSizeFull();
	    pageTable.addValueChangeListener(pateTablelistener);	        
		
	    pageTable.setContainerDataSource(fileListContainer);
        pageTable.setColumnHeader("isLock"  ,"" );
        pageTable.setColumnHeader("m_name"  ,"檔案名稱" );
        pageTable.setColumnHeader("m_date"  ,"更新時間" );
        pageTable.setColumnHeader("m_size"  ,"檔案大小" );
        pageTable.setVisibleColumns(new Object[]{"isLock","m_name","m_date","m_size"});
        pageTable.setColumnWidth("isLock", 60);
        
	}	
	//選取Table資料時
    Property.ValueChangeListener pateTablelistener = new Property.ValueChangeListener() {
       	public void valueChange(Property.ValueChangeEvent event) {
       		Item item=fileListContainer.getItem(pageTable.getValue());
			if (item != null){
				Button isLock =(Button)item.getItemProperty("isLock").getValue();
				boolean isEdit =((FileData)item.getItemProperty("file").getValue()).isEdit;
				if (!isLock.getIcon().equals(FontAwesome.LOCK)){
					String m_path=(String)item.getItemProperty("m_path").getValue();
					String m_name=(String)item.getItemProperty("m_name").getValue();
					loadFile(m_path ,m_name,isEdit);
				}
       		}
        }
    };
    
	
	/**
	 * 按下檔案時,開啟aceedit載入檔案
	 * @param fileName
	 * @param isEdit
	 */
	public void loadFile(String filePath ,String fileName ,boolean isEdit){
		String fileData ="";
		String fileEncode="";	
		
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		   detector.add(new ParsingDetector(false));                                      
		   detector.add(JChardetFacade.getInstance());                            
		   //detector.add(ASCIIDetector.getInstance());                                     
		   //detector.add(UnicodeDetector.getInstance());                                   
		   java.nio.charset.Charset charset = null;
		   File file = new File(filePath);
		   try {                                                                          
			    charset = detector.detectCodepage(file.toURI().toURL());                     
			   } catch (Exception ex) {                                                       
			    ex.printStackTrace();                                                        
			   }                                                                              
		   if (charset != null) fileEncode=charset.name();                                                       
		   else fileEncode="utf-8";			
		
		try {
			FileInputStream fis = new FileInputStream(filePath);
			InputStreamReader isr = new InputStreamReader(fis);
			fileEncode = isr.getEncoding();
			
			BufferedReader br = new BufferedReader(isr);

			String line = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
			br.close();
			fileData = sb.toString();
		} catch (Exception e) {
		}
		if ( isEdit){
			AceWindow popover =new AceWindow(themePath ,fileEncode ,filePath ,fileName ,fileData ,true );
		}else{
			AceWindow popover =new AceWindow(themePath ,fileEncode ,filePath ,fileName ,fileData ,false );
		}
	}	
}


