package com.scsb.crpro.maskcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.beanutils.BeanUtils;
import org.vaadin.teemu.jsoncontainer.JsonContainer;

import com.scsb.crpro.CrproLayout;
import com.scsb.crpro.DBService;
import com.scsb.db.customfield.FieldConstraint;
import com.scsb.db.customfield.PropertyField;
import com.scsb.db.customfield.TextField4;
import com.scsb.crpro.fileio.INIReader;
import com.scsb.util.IO;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class ChooseTable extends CrproLayout{

	private static final long serialVersionUID = 3325233253592861773L;

	DBService dbSer = new DBService();
	public ComboBox dbPoolCombo = new ComboBox("");
	public ComboBox dataCombo = new ComboBox("");
	public IndexedContainer container =new IndexedContainer();
	public Table    table =new Table();
	
	public ChooseTable(String comboName){
		dataCombo.setCaption(comboName);
		OptionGroup KEY =new OptionGroup("",Arrays.asList(new String[]{"BeanKey","PrimaryKey"}));
		KEY.setMultiSelect(true);

		//宣告一組Combox for 欄位使用
		ComboBox fieldCombox =new ComboBox();
		
		//定義欄位
		container.addContainerProperty("COLUMN_NAME"	, TextField.class	,new TextField());
		container.addContainerProperty("KEY"			, OptionGroup.class	,KEY);
		container.addContainerProperty("BEAN_NAME"		, TextField.class	,new TextField());
		container.addContainerProperty("REMARKS"		, TextField.class	,new TextField());
		//container.addContainerProperty("QueryForm"		, TextField4.class	,new TextField4());
		container.addContainerProperty("QueryField"		, PropertyField.class		,new PropertyField(null));
		//container.addContainerProperty("DataForm"		, TextField4.class	,new TextField4());
		//container.addContainerProperty("DataField"		, PropertyField.class		,new PropertyField(null));
		container.addContainerProperty("DATA_TYPE"		, TextField.class	,new TextField());
		container.addContainerProperty("COLUMN_SIZE"	, TextField.class	,new TextField());
		container.addContainerProperty("DECIMAL_DIGITS"	, TextField.class	,new TextField());
		
		//選擇dbpool
		setDBPool();
		dbPoolCombo.setId("dbPoolCombo");
		dbPoolCombo.setCaption("DB Pool");
	    dbPoolCombo.addValueChangeListener(new ValueChangeListener(){
			@Override
			public void valueChange(ValueChangeEvent event) {
			    //選擇Table
			    setTable();
			}
	    });
	    dataCombo.setId("dataCombo");
	    
	    addComponent(dbPoolCombo);
	    addComponent(dataCombo);			
	    
	    Button button_Reload = new Button();
	    button_Reload = new Button("載入欄位");
	    //button_Reload.setStyleName("small default");
	    button_Reload.addClickListener(new  Button.ClickListener(){
			public void buttonClick(ClickEvent event) {
		        container =setField(dataCombo.getValue()+"");
		        table.setContainerDataSource(container);
		        table.setPageLength(container.size());
			}
		});
	    //載入欄位
	    addComponent(button_Reload);
	    //欄位列表
	    table.setSelectable(true);
	    table.setImmediate(true);        
	    //table.setStyleName("striped");        
	    table.setSizeFull();
	    table.setId("table");
	    addComponent(table);		 
	}
	
	/**
	 * table list combobox
	 */
	public void setDBPool(){
	    //db列表
    	ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
		String jsonString =IO.read(servletContext.getRealPath("/")+systemProps.getProperty("ConnectionPoolJson"));
		 JsonContainer jsonData= JsonContainer.Factory.newInstance(jsonString);		
		
		 dbPoolCombo.setNullSelectionAllowed(false);
		 int idb=0;
		 for(int i=0;i<jsonData.size();i++){
	        	Item jsonItem=jsonData.getItem(jsonData.getIdByIndex(i));
	        	String running=(String)jsonItem.getItemProperty("Running").getValue();
	        	if (running.toUpperCase().equals("TRUE")  ){
		        	String dbpool=(String) jsonItem.getItemProperty("DBPool").getValue();
		        	String dbowner=(String)jsonItem.getItemProperty("DBOwner").getValue();
		        	String key =dbpool+"!@!"+dbowner;
					 Item item=dbPoolCombo.addItem(key);
					 dbPoolCombo.setItemCaption(key, dbpool+"("+dbowner+")");
					//一進去,先連第一個位置
					if (idb ==0){
						dbPoolCombo.select(key);
					}
					idb++;
	        	}
			}//for	
		    //第一次載入選擇Table
		    setTable();
	}	
	
	/**
	 * table list combobox
	 * @param theTable
	 * @return
	 */
	public void setTable(){
		dataCombo.removeAllItems();
	    //Table列表
		String value=(String)dbPoolCombo.getValue();
		String[] arrString  =value.split("!@!");
		String dbpool=arrString[0];
		String dbowner=arrString[1];
	    IndexedContainer container = dbSer.getTableList(dbpool ,dbowner);    
        for (int i = 0; i < container.size(); i++) {
        	dataCombo.addItem(container.getItem(i).getItemProperty("TABLE_NAME").getValue());
        }
	}
	
	/**
	 * field list combobox
	 * @param tableName
	 * @return
	 */
	public IndexedContainer setField(String tableName){
		String value=(String)dbPoolCombo.getValue();
		String[] arrString  =value.split("!@!");
		String dbpool=arrString[0];
		String dbowner=arrString[1];
		
	    IndexedContainer container = dbSer.getFieldList(dbpool , dbowner,tableName ,this.container);
        return container;
	}
	
	@SuppressWarnings("unchecked")
	public void setObjValue(INIReader iniReader ,String sTable){
		HashMap<String, String> HashData =iniReader.Read(sTable);
		dataCombo.setValue(HashData.get("dataCombo")+"");		
		if (HashData.get("Table") != null){
			String tableName =HashData.get("Table")+"";
			if (tableName.equals("")) return;
			HashMap<String, String> HashTable =iniReader.Read(tableName);
			int iRows = Integer.parseInt(HashTable.get("TableRows")+"");
			
			//先定好table的欄位
			container.removeAllItems();
			for (int i=1;i<=iRows;i++){
				Item item =container.addItem(HashTable.get("COLUMN_NAME."+i));
				item.getItemProperty("COLUMN_NAME").setValue(setTextField(HashTable.get("COLUMN_NAME."+i)+"" ,true));
				item.getItemProperty("BEAN_NAME").setValue(setTextField(HashTable.get("BEAN_NAME."+i)+"" ,false));
				item.getItemProperty("REMARKS").setValue(setTextField(HashTable.get("REMARKS."+i)+"" ,false));
				item.getItemProperty("DATA_TYPE").setValue(setTextField(HashTable.get("DATA_TYPE."+i)+"" ,true));
				item.getItemProperty("COLUMN_SIZE").setValue(setTextField(HashTable.get("COLUMN_SIZE."+i)+"" ,true));
				item.getItemProperty("DECIMAL_DIGITS").setValue(setTextField(HashTable.get("DECIMAL_DIGITS."+i)+"" ,true));
				//key & QuerForm & DetailForm特殊處理
				item.getItemProperty("KEY").setValue(setKey(HashTable.get("KEY."+i)+""));
				//item.getItemProperty("QueryForm").setValue(setQueryForm(HashTable.get("QueryForm."+i)+""));
				item.getItemProperty("QueryField").setValue(setPropertyField(HashTable  ,"QueryField."+i ,item));
				//item.getItemProperty("DataForm").setValue(setQueryForm(HashTable.get("DataForm."+i)+""));
				//item.getItemProperty("DataField").setValue(setPropertyField(HashTable ,"DataField."+i ,item));
			}
			table.setContainerDataSource(container);
	        //table.setPageLength(container.size());

		}
	}
	TextField setTextField(String value ,boolean ReadOnly){
		TextField tObj =new TextField();
		tObj.setValue(value);
		tObj.setReadOnly(ReadOnly);
		return tObj;
	}

	Hashtable<String, String> StringToMap(String value){
		Hashtable<String, String> map =new Hashtable<String, String>();
		
		value=value.replace("{","");
		value=value.replace("}","");
		String sTemp=value;
		int maxCount =sTemp.length();
		int i=0;		
		while(i<=maxCount){
			i++;
			String sKey="";
			String sValue="";
			int iChk=sTemp.indexOf("=");
			if (iChk >= 0){
				sKey =sTemp.substring(0, iChk).trim();
				sTemp=sTemp.substring(iChk+1);
				int iChk2=sTemp.indexOf(",");
				if (iChk2 >=0){
					sValue=sTemp.substring(0, iChk2).trim();
					sTemp=sTemp.substring(iChk2+1);
				}else{
					sValue=sTemp.trim();
					sTemp="";
				}			
				map.put(sKey,sValue);
			}else{
				i =maxCount+1;
			}//if
		}//while
		return map;
	}
	/**
	 * setKey
	 * @param value
	 * @return
	 */
	OptionGroup setKey(String value){
		OptionGroup KEY =new OptionGroup("",Arrays.asList(new String[]{"LogKey","PrimaryKey"}));
		KEY.setMultiSelect(true);
		String sKey =value;
		HashSet hset =new HashSet();
		if (sKey.indexOf("LogKey") >=0) hset.add("LogKey");
		if (sKey.indexOf("PrimaryKey") >=0) hset.add("PrimaryKey");
		KEY.setValue(hset);		
		return KEY;
	}
	/**
	 * setPropertyField 
	 * @param hashTable
	 * @param sType
	 * @param item
	 * @return
	 */
	PropertyField setPropertyField(HashMap hashTable ,String sType ,Item item){
		PropertyField propField =new PropertyField(item);
		FieldConstraint fieldCons =new FieldConstraint();
		HashMap beanMap =new HashMap();
		for (Iterator<String> iter =hashTable.keySet().iterator();iter.hasNext();){
			String key =iter.next();
			Object value =hashTable.get(key);
			if (key.indexOf(sType) > -1){
				String beanKey =key.substring(key.lastIndexOf(".")+1).trim();				
				beanMap.put(beanKey, value);
			}
		}
		if (beanMap.size() > 0){
			try {
				BeanUtils.copyProperties(fieldCons, beanMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		propField.setBean(fieldCons);
		return propField;
	}	
}
