package com.scsb.vaadin;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import com.scsb.domain.HashSystem;
import com.scsb.domain.HashTrans;
import com.scsb.vaadin.composite.manager.ScsbManager;
import com.scsb.vaadin.ui.Header;
import com.scsb.vaadin.ui.LoginView;
import com.scsb.vaadin.ui.MainView;
import com.scsb.vaadin.ui.gc.gc5001;
import com.scsb.vaadin.ui.gc.gc6030;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;

/**
 * Main UI class
 */
@Theme("scsb_theme")
@Widgetset ( "com.scsb.addons.gwt.AddonsWidgetset" )
public class ScsbUI extends UI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user;
	public String      system_url="";
	boolean loginFailed = false;
	protected static HashSystem    hashSystem    	= HashSystem.getInstance();
	protected static HashTrans     hashTrans    	= HashTrans.getInstance();
	protected static Properties    systemProps 		= hashSystem.getProperties();
	protected ScsbManager manager = new ScsbManager();
	
	String SERVER_NAME="" ,errMsg="" ,defaultLanguage="" ,app_type="";
	
	protected void init(VaadinRequest request) {
		this.setHeight("100%");
		this.setWidth("100%");
		manager= new ScsbManager();
	
		//String servletPath = VaadinServlet.getCurrent()
		//	    .getServletContext().getContextPath() + VaadinServletService 
		//	    .getCurrentServletRequest().getServletPath();		
		//System.out.println("servletPath:"+servletPath);		
		
		WrappedSession session =getCurrent().getSession().getSession();
		//載入語系
		Object objLang=session.getAttribute("Language");
		if (objLang == null) session.setAttribute("Language", "tw");
		
		//載入Theme
		Object objTheme=session.getAttribute("myTheme");
		if (objLang != null){
			setTheme((String) objTheme);
		}else{
			session.setAttribute("myTheme","scsb_theme");
			setTheme("scsb_theme");
		}
		
		//使用參數直接登入者
		VaadinServletRequest req =(VaadinServletRequest) request;
		HttpServletRequest reqServlet = req.getHttpServletRequest();
		Hashtable<String,String> hashRequest =new Hashtable<String,String>();
		for (Enumeration<String> enu =reqServlet.getParameterNames();enu.hasMoreElements();){
			String reqName =enu.nextElement();
			hashRequest.put(reqName, reqServlet.getParameter(reqName));
		}

		//msg_id:gc5001 使用email直接登入查看訊息者
		if ("gc5001".equals(hashRequest.get("msg_id"))){
			//UI.getCurrent().addWindow(new gc5001(hashRequest.get("msg_code")));
			setContent(new gc5001(hashRequest.get("msg_code")));
		}else{
			//檢查有無登入
			if (session.getAttribute("PassOK") != null){
				if ("MustChange".equals(session.getAttribute("PassOK"))){
					setContent(new gc6030().getView());
				}else{
					Header mainTop=new Header();
				    mainTop.vLayout.setHeight("100%");
				    mainTop.vLayout.setWidth("100%");
				    mainTop.vLayout.setId("mainTop");
				    mainTop.setManager(manager);
				    
					MainView mainView =new MainView("DW");
					manager.navigateTo(mainView.getView());
					
					VerticalSplitPanel rootLayout =new VerticalSplitPanel();
					rootLayout.setSizeFull();
					rootLayout.setLocked(false);
					rootLayout.setSplitPosition(45,Unit.PIXELS);// 100px :UNITS_PIXELS=0
					rootLayout.setFirstComponent(mainTop.vLayout);	
					rootLayout.setSecondComponent(manager);
	
					setContent(rootLayout);
				}		
			}else{
				LoginView loginView =new LoginView();
				loginView.vLayout.setSizeFull();		
		        setContent(loginView.vLayout);
			}				
		}
	}
	
    public void logout() {
        // Close the VaadinServiceSession
        this.getSession().close();

        // Invalidate underlying session instead if login info is stored there
         VaadinService.getCurrentRequest().getWrappedSession().invalidate();

        // Redirect to avoid keeping the removed UI open in the browser
        this.getPage().setLocation(systemProps.getProperty("SYSTEM_PATH"));
    }
	
    public static ScsbUI getApp() {
        return (ScsbUI) UI.getCurrent();
    }

    public String getUser() {
        return user;
    }
    public ScsbManager  getScsbManager() {
        return manager;
    }    

    public void setUser(String user) {
        this.user = user;
    }	
	public String getSessionXML(){
		String sXML="";
		WrappedSession session =this.getSession().getSession();
		for (java.util.Iterator<String> iter=session.getAttributeNames().iterator();iter.hasNext();){
			String sessionName=iter.next();
			Object obj =session.getAttribute(sessionName);
			String objName=obj.getClass().toString()+"";
			sXML+="<var_set type=\""+objName+"\" name=\""+sessionName+"\" objtype=\"S\" >\n";
			sXML+="<var_data> "+obj+" </var_data>\n";
			sXML+="</var_set>\n";
		}
		return sXML;
	}
	
}