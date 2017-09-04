package com.wgh.model;
import java.security.MessageDigest;
import java.util.*;

public class GlobalObject {
	public static Map UserValidateMap = new HashMap();
	
	public static boolean JudgeValid(String username,String jessonid)
	{
		if(UserValidateMap.containsKey(username) && ((String)UserValidateMap.get(username)).equals(jessonid))
			return true;
		return false;
	}
	
	public static void StoreMap(String username,String jessonid)
	{
		UserValidateMap.put(username, jessonid);
	}
	
	public static String GenerateJessonid(String username)
	{
		Random random = new Random();
		String jessonid=username+random.nextInt(1000000000);
		jessonid=MD5(jessonid);
		return jessonid;
	}
	
	
	public static String MD5(String buf) {  
	    try {  
	        MessageDigest digist = MessageDigest.getInstance("MD5");  
	        byte[] rs = digist.digest(buf.getBytes("UTF-8"));  
	        StringBuffer digestHexStr = new StringBuffer();  
	        for (int i = 0; i < 16; i++) {  
	            digestHexStr.append(byteHEX(rs[i]));  
	        }  
	        return digestHexStr.toString();  
	    } catch (Exception e) {  
	        System.out.println(e);
	    }  
	    return "";
	}
	
	public static String byteHEX(byte ib) {  
	    char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
	    char[] ob = new char[2];  
	    ob[0] = Digit[(ib >>> 4) & 0X0F];  
	    ob[1] = Digit[ib & 0X0F];  
	    String s = new String(ob);  
	    return s;  
	}  
	
	public static void main(String val[])
	{
	}
	
	
}
