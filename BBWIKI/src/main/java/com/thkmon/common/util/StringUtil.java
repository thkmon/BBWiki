package com.thkmon.common.util;

public class StringUtil {
	
	
	public static String parseString(Object obj) {
		if (obj == null) {
			return "";
		}
		
		String result = "";
		
		try {
			result = String.valueOf(obj);
			
		} catch (Exception e) {
			result = "";
		}
		
		return result;
	}
	
	
	public static String parseString(Object obj, String defaultStr) {
		if (obj == null) {
			return defaultStr;
		}
		
		String result = defaultStr;
		
		try {
			result = String.valueOf(obj);
			
		} catch (Exception e) {
			result = defaultStr;
		}
		
		return result;
	}
	
	
	public static int parseNumber(Object obj, int defaultNum) {
		if (obj == null) {
			return defaultNum;
		}
		
		int result = defaultNum;
		
		try {
			result = Integer.parseInt(String.valueOf(obj));
			
		} catch (Exception e) {
			result = defaultNum;
		}
		
		return result;
	}
	
	
	public static boolean checkEngNumber(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		String oneChar = null;
		int len = str.length();
		for (int i=0; i<len; i++) {
			oneChar = str.substring(i, i+1);
			if (oneChar == null) {
				return false;
			}
			
			if (!oneChar.matches("[a-zA-Z0-9]")) {
				return false;
			}
		}
		
		return true;
	}
	
	public static String remove(String str, String target) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		if (str.indexOf(target) > -1) {
			return str.replace(target, "");
		}
		
		return str;
	}
	
	public static String removeTag(String str) {
		if (str == null || str.length() == 0) {
			return "";
		}
		
		int loopCount = 0;
		
		while (loopCount < 1000) {
			int beginIndex = str.indexOf("<");
			if (beginIndex < 0) {
				// 여는 태그는 없어도 혹시나 닫는 태그가 있을지 모르니 제거하고 돌려준다.
				return str.replace(">", "&gt;");
			}
			
			int endIndex = str.indexOf(">", beginIndex + 1);
			if (endIndex < 0) {
				return str;
			}
			
			str = str.substring(0, beginIndex) + str.substring(endIndex + 1);
			loopCount++;
		}
		
		return str;
	}
	
	public static String getDateString(String dateNumber) {
		if (dateNumber == null || dateNumber.length() == 0) {
			return "";
		}
		
		if (dateNumber.indexOf(" ") > -1) {
			dateNumber = dateNumber.replace(" ", "");
		}
		
		StringBuffer buff = new StringBuffer();
		if (dateNumber.length() >= 4) {
			buff.append(dateNumber.substring(0, 4)).append("년 ");
		}
		
		if (dateNumber.length() >= 8) {
			buff.append(dateNumber.substring(4, 6)).append("월 ");
			buff.append(dateNumber.substring(6, 8)).append("일 ");
		}
		
		if (dateNumber.length() >= 12) {
			buff.append(dateNumber.substring(8, 10)).append("시 ");
			buff.append(dateNumber.substring(10, 12)).append("분 ");
		}
		
		if (dateNumber.length() >= 14) {
			buff.append(dateNumber.substring(12, 14)).append("초 ");
		}
		
		return buff.toString().trim();
	}
}
