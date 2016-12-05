package com.scsb.vaadin.ui.gc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.omg.CORBA.SystemException;

import com.scsb.db.bean.Ldapou;
import com.scsb.db.bean.Users;
import com.scsb.db.service.LdapouService;
import com.scsb.db.service.UsersService;
import com.scsb.db.service.UsersaService;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ScsbGlobView;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;

/**
 * 組織圖
 * @author 34711
 *
 */
@SuppressWarnings("serial")
public class gc6040 extends ScsbGlobView {

    public gc6040() {
		String className =this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".")+1);
		if (className.indexOf("_") > -1) className=className.substring(0,className.indexOf("_"));
		this.lang =session.getAttribute("Language")+"";
		loadI18N(lang,className);
		
		List orgList = new ArrayList();
		HierarchicalContainer container = createTreeContent(orgList);

		Tree tree = new Tree("SCSB", container);
		tree.addStyleName("checkboxed");
		tree.setSelectable(false);
		tree.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		tree.setItemCaptionPropertyId("name");
		tree.setItemIconPropertyId("icon");
		tree.setWidth("100%");

		tree.addItemClickListener(
				new ItemClickListener() {
					@Override
					public void itemClick(ItemClickEvent event) {
						event.getItemId();
						
					}

        		});
		
    	Panel panel = new Panel(i18nProps.getProperty("OrgTree","組織圖"));
    	panel.setContent(tree);
    	panel.setSizeFull();

    	getView().setContent(panel);
    	
    }
    
    public static HierarchicalContainer createTreeContent(List oTrees)
    		throws SystemException {

    		HierarchicalContainer container = new HierarchicalContainer();
    		container.addContainerProperty("name", String.class, "");
    		container.addContainerProperty("icon", Resource.class, null);
    		container.addContainerProperty("haveLoad", String.class, "N");

    		new Object() {

    			@SuppressWarnings("unchecked")
    			public void put(List data, HierarchicalContainer container)
    				throws SystemException {

    				LdapouService ldapsrv =new LdapouService();
    				BeanContainer<String,Ldapou> ldapContainer =ldapsrv.getSasLdapou_All();
    				
    				Ldapou rootBean =new Ldapou();
    				rootBean.setOu("root");
    				rootBean.setOu6digit("root");
    				rootBean.setParentou("");
    				rootBean.setDescription("SCSB");
    				ldapContainer.addBean(rootBean);
    				
    				for(java.util.Iterator<String> iter=ldapContainer.getItemIds().iterator() ; iter.hasNext();){
    					String id =iter.next();
    					Ldapou ldapBean =ldapContainer.getItem(id).getBean();
    					String orgId =ldapBean.getOu6digit();
    					
    					//將全部組織(parentou無值的)預設到上海銀行下
    					if ( ldapBean.getParentou().equals("")){
    						ldapBean.setParentou("root");
    					}
    					
    					//99.業外單位
    					if (orgId.substring(0,2).equals("99") ){
    						ldapBean.setParentou("scsb04");
    					}
    					if (!container.containsId(orgId)) {
    						container.addItem(orgId);
    						container.getItem(orgId).getItemProperty("name").setValue("【"+ldapBean.getOu6digit()+"】"+ldapBean.getDescription());
    						container.getItem(orgId).getItemProperty("icon").setValue(	new ThemeResource("./images/User-Group-icon.png"));
    						container.setChildrenAllowed(orgId, true);

        					//起始(SCSB)
        					if (orgId.equals("root")){
        						container.setParent(orgId, null);
        						container.getItem(orgId).getItemProperty("icon").setValue(	new ThemeResource("./images/home-icon.png"));
        					}else{
        						if (!container.containsId( ldapBean.getParentou())) {
    								List sub = new ArrayList();
    								sub.add( ldapBean.getParentou());
    								put(sub, container);
    							}
        						container.setParent(orgId, ldapBean.getParentou());
        					}	
    					}    					
    				}//for    
    			}
    		}.put(oTrees, container);

    		
			//補入全行行員 ,因為不用考慮有沒有子層,所以抽出來做效能比較好
			UsersService usersSrv =new UsersService();
			BeanContainer<String,Users> usersContainer =usersSrv.getUsers_All();
			usersContainer.sort(new Object[]{"userId"}, new boolean[]{true});
			for(java.util.Iterator<String> iter=usersContainer.getItemIds().iterator() ; iter.hasNext();){
				String id =iter.next();
				Users usersBean =usersContainer.getItem(id).getBean();
				String userId =usersBean.getUserid();
				String deptId =usersBean.getDeptid();
				if (!container.containsId(userId)) {
					container.addItem(userId);
					container.getItem(userId).getItemProperty("name").setValue("【"+userId+"】"+usersBean.getUserName());
					container.getItem(userId).getItemProperty("icon").setValue(	new ThemeResource("./images/User-Coat-Green-icon.png"));
					container.setChildrenAllowed(userId, false);
					container.setParent(userId, deptId);
				}
			}//for    		
    		
    		return container;
    	}    
   
}



