package com.thkmon.common.util;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	public static String encodeBase64FromByteArray(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] decodeBase64ToByteArray(String str) {
        return Base64.decodeBase64(str);
    }

	public static String encodeBase64(String str) {
		byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);
		return Base64.encodeBase64String(byteArray);
	}
		
	public static String decodeBase64(String str) {
		byte[] byteArray = Base64.decodeBase64(str);
		String str2 = new String(byteArray, StandardCharsets.UTF_8);
		return str2;
	}
}
