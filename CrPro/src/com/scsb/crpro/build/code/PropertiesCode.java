package com.scsb.crpro.build.code;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.TextField;

public class PropertiesCode {
	
	public String Code ="";
	
	String BR="\r\n";
	String tableName="";
	IndexedContainer container;	
	
	public PropertiesCode(String titleName ,String tableName ,IndexedContainer container){
		
		this.tableName=tableName;
		this.container=container;
		
		String sCode="";
		String sField="";
		if (container.size() > 0){
			for(int i=0;i<container.size();i++){
				
				Item item=container.getItem(container.getIdByIndex(i));
				String fieldname=((TextField)item.getItemProperty("BEAN_NAME").getValue()).getValue()+"";
				String fieldtw=((TextField)item.getItemProperty("REMARKS").getValue()).getValue()+"";
				sField+="field_"+fieldname+"="+fieldtw+""+BR;
			}			
			sCode+="# title"+BR;
			sCode+="title_name="+titleName+""+BR+BR;
			sCode+="# parent tabbed name parameters"+BR;
			sCode+="tabbed_name=明細資料"+BR;			
			sCode+="# field value parameters"+BR;
			sCode+="field_validator=請輸入"+BR;	
			sCode+=sField+BR;
			sCode+="# child tabbed name parameters"+BR+BR;
			sCode+="field_inqueryMode=查詢"+BR;
			sCode+="field_insertMode=新增"+BR;
			sCode+="field_updateMode=修改"+BR;
			sCode+="field_deleteMode=刪除"+BR;
			sCode+="field_confirmMode=確認"+BR;
			sCode+="field_setMode=設定"+BR;
			sCode+="field_authorizeMode=核可"+BR;
			sCode+="field_reportMode=報表"+BR;
			sCode+="field_browseMode=瀏覽"+BR;
			sCode+="field_downloadMode=下載"+BR+BR;
			sCode+="array_FlgY=是"+BR+BR;
			sCode+="array_FlgN=否"+BR+BR;
			sCode+="# javaScript message"+BR;
			sCode+="field_required=不可為空值"+BR;
			sCode+="action_confirm=確認"+BR;
			sCode+="action_cancel=取消"+BR;
			sCode+="detail_query=查詢結果"+BR;
			sCode+="detail_action=明細維護"+BR;
		}		
		this.Code=sCode;
	}
}