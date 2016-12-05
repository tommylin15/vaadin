package com.scsb.crpro;

import javax.servlet.ServletContext;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WrappedSession;
import com.vaadin.ui.UI;

/**
 * Main UI class
 */
@Theme("valo")
public class CrproUI extends UI {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7158284432815068160L;
	
	@Override
	protected void init(VaadinRequest request) {
		this.setId("Crpro");

		WrappedSession session =this.getSession().getSession();
		String user =session.getAttribute("User")+"";
		if (user.equals("null") || user.equals("")){
			LoginForm layout = new LoginForm();
			setContent(layout);			
		}else{
			MainLayout layout = new MainLayout();
			setContent(layout);
		}
	}
}