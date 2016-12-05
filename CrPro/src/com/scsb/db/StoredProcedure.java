package com.scsb.db;

import com.scsb.util.StrUtil;

public class StoredProcedure {
	public String ErrMsg = "";
	private DBAction dba;

	public StoredProcedure() {
		dba = new DBAction();
	}

	/**
	 * 判斷訂價計算條件是否為最新資料(程式代號：SP_PRC_CHKVER) 
	 * @param vstrCASE_SN 案件編號
	 * @param vstrCUST_ID 借款人ID
	 * @param vstrTABLE_KND 資料類別，"1"為批覆前資料，"2"為批覆後資料
	 * @param vstrUSRID 使用者ID
	 * @return String[] {OUTPUT_BLN, OUTPUT_MSG}
	 */
	public String[] spPrcChkVer(String vstrCASE_SN, String vstrCUST_ID, String vstrTABLE_KND, String vstrUSRID){
		String[] sInValues = new String[]{vstrCASE_SN, vstrCUST_ID, vstrTABLE_KND, vstrUSRID, "", ""};
		int iOutNumbers = 2;
		String[] sOutValues = new String[iOutNumbers];
		sOutValues = dba.executeSP("SP_PRC_CHKVER", sInValues, iOutNumbers);
		sOutValues[0] = convStdRtn(sOutValues[0]);
		if (StrUtil.isNotNull(sOutValues[1])) {
			ErrMsg = sOutValues[1] + dba.ErrMsg;
		} else {
			ErrMsg = dba.ErrMsg;
		}
		return sOutValues;
	}
	
	/**
	 * 將回傳TRUE/FALSE資料一致化 
	 * @param value
	 * @return
	 */
	private String convStdRtn(String value){
		String rtnValue = "";
		if (value == null) 
			return rtnValue;
		if (value.toUpperCase().equals("TRUE")) 
			return "TURE";
		else 
			rtnValue = value;
		return rtnValue;
	}
}
