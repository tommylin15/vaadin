package com.scsb.crpro.build.code;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import com.scsb.db.customfield.FieldConstraint;
import com.scsb.db.customfield.PropertyField;
import com.scsb.db.customfield.TextField4;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

public class OneFileCode {
	
	public String Code ="";
	
	String BR="\r\n";
	String serviceName="";
	String servicePackage="";
	String beanName="";
	String beanPackage="";
	String titleName="";
	String fileName="";
	String filePackage="";
	String tableName="";
	IndexedContainer container;	
	
	String columnName="";
	String fieldName ="";
	String fieldNameUp ="";
	String fieldType  ="";
	String DATA_TYPE ="";
	String DECIMAL_DIGITS ="";
	String PrimaryKey="";
	String BeanKey="";
	String LogKey="";
	FieldConstraint QueryField;
	FieldConstraint DataField;
	
	String isQueryKey="";
	Hashtable<String, Hashtable<String,String>> HashQuery =new Hashtable<String, Hashtable<String,String>>();
    int iMaxFormCol=0;
    int iMaxFormRow=0;
    
	String isDataFormKey="";
	Hashtable<String, Hashtable<String,String>> HashDataForm =new Hashtable<String, Hashtable<String,String>>();
    int iMaxDataFormCol=0;
    int iMaxDataFormRow=0;
	
	public OneFileCode(String serviceName ,String servicePackage 
			          ,String beanName ,String beanPackage
			          ,String fileName ,String filePackage
			          ,String titleName ,String tableName 			          
			          ,IndexedContainer container){
		
		this.serviceName=serviceName;
		this.servicePackage=servicePackage;
		this.beanName=beanName;
		this.beanPackage=beanPackage;
		this.fileName=fileName;
		this.titleName=titleName;
		this.filePackage=filePackage;
		this.tableName=tableName;
		this.container=container;
		
		String sCode="";
		if (container.size() > 0){
			sCode+="package "+filePackage+"; "+BR+BR;
			
			sCode+="import "+beanPackage+"."+beanName+";"+BR;
			sCode+="import "+servicePackage+"."+serviceName+";"+BR;
			
			sCode+="import java.util.Iterator;"+BR+BR;
			sCode+="import org.apache.commons.beanutils.BeanUtils;"+BR+BR;
			sCode+="import com.scsb.loan.composite.GridConstraints;"+BR;
			sCode+="import com.scsb.loan.composite.GridForm;"+BR;
			sCode+="import com.scsb.loan.composite.MessageBox;"+BR;
			sCode+="import com.scsb.loan.composite.SCSBvLayout;"+BR;
			sCode+="import com.scsb.loan.composite.MessageBox.ButtonType;"+BR;
			sCode+="import com.scsb.loan.composite.MessageBox.EventListener;"+BR;
			sCode+="import com.scsb.loan.include.ButtonLayout;"+BR;
			sCode+="import com.scsb.loan.include.ControlBar;"+BR;
			sCode+="import com.scsb.loan.include.DetailTabLayout;"+BR;
			sCode+="import com.scsb.loan.include.TitleLayout;"+BR;
			sCode+="import com.scsb.loan.util.FieldCheck;"+BR;
			sCode+="import com.scsb.loan.util.TraceLog;"+BR;
			sCode+="import com.scsb.util.DateUtil;"+BR+BR;
			//加上元件的import
			if (container.size() > 0){
				Hashtable hImport =new Hashtable();
				for(int i=0;i<container.size();i++){
					Item item=container.getItem(container.getIdByIndex(i));
					setItemValue(item);
					if (!this.QueryField.getFieldObj().equals("")){
						hImport.put(this.QueryField.getFieldType(), this.QueryField.getFieldObj());
					}
					if (!this.DataField.getFieldObj().equals("")){
						hImport.put(this.DataField.getFieldType(), this.DataField.getFieldObj());
					}					
				}
				for(Iterator iter=hImport.keySet().iterator();iter.hasNext();){
					sCode+="import "+hImport.get(iter.next())+";"+BR;
				}
			}			
			
			sCode+="import com.vaadin.data.Property;"+BR;
			sCode+="import com.vaadin.data.fieldgroup.FieldGroup;"+BR;
			sCode+="import com.vaadin.data.util.BeanContainer;"+BR;
			sCode+="import com.vaadin.data.util.BeanItem;"+BR;
			sCode+="import com.vaadin.server.ThemeResource;"+BR;
			sCode+="import com.vaadin.ui.*;"+BR;
			sCode+="import com.vaadin.ui.Button.ClickEvent;"+BR;
			sCode+="import com.vaadin.ui.TabSheet.Tab;"+BR;
			sCode+="import com.vaadin.ui.themes.Reindeer;"+BR+BR;			
			
			sCode+="/**"+BR;
			sCode+=" * "+titleName+""+BR;
			sCode+=" *"+BR;
			sCode+=" */"+BR;			
			sCode+="@SuppressWarnings(\"serial\")"+BR;	
			sCode+="public class "+fileName+" extends SCSBvLayout{"+BR;
			sCode+="	VerticalLayout       bodyLayout = new VerticalLayout();"+BR;
			sCode+="	"+serviceName+"    DataSrv;"+BR;
			sCode+="	"+beanName+"           QueryBean ;"+BR;
			sCode+="	BeanItem<"+beanName+"> QueryItem ;"+BR;
			sCode+="	FieldGroup         QueryBinder;"+BR;
			sCode+="	GridForm 		   QueryLayout;"+BR+BR;
			
			sCode+="	"+beanName+"           DetailBean ;"+BR;
			sCode+="	BeanItem<"+beanName+"> DetailItem ;"+BR;
			sCode+="	FieldGroup         DetailBinder;"+BR;
			sCode+="	GridForm 		   DetailLayout;"+BR+BR;
			
			sCode+="	BeanItem<"+beanName+"> oldBeanItem;"+BR;
			sCode+="	//TabSheet	"+BR;
			sCode+="	DetailTabLayout        tabLayout;"+BR;
			sCode+="	VerticalLayout         vQuery;"+BR;
			sCode+="	Tab 				   tQuery;"+BR;
			sCode+="	VerticalLayout 		   vDetailAction;"+BR;
			sCode+="	Tab 				   tDetailAction;"+BR;
			sCode+="	//上下筆"+BR;
			sCode+="	ControlBar		       controlBar;"+BR;
			sCode+="	Table                  DetailTable;"+BR;
			sCode+="	BeanContainer<String,  "+beanName+">    DetailData;"+BR+BR;

			//加上元件的有CC_Code的
			if (container.size() > 0){
				Hashtable hCcCode =new Hashtable();
				for(int i=0;i<container.size();i++){
					Item item=container.getItem(container.getIdByIndex(i));
					setItemValue(item);
					if (this.QueryField.getFieldType().equals("CcCodeCombox")){
						hCcCode.put(this.QueryField.getCcCodeKind(), this.QueryField.getCcCodeKind());
					}
					if (this.DataField.getFieldType().equals("CcCodeCombox")){
						hCcCode.put(this.DataField.getCcCodeKind(), this.DataField.getCcCodeKind());
					}					
				}
				for(Iterator iter=hCcCode.keySet().iterator();iter.hasNext();){
					String key =(String)iter.next();
					sCode+="	CcCodeCombox oCombo"+key+" 	;"+BR;
				}
			}			
			
			sCode+="	public "+fileName+"(){"+BR;
			sCode+="	}"+BR+BR;
			sCode+=		getCreate()+BR;
			sCode+=		getInit()+BR;
			sCode+=		getTableshowName()+BR;
			sCode+=		getQueryButton()+BR;
			sCode+=		getDetailButton()+BR;
			sCode+=		getDetailClearAction()+BR;
			sCode+=		getDetailDeleteAction()+BR;
			sCode+=		getDetailUpdateAction()+BR;
			sCode+=		getDetailInsertAction()+BR;
			sCode+=		getShowDetail()+BR;
			sCode+=		getDetailBean()+BR;
			sCode+=		getClearAction()+BR;
			sCode+=		getQueryAction()+BR;
			sCode+=		getSetDetailBean()+BR;
			sCode+=		getQueryForm()+BR;
			sCode+=		getQueryFactory()+BR;
			sCode+=		getDataForm()+BR;
			sCode+=		getDataFactory()+BR;
			sCode+="}"+BR;	
		}		
		this.Code=sCode;
	}
	
