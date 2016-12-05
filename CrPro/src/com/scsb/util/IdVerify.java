package com.scsb.util;

import java.util.Hashtable;

/**
 * 套用eBracnh的SubTXCHKPID功能
 * @author 
 *
 */
public class IdVerify {

	/**
	 * 
	 * @param PID
	 * @return 0:傳入值為空.不檢核 1:正確 2:企業戶 8:檢碼錯 9:格式錯
	 */
	public static int check(String PID) {
		int varRes = 0;
		if (PID==null || PID.equals(""))
			return 0;
		//判斷若是外國人臨時身份證號10位,後兩位是英文,則不檢核
		if (PID.length() == 10) {
			byte word1 = PID.getBytes()[8];
			byte word2 = PID.getBytes()[9];
			if (((word1 >= 97 && word1 <= 122) || (word1 >= 65 && word1 <= 90))
					&& ((word2 >= 97 && word2 <= 122) || (word2 >= 65 && word2 <= 90))) {
				varRes = 1;
				return varRes;
			}
		}
		if (PID.length()<8) {
			varRes = 9;
			return varRes;
		}

		//判斷是本國人或外國人(含OBU)
		byte word1 = PID.getBytes()[0];
		byte word2 = PID.getBytes()[1];
		byte word3 = PID.getBytes()[2];
		byte word4 = PID.getBytes()[3];
		if (((word1 >= 97 && word1 <= 122) || (word1 >= 65 && word1 <= 90))
				&& ((word2 >= 97 && word2 <= 122) || (word2 >= 65 && word2 <= 90))) {
			if (((word3 >= 97 && word3 <= 122) || (word3 >= 65 && word3 <= 90))
					&& ((word4 >= 97 && word4 <= 122) || (word4 >= 65 && word4 <= 90))) {
				//20081002 異動單 E-709329 "聯徵中心"OBU虛擬統編
				varRes = checkSubV(PID);
			} else {
				//外國人
				varRes = checkSubO(PID);
			}
		} else {
			//本國人
			varRes = checkSubI(PID);
		}
		return varRes;
	}

