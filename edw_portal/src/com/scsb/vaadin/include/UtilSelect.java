package com.scsb.vaadin.include;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import com.scsb.db.bean.Fnbct0;
import com.scsb.db.service.Fnbct0Service;
import com.scsb.util.UserAction;
import com.scsb.vaadin.composite.ScsbGlob;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.NativeSelect;

/**
 * 取得 公用的下拉選單
 */
public class UtilSelect  extends ScsbGlob {
	
	
	public UtilSelect(){
	}
	/**
	 * Yes /No
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect YNSelect(String codeId ,String caption ,boolean haveEmpty ){
		Hashtable<String,String> dataTable =new Hashtable<String,String>();
		dataTable.put("Y","是");		
		dataTable.put("N","否");		
		
		return createSelect(codeId ,caption ,dataTable ,haveEmpty );
	}
	/**
	 * 單位主管授權
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect deptactionSelect(String codeId ,String caption ,boolean haveEmpty ){
		return createSelect(codeId ,caption ,new CcCodeSelect("005" ,false ,haveEmpty).getHashData() ,haveEmpty );		
	}		
	/**
	 * 帳戶狀態
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect accountModeSelect(String codeId ,String caption ,boolean haveEmpty ){
		return createSelect(codeId ,caption ,new CcCodeSelect("002" ,false ,haveEmpty).getHashData() ,haveEmpty );
	}	
	/**
	 * 程式機密分類
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect doclevelSelect(String codeId ,String caption ,boolean haveEmpty ){
		return createSelect(codeId ,caption ,new CcCodeSelect("004" ,false ,haveEmpty).getHashData() ,haveEmpty );
	}
	/**
	 * 程式種類
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	
	public NativeSelect progSelect(String codeId ,String caption ,boolean haveEmpty ){
		return createSelect(codeId ,caption ,new CcCodeSelect("003" ,false ,haveEmpty).getHashData() ,haveEmpty );
	}
	
	/**
	 * 權責單位
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect unitSelect(String codeId ,String caption ,boolean haveEmpty ){
		Hashtable<String,String> dataTable =new Hashtable<String,String>();
		return createSelect(codeId ,caption ,new CcCodeSelect("006" ,false ,haveEmpty).getHashData() ,haveEmpty );
	}
	/**
	 * QUERY MODE
	 * @param codeId
	 * @param caption
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect serviceSelect(String codeId ,String caption ,boolean haveEmpty ){
		return createSelect(codeId ,caption ,new CcCodeSelect("007" ,false ,haveEmpty).getHashData() ,haveEmpty );
	}
	
	/**
	 * 全行人員-list
	 * @param codeId
	 * @param caption
	 *  @param is0000 (單位尾數為0000者 TRUE/FALSE)
	 * @param haveEmpty
	 *  @param haveAA
	 * @return
	 */
	public TwoSelect usersSelect(String codeId ,String caption ,boolean  is0000 ,boolean haveEmpty ,boolean haveAA){
		IndexedContainer container =UserAction.getAllBrhUsers(is0000 ,haveAA);

		IndexedContainer mContainer =new IndexedContainer();
		mContainer.addContainerProperty("BRH_COD",String.class, null);
		mContainer.addContainerProperty("BRH_NAME", String.class, null);
		
		NativeSelect brhSelect= brhCodSelect(codeId ,caption ,false ,false  ,false);
				
		for (java.util.Iterator<?> iter=brhSelect.getItemIds().iterator();iter.hasNext();){
			Object itemId=iter.next();
        	String itemCaption =brhSelect.getItemCaption(itemId);
			if (mContainer.getItem(itemId) == null){
	        	Item mItem =mContainer.addItem(itemId);
	        	mItem.getItemProperty("BRH_COD").setValue(itemId);
	        	mItem.getItemProperty("BRH_NAME").setValue(itemCaption.substring(3));
			}			
		}

        TwoSelect twoSelect =new TwoSelect(container);
        twoSelect.setMasterContainer("BRH_COD","BRH_NAME",mContainer);
        twoSelect.setDetailContainer("","USERID","USER_NAME");
        twoSelect.setSelect(null,  null);
        twoSelect.setDefaultMaster(users.getDeptid().substring(0,2));
        return twoSelect;
	}
	/**
	 * 允視分行(營業單位)-list
	 * @param codeId
	 * @param caption
	 * @param isBus(Y:限營業單位/N:不限營業單位)
	 * @param haveZZ
	 * @param haveEmpty
	 * @return
	 */
	public NativeSelect brhCodSelect(String codeId ,String caption ,boolean haveZZ  ,boolean haveEmpty  ){
		return brhCodSelect(codeId ,caption ,true ,haveZZ  ,haveEmpty );
	}
	public NativeSelect brhCodSelect(String codeId ,String caption ,boolean isBus ,boolean haveZZ  ,boolean haveEmpty ){
        //個人的允視分行(包括ZZ全行)
		HashSet<String> propertyValue =UserAction.getUsersProperty(users.getUserid() ,"ALLOW_BRH");		
		
		Fnbct0Service fnbct0Srv =new Fnbct0Service();
		BeanContainer<String,Fnbct0> fnbct0Container =fnbct0Srv.getSasFnbct0_All();
		fnbct0Container.sort(new Object[]{"ou"},new boolean[]{true});
		
		Hashtable<String, String> brhValue = new Hashtable<String,String>();
		
        for (int i=0;i<fnbct0Container.size();i++) {
    		BeanItem<Fnbct0> beanitem = fnbct0Container.getItem(fnbct0Container.getIdByIndex(i));
    		Fnbct0 fnbct0 =beanitem.getBean();
    		String brhCod =fnbct0.getBrhCod();
    		String CODE_NAME =fnbct0.getChinAl1();
    		if (isBus){
    			//全部營業單位(brh_typ=2) ,外加一個27
	    		if (fnbct0.getBrhTyp().equals("2") || fnbct0.getBrhCod().equals("27")){
		    		if (propertyValue.contains(brhCod)){
						brhValue.put(brhCod, CODE_NAME);
					}
	    		}
    		}else{
	    		if (propertyValue.contains(brhCod)){
					brhValue.put(brhCod, CODE_NAME);
				}    			
    		}
        }//for
		if (haveZZ){
    		if (propertyValue.contains("ZZ")){
				brhValue.put("ZZ", "ZZ.全行");
			}			
		}        
		return createSelect(codeId ,caption ,brhValue ,haveEmpty );
	}
	