	//啟動主程式
	public String getCreate(){
		String sCode ="";
		sCode+="	public void Create(){"+BR;
		sCode+="		DataSrv = new "+beanName+"Service(defaultLanguage);"+BR;
		//加上元件的有CC_Code的
		if (container.size() > 0){
			Hashtable hCcCode =new Hashtable();
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				if (this.QueryField.getFieldType().equals("CcCodeCombox")){
					hCcCode.put(this.QueryField.getCcCodeKind(), this.QueryField.getCcCodeKind());
				}
				if (this.DataField.getFieldType().equals("CcCodeCombox")){
					hCcCode.put(this.DataField.getCcCodeKind(), this.DataField.getCcCodeKind());
				}					
			}
			for(Iterator iter=hCcCode.keySet().iterator();iter.hasNext();){
				String key =(String)iter.next();
				sCode+="		oCombo"+key+" 		=new CcCodeCombox(session ,\"\",\""+key+"\" ,true ,true );	"+BR;	
			}
		}		
		sCode+="		//body主頁"+BR;
		sCode+="		bodyLayout.setSizeUndefined();"+BR;
		sCode+="		bodyLayout.setWidth(\"100%\");"+BR+BR;

		sCode+="		//表頭名稱 "+BR;
		sCode+="		TitleLayout title =new TitleLayout(heading);"+BR;
		sCode+="		bodyLayout.addComponent(title);"+BR+BR;

		sCode+="		//Query Form "+BR;
		sCode+="		QueryCreate();"+BR;
		sCode+="		bodyLayout.addComponent(QueryLayout);"+BR;
		sCode+="		bodyLayout.setComponentAlignment(QueryLayout, Alignment.TOP_CENTER);"+BR;
		sCode+="		ButtonLayout masterButtons =new ButtonLayout(\"Q\");"+BR;
		sCode+="		masterButtons.getClearButton().addClickListener(buttonsListener);"+BR;
		sCode+="		masterButtons.getQueryButton().addClickListener(buttonsListener);"+BR;
		sCode+="		bodyLayout.addComponent(masterButtons);"+BR;
		sCode+="		bodyLayout.setComponentAlignment(masterButtons, Alignment.TOP_CENTER);"+BR+BR;

		sCode+="		//Detail Form"+BR;
		sCode+="		DetailCreate();"+BR+BR;

		sCode+="		//Detail Action"+BR;
		sCode+="		ButtonLayout detailButtons =new ButtonLayout(\"AUD\");"+BR;
		sCode+="		detailButtons.getClearButton().addClickListener(DetailbuttonsListener);"+BR;
		sCode+="		detailButtons.getInsertButton().addClickListener(DetailbuttonsListener);"+BR;
		sCode+="		detailButtons.getUpdateButton().addClickListener(DetailbuttonsListener);"+BR;
		sCode+="		detailButtons.getDeleteButton().addClickListener(DetailbuttonsListener);"+BR;
		sCode+="		detailButtons.removeButton(detailButtons.getQueryButton());"+BR;
		sCode+="		detailButtons.setWidth(\"100%\");"+BR+BR;

		sCode+="		//明細列表"+BR;
		sCode+="		DetailTable   = new Table();"+BR;
		sCode+="		DetailTable.setSizeFull();"+BR;
		sCode+="		DetailTable.setPageLength(0);"+BR;
		sCode+="		DetailTable.setId(\"DetailTable\");"+BR;
		sCode+="		DetailTable.setSelectable(true);"+BR;
		sCode+="		DetailTable.setImmediate(true); "+BR;
		sCode+="		DetailTable.setStyleName(\"striped\");"+BR+BR;

		sCode+="		tabLayout= new DetailTabLayout(screenProps.getProperty(\"tabbed_name\")); 	"+BR;
		sCode+="		tabLayout.setId(\"TabLayout\");"+BR+BR;
		 	
