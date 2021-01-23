package com.thkmon.bbwiki.doc.helper;

import java.net.URLEncoder;

public class WikiDocTextHelper {

	
	public static String replaceWikiMarkForLink(String oldTextObj) {
		if (oldTextObj == null || oldTextObj.length() == 0) {
			return "";
		}
		
		int loopCnt = 0;
		
		// 루프값 초기화
		loopCnt = 0;
		
		while (true) {
			int beginIndex = oldTextObj.indexOf("[[");
			if (beginIndex < 0) {
				return oldTextObj;
			}
			
			int endIndex = oldTextObj.indexOf("]]", beginIndex + 2);
			if (endIndex < 0) {
				return oldTextObj;
			}
			
			String slice = oldTextObj.substring(beginIndex + 2, endIndex);
			int colonIndex = slice.indexOf(":");
			if (colonIndex > -1) {
				String leftSlice = slice.substring(0, colonIndex);
				String rightSlice = slice.substring(colonIndex + 1);
				
				try {
					String newSlice = "<a href=\"/wiki/doc/read.do?doc_link=" + URLEncoder.encode(rightSlice.trim(), "UTF-8") + "\">" + leftSlice.trim() + "</a>";
					oldTextObj = oldTextObj.substring(0, beginIndex) + newSlice + oldTextObj.substring(endIndex + 2);
				} catch (Exception e) {
					// 무시
				}
				
			} else {
				try {
					String newSlice = "<a href=\"/wiki/doc/read.do?doc_link=" + URLEncoder.encode(slice.trim(), "UTF-8") + "\">" + slice.trim() + "</a>";
					oldTextObj = oldTextObj.substring(0, beginIndex) + newSlice + oldTextObj.substring(endIndex + 2);
				} catch (Exception e) {
					// 무시
				}
			}

			// 무한루프 방지.
			loopCnt++;
			if (loopCnt > 100) {
				break;
			}
		}
		
		return oldTextObj;
	}
	
	
	public static String replaceWikiMarkForTitle(String oldTextObj, int count) {
		// 루프값 초기화
		int loopCnt = 0;
		
		String leftMark = "";
		String rightMark = "";
		String tagName = "";
		
		if (count == 2) {
			// 소제목
			leftMark = "[==";
			rightMark = "==]";
			tagName = "h2";
			
		} else if (count == 3) {
			// 중제목
			leftMark = "[===";
			rightMark = "===]";
			tagName = "h3";
			
		} else if (count == 4) {
			// 대제목
			leftMark = "[====";
			rightMark = "====]";
			tagName = "h4";
			
		} else {
			return oldTextObj;
		}
		
		while (true) {
			int beginIndex = oldTextObj.indexOf(leftMark);
			if (beginIndex < 0) {
				return oldTextObj;
			}
			
			int leftLength = leftMark.length();
			int endIndex = oldTextObj.indexOf(rightMark, beginIndex + leftLength);
			if (endIndex < 0) {
				return oldTextObj;
			}
			
			String slice = oldTextObj.substring(beginIndex + leftLength, endIndex);
			
			String newSlice = "<" + tagName + ">" + slice.trim() + "</" + tagName + ">";
			oldTextObj = oldTextObj.substring(0, beginIndex) + newSlice + oldTextObj.substring(endIndex + leftLength);
			
			// 무한루프 방지.
			loopCnt++;
			if (loopCnt > 100) {
				break;
			}
		}
		
		return oldTextObj;
	}
	
	
	public static String replaceWikiMarkForLine(String oldTextObj) {
		// 루프값 초기화
		int loopCnt = 0;
		
		String leftMark = "";
		
		// 대제목
		leftMark = "[----]";
		
		while (true) {
			int beginIndex = oldTextObj.indexOf(leftMark);
			if (beginIndex < 0) {
				return oldTextObj;
			}
			
			oldTextObj = oldTextObj.substring(0, beginIndex) + "<hr></hr>" + oldTextObj.substring(beginIndex + 6);
			
			// 무한루프 방지.
			loopCnt++;
			if (loopCnt > 100) {
				break;
			}
		}
		
		return oldTextObj;
	}
}
