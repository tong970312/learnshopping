package com.neuedu.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    /**
     * 将时间转成字符串
     */
    private static  final String STANDARD_FORMATE="yyyy-MM-dd HH:mm:ss";

    public static String dateToString(Date date,String formate){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formate);
    }
     public static String dateToString(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMATE);
    }

    /**
     * 将字符串转成时间
     */
    public static  Date stringToDate(String str){
        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(STANDARD_FORMATE);
        DateTime dateTime = dateTimeFormat.parseDateTime(str);
        return dateTime.toDate();
    }
     public static  Date stringToDate(String str,String format){
         DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormat.parseDateTime(str);
        return dateTime.toDate();
    }


}
