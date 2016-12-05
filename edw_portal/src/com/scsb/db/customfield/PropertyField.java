package com.scsb.db.customfield;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class PropertyField extends CustomField<FieldConstraint> {
	protected ComboBox oComboFieldType = new ComboBox();
	protected ComboBox oComboRequired = new ComboBox();
	protected TextField oCcCodeKind = new TextField();
	protected Button buBrowse =new Button();
	protected HorizontalLayout hlayout = new HorizontalLayout();
	protected FormLayout  propLayout =new FormLayout();
	
	protected FieldGroup binder;
	protected BeanItem<FieldConstraint> beanItem;
	protected FieldConstraint bean;
	protected Item item;
	public PropertyField(){
		this(null);
	}
	public PropertyField(Item item){
		this.item=item;
		propLayout.setSizeUndefined();
		propLayout.setWidth("100%");
		propLayout.setMargin(true);
		buBrowse.setCaption("...");
		buBrowse.setStyleName("small default");
		buBrowse.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
		    	//建立子視窗
				Window window =new Window();
				window.setModal(true);
				window.setWidth("460px");
				window.setHeight("500px");
		    	window.setContent(propLayout);				
		    	UI.getCurrent().addWindow(window);    					
			}			
		});	
		hlayout.setSizeUndefined();
        hlayout.addComponent(buBrowse);
        bean =new FieldConstraint();
        setPropLayout();
        setBinder();
	}
	
	private void setPropLayout(){
		Label label =new Label();
		if (item != null){
			label.setValue(item.getItemProperty("BEAN_NAME").getValue()
					+" / "
					+item.getItemProperty("REMARKS").getValue()
					);
		}
		propLayout.addComponent(label);
		propLayout.setComponentAlignment(label, Alignment.TOP_LEFT);
		//請選擇欄位型態
		oComboFieldType.setSizeUndefined();
		oComboFieldType.setCaption("請選擇欄位型態：");
		oComboFieldType.setNullSelectionAllowed(false);
		oComboFieldType.setImmediate(true);
		
		oComboFieldType.addItem("TextField");
		oComboFieldType.setItemCaption("TextField", "TextField(預設值)");
		
		oComboFieldType.addItem("NumberText");
		oComboFieldType.setItemCaption("NumberText", "NumberText(整數位數) ");
		
		oComboFieldType.addItem("DoubleText");
		oComboFieldType.setItemCaption("DoubleText", "DoubleText(有小數位數) ");

		oComboFieldType.addItem("DateField");
		oComboFieldType.setItemCaption("DateField", "DateField(日期格式) ");		
		
		oComboFieldType.addItem("ComboBox");
		oComboFieldType.setItemCaption("ComboBox", "ComboBox(內容自定) ");
		
		oComboFieldType.addItem("YNComboBox");
		oComboFieldType.setItemCaption("YNComboBox", "YNComboBox(Yes/No) ");
		
		//oComboFieldType.addItem("CcCodeCombox");
		//oComboFieldType.setItemCaption("CcCodeCombox", "CcCodeCombox(記得定義Code_Kind) ");
		
		oComboFieldType.select("TextField");
		oComboFieldType.addValueChangeListener(ccCodelistener);
		propLayout.addComponent(oComboFieldType);
		propLayout.setComponentAlignment(oComboFieldType, Alignment.TOP_LEFT);
		
		//CcCodeKind value
		oCcCodeKind.setSizeUndefined();
		oCcCodeKind.setCaption("請輸入CODE_KIND：");
		oCcCodeKind.setImmediate(true);
		oCcCodeKind.setVisible(false);
		propLayout.addComponent(oCcCodeKind);
		propLayout.setComponentAlignment(oCcCodeKind, Alignment.TOP_LEFT);		
		
		//是否為必須有值
		oComboRequired.setSizeUndefined();
		oComboRequired.setCaption("是否為必須有值：");
		oComboRequired.setNullSelectionAllowed(false);
		oComboRequired.setImmediate(true);
		oComboRequired.addItem("N");
		oComboRequired.setItemCaption("N", "No");
		oComboRequired.addItem("Y");
		oComboRequired.setItemCaption("Y", "Yes");
		oComboRequired.select("N");
		propLayout.addComponent(oComboRequired);
		propLayout.setComponentAlignment(oComboRequired, Alignment.TOP_LEFT);		
	}
	Property.ValueChangeListener ccCodelistener = new Property.ValueChangeListener() {
      	public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
              String selected = (String) event.getProperty().getValue();
              if (selected.equals("CcCodeCombox")) oCcCodeKind.setVisible(true);
              else  oCcCodeKind.setVisible(false);
          }
	};	
	
	private void setBinder(){
        beanItem = new BeanItem<FieldConstraint>(bean);        
        binder = new FieldGroup(beanItem);
        binder.setBuffered(true);
        binder.bind(oComboFieldType, "fieldType");		
        binder.bind(oCcCodeKind		,  "ccCodeKind");
        binder.bind(oComboRequired,  "fieldRequired");
        
        
	}
	
    public Class<FieldConstraint> getType() {
        return FieldConstraint.class;
    }
   
    public ComboBox getComboFieldType() {
        return oComboFieldType;
    }    
 
	@Override
	protected Component initContent() {
		return hlayout;
	}
	
	public FieldConstraint getBean(){
		try {
			binder.commit();
		} catch (CommitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	public void setBean(FieldConstraint srcBean){
		try {
			BeanUtils.copyProperties(bean, srcBean);
			setBinder();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * 組欄位字串
	 * @param runBean
	 * @return
	 */
	public static String setString4PropertyField(String sREMARK ,String sField ,FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";
		sProp+="  		//"+sREMARK+" "+BR;
		sProp+="	    if (\""+sField+"\".equals(pid)) {"+BR;
		if (runBean.getFieldType().equals("TextField"))		sProp+=getTextFieldString(runBean);
		if (runBean.getFieldType().equals("NumberText"))	sProp+=getNumberTextString(runBean);
		if (runBean.getFieldType().equals("DoubleText"))    sProp+=getDoubleTextString(runBean);
		if (runBean.getFieldType().equals("YNComboBox"))	sProp+=getYNComboBoxString(runBean);
		if (runBean.getFieldType().equals("ComboBox"))		sProp+=getComboBoxString(runBean);
		if (runBean.getFieldType().equals("DateField"))		sProp+=getDateFieldString(runBean);
		if (runBean.getFieldType().equals("CcCodeCombox"))	sProp+=getCcCodeComboxString(runBean);
		
		sProp+="	    }"+BR;		
		return sProp;
	}
	private static String getTextFieldString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	TextField oText =new TextField(pname);"+BR;
		sProp+="	    	oText.setId(pid);"+BR;		
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oText.setRequired(true);"+BR;
		}
		sProp+="	    	oText.setImmediate(true);"+BR;
		sProp+="	    	return oText;	"+BR;
		return sProp; 
	}	
	private static String getNumberTextString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	NumberText oText =new NumberText(\"0\");"+BR;
		sProp+="	    	oText.setId(pid);"+BR;
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oText.setRequired(true);"+BR;
		}
		sProp+="	    	oText.setCaption(pname);"+BR;
		sProp+="	    	oText.setImmediate(true);"+BR;
		sProp+="	    	return oText;	"+BR;
		return sProp; 
	}
	private static String getDoubleTextString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	DoubleText oText =new DoubleText(\"0\");"+BR;
		sProp+="	    	oText.setId(pid);"+BR;		
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oText.setRequired(true);"+BR;
		}
		sProp+="	    	oText.setCaption(pname);"+BR;
		sProp+="	    	oText.setImmediate(true);"+BR;
		sProp+="	    	return oText;	"+BR;
		return sProp; 
	}	
	private static String getYNComboBoxString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	ComboBox oCombo = new ComboBox();"+BR;
		sProp+="	    	oCombo.setCaption(pname);"+BR;
		sProp+="	    	oCombo.setImmediate(true);"+BR;
		sProp+="	    	oCombo.setId(pid);"+BR;
		sProp+="	    	oCombo.addItem(\"\");"+BR;
		sProp+="	    	oCombo.addItem(\"Y\");"+BR;
		sProp+="	    	oCombo.addItem(\"N\");"+BR;
		sProp+="	    	oCombo.setItemCaption(\"\",loanMessage.getString(\"message_select_one\"));"+BR;
		sProp+="	    	oCombo.setItemCaption(\"Y\",screenProps.getProperty(\"array_FlgY\"));"+BR;
		sProp+="	    	oCombo.setItemCaption(\"N\",screenProps.getProperty(\"array_FlgN\"));"+BR;
		sProp+="	    	oCombo.setNullSelectionAllowed(false);	"+BR;
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oCombo.setRequired(true);"+BR;
		}		
		sProp+="	    	return oCombo;	"+BR;
		return sProp; 
	}
	private static String getComboBoxString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	ComboBox oCombo = new ComboBox();"+BR;
		sProp+="	    	oCombo.setCaption(pname);"+BR;
		sProp+="	    	oCombo.setImmediate(true);"+BR;
		sProp+="	    	oCombo.setId(pid);"+BR;
		sProp+="	    	oCombo.addItem(\"\");"+BR;
		sProp+="	    	oCombo.setItemCaption(\"\",loanMessage.getString(\"message_select_one\"));"+BR;
		sProp+="			//TODO 請自行增加內容;"+BR;
		sProp+="	    	oCombo.setNullSelectionAllowed(false);	"+BR;
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oCombo.setRequired(true);"+BR;
		}		
		sProp+="	    	return oCombo;	"+BR;
		return sProp; 
	}	
	
	private static String getDateFieldString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";		
		sProp+="	    	NewDateField oDate =new NewDateField(loanMessage.getString(\"String.DateFormat.Error\"));"+BR;
		sProp+="	    	oDate.setId(pid);"+BR;
		sProp+="	    	oDate.setCaption(pname);"+BR;
		sProp+="	    	oDate.setImmediate(true);"+BR;
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oDate.setRequired(true);"+BR;
		}		
		sProp+="	    	return oDate;"+BR;
		return sProp; 
	}
	
	private static String getCcCodeComboxString(FieldConstraint runBean){
		String BR="\r\n";
		String sProp="";
		sProp+="	    	ComboBox oCombo =oCombo"+runBean.getCcCodeKind()+".show();"+BR;
		sProp+="	    	oCombo.setId(pid);"+BR;
		sProp+="	    	oCombo.setCaption(pname);"+BR;
		if (runBean.getFieldRequired().equals("Y")){
			sProp+="	    	oCombo.setRequired(true);"+BR;
		}		
		sProp+="	    	return oCombo;"+BR;
		return sProp; 
	}	
}