	    sCode+="		vQuery = new VerticalLayout();"+BR;
		sCode+="		vQuery.addComponent(DetailTable);"+BR+BR;
			
		sCode+="		//新增明細"+BR;
		sCode+="		Button button_insert = new Button();"+BR;
		sCode+="		button_insert.setDescription(screenProps.getProperty(\"detail_insertbutton\"));"+BR;
		sCode+="		button_insert.setIcon(new ThemeResource(\"images/add.gif\"));"+BR;
		sCode+="		button_insert.setId(\"DetailInsert\");"+BR;
		sCode+="		button_insert.addStyleName(Reindeer.BUTTON_LINK);"+BR;
		sCode+="		button_insert.addClickListener(new  Button.ClickListener(){"+BR;
		sCode+="			public void buttonClick(ClickEvent event) {"+BR;
		sCode+="				DetailBean =new "+beanName+"();"+BR;
		sCode+="				DetailBinderFactory();"+BR;
		sCode+="				setDetailComboBox();"+BR;
		sCode+="				tDetailAction.setVisible(true);"+BR;
		sCode+="				tabLayout.DetailTab.setSelectedTab(tDetailAction);"+BR;
		sCode+="				//新增模式關閉上下筆切換功能"+BR;
		sCode+="				controlBar.setVisible(false);"+BR;
		sCode+="			}"+BR;
		sCode+="		});"+BR+BR;

		sCode+="		vQuery.addComponent(button_insert);"+BR;
		sCode+="		tQuery   = tabLayout.DetailTab.addTab(vQuery ,screenProps.getProperty(\"detail_query\"));"+BR+BR;
		 	
		sCode+="		vDetailAction = new VerticalLayout();"+BR;
		sCode+="		//設定上下筆切換功能"+BR;
		sCode+="		controlBar    =new ControlBar(session) ;"+BR;
		sCode+="		//上下筆切換====begin=========================================="+BR;
		sCode+="		controlBar.nextBu.addClickListener(new  Button.ClickListener(){"+BR;
		sCode+="			public void buttonClick(ClickEvent event) {"+BR;
		sCode+="				controlBar.setNowRow(controlBar.iNowRow+1);"+BR;
		sCode+="				getDetailBean(DetailData.getItem(DetailData.getIdByIndex(controlBar.iNowRow-1)));"+BR;
		sCode+="			}"+BR;
		sCode+="		});"+BR+BR;