	/**
	 * 函式功能: 本國人檢查身份證號或身份證字號的副程式
	 * @param PID (String) 要檢核的身份證字號或統一編號
	 * @return (Integer) 檢查結果 ( 1正確 / 8檢碼錯 / 9格式錯 )
	 */
	public static int checkSubI(String PID) {
		if (PID.equals(""))
			return 0;

		int varRc = 0;
		char[] arrVars = PID.toCharArray();
		char varPid1, varPid2, varPid3, varPid4, varPid5, varPid6, varPid7, varPid8;
		char varPid9 = '0';
		char varPid10 = '0';
		if (PID.length() == 8) { // (PID長度 != 8) && (PID長度 != 10)
			varPid1 = arrVars[0]; // PID的左起第1碼
			varPid2 = arrVars[1]; //  PID的左起第2碼
			varPid3 = arrVars[2]; // PID的左起第3碼
			varPid4 = arrVars[3]; // PID的左起第4碼
			varPid5 = arrVars[4]; //  PID的左起第5碼
			varPid6 = arrVars[5]; // PID的左起第6碼
			varPid7 = arrVars[6]; // PID的左起第7碼
			varPid8 = arrVars[7]; // PID的左起第8碼
		} else if (PID.length() == 10) {
			varPid1 = arrVars[0]; // PID的左起第1碼
			varPid2 = arrVars[1]; //  PID的左起第2碼
			varPid3 = arrVars[2]; // PID的左起第3碼
			varPid4 = arrVars[3]; // PID的左起第4碼
			varPid5 = arrVars[4]; //  PID的左起第5碼
			varPid6 = arrVars[5]; // PID的左起第6碼
			varPid7 = arrVars[6]; // PID的左起第7碼
			varPid8 = arrVars[7]; // PID的左起第8碼
			varPid9 = arrVars[8]; // PID的左起第9碼
			varPid10 = arrVars[9]; // PID的左起第10碼
		} else {
			return 9;
		}
		if ((varPid1 == 'O') && (varPid2 == 'U') && (varPid10 == 'E'))
			return 0;

		int varX1 = 0;
		int varX2 = 0;

		if ((varPid1 >= 97 && varPid1 <= 122)
				|| (varPid1 >= 65 && varPid1 <= 90)) { // varPid1 是英文字母是檢查身份證字號
			//System.out.println("varPid1 = " + varPid1);
			switch (varPid1) {
			case 'A':
				varX1 = 1;
				varX2 = 0;
				break;
			case 'B':
				varX1 = 1;
				varX2 = 1;
				break;
			case 'C':
				varX1 = 1;
				varX2 = 2;
				break;
			case 'D':
				varX1 = 1;
				varX2 = 3;
				break;
			case 'E':
				varX1 = 1;
				varX2 = 4;
				break;
			case 'F':
				varX1 = 1;
				varX2 = 5;
				break;
			case 'G':
				varX1 = 1;
				varX2 = 6;
				break;
			case 'H':
				varX1 = 1;
				varX2 = 7;
				break;
			case 'I':
				varX1 = 3;
				varX2 = 4;
				break;				
			case 'J':
				varX1 = 1;
				varX2 = 8;
				break;
			case 'K':
				varX1 = 1;
				varX2 = 9;
				break;
			case 'L':
				varX1 = 2;
				varX2 = 0;
				break;
			case 'M':
				varX1 = 2;
				varX2 = 1;
				break;
			case 'N':
				varX1 = 2;
				varX2 = 2;
				break;
			case 'O':
				varX1 = 3;
				varX2 = 5;
				break;				
			case 'P':
				varX1 = 2;
				varX2 = 3;
				break;
			case 'Q':
				varX1 = 2;
				varX2 = 4;
				break;
			case 'R':
				varX1 = 2;
				varX2 = 5;
				break;
			case 'S':
				varX1 = 2;
				varX2 = 6;
				break;
			case 'T':
				varX1 = 2;
				varX2 = 7;
				break;
			case 'U':
				varX1 = 2;
				varX2 = 8;
				break;
			case 'V':
				varX1 = 2;
				varX2 = 9;
				break;
			case 'X':
				varX1 = 3;
				varX2 = 0;
				break;
			case 'Y':
				varX1 = 3;
				varX2 = 1;
				break;
			case 'W':
				varX1 = 3;
				varX2 = 2;
				break;
			case 'Z':
				varX1 = 3;
				varX2 = 3;
				break;
			}
			if (varPid2 != '1' && varPid2 != '2')
				return 9;
			if (PID.length() == 10) {
				if (!(isNumeric(PID.substring(1, 10)))) {
					// PID的左邊起第2, 3, 4, 5, 6, 7, 8, 9, 10 碼 全都是數字
					return 9;
				}
			}

			if (((varX1 + 9 * varX2 + 
					(8 * Character.getNumericValue(varPid2))+ 
					(7 * Character.getNumericValue(varPid3))+ 
					(6 * Character.getNumericValue(varPid4))+ 
					(5 * Character.getNumericValue(varPid5))+ 
					(4 * Character.getNumericValue(varPid6))+ 
					(3 * Character.getNumericValue(varPid7))+ 
					(2 * Character.getNumericValue(varPid8))+ 
					Character.getNumericValue(varPid9) + 
					Character.getNumericValue(varPid10)) % 10) == 0) {
				return 1;
			} else {
				return 8;
			}

		} else { //檢查統一編號

			if (!(isNumeric(PID.substring(0, 8)))) {
				// PID的左邊起第1, 2, 3, 4, 5, 6, 7, 8 碼 全都是數字
				System.out.println("return 9");
				return 9;
			}

			if (PID.length() == 8) {
				int[] varA = new int[8];
				varA[0] = Character.getNumericValue(varPid1);
				varA[1] = Character.getNumericValue(varPid2) * 2;
				varA[2] = Character.getNumericValue(varPid3);
				varA[3] = Character.getNumericValue(varPid4) * 2;
				varA[4] = Character.getNumericValue(varPid5);
				varA[5] = Character.getNumericValue(varPid6) * 2;
				varA[6] = Character.getNumericValue(varPid7) * 4;
				varA[7] = Character.getNumericValue(varPid8);
				varX1 = 0;
				for (int i = 0; i < 8; i++) {
					//System.out.println("varX1(" + i + ")= " + varX1);
					varX1 += varA[i] / 10 + varA[i] % 10;
				}
				//System.out.println("varx1 = " + varX1);
				if ((varX1 % 10) == 0) {
					return 2;
				} else {
					if (varPid7 == '7') {
						varX1 = varX1
								- (varA[6] / 10 + varA[6] % 10)
								+ ((varA[6] / 10 + varA[6] % 10) / 10 + (varA[6] / 10 + varA[6] % 10) % 10);
						//System.out.println("varX1 = " + varX1);
						if ((varX1 % 10) == 0)
							return 2;
					}
					return 8;
				}
			} else { //PID長度不是8
				if ((varPid9 < '0' || varPid9 > '9')
						&& (varPid10 < '0' || varPid10 > '9')) { // varPid9 非數字 && varPid10 非數字
					if (varPid1 != '1' && varPid1 != '2')
						return 9;
					else
						return 3;
				} else {
					return 9;
				}
			}
		}
	}

