package com.thkmon.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class HttpUtil {
	public static String getIpAddress() {
		
		try {
//			InetAddress ip = InetAddress.getLocalHost();  
//			System.out.println("Host Name = [" + ip.getHostName() + "]");
//			System.out.println("Host Address = [" + ip.getHostAddress() + "]");
			
			
			Enumeration<NetworkInterface> nienum = NetworkInterface.getNetworkInterfaces();

	        while (nienum.hasMoreElements()) {

	        	NetworkInterface ni = nienum.nextElement();

	        	Enumeration<InetAddress> kk = ni.getInetAddresses();

	            while (kk.hasMoreElements()) {

	    			InetAddress inetAddress = (InetAddress) kk.nextElement();

	    			// System.out.println(inetAddress.getHostName()+" : "+inetAddress.getHostAddress());
	    			
	    			String address = inetAddress.getHostAddress();
	    			
	    			if (address.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}")) {
	    				if (!address.equals("127.0.0.1")) {
	    					return address;
	    				}
	    			}

	    		}

	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "unknown";
	}
}