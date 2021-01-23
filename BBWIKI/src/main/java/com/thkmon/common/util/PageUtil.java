package com.thkmon.common.util;

public class PageUtil {

	
	public static int getTotalPage(int totalCount, int countInPage) {
		if (totalCount < 1) {
			return 0;
		}
		
		if (countInPage < 1) {
			return 0;
		}
		
		int remain = totalCount % countInPage;
		int totalPage = (totalCount - remain) / countInPage;
		if (remain > 0) {
			totalPage = totalPage + 1;
		}
		
		return totalPage;
	}
}