	/**
	 * 產出select元件
	 * @param codeType
	 * @param codeId
	 * @param caption
	 * @param dataTable
	 * @param haveEmpty
	 * @param desc (升降排序)
	 * @return
	 */
	public NativeSelect createSelect(String codeId ,String caption 
			,Hashtable<String,String> dataTable ,boolean haveEmpty
			){
		return createSelect(codeId ,caption , dataTable , haveEmpty ,false);
	}
	
	public NativeSelect createSelect(String codeId ,String caption 
			,Hashtable<String,String> dataTable ,boolean haveEmpty
			,boolean desc
			){	
		return createSelect(codeId ,caption , dataTable , haveEmpty ,desc  ,".");
	}
	
	public NativeSelect createSelect(String codeId ,String caption 
																,Hashtable<String,String> dataTable ,boolean haveEmpty
																,final boolean desc ,String slink
																){
		NativeSelect select =new NativeSelect();
		select.setCaption(caption);
		select.setId(codeId);		
		select.setImmediate(true);
		select.setNullSelectionAllowed(haveEmpty);		
		
		Comparator<String> compSort =new Comparator<String>(){ 
			   public int compare(String obj1,String obj2){ 
			    //降序排序 
				if (desc)    return obj2.compareTo(obj1);
				//升序排序
				else return obj1.compareTo(obj2);
			   } 
		 };
		
		Map<String,String> map = new TreeMap<String,String>(compSort);
		map.putAll(dataTable);
		
		int iflag=0;
		for (java.util.Iterator<String> iter=map.keySet().iterator() ;iter.hasNext();){
			String key =iter.next();
			String value=map.get(key);
			select.addItem(key);
			select.setItemCaption(key,key+slink+value);
			if (iflag==0) select.select(key);
			iflag++;
		}
		
		return select;		
	}		

}