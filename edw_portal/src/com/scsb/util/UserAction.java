
package com.scsb.util;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;

import com.scsb.db.bean.Trans;
import com.scsb.db.bean.Transd;
import com.scsb.db.bean.Users;
import com.scsb.db.bean.Usersa;
import com.scsb.db.bean.Usersf;
import com.scsb.db.service.UsersService;
import com.scsb.db.service.UsersaService;
import com.scsb.db.service.UsersdService;
import com.scsb.db.service.UsersfService;
import com.scsb.db.service.UserspService;
import com.scsb.domain.HashTrans;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.util.IndexedContainer;

/**
 * 使用者相關資訊
 * @author 3471
 *
 */
public class UserAction{
	/**
	 * 檢查帳號密碼是否正確
	 * @param userid
	 * @param pwd
	 * @return
	 */
	
	public static String checkLoginLdap(String ldapWebService ,String userid ,String pwd){	
		DynamicClientFactory dcf = DynamicClientFactory.newInstance();
		Client client = dcf.createClient(ldapWebService);
		try {
			//先去掉前面的0,再丟進去驗證
			userid=userid.replaceFirst("^0*", "");
			Object[] res = client.invoke("authUser", new String[]{userid,pwd});
			//System.out.println("result: " + res[0].toString());
			String retString=res[0].toString();
			if (retString.equals("true")) return "";
			else return "Ldap驗證失敗!!";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static String checkLoginDB(String userid ,String pwd){
		//暫不加密
		//pwd=StrUtil.rpad(pwd," ",8);
		Usersa dataBean=getUsersaBean(userid);
		if (userid.equals("")) return "1.請輸入使用者代號";
		if (pwd.equals("")) return "2.請輸入使用者密碼";
		if (dataBean.getAccountmode().equals("F")) return "F.首次登入";
		if (dataBean.getAccountmode().equals("L")) return "L.帳號已鎖住";
		if (dataBean.getAccountmode().equals("R")) return "R.帳號已失效";
		if (dataBean.getUserid().equals(userid) ){
			//暫不加密
			//String pwdEncode =EncodeUtil.getEncodeSASString(pwd);
			String pwdEncode =pwd;
			if (pwdEncode.equals(dataBean.getUserPwd())){
				return "";
			}else{
				dataBean.setErrortimes(dataBean.getErrortimes()+1);
				if (dataBean.getErrortimes() > 5 ){
					dataBean.setAccountmode("L");
				}
				UsersaService usersaSrv =new UsersaService();
				usersaSrv.updateUsersa(dataBean);
				return "3.密碼錯誤,請重新查詢";
			}
		}else{
			return "4.查無此帳號";
		}
	}
	/**
	 * 
	 * @param userid
	 * @return Users
	 */
	public static Users getUsersBean(String userid){
		UsersService usersSrv =new UsersService();
		Users bean =new Users();
		bean.setUserid(userid);
		return usersSrv.getUsers_PK(bean);
	}
	
	/**
	 * 
	 * @return BeanContainer<String,Users>
	 */
	public static IndexedContainer getAllBrhUsers(boolean is0000 ,boolean haveAA){
		UsersService usersSrv =new UsersService();
		return usersSrv.getUsers_Brhall(is0000 ,haveAA);
	}	
	
	/**
	 * 
	 * @param userid
	 * @return Usersa
	 */
	public static Usersa getUsersaBean(String userid){
		UsersaService usersaSrv =new UsersaService();
		Usersa bean =new Usersa();
		bean.setUserid(userid);
		return usersaSrv.getUsersa_PK(bean);
	}	
	
	/**
	 * 變更密碼
	 * @param userid
	 * @param pwd
	 * @return
	 */
	public static String updatePassWord(String userid ,String pwd){
		if (userid.equals("")) return "請輸入使用者代號";
		if (pwd.equals("")) return "請輸入使用者密碼";
		pwd=StrUtil.rpad(pwd," ",8);
		Usersa dataBean=getUsersaBean(userid);
		UsersaService usersaSrv =new UsersaService();
		//暫不加密
		//String pwdEncode =EncodeUtil.getEncodeSASString(pwd);
		String pwdEncode =pwd;
		dataBean.setUserPwd(pwdEncode);
		dataBean.setMustchange("N");
		dataBean.setAccountmode("0");
		dataBean.setErrortimes(0);
		if (usersaSrv.updateUsersa(dataBean)){
			return "";
		}else{
			return usersaSrv.ErrMsg;
		}
	}	
	
	/**
	 * 清空密碼錯誤次數
	 * @param userid
	 * @param pwd
	 * @return
	 */
	public static String clearErrorTimes(Usersa usersa ){
		UsersaService usersaSrv =new UsersaService();
		usersa.setMustchange("N");
		usersa.setAccountmode("0");
		usersa.setErrortimes(0);
		if (usersaSrv.updateUsersa(usersa)){
			return "";
		}else{
			return usersaSrv.ErrMsg;
		}
	}	
	/**
	 * 使用者可用權限(程式代碼,動作)
	 * @param userid
	 * @return Usersa
	 */
	public static Hashtable<String ,String> getUsersAction(String userid){
		UsersdService usersdSrv =new UsersdService();
		return usersdSrv.getUsers_Action(userid);
	}
	/**
	 * 使用者的相關Property(包括 個人所屬分行 ,個人屬性 ,部門屬性 ,角色屬性 ,職稱屬性 ,工作內容屬性)
	 * @param userid
	 * @return Usersa
	 */
	public static HashSet<String > getUsersProperty(String userid ,String propertyKey){
		UserspService userspSrv = new UserspService();
		HashSet<String> propertyValue =userspSrv.getUsers_Property(userid ,propertyKey);
		return propertyValue;
	}	
	/**
	 * 使用者可用項目(主選單,次選單)
	 * @param userid
	 * @return Usersa
	 */
	public static Hashtable<String ,String> getUsersTrans(Hashtable<String ,String> userAction){
		Hashtable<String ,String> usersTrans =new Hashtable<String ,String>();
		HashTrans    hashTrans    	= HashTrans.getInstance();
		
	    //主選單
		BeanContainer<String,Trans> transContainer = hashTrans.getAllTrans();
		for(int i=0;i<transContainer.size();i++){
			Trans trans=transContainer.getItem(transContainer.getIdByIndex(i)).getBean();
	    	//導航選單
			if (trans.getGroupid().indexOf("_") > -1){
				//次選單
				BeanContainer<String,Transd> transdContainer = hashTrans.getAllTransD(trans.getGroupid());
		        transdContainer.sort(new Object[]{"groupid","programid"}, new boolean[]{true,true});
				for(int j=0;j<transdContainer.size();j++){
					Transd transd=transdContainer.getItem(transdContainer.getIdByIndex(j)).getBean();
					if (userAction.get(transd.getProgramid()) != null){
						usersTrans.put(trans.getGroupid().substring(0,2),"Y");
						usersTrans.put(trans.getGroupid(),"Y");
						break;
					}
				}
			}//if
		}//for
		return usersTrans;
	}	
	/**
	 * 使用者的最愛
	 * @param userid
	 * @return Usersf
	 */
	public static Vector  getUsersf(String userid){
		Vector vUsersf =new Vector();
		UsersfService usersfSrv =new UsersfService();
		BeanContainer<String,Usersf> usersfContainer =usersfSrv.getUsersfUserId(userid);
		for(int j=0;j<usersfContainer.size();j++){
			Usersf usersf=usersfContainer.getItem(usersfContainer.getIdByIndex(j)).getBean();
			vUsersf.add(usersf.getProgramid());
		}		
		return vUsersf;

	}
	public static void  insertUsersf(String userid ,String programid){
		UsersfService usersfSrv =new UsersfService();
		Usersf bean=new Usersf();
		bean.setProgramid(programid);
		bean.setUserid(userid);
		usersfSrv.insertUsersf(bean);
	}
	public static void  deleteUsersf(String userid ,String programid){
		UsersfService usersfSrv =new UsersfService();
		Usersf bean=new Usersf();
		bean.setProgramid(programid);
		bean.setUserid(userid);
		usersfSrv.deleteUsersf(bean);
	}
}
