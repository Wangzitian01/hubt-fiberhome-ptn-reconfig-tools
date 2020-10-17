package cn.edu.hbut.hfcrt.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static String BigDecimalToIntNum(String str){
		double num =Double.parseDouble(str);
		BigDecimal bd=new BigDecimal(num);			
		return bd.toPlainString();
		
	}

}