		sCode+="		//上下筆切換"+BR;
		sCode+="		controlBar.prevBu.addClickListener(new  Button.ClickListener(){"+BR;
		sCode+="			public void buttonClick(ClickEvent event) {"+BR;
		sCode+="				controlBar.setNowRow(controlBar.iNowRow-1);"+BR;
		sCode+="				getDetailBean(DetailData.getItem(DetailData.getIdByIndex(controlBar.iNowRow-1)));"+BR;
		sCode+="			}"+BR;
		sCode+="		});"+BR;
		sCode+="		//上下筆切換====end============================================"+BR;
		sCode+="		vDetailAction.addComponent(controlBar);"+BR;
		sCode+="		vDetailAction.setComponentAlignment(controlBar, Alignment.TOP_CENTER);"+BR;
		sCode+="		vDetailAction.addComponent(DetailLayout);"+BR;
		sCode+="		vDetailAction.addComponent(detailButtons);"+BR;
		sCode+="		vDetailAction.setComponentAlignment(detailButtons, Alignment.TOP_CENTER);"+BR;
		sCode+="		tDetailAction = tabLayout.DetailTab.addTab(vDetailAction ,screenProps.getProperty(\"detail_action\"));"+BR;
		sCode+="		bodyLayout.addComponent(tabLayout);"+BR;
		sCode+="		bodyLayout.setComponentAlignment(tabLayout,Alignment.TOP_CENTER);"+BR;
		sCode+="		setHeight(\"100%\");"+BR;
		sCode+="		setWidth(\"100%\");"+BR;
		sCode+="		addComponent(bodyLayout);"+BR;
		sCode+="		setComponentAlignment(bodyLayout, Alignment.TOP_CENTER);"+BR;
		sCode+="		//初始化"+BR;
		sCode+="		Init();"+BR+BR;
		sCode+=" 	}"+BR;		
		return sCode;
	}		
	
	//建立初始化
	public String getInit(){
		String sCode ="";
		String sLogData ="";
			
		if (container.size() > 0){
			//處理 LogKey
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
		        if (LogKey.indexOf(fieldName) >= 0){		        					
			        if (PrimaryKey.indexOf(fieldName) >= 0){
			        	sLogData+="		traceLog.addLogItem(\""+fieldName+"\",screenProps.getProperty(\"field_"+fieldName+"\"), true);"+BR;		        	
			        }else{
			        	sLogData+="		traceLog.addLogItem(\""+fieldName+"\",screenProps.getProperty(\"field_"+fieldName+"\"));"+BR;
			        }
		        }
			}
			sCode+="	/**"+BR;
			sCode+="	 * 資料初始化"+BR;
			sCode+="	 */"+BR;
			sCode+="	public void Init(){"+BR;
			sCode+="		//設定log欄位"+BR;
			sCode+="		//欄位名稱 ,說明 ,是否為Key"+BR;
			sCode+="		traceLog =new TraceLog(ui);"+BR;
			sCode+=sLogData;
			sCode+="		//QueryLayout"+BR;
			sCode+="		QueryBean = new "+beanName+"();"+BR;
			sCode+="		QueryBinderFactory();"+BR+BR;

			sCode+="		//DetailLayout"+BR;
			sCode+="		DetailBean = new "+beanName+"();"+BR;
			sCode+="		DetailBinderFactory();"+BR;
			sCode+="		setDetailComboBox();"+BR+BR;
					
			sCode+="		DetailData =new BeanContainer<String, "+beanName+">("+beanName+".class);"+BR;
			sCode+="		DetailData.setBeanIdProperty(\"beanKey\");"+BR;
			sCode+="		DetailData.removeAllItems();"+BR;
			sCode+="		showDetailTable();	"+BR;
			sCode+="		tDetailAction.setVisible(false);"+BR;
			sCode+="	}"+BR;
		}
		return sCode;
	}
	
	//定義明細table的Combox中文說明
	public String getTableshowName(){
		String sCode ="";
		sCode+="	/**"+BR;
		sCode+="	 * 定義明細table的Combox中文說明"+BR;
		sCode+="	 */"+BR;
		sCode+="	void setTableshowName(){"+BR;
		sCode+="		//TODO 定義明細table的Combox中文說明"+BR;
		sCode+="		/*example"+BR;
		sCode+="		ccCodeUtil.setTableFieldName(DetailData, \"caseKndName\" ,\"caseKnd\" , oComboPC_066);"+BR;
		sCode+="		*/"+BR;
		sCode+="	}	"+BR;
		sCode+="	/**"+BR;
		sCode+="	 * 資料修改的ComboBox設定"+BR;
		sCode+="	 */"+BR;
		sCode+="	public void setDetailComboBox(){"+BR;
		sCode+="		//TODO 定義ComboBox的Listener"+BR;
		sCode+="	}"+BR;		
		return sCode;
	}
	
	//建立Query Button
	public String getQueryButton(){
		String sCode ="";
		sCode+="	//===Query Button Begin====================================================================="+BR;
		sCode+="	Button.ClickListener buttonsListener =new  Button.ClickListener(){"+BR;
		sCode+="	    public void buttonClick(ClickEvent event) {"+BR;
		sCode+="	    	cpuStartTime = System.currentTimeMillis();"+BR;
		sCode+="	    	String actionMode =event.getButton().getId();"+BR;
		sCode+="        	String message=\"\";"+BR;
		sCode+="	    	  //清除"+BR;
		sCode+="	        if (actionMode.equals(\"Clear\")){"+BR;
		sCode+="	         	onClearButton();"+BR;
		sCode+="	         }"+BR;
		sCode+="	         //查詢"+BR; 
		sCode+="	         if (actionMode.equals(\"Query\")){"+BR;
		sCode+="	        	//檢核欄位"+BR;
		sCode+="	        	FieldCheck DetailFieldCheck =new FieldCheck();"+BR;
		sCode+="        		message =DetailFieldCheck.checkBinderField(QueryBinder, loanMessage, screenProps ,\"field\");"+BR;
		sCode+="              	//檢核無誤才執行"+BR;
		sCode+="	            if (message.equals(\"\")){"+BR;
		sCode+="	            	onQueryButton();"+BR;
		sCode+="	            }else{"+BR;
		sCode+="	    			new MessageBox(ui,msgTitle,message).show();"+BR;
		sCode+="	            }"+BR;
		sCode+="	        }"+BR;
		sCode+="	    }"+BR;
		sCode+="	 };"+BR;
		sCode+="	//===Query Button End====================================================================="+BR;	
		return sCode;
	}
	//建立Detail Button
	public String getDetailButton(){
		String sCode ="";
		sCode+="	//===Detail Button Begin====================================================================="+BR;
		sCode+="	Button.ClickListener DetailbuttonsListener =new  Button.ClickListener(){"+BR;
		sCode+="	    public void buttonClick(ClickEvent event) {"+BR;
		sCode+="	    	cpuStartTime = System.currentTimeMillis();"+BR;
		sCode+="	    	String actionMode =event.getButton().getId();"+BR;
		sCode+="	    	String message=\"\";"+BR;
		sCode+="	    	//清除明細"+BR;
		sCode+="	        if (actionMode.equals(\"Clear\")){"+BR;
		sCode+="	        	onDetailClearButton();"+BR;
		sCode+="	        }"+BR;
		sCode+="	        if (actionMode.equals(\"Insert\") || actionMode.equals(\"Update\")){"+BR;
		sCode+="	        	//檢核欄位"+BR;
		sCode+="	        	FieldCheck DetailFieldCheck =new FieldCheck();"+BR;
		sCode+="        		message =DetailFieldCheck.checkBinderField(DetailBinder, loanMessage, screenProps ,\"field\");"+BR;
		sCode+="	        }"+BR;
		sCode+="	        //明細新增"+BR;
		sCode+="	        if (actionMode.equals(\"Insert\")){"+BR;
		sCode+="	        	if (message.equals(\"\")){"+BR;
		sCode+="	        		new MessageBox(ui,"+BR;
		sCode+="		            	msgTitle,"+BR;
		sCode+="		                MessageBox.Icon.QUESTION,"+BR;
		sCode+="		                screenProps.getProperty(\"action_confirm\")+screenProps.getProperty(\"field_insertMode\")+\"?\","+BR;
		sCode+="		                new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, screenProps.getProperty(\"action_confirm\")),"+BR;
		sCode+="		                new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, screenProps.getProperty(\"action_cancel\"))"+BR;
		sCode+="					).show("+BR;
		sCode+="		                new EventListener() {"+BR;
		sCode+="			                public void buttonClicked(ButtonType buttonType) {"+BR;
		sCode+="			                    if (buttonType == MessageBox.ButtonType.YES) {"+BR;
		sCode+="			               			onDetailInsertButton();"+BR;
		sCode+="			               		}"+BR;
		sCode+="			               	}"+BR;
		sCode+="			            }"+BR;
		sCode+="		            );"+BR;
		sCode+="				}else new MessageBox(ui,msgTitle,message).show();"+BR;
		sCode+="	        }"+BR;
		sCode+="	        //明細修改"+BR;
		sCode+="	        if (actionMode.equals(\"Update\")){"+BR;
		sCode+="	        	if (message.equals(\"\")){"+BR;
		sCode+="	        		new MessageBox(ui,"+BR;
		sCode+="	                		msgTitle,"+BR;
		sCode+="	                        MessageBox.Icon.QUESTION,"+BR;
		sCode+="	                        screenProps.getProperty(\"action_confirm\")+screenProps.getProperty(\"field_updateMode\")+\"?\","+BR;
		sCode+="	                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, screenProps.getProperty(\"action_confirm\")),"+BR;
		sCode+="	                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, screenProps.getProperty(\"action_cancel\"))"+BR;
		sCode+="					).show("+BR;
		sCode+="	                    new EventListener() {"+BR;
		sCode+="		                	public void buttonClicked(ButtonType buttonType) {"+BR;
		sCode+="		                      	if (buttonType == MessageBox.ButtonType.YES) {"+BR;
		sCode+="				           			onDetailUpdateButton();"+BR;
		sCode+="				               	}"+BR;
		sCode+="					       	}"+BR;
		sCode+="	    			    } "+BR;
		sCode+="	    			);"+BR;
		sCode+="				}else new MessageBox(ui,msgTitle,message).show();"+BR;
	
		sCode+="		    }"+BR;
		sCode+="	        //明細刪除"+BR;
		sCode+="	        if (actionMode.equals(\"Delete\")){"+BR;
		sCode+="	           FieldCheck DetailFieldCheck =new FieldCheck();"+BR;
		sCode+="               new MessageBox(ui,"+BR;
		sCode+="                		msgTitle,"+BR;
		sCode+="                        MessageBox.Icon.QUESTION,"+BR;
		sCode+="                        screenProps.getProperty(\"action_confirm\")+screenProps.getProperty(\"field_deleteMode\")+\"?\","+BR;
		sCode+="                        new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, screenProps.getProperty(\"action_confirm\")),"+BR;
		sCode+="                        new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, screenProps.getProperty(\"action_cancel\"))"+BR;
		sCode+="				).show("+BR;
		sCode+="                    new EventListener() {"+BR;
		sCode+="                    	public void buttonClicked(ButtonType buttonType) {"+BR;
		sCode+="                        	if (buttonType == MessageBox.ButtonType.YES) {"+BR;
		sCode+="		                 		onDetailDeleteButton();"+BR;
		sCode+="		                	}"+BR;
		sCode+="		            	}"+BR;
		sCode+=" 			    	}"+BR;
		sCode+=" 				);"+BR;
		sCode+="	        }"+BR;
		sCode+="	    }"+BR;
		sCode+="	};"+BR;
		sCode+="	//===Detail Button End====================================================================="+BR;
		return sCode;
	}
	
	public String getDetailClearAction(){	
		String sCode="";
		sCode+="	/**"+BR;
		sCode+="	 * 執行明細清空 "+BR;
		sCode+="	 */"+BR;
		sCode+="	public void onDetailClearButton(){"+BR;
		sCode+="		DetailBean =new "+beanName+"();"+BR;
		sCode+="		DetailBinderFactory();"+BR;
		sCode+="		setDetailComboBox();"+BR;
		sCode+="	 } "+BR;
		return sCode;
	}

	//建立DetailDelete Action
	public String getDetailDeleteAction(){	
		String sCode ="";
		sCode+="    /**"+BR;
		sCode+="     * 執行明細刪除"+BR;
		sCode+="     */  "+BR;
		sCode+="	public void onDetailDeleteButton(){"+BR;
		sCode+="		if (DetailBean.getBeanKey() != \"\"){"+BR;
		sCode+="	    	boolean upflag=DataSrv.delete"+beanName+"(DetailBean);"+BR;
		sCode+="	        if (!upflag){"+BR;
		sCode+="	        	new MessageBox(ui,msgTitle,loanMessage.getString(\"message_error\")+DataSrv.ErrMsg).show();"+BR;
		sCode+="	        }else{"+BR;
		sCode+="	        	DetailData.removeItem(DetailBean.getBeanKey());"+BR;
		sCode+="	        	//寫log"+BR;
		sCode+="	        	traceLog.writeLog(\"D\" ,programId ,null ,DetailItem ,cpuStartTime );"+BR;
		sCode+="				tDetailAction.setVisible(false);"+BR;
		sCode+="				showDetailTable();"+BR;
		sCode+="	        }"+BR;
		sCode+="		}"+BR;
		sCode+="	}"+BR;			
		return sCode; 
	}		
	
	//建立DetailUpdate Action
	public String getDetailUpdateAction(){
		String sCode ="";
		sCode+="    /**"+BR;
		sCode+="     *  執行明細修改"+BR;
		sCode+="     */"+BR;
		sCode+="	public void onDetailUpdateButton(){"+BR;
		sCode+="		if (DetailData.indexOfId(DetailBean.getBeanKey()) >= 0){"+BR;
		sCode+="			DetailBean.setLstModUsrid(users.getUserId());"+BR;
		sCode+="			DetailBean.setLstModDt(DateUtil.getDT());"+BR;
		sCode+="	   		BeanContainer<String ,"+beanName+">  con_data =DataSrv.get"+beanName+"_PKs(DetailBean);"+BR;
		sCode+="	    	if (con_data.size() == 0){"+BR;
		sCode+="	   		if (DataSrv.ErrMsg.length()==0){"+BR;
		sCode+="	    			//資料不存在"+BR;
		sCode+="	    			new MessageBox(ui,msgTitle,loanMessage.getString(\"message_data_not_exist\")).show();"+BR;
		sCode+="	    		}else{"+BR;
		sCode+="	    			new MessageBox(ui,msgTitle,loanMessage.getString(\"message_error\")+DataSrv.ErrMsg).show();"+BR;
		sCode+="	    		}"+BR;
		sCode+="	    	}else{"+BR;
		sCode+="	   			boolean upflag=DataSrv.update"+beanName+"(DetailBean);"+BR;
		sCode+="		        if (upflag){"+BR;
		sCode+="		        	//寫log"+BR;
		sCode+="		        	traceLog.writeLog(\"U\" ,programId ,oldBeanItem ,DetailItem ,cpuStartTime );"+BR;
		sCode+="		    		tDetailAction.setVisible(false);"+BR;
		sCode+="		        }else{"+BR;
		sCode+="		        	new MessageBox(ui,msgTitle,loanMessage.getString(\"message_error\")+DataSrv.ErrMsg).show();"+BR;
		sCode+="		        }"+BR;
		sCode+="	    	}"+BR;
		sCode+="	 		showDetailTable();"+BR;		
		sCode+="		}else{"+BR;
		sCode+="			new MessageBox(ui,msgTitle,loanMessage.getString(\"message_data_not_exist\")).show();"+BR;
		sCode+="			return; "+BR;
		sCode+="		}"+BR;
		sCode+="	}"+BR;		
		return sCode;
	}	
	
	//建立DetailInsert Action
	public String getDetailInsertAction(){	
		String sCode ="";
		sCode+="    /**"+BR;
		sCode+="     * 執行明細新增 "+BR;
		sCode+="     */"+BR;
		sCode+="	public void onDetailInsertButton(){"+BR;
		sCode+="	 	"+beanName+" newBean =setDetailBean(new "+beanName+"() ,FieldCheck.getValue(DetailBinder));"+BR;
		sCode+="	 	newBean.setLstModUsrid(	users.getUserId());"+BR;
		sCode+="	 	newBean.setLstModDt(DateUtil.getDT());"+BR;
		sCode+="	 	DetailData.addBean(newBean);"+BR;
		sCode+="	 	showDetailTable();"+BR;
		sCode+="	  	BeanContainer<String ,"+beanName+">       con_chk =	DataSrv.get"+beanName+"_PKs(newBean);"+BR;
		sCode+="	 	if (con_chk.size() > 0){"+BR;
		sCode+="	 		//資料已存在"+BR;
		sCode+="	  		new MessageBox(ui,msgTitle,loanMessage.getString(\"message_data_exist\")).show();"+BR;
		sCode+="	  		DetailData.removeItem(newBean.getBeanKey());"+BR;
		sCode+="		}else{"+BR;
		sCode+="	        boolean upflag=DataSrv.insert"+beanName+"(newBean);"+BR;
		sCode+="	        if (upflag){"+BR;
		sCode+="	        	//寫log"+BR;
		sCode+="	        	traceLog.writeLog(\"A\" ,programId ,null ,new BeanItem(newBean) ,cpuStartTime );"+BR;
		sCode+="	        	tDetailAction.setVisible(false);"+BR;
		sCode+="	        }else{"+BR;
		sCode+="	        	new MessageBox(ui,msgTitle,loanMessage.getString(\"message_error\")+DataSrv.ErrMsg).show();"+BR;
		sCode+="	        	DetailData.removeItem(newBean.getBeanKey());"+BR;
		sCode+="	        }"+BR;
		sCode+="		}"+BR;
		sCode+="		//必須重新Query"+BR;
		sCode+="		onQueryButton();"+BR;		
		sCode+="	}"+BR;		
		return sCode; 
	}	
	//整理DetailBean
	public String getDetailBean(){	
		String sCode="";
		sCode+="	/**"+BR;
		sCode+="	 * 整理DetailBean"+BR;
		sCode+="	 * @param item"+BR;
		sCode+="	 */"+BR;
		sCode+="	void getDetailBean(BeanItem<"+beanName+"> item){"+BR;
		sCode+="		DetailBean =item.getBean();"+BR;
		sCode+="	 	DetailBinderFactory();	"+BR;
		sCode+="		setDetailComboBox(); 	"+BR;
		sCode+="	}	"+BR;	
		return sCode;
	}
	
	//建立showDetail
	public String getShowDetail(){	
		String sCode ="";
		String sShowName="";
		String sColumns="";
		if (container.size() > 0){
			sColumns+="	 		DetailTable.setVisibleColumns(new Object[]{";
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				sShowName+="	 		DetailTable.setColumnHeader(\""+fieldName+"\"    , screenProps.getProperty(\"field_"+fieldName+"\"));"+BR;
				if (i>0) sColumns+=",";
				sColumns+="\""+fieldName+"\"";
			}
			sColumns+="}); "+BR;
		}
		sCode+="    /**"+BR;
		sCode+="     * Detail Table 設定"+BR;
		sCode+="     */	"+BR;
		sCode+="	void showDetailTable(){	"+BR;
		sCode+="		DetailTable.setContainerDataSource(DetailData);"+BR;		
		sCode+="		setTableshowName();"+BR;
		sCode+="		//定義要顯示的欄位與名稱"+BR;
		sCode+=sShowName+BR;
		sCode+=sColumns+BR;
		sCode+="		 	DetailTable.addValueChangeListener(listener);"+BR;
		sCode+=" 			if (DetailTable.size() >=20)	DetailTable.setPageLength(20);"+BR;
		sCode+=" 			else DetailTable.setPageLength(DetailTable.size());"+BR;
		sCode+="	}"+BR;
		sCode+="    /**"+BR;
		sCode+="	 *選取明細資料時"+BR;
		sCode+="     */	"+BR;
		sCode+="	Property.ValueChangeListener listener = new Property.ValueChangeListener() {"+BR;
		sCode+="	   	@SuppressWarnings(\"unchecked\")"+BR;
		sCode+="	   	public void valueChange(Property.ValueChangeEvent event) {"+BR;
		sCode+="	   		BeanItem<"+beanName+"> beanitem=(BeanItem<"+beanName+">) DetailTable.getItem(DetailTable.getValue());"+BR;
		sCode+="			if (beanitem != null ){"+BR;
		sCode+="				getDetailBean(beanitem);"+BR;
		sCode+="				tDetailAction.setVisible(true);"+BR;
		sCode+="				tabLayout.DetailTab.setSelectedTab(tDetailAction);"+BR;
		sCode+="				//上/下筆切換 begin==============================================================================="+BR;
		sCode+="				controlBar.setVisible(true);"+BR;
		sCode+="				controlBar.setTotalRow(DetailData.size());"+BR;
		sCode+="				controlBar.setNowRow(DetailData.indexOfId(DetailBean.getBeanKey())+1);"+BR;
		sCode+="				//上/下筆切換 end================================================================================="+BR;
		sCode+="        		//寫log"+BR;
		sCode+="        		traceLog.writeLog(\"B\" ,programId ,null ,DetailItem ,cpuStartTime );"+BR;
		sCode+="				//記錄原始的資料==begin================================================================="+BR;
		sCode+="				"+beanName+" oldBean =new "+beanName+"();"+BR;
		sCode+="				setDetailBean(oldBean ,beanitem.getBean());"+BR;
		sCode+="				oldBeanItem = new BeanItem<"+beanName+">(oldBean);"+BR;
		sCode+="				//記錄原始的資料==end==================================================================="+BR;
		sCode+="			}"+BR;
		sCode+="     	}"+BR;
		sCode+=" 	};"+BR;		
		return sCode;
	}
	
	//DetailBean轉換
	public String getSetDetailBean(){
		String sCode ="";
		sCode+="    /**"+BR;
		sCode+="     * DetailBean轉換"+BR;
		sCode+="     */"+BR;
		sCode+="	"+beanName+" setDetailBean("+beanName+" binBean ,Object srcBean){"+BR;
		sCode+="    	try {"+BR;
		sCode+="			BeanUtils.copyProperties(binBean, srcBean);"+BR;
		sCode+="		} catch (Exception e) {"+BR;
		sCode+="			e.printStackTrace();"+BR;
		sCode+="		}"+BR;
		sCode+="    	return binBean;"+BR;
		sCode+="    }"+BR;
		return sCode;
	}
	
	//建立Clear Action
	public String getClearAction(){	
		String sCode ="";
		sCode+="    /**"+BR;
		sCode+="     * 執行清空動作"+BR;
		sCode+="     */"+BR;				
		sCode+="	 public void onClearButton(){"+BR;	
		sCode+="	 	Init();"+BR;	
		sCode+="	 }"+BR;				
		return sCode;
	}	
	
	//建立Query Action
	public String getQueryAction(){	
		String sCode ="";
		sCode+="	/**"+BR;
		sCode+="	 * 執行查詢動作"+BR;
		sCode+="	 */"+BR;
		sCode+="	 public void onQueryButton(){"+BR;
		sCode+="	 	//從db取資料"+BR;
		sCode+="	 	DetailData = DataSrv.get"+beanName+"_Query(QueryBean);"+BR;
		sCode+="	 	//show detail"+BR;
		sCode+="	 	showDetailTable();"+BR;
		sCode+="	 	//查無資料"+BR;
		sCode+="	 	if (DetailData.size()==0){"+BR;
		sCode+="	  		if (DataSrv.ErrMsg.length()==0){"+BR;
		sCode+="	  			//資料不存在"+BR;
		sCode+="	  			new MessageBox(ui,msgTitle,loanMessage.getString(\"message_data_not_found\")).show();"+BR;
		sCode+="	  		}else{"+BR;
		sCode+="	  			new MessageBox(ui,msgTitle,loanMessage.getString(\"message_error\")+DataSrv.ErrMsg).show();"+BR;
		sCode+="	  		}"+BR;
		sCode+="	 	}else{"+BR;
		sCode+="	 		tDetailAction.setVisible(false);"+BR;
		sCode+="	 	}"+BR;
		sCode+="	  }"+BR;
		return sCode;
	}	
	
	//建立Query Form
	public String getQueryForm(){	
		String sCode ="";
		String sMap ="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				String sField=(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
				String sREMARK=(((TextField)item.getItemProperty("REMARKS").getValue()).getValue()+"").trim();
				 if (isQueryKey.indexOf(columnName) >= 0){
					 Hashtable<String, String> map =this.HashQuery.get(sField);
					 sMap+="	    gridconstraints.addConstraint(QueryCreateField(\""+sField+"\")   ,"+map.get("TEXT1")+","+map.get("TEXT2")+" ,"+map.get("TEXT3")+","+map.get("TEXT4")+",true);"+BR;
				 }
			}
		}
		sCode+="    /**"+BR;
		sCode+="     * 建立查詢的Form Layout"+BR;
		sCode+="     */	 "+BR;
		sCode+="	 public void QueryCreate(){"+BR;
		sCode+="	 	QueryLayout = new GridForm("+this.iMaxFormCol+","+this.iMaxFormRow+");"+BR;
		sCode+="	 	//位置"+BR;
		sCode+="	 	GridConstraints gridconstraints = new GridConstraints();"+BR;		
		sCode+=			sMap;
		sCode+="	 	//form , item , data 連結 	"+BR;
		sCode+="	 	QueryLayout.setConstraints(gridconstraints);"+BR;
		sCode+="	 	QueryLayout.setAllComponents();"+BR;
		sCode+="	 }"+BR+BR;	
		sCode+="	/**"+BR;
		sCode+="	 * QueryLayout的Field Factory"+BR;
		sCode+="	 */"+BR;
		sCode+="     private void QueryBinderFactory(){"+BR;
		sCode+="      	QueryItem = new BeanItem<"+beanName+">(QueryBean);"+BR;
		sCode+=" 		QueryBinder = new FieldGroup(QueryItem);"+BR;
		sCode+=" 		QueryBinder.setBuffered(true);"+BR;
		sCode+="      	for (Iterator iter=QueryLayout.getConstraints().getPropertyIds().iterator() ;iter.hasNext();){"+BR;
		sCode+="      		String pid=(String)iter.next();"+BR;
		sCode+="      		Field field =QueryLayout.getField(pid);"+BR;
		sCode+="  	        QueryBinder.bind(field, pid);"+BR;
		sCode+="      	}"+BR;
		sCode+="  	 }"+BR;		
		return sCode;
	}	
	//建立Query Factory
	public String getQueryFactory(){	
		String sCode ="";
		String sMap ="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				String sField=(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
				String sREMARK=(((TextField)item.getItemProperty("REMARKS").getValue()).getValue()+"").trim();
				 if (isQueryKey.indexOf(columnName) >= 0){
					 sMap+=PropertyField.setString4PropertyField(sREMARK ,sField ,QueryField);
				 }
			}
		}
		sCode+="	 /**"+BR;
		sCode+="	  * QueryLayout的欄位定義"+BR;
		sCode+="	  * @param pid"+BR;
		sCode+="	  * @return"+BR;
		sCode+="	  */"+BR;
		sCode+="  	 private Field QueryCreateField(String pid) {"+BR;
		sCode+="  		String pname =screenProps.getProperty(\"field_\"+pid);"+BR;
		sCode+=			sMap;
		sCode+="	    return null;"+BR;
		sCode+="	  }"+BR;		
		return sCode;
	}	
	
	//建立Data Form
	public String getDataForm(){	
		String sCode ="";
		String sMap ="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				String sField=(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
				String sREMARK=(((TextField)item.getItemProperty("REMARKS").getValue()).getValue()+"").trim();
				 if (isDataFormKey.indexOf(columnName) >= 0){
					 Hashtable<String, String> map =this.HashDataForm.get(sField);
					 sMap+="	    gridconstraints.addConstraint(createField(\""+sField+"\")   ,"+map.get("TEXT1")+","+map.get("TEXT2")+" ,"+map.get("TEXT3")+","+map.get("TEXT4")+",true);"+BR;
				 }
			}
		}
		sCode+="     /**"+BR;
		sCode+="      * 建立Detail Form Layout"+BR;
		sCode+="      */"+BR;
		sCode+="	 public void DetailCreate(){"+BR;
		sCode+="	 	DetailLayout = new GridForm("+this.iMaxDataFormCol+","+this.iMaxDataFormRow+");"+BR;
		sCode+="	 	//位置"+BR;
		sCode+="	    GridConstraints gridconstraints = new GridConstraints();"+BR;
		sCode+=			sMap;
		sCode+="	 	//form , item , data 連結 "+BR;
		sCode+="	 	DetailLayout.setConstraints(gridconstraints);"+BR;
		sCode+="	 	DetailLayout.setAllComponents();"+BR;
		sCode+="	 }"+BR+BR;	
		sCode+="	/**"+BR;
		sCode+="	 * DetailLayout的Field Factory"+BR;
		sCode+="	 */"+BR;
		sCode+="     private void DetailBinderFactory(){"+BR;
		sCode+="     	DetailItem = new BeanItem<"+beanName+">(DetailBean);"+BR;
		sCode+="		DetailBinder = new FieldGroup(DetailItem);"+BR;
		sCode+="		DetailBinder.setBuffered(true);"+BR;
		sCode+="      	for (Iterator iter=DetailLayout.getConstraints().getPropertyIds().iterator() ;iter.hasNext();){"+BR;
		sCode+="      		String pid=(String)iter.next();"+BR;
		sCode+="      		Field field =DetailLayout.getField(pid);"+BR;
		sCode+="  	        DetailBinder.bind(field, pid);"+BR;
		sCode+="      	}"+BR;
		sCode+="  	 }"+BR;		
		return sCode;
	}
	
	//建立Data Factory
	public String getDataFactory(){	
		String sCode ="";
		String sMap ="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				Item item=container.getItem(container.getIdByIndex(i));
				setItemValue(item);
				String sField=(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
				String sREMARK=(((TextField)item.getItemProperty("REMARKS").getValue()).getValue()+"").trim();
				 if (this.isDataFormKey.indexOf(columnName) >= 0){
					 sMap+=PropertyField.setString4PropertyField(sREMARK ,sField ,DataField);
				 }
			}
		}
		sCode+=" 	/**"+BR;
		sCode+=" 	 * DetailLayout的欄位定義"+BR;
		sCode+=" 	 */"+BR;
		sCode+="  	 private Field createField(String pid) {"+BR;
		sCode+="  		String pname =screenProps.getProperty(\"field_\"+pid);"+BR;
		sCode+=			sMap;
		sCode+="	    return null;"+BR;
		sCode+="	  }"+BR;		
		return sCode;
	}	
	
	@SuppressWarnings("unchecked")
	void setItemValue(Item item){
		this.columnName =(((TextField)item.getItemProperty("COLUMN_NAME").getValue()).getValue()+"").trim();
		this.fieldName =(((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"").trim();
		this.fieldNameUp =fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
		this.fieldType  ="String";
		this.DATA_TYPE =(((TextField)item.getItemProperty("DATA_TYPE").getValue()).getValue()+"").trim();
		this.DECIMAL_DIGITS =(((TextField)item.getItemProperty("DECIMAL_DIGITS").getValue()).getValue()+"").trim();
		this.QueryField =((PropertyField)item.getItemProperty("QueryField").getValue()).getBean();
		this.DataField =((PropertyField)item.getItemProperty("DataField").getValue()).getBean();
		this.isQueryKey="";
		this.isDataFormKey="";
		this.PrimaryKey="";
		if (DATA_TYPE.equals("NUMBER") && DECIMAL_DIGITS.equals("0") ) fieldType="int";
		if (DATA_TYPE.equals("NUMBER") && !DECIMAL_DIGITS.equals("0") ) fieldType="float";
		
    	//拆key
		Collection<String> itemArrayList =new HashSet<String>();
		OptionGroup keyList =(OptionGroup)item.getItemProperty("KEY").getValue();
		itemArrayList =(Collection<String>)keyList.getValue();				
    	Iterator itr = itemArrayList.iterator();				
        while (itr.hasNext()) {
        	String value=(String) itr.next();
        	if (value.equals("PrimaryKey")){
        		PrimaryKey+=columnName+",";
        		BeanKey+=fieldName+",";
        	}
        	if (value.equals("LogKey")) LogKey+=fieldName+",";
        }
        //isQueryKey
        TextField4 queryForm =(TextField4)item.getItemProperty("QueryForm").getValue();
        Hashtable<String, String> map =(Hashtable<String, String>)queryForm.getValue();
        if (map.get("CHECK").equals("true")){
        	this.isQueryKey+=columnName+",";
        	this.HashQuery.put(fieldName,map);
			int itempX =Integer.parseInt(map.get("TEXT3")+"");
			int itempY =Integer.parseInt(map.get("TEXT4")+"");
			if (itempX+1 > this.iMaxFormCol) this.iMaxFormCol=itempX+1;
			if (itempY+1 > this.iMaxFormRow) this.iMaxFormRow=itempY+1;
        }
        //isDataFormKey
        TextField4 dataForm =(TextField4)item.getItemProperty("DataForm").getValue();
        Hashtable<String, String> map2 =(Hashtable<String, String>)dataForm.getValue();
        if (map2.get("CHECK").equals("true")){
        	this.isDataFormKey+=columnName+",";
        	this.HashDataForm.put(fieldName,map2);
			int itempX =0;
			int itempY =0;
			try{
				itempX=Integer.parseInt(map2.get("TEXT3")+"");
				itempY=Integer.parseInt(map2.get("TEXT4")+"");
			} catch (NumberFormatException se) {
			}
			if (itempX+1 > this.iMaxDataFormCol) this.iMaxDataFormCol=itempX+1;
			if (itempY+1 > this.iMaxDataFormRow) this.iMaxDataFormRow=itempY+1;
        }        
	}
}