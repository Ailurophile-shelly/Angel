package com.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateChange {
    /*
    将时间戳转换成日期格式字符,精确到秒
    */
    public static String timeStamp2Date(String seconds,String format){
        //秒数为0
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        //日期格式转换
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    //日期格式字符串转换成时间字符串
    public static String date2TimeStamp(String date_str,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    //日期格式字符串转换成时间秒数
    public static long dateTimeStamp(String datr_str,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(datr_str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取当天日期时间
    public static Long getDate(){
        int y,m,d;//年月日
        Calendar calendar = Calendar.getInstance();//获取系统时间
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH);
        d = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long l = 0;
        try {
            l = sdf.parse(y+"-"+(m+1)+"-"+d).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    //获取当前时间戳，精确到秒
    public static String timeStamp(){
        long time = System.currentTimeMillis();//获取系统当前时间
        String t = String.valueOf(time);
        return t;
    }

    //计算两个日期相差天数，返回X周X天
    public static String getTime(long endTime,long startTime){
        int M = (int)((endTime - startTime) / 86400000 / 7);//周
        int D = (int)((endTime - startTime) / 86400000 % 7);//天
        if (M != 0 && D != 0){
            return M + "周" + D + "天";
        }else if (M == 0 && D != 0){
            return  D + "天";
        }else if (M != 0 && D == 0){
            return M + "周" ;
        }
        return "1天";
    }
    public static void main(String[] args) {
        String timeStamp = timeStamp();
        System.out.println("timeStamp="+timeStamp);

        String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
        System.out.println("date="+date);

        String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println(timeStamp2);
    }
}
