package com.wgh.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeHandler {
	 public static String TimeStamp2Date(long l, String formats) {
	        if(formats.equals(""))
	            formats = "yyyy-MM-dd HH:mm:ss";
	        Long timestamp = Long.parseLong((l * 1000)+"");
	        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
	        return date;
	    }
	 
	 public static long Date2TimeStamp(String dateStr, String format) {
	        try {
	            SimpleDateFormat sdf = new SimpleDateFormat(format);
	            return (long) (sdf.parse(dateStr).getTime() / 1000);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	 
	 public static long getNowTimeStamp() {
	        long time = System.currentTimeMillis();
	        return (long) time / 1000;
	      
	    }
	 
	 public static void main(String arg[])
	 {
		 System.out.println("System time test begin!");
		 System.out.println("unix time stamp="+getNowTimeStamp());
		 System.out.println("Time stamp to date:"+TimeStamp2Date(getNowTimeStamp(),"yyyy-MM-dd HH:mm:ss"));
		 System.out.println("Date to stamp:"+Date2TimeStamp("2017-09-02 10:30:21","yyyy-MM-dd HH:mm:ss"));
	 }
	 
}
