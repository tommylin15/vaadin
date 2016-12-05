package com.scsb.vaadin.include;

import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.scsb.db.bean.CcCode;
import com.scsb.db.service.CcCodeService;
import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.NativeSelect;

@SuppressWarnings("serial")
/**
 * 取得 cc_code的下拉選單
 */
public class CcCodeSelect  extends ScsbGlob {

	Hashtable<String,String> dataTable =new Hashtable<String,String>();
	/**
	 * 取得 cc_code的代碼列表
	 * @param Code_Kind 
	 * @param IsShowId
	 * @param haveEmpty
	 */
	public CcCodeSelect(String Code_Kind ,boolean IsShowId ,boolean haveEmpty){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' ";
		getData(sWhere ,IsShowId ,haveEmpty);
	}
	/**
	 * 取得 cc_code的代碼列表
	 * @param Code_Kind 
	 * @param Code_Id :String[]
	 * @param IsShowId
	 * @param haveEmpty
	 */
	public CcCodeSelect(String Code_Kind  ,String[] Code_Id,boolean IsShowId ,boolean haveEmpty){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' ";
		String codeIdin ="";
		for(int i=0;i<Code_Id.length;i++){
			if (codeIdin.length() > 0 ) codeIdin+=",";
			codeIdin+="'"+Code_Id[i]+"'";
		}
		if (codeIdin.length() > 0 ) sWhere+=" and a.code_id in ("+codeIdin+")";
		getData(sWhere ,IsShowId ,haveEmpty);
	}	
	/**
	 * 取得 cc_code的代碼列表 for Code_Kind + Code_Parent
	 * @param Code_Kind
	 * @param IsShowId  :是否列出Id
	 * @param haveEmpty :是否可以為空白
	 * @param have99    :code_id是否有'99'(其它) 
	 */
	public CcCodeSelect( String Code_Kind ,boolean IsShowId  ,boolean haveEmpty ,boolean have99){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' ";
		if (!have99) sWhere+=" and a.code_id not in ('99') ";
	    getData(sWhere ,IsShowId ,haveEmpty);
	}	
	
	/**
	 * 取得 cc_code的代碼列表 for Code_Kind
	 * @param Code_Kind
	 * @param IsShowId  :是否列出Id
	 * @param haveEmpty :是否可以為空白
	 * @param NotCodeId :排除的code id 
	 */
	public CcCodeSelect( String Code_Kind ,boolean IsShowId  ,boolean haveEmpty ,String NotCodeId){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' ";
		sWhere+=" and a.code_id not in ("+NotCodeId+") ";
	    getData(sWhere ,IsShowId ,haveEmpty);
	}
	
	/**
	 * 取得 cc_code的代碼列表 for Code_Kind + Code_Parent
	 * @param Code_Kind
	 * @param Code_Parent
	 * @param IsShowId
	 * @param haveEmpty
	 */
	public CcCodeSelect( String Code_Kind ,String Code_Parent ,boolean IsShowId  ,boolean haveEmpty){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' and (a.code_parent in ("+Code_Parent.trim()+") or a.code_id='99') ";
	    getData(sWhere ,IsShowId ,haveEmpty);
	}
	
	/**
	 * 取得 cc_code的代碼列表 for Code_Kind + Code_Parent
	 * @param Code_Kind
	 * @param Code_Parent
	 * @param IsShowId  :是否列出Id
	 * @param haveEmpty :是否可以為空白
	 * @param NotCodeId :排除的code id 
	 */
	public CcCodeSelect(String Code_Kind ,String Code_Parent ,boolean IsShowId  ,boolean haveEmpty ,String NotCodeId){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' and (a.code_parent in ("+Code_Parent.trim()+") or a.code_id='99')";
		sWhere+=" and a.code_id not in ("+NotCodeId+") ";
	    getData(sWhere ,IsShowId ,haveEmpty);
	}
	
	/**
	 * 取得 cc_code的代碼列表 for Code_Kind + Code_Parent
	 * @param Code_Kind
	 * @param Code_Parent
	 * @param IsShowId  :是否列出Id
	 * @param haveEmpty :是否可以為空白
	 * @param have99    :code_id是否有'99'(其它) 
	 */
	public  CcCodeSelect(String Code_Kind ,String Code_Parent ,boolean IsShowId  ,boolean haveEmpty ,boolean have99){
		String sWhere =" and a.code_kind ='"+Code_Kind.trim()+"' and a.code_parent in ("+Code_Parent.trim()+") ";
		if (!have99) sWhere+=" and a.code_id not in ('99') ";
	    getData(sWhere ,IsShowId ,haveEmpty);
	}	
	
	void getData(String sWhere ,boolean IsShowId  ,boolean haveEmpty){
		dataTable =new Hashtable<String,String>();
		String defaultLanguage = (String)session.getAttribute("defaultLanguage");
		defaultLanguage="";
		String isReadonly =(String)session.getAttribute("_Readonly");
		//取得系統別
		CcCodeService         cc_codesrv = new CcCodeService(defaultLanguage);
		BeanContainer<String ,CcCode>	container;
		if (isReadonly !=null){
			if (isReadonly.equals("Y")) container =cc_codesrv.getCcCodeAll(sWhere );
			else container =cc_codesrv.getCcCodeNoDel(sWhere);
		}else container =cc_codesrv.getCcCodeNoDel(sWhere);
		
    	if (haveEmpty){
    		dataTable.put("", "message_select_one");
    	}    	
		
    	for(int i=0;i<container.size();i++){
    		BeanItem<CcCode> beanitem = container.getItem(container.getIdByIndex(i));
    		CcCode ccCode =beanitem.getBean();
    		String CODE_ID =ccCode.getCodeId();
    		String CODE_NAME =ccCode.getCodeName();
    		if (IsShowId)	 dataTable.put(CODE_ID, ""+CODE_ID+" "+CODE_NAME);
    		else     		dataTable.put(CODE_ID, CODE_NAME);
    	}
	}

	public Hashtable<String,String> getHashData(){
		return dataTable;
	}
	
}