	/**
	 * 函式功能: 檢查"外國人"身份證號或身份證字號的副程式
	 * @param PID (String) 要檢核的身份證字號或統一編號
	 * @return (Integer) 檢查結果 ( 1正確 / 8檢碼錯 / 9格式錯 )
	 */
	public static int checkSubO(String PID) {
		int result = 9;
		String temp = "";
		Hashtable codeMap = new Hashtable(); //英文字的對應碼
		codeMap.put("A", "10");
		codeMap.put("B", "11");
		codeMap.put("C", "12");
		codeMap.put("D", "13");
		codeMap.put("E", "14");
		codeMap.put("F", "15");
		codeMap.put("G", "16");
		codeMap.put("H", "17");
		codeMap.put("J", "18");
		codeMap.put("K", "19");
		codeMap.put("L", "20");
		codeMap.put("M", "21");
		codeMap.put("N", "22");
		codeMap.put("P", "23");
		codeMap.put("Q", "24");
		codeMap.put("R", "25");
		codeMap.put("S", "26");
		codeMap.put("T", "27");
		codeMap.put("U", "28");
		codeMap.put("V", "29");
		codeMap.put("X", "30");
		codeMap.put("Y", "31");
		codeMap.put("W", "32");
		codeMap.put("Z", "33");
		codeMap.put("I", "34");
		codeMap.put("O", "35");

		int powerNum[] = { 1, 9, 8, 7, 6, 5, 4, 3, 2, 1 }; //各位數的加權值
		int valueNum[] = new int[10];

		if (PID.length() != 10)
			return 9; //不滿10位格式錯誤

		// 例外 開頭為OU 者 [[
		char[] tmpVars = PID.toCharArray();
		if (tmpVars[0] == 'O' && tmpVars[1] == 'U') {
			if (!(('0' <= tmpVars[2] && tmpVars[2] <= '9')
					&& ('0' <= tmpVars[3] && tmpVars[3] <= '9')
					&& ('0' <= tmpVars[4] && tmpVars[4] <= '9')
					&& ('0' <= tmpVars[5] && tmpVars[5] <= '9')
					&& ('0' <= tmpVars[6] && tmpVars[6] <= '9')
					&& ('0' <= tmpVars[7] && tmpVars[7] <= '9') && ('0' <= tmpVars[8] && tmpVars[8] <= '9'))) {
				return 9;
			}

			if (tmpVars[9] != 'E') {
				return 9;
			}

			return 1;
		}
		// end -  例外

		try {
			//將要作加權運算的值都放入valueNum的Array內
			for (int i = 0; i < 9; i++) {
				if (i == 0) {
					temp = PID.substring(0, 1);
					valueNum[0] = Integer.parseInt(codeMap.get(temp).toString().substring(0, 1));
					valueNum[1] = Integer.parseInt(codeMap.get(temp).toString().substring(1, 2));
				} else if (i == 1) {
					temp = PID.substring(1, 2);
					valueNum[2] = Integer.parseInt(codeMap.get(temp).toString().substring(1, 2));
				} else {
					valueNum[i + 1] = Integer.parseInt(PID.substring(i, i + 1));
				}
			}

			//開始加權
			int tempValue = 0;
			int powerValue = 0;
			for (int i = 0; i < 10; i++) {
				tempValue = valueNum[i] * powerNum[i];
				powerValue += tempValue % 10;
			}

			powerValue = powerValue % 10;
			if (powerValue != 0)
				powerValue = 10 - powerValue;
			else
				powerValue = 0;

			//比對檢查碼
			int checkValue = Integer.parseInt(PID.substring(9, 10));
			if (checkValue == powerValue) {
				result = 1;
			} else {
				result = 8;
			}
		} catch (Exception e) {
			result = 9;
		}
		return result;
	}


