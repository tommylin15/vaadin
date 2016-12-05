package com.scsb.crpro;


import com.scsb.crpro.build.tab.CreatePrg;
import com.scsb.crpro.explorer.explorer;
import com.scsb.etl.EtlLogLayout;
import com.scsb.schedule.ScheduleLayout;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

class MainLayout extends CrproLayout{
	private static final long serialVersionUID = 3325233253592861773L;
    MenuBar menuSet =new MenuBar();	
    Panel bodyPanel = new Panel("");
	
	public MainLayout(){
	    this.setMargin(false);
	    this.setSpacing(true);

	    menuSet.setHeight("40px");
        //menu bar
	    if (this._hashUserAction.get("haveExplorer").equals("Y")){
	    	MenuBar.MenuItem folder=menuSet.addItem("檔案總管", null, runExplorer);
	    	folder.setIcon(FontAwesome.FOLDER);
	    }
        
        if (this._Schedule.equals("Y") && this._hashUserAction.get("haveSchedule").equals("Y")){
        	MenuBar.MenuItem search=menuSet.addItem("批次管理", null, runSchedule);
        	search.setIcon(FontAwesome.SEARCH);
        }
        if (this._EtlLog.equals("Y") && this._hashUserAction.get("haveEtlLog").equals("Y")){
        	MenuBar.MenuItem search=menuSet.addItem("ETL Log瀏覽", null, runEtlLog);
        	search.setIcon(FontAwesome.CHECK);
        }        
        /*
        if (this._hashUserAction.get("haveActiviti").equals("Y")){
        	MenuBar.MenuItem activitiItem=menuSet.addItem("activiti", null, runActiviti);
        	activitiItem.setIcon(FontAwesome.ASTERISK);
        }
        */
        if (this._hashUserAction.get("haveCrpro").equals("Y")){
        	MenuBar.MenuItem crproItem=menuSet.addItem("程式碼管理", null, runCreatePrg);
        	crproItem.setIcon(FontAwesome.BUILDING_O);
        }   
        /*
        if (this._hashUserAction.get("haveMaskCode").equals("Y")){
        	MenuBar.MenuItem crproItem=menuSet.addItem("遮蔽程式碼管理", null, runMaskCode);
        	crproItem.setIcon(FontAwesome.MAGIC);
        }  
        */ 
        /*
        if (this._Jython.equals("Y")){
        	MenuBar.MenuItem crproItem=menuSet.addItem("Jython", null, runJython);
        	crproItem.setIcon(FontAwesome.BUILDING_O);
        }
        */
        MenuBar.MenuItem logOut=menuSet.addItem("登出", null, logout);
        logOut.setIcon(FontAwesome.OUTDENT);

        this.addComponent(menuSet);
        this.setComponentAlignment(menuSet, Alignment.TOP_LEFT);
        this.setExpandRatio(menuSet, 1.0f); 	    

	    this.addComponent(bodyPanel);
        this.setComponentAlignment(bodyPanel, Alignment.TOP_LEFT);
        this.setExpandRatio(bodyPanel, 1.0f); 
   	}
	
    private Command runExplorer = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("檔案總管");
			bodyPanel.setContent(new explorer());
		}
    };
    
    private Command runSchedule = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("批次管理");
			bodyPanel.setContent(new ScheduleLayout());
		}
    };    
    
    private Command runEtlLog = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("ETL Log瀏覽");
			bodyPanel.setContent(new EtlLogLayout());
		}
    };  
    /*
    private Command runActiviti = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("Activiti admin");
			bodyPanel.setContent(new ActivitiLayout());
		}
    }; 
    */     
    /*
    private Command runMaskCode = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("遮蔽程式碼管理");
			//bodyPanel.setContent(new MaskCodeLayout());
		}
    };     
    */
    /*
    private Command runChat = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("Chat");
			//bodyPanel.setContent(new shareChat());
		}
    };     
    */
    private Command logout = new Command() {
		public void menuSelected(MenuItem selectedItem) {
	    	session.setAttribute("User","");
	    	session.setAttribute("_hashUserAction",null);
	    	UI.getCurrent().getPage().reload();        				
		}
    };    
    
    private Command runCreatePrg = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("Create Prg");
			bodyPanel.setContent(new CreatePrg());
		}
    };
    /*
    private Command runJython = new Command() {
		public void menuSelected(MenuItem selectedItem) {
			bodyPanel.setCaption("Jython");
			bodyPanel.setContent(new PythonLayout());
		}
    };    
    */
    
}
