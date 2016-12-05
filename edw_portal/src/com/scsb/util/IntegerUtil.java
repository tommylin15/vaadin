package com.scsb.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class IntegerUtil {
    public static int[] randomArray(int min,int max,int n){
		int len = max-min+1;
		
		if(max < min || n > len){
			return null;
		}
		
		//初始化给定範圍的待選數組
		int[] source = new int[len];
        for (int i = min; i < min+len; i++){
        	source[i-min] = i;
        }
        
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
        	//待選數組0到(len-2)隨機一個下標
            index = Math.abs(rd.nextInt() % len--);
            //將隨機到的數放入結果集
            result[i] = source[index];
            //將待選數組中被隨機到的數，用待選數組(len-1)下標對應的數替換
            source[index] = source[len];
        }
        return result;
	}  
    public static void main(String[] args){

    }
	
}