package com.qcxk.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtils {
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yy = "yy";
    public static final String MM = "MM";
    public static final String dd = "dd";
    public static final String HH = "HH";
    public static final String mm = "mm";
    public static final String ss = "ss";
    public static final String WEEK = "";
    public static final String HH_MM_SS = "HH:mm:ss";


    public static String getMinuteHex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String hour = Integer.toHexString(cal.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toHexString(cal.get(Calendar.MINUTE));
        String second = Integer.toHexString(cal.get(Calendar.SECOND));

        hour = hour.length() == 1 ? "0" + hour : hour;
        minute = minute.length() == 1 ? "0" + minute : minute;
        second = second.length() == 1 ? "0" + second : second;

        return hour + minute + second;
    }

    public static String getWeekHex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int i = cal.get(Calendar.DAY_OF_WEEK);
        int i1 = i - 1 == 0 ? 7 : i - 1;
        return "0" + Integer.toHexString(i1);
    }

    public static String getDateHex(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String year = Integer.toHexString(cal.get(Calendar.YEAR) - 2000);
        String month = Integer.toHexString(cal.get(Calendar.MONTH) + 1);
        String day = Integer.toHexString(cal.get(Calendar.DAY_OF_MONTH));

        return year + "0" + month + (day.length() == 1 ? "0" + day : day);
    }

    public static Date getNextDate(String dateStr) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(yyyy_MM_dd);
        LocalDate localDate = LocalDate.parse(dateStr, fmt).plusDays(1);
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * change date into different date format
     */
    public static String format(Date date, String fmt) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
        return dateFmt.format(date);
    }

    public static String format(Date date, String fmt, String timeZone) {
        TimeZone tz = TimeZone.getTimeZone(timeZone);
        if (tz == null) {
            throw new RuntimeException(String.format("timeZone %s is not exist", timeZone));
        }

        SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
        dateFmt.setTimeZone(tz);
        return dateFmt.format(date);
    }

    public static Date parseDate(String dateStr, String dateFormat) throws ParseException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        return dateFormatter.parse(dateStr);
    }

    /**
     * 获取时间对应的毫秒数
     */
    public static long getTime(String dateStr, String fmt) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        Date date = format.parse(dateStr);
        return date.getTime();
    }

    /**
     * 获得当天日期
     */
    public static Date initDateByDay() throws ParseException {
        String dateStr = format(new Date(), yyyy_MM_dd);
        return parseDate(dateStr, yyyy_MM_dd);
    }

    /**
     * 日期转换为0点时刻
     */
    public static Date initDate(Date date) throws ParseException {
        String dateStr = format(date, yyyy_MM_dd);
        return parseDate(dateStr, yyyy_MM_dd);
    }

    /**
     * 获取在指定时区所对应的时间戳
     * <p>
     * 同样的时间，在不同的时区对应的时间戳不一样。
     * 比如 2019-07-25 09:00:00
     * 在东八区对应的时间戳为 1564016400000
     * 在东九区对应的时间戳为 1564012800000
     * <p>
     * 本方法做如下转换
     * (1564016400000,utc+9) => 1564012800000
     */
    private static long translateMillis(long epochMilli, String zoneId) {
        //根据epochMilli和serverZoneId得到localDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
        //根据localDateTime和zoneId得到纪元毫秒数
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of(zoneId));
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static LocalTime date2LocalTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalTime();
    }

    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * 获取时间区间内的日期，返回yyyyMMdd格式的日期列表
     *
     * @param start 开始日期时间
     * @param end   结束日期时间
     * @return yyyyMMdd
     */
    public static List<String> getDays(Date start, Date end) {


        // 返回的日期集合
        List<String> days = new ArrayList<String>();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar tempStart = Calendar.getInstance();
        if (start == null) {
            return days;
        }
        tempStart.setTime(start);
        Calendar tempEnd = Calendar.getInstance();
        if (end == null) {
            end = new Date();
        }
        tempEnd.setTime(end);
        while (!tempStart.after(tempEnd)) {
            days.add(dateFormat.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        return days;
    }

    /**
     * 规范化日期，规范成yyyy-MM-dd HH:mm:ss
     *
     * @param timestamp 秒级时间戳
     * @return
     */
    public static String timestamp2Datetime(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(timestamp * 1000));
    }

    /**
     * yyyy年MM月dd日 hh:mm:ss SSS
     *
     * @param millisecond 毫秒级时间戳
     * @return
     */
    public static String convertTimestampToDatetime(long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return format.format(date);
    }
}
