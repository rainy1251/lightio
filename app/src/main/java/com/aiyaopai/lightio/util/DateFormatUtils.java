package com.aiyaopai.lightio.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtils {

    public static String formatDate(String dateString) {
        String t = dateString.replace("T", "");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
        long timeMs = dateTimeMs(t);
        Date d1 = new Date(timeMs);
        String t1 = dateFormat.format(d1);
        return t1;
    }

    public static long dateTimeMs(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (Exception e) {

        }
        return returnMillis;
    }

    public static String formatMils(Long millSec) {
        Date date = new Date(millSec);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
        return sdf.format(date);
    }

    public static String getDate(Date date,String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(date);
    }

    public static String getCurDate(String pattern) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date());
    }
}