	/**
	 * 函式功能: 檢查"聯徵中心"OBU虛擬統編的副程式 
	 * 			20081002 異動單 E-709329 "聯徵中心"OBU虛擬統編
	 * @param PID (String) 要檢核的虛擬統編
	 * @return (Integer) 檢查結果 ( 1正確 / 8檢碼錯 / 9格式錯 )
	 */
	public static int checkSubV(String PID) {
		int result = 9;

		if (PID.length() != 8)
			return 9; //非8位格式錯誤

		if (!(isNumeric(PID.substring(4, 8)))) {
			// PID的左邊起第 5, 6, 7, 8 碼 全都是數字
			return 9;
		}

		//例外之特殊虛擬統編合理值(Hard coded)
		String[] VrIDList = { "AUBZ0003", "BZAS0026", "BZAV0020", "HKAA0433",
				"HKAC0395", "HKAF0378", "HKBH0114", "KYAH0005", "KYAK0008",
				"NUAA0449", "THAP0162", "USAS0067", "VGAC0004", "VGAC0010",
				"VGAC0899", "VGAE0006", "VGAE0360", "VGAG0002", "VGAG0007",
				"VGAG0008", "VGAH0005", "VGAK0042", "VGAK0394", "VGAM0004",
				"VGAN0003", "VGAS0002", "VGAS0007", "VGAT0000", "VGAT0298",
				"VGAU0009", "VGAW0005", "VGAZ0009", "VGBA0041", "VGBC0004",
				"VGBG0007", "VGBL0000", "VGBM0003", "VGBT0000", "VGBT0009",
				"VGBT0016", "VGBW0003", "WSAD0005", "WSAS0008", "WSAW0019",
				"WSBF0000", "WSBR0006" };
		for (int i = 0; i < VrIDList.length; i++) {
			if (PID.equalsIgnoreCase(VrIDList[i]))
				return 1;
		}

		//一般虛擬統編編碼檢核,ex:BMAN0006,BZAC0093 is OK!
		int powerNum[] = { 1, 2, 1, 2, 1, 2, 4, 1 }; //各位數的加權值
		int valueNum[] = new int[8];
		try {
			//將要作加權運算的值都放入valueNum的Array內
			char chrValue = ' ';
			for (int i = 0; i < 8; i++) {
				if (i < 4) {
					chrValue = PID.charAt(i);
					valueNum[i] = (chrValue - '0') % 10;
				} else {
					valueNum[i] = Integer.parseInt(PID.substring(i, i + 1));
				}
			}

			//開始加權
			int tempValue = 0;
			int powerValue = 0;
			for (int i = 0; i < 8; i++) {
				tempValue = valueNum[i] * powerNum[i];
				powerValue += tempValue % 10;
				tempValue = tempValue / 10;
				powerValue += tempValue;
			}

			//比對檢查結果			 
			powerValue = powerValue % 10;
			if (powerValue == 9 && PID.substring(6, 7).equals("7")) {
				result = 1;
			} else {
				if (powerValue == 0)
					result = 1;
				else
					result = 8;
			}
		} catch (Exception e) {
			result = 9;
		}
		return result;
	}

	private static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	/**
	 * 判斷是哪類的統編.再去套用檢核公式
	 * @version 20110512.G.
	 * @param id
	 * @return 0:傳入值為空.不檢核 1:正確 8:檢碼錯 9:格式錯
	 */
	public static String[] idCheck(String sID){
		//   	香港分行及同奈分行授信戶統編資訊如下：
		//		→同奈分行授信統編格式為 VN+7碼流水號+E ， 總共10碼，不分法人和個人，且無檢查碼
		//		→香港分行授信戶為 6碼人工編號，不分法人和個人，亦無檢查碼；
		//		為避免與台灣法人統編混淆，請鍵入人員在前面加上“ HK-”，  
		// 		所以需要在畫面上附註「香港分行授信戶6碼統編前面要加上“ HK- ”，最後一碼為空格」
		String[] rtn = {"","0"};
		String id = sID.toUpperCase();
		if (id==null || id.length()==0) { // 空值不檢核 

		} else if (id.matches("[A-Z][0-9]{9}")) { // 本國.自然人 Natural Person
			rtn[0] = "TWNP";
			rtn[1] = checkSubI(id)+"";
		} else if (id.matches("[0-9]{8}")) { // 本國.法人 Legal Person
			rtn[0] = "TWLP";
			rtn[1] = checkSubI(id)+"";
			if (rtn[1].equals("2"))	rtn[1] = "1"; 
		} else if (id.matches("OU[0-9]{7}[eE]")){ // OBU
			rtn[0] = "OBU";

		} else if (id.matches("VN[0-9]{7}[eE]")){ // VN
			rtn[0] = "VN";

		} else if (id.matches("HK-[0-9]{6}")){ // HK-
			rtn[0] = "HK";
			
		} else if (id.matches("[A-Z]{2}[0-9]{8}")){ // 外國人
			rtn[0] = "FOREIGN";
			rtn[1] = checkSubO(id)+"";
		} else if (id.matches("[0-9]{8}[A-Z]{2}")){ //判斷若是外國人臨時身份證號10位,後兩位是英文,則不檢核
			rtn[0] = "FOREIGN";

		} else if (id.matches("[A-Z]{4}[0-9]{4}")){ //20081002 異動單 E-709329 "聯徵中心"OBU虛擬統編
			rtn[0] = "VOBU";
			rtn[1] = checkSubV(id)+"";
		} else {
			rtn[1] = "9";
		}		
		return rtn;
	}
	
	public static void main(String[] args){
		//int result = check("OU0000015E");
		String[] result = idCheck("Y120054255");
		System.out.print("結果:"+result[0]+" "+result[1]);
	}
}
