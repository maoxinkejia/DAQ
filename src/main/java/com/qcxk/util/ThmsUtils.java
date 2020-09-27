package com.qcxk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class ThmsUtils {
    public static List<String> imgSuffix = Arrays.asList("jpg", "jpeg", "bmp", "png", "gif", "tiff", "psd", "eps", "raw", "pdf", "png", "pxr", "mac");
    public static List<String> auSuffix = Arrays.asList("amr");
    public static List<String> viSuffix = Arrays.asList("mp4");

    /**
     * 获取本周的第一天
     *
     * @return String
     **/
    public static String getWeekStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取本周的最后一天
     *
     * @return String
     **/
    public static String getWeekEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time = cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }

    /**
     * 获取最近12个月，专门用于统计图表的X轴
     */
    public static HashMap<String, String> getLast12Months() {
        HashMap<String, String> hsmap = new HashMap<>();
        LocalDate today = LocalDate.now();
        for (long i = 0L; i < 12L; i++) {
            LocalDate localDate = today.minusMonths(i);
            String ss = localDate.toString().substring(0, 7);
            hsmap.put(ss, "0");
        }
        return hsmap;
    }

    public static String getDateAddDay(String format, int addNum) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, addNum);//如果把0修改为-1就代表昨天
        date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateString = format1.format(date);
        return dateString;
    }

    public static String getDateAddMin(String format, int addNum) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.MINUTE, addNum);//如果把0修改为-1就代表减一分钟
        date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateString = format1.format(date);
        return dateString;
    }

    public static String getDateAddHours(String format, int addNum) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.HOUR, addNum);//如果把0修改为-1就代表昨天
        date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateString = format1.format(date);
        return dateString;
    }

    public static String getDateAddHours(String time, String format, int addNum) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateString = null;
        try {
            Date date = format1.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendar.HOUR, addNum);//如果把0修改为-1就代表昨天
            date = calendar.getTime();
            dateString = format1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public static String getDate(String format) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 0);
        date = calendar.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        String dateString = format1.format(date);
        return dateString;
    }

    public static boolean validateList(List list) {
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean compareTime(String time1, String time2, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        Date ten = null;
        Date now = null;
        try {
            ten = format1.parse(time1);
            now = format1.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (ten.after(now));
    }

    public static String listToString(List<String> mList) {
        String convertedListStr = "";
        if (null != mList && mList.size() > 0) {
            String[] mListArray = mList.toArray(new String[mList.size()]);
            for (int i = 0; i < mListArray.length; i++) {
                if (i < mListArray.length - 1) {
                    convertedListStr += mListArray[i] + ",";
                } else {
                    convertedListStr += mListArray[i];
                }
            }
            return convertedListStr;
        } else {
            return "List is null!!!";
        }
    }

    public static String timeSub(String time1, String time2, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format1.parse(time1);
            date2 = format1.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date1.getTime() - date2.getTime();
        long minute = diff / 60 / 1000;
        return Math.abs(minute) + "";
    }

    public static String timeSubReSec(String time1, String time2, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format1.parse(time1);
            date2 = format1.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = date1.getTime() - date2.getTime();
        return (diff / 1000) + "";
    }
}
