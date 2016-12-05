package com.scsb.vaadin.include;

import com.scsb.db.bean.ActionItem;
import com.scsb.db.bean.CcCode;
import com.scsb.db.bean.Roles;
import com.scsb.db.bean.Fnbct0;
import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.service.ActionItemService;
import com.scsb.db.service.CcCodeService;
import com.scsb.db.service.RolesService;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.OptionGroup;

@SuppressWarnings("serial")
/**
 * 取得 公用的群組元件
 */
public class UtilOptionGroup extends ScsbGlob {
	public UtilOptionGroup(){
	}
	
	/**
	 * 可執行動作設定
	 * @return OptionGroup
	 */
	public OptionGroup actionOptionGroup(){
		OptionGroup actionItemList =new OptionGroup();		
		ActionItemService actionSrv =new ActionItemService();
		BeanContainer<String,ActionItem> actionContainer =actionSrv.getActionItem_All();
		actionItemList.setContainerDataSource(actionContainer);
    	actionItemList.setImmediate(true);
    	actionItemList.setItemCaptionPropertyId("actionName");
    	actionItemList.setMultiSelect(true);
    	actionItemList.setCaption(i18nProps.getProperty("actionCode","可執行動作設定"));
		return actionItemList;
	}	
	
	/**
	 * 區域列表
	 * @return
	 */
	public OptionGroup getAreaOptionGroup(boolean haveZZ){
		//全部區域
		CcCodeService ccCodeSrv =new CcCodeService();
		BeanContainer<String,CcCode> ccCodeContainer =ccCodeSrv.getCcCode_Kind_All("011");
		ccCodeContainer.sort(new Object[]{"codeId"},new boolean[]{true});
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
	    for (int i=0;i<ccCodeContainer.size();i++) {
			BeanItem<CcCode> beanitem = ccCodeContainer.getItem(ccCodeContainer.getIdByIndex(i));
			CcCode ccCode =beanitem.getBean();
			String CODE_ID =ccCode.getCodeId();
			String CODE_NAME =ccCode.getCodeName();
			optionGroup.addItem(CODE_ID);
			optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
	    }//for
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);
		
		return optionGroup;
	}	
	
	/**
	 * 分行列表
	 * @return
	 */
	public OptionGroup getBrhOptionGroup(boolean haveZZ){
		//全部分行
		Fnbct0Service fnbct0Srv =new Fnbct0Service();
		BeanContainer<String,Fnbct0> fnbct0Container =fnbct0Srv.getSasFnbct0_All();
		fnbct0Container.sort(new Object[]{"brhCod"},new boolean[]{true});
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
	    for (int i=0;i<fnbct0Container.size();i++) {
			BeanItem<Fnbct0> beanitem = fnbct0Container.getItem(fnbct0Container.getIdByIndex(i));
			Fnbct0 sasFnbct0 =beanitem.getBean();
			String CODE_ID =sasFnbct0.getBrhCod();
			String CODE_NAME =sasFnbct0.getChinAl1();
			optionGroup.addItem(CODE_ID);
			optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
	    }//for
	    //加一個ZZ.全行
	    if (haveZZ){
	    	optionGroup.addItem("ZZ");
	    	optionGroup.setItemCaption("ZZ", "ZZ 全行");
	    }
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);
		
		return optionGroup;
	}		
	
	/**
	 * 全部角色
	 * @return
	 */
	public OptionGroup getRolesOptionGroup(){
		//全部角色
		RolesService rolesSrv =new RolesService();
		BeanContainer<String,Roles> rolesContainer =rolesSrv.getRoles_All();
		rolesContainer.sort(new Object[]{"roleid"},new boolean[]{true});
		OptionGroup optionGroup = new OptionGroup();
        for (int i=0;i<rolesContainer.size();i++) {
    		BeanItem<Roles> beanitem = rolesContainer.getItem(rolesContainer.getIdByIndex(i));
    		Roles roles =beanitem.getBean();
    		String CODE_ID =roles.getRoleid();
    		String CODE_NAME =roles.getRolename();
    		optionGroup.addItem(CODE_ID);
    		optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
        }//for
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		optionGroup.setHtmlContentAllowed(true);
		
		return optionGroup;
	}	
	
	/**
	 * 細項功能列表
	 * @return
	 */
	public OptionGroup getTransOptionGroup(){
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.setMultiSelect(true);
		optionGroup.setImmediate(true);
		//明細功能 for UsersD
        BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
        for(int i=0;i<transContainer.size();i++){
        	Trans bean=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
        	BeanContainer<String,Transd> transdContainer = hashTrans.getAllTransD(bean.getGroupid());
        	for(int j=0;j<transdContainer.size();j++){
				Transd transdBean=transdContainer.getItem(transdContainer.getIdByIndex(j)).getBean();;
	        	if (transdBean.getSetmode().equals("Y")){
		    		String CODE_ID =transdBean.getProgramid();
		    		String CODE_NAME =transdBean.getProgramname();
		    		optionGroup.addItem(CODE_ID);
		    		optionGroup.setItemCaption(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
	        	}	
        	}
		}//for
		
		return optionGroup;
	}	
	
}