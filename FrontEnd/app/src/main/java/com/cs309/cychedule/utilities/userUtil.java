package com.cs309.cychedule.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class userUtil {
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	public static boolean hasSpecialChar(String str) {
		String regEx = "[ _`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}
}
