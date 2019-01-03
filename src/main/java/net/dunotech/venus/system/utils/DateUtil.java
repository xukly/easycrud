/*
 * ====================================================================
 * OA系统
 * ====================================================================
 */

package net.dunotech.venus.system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具
 *
 * @author dunoinfo
 * @date 2016/11/25
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * yyyy/MM/dd
     */
    public static final String PATTERN_YMD = "yyyy/MM/dd"; // pattern_ymd

    /**
     * yyyy/MM/dd
     */
    public static final String PATTERN_YMD_HYPHEN = "yyyy-MM-dd"; // pattern_ymd

    /**
     * yyyyMMdd
     */
    public static final String PATTERN_YYYYMMDD = "yyyyMMdd"; // pattern_ymd
    
    /**
     * yyyyyMMddHHmmss
     */
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss"; // pattern_ymd

    /**
     * yyyy/MM/dd HH:mm:ss
     */
    public static final String PATTERN_YMD_HMS = "yyyy/MM/dd HH:mm:ss"; // pattern_ymdtime

    /**
     * yyyy/MM/dd HH:mm
     */
    public static final String PATTERN_YMD_HM = "yyyy/MM/dd HH:mm"; // pattern_ymdtime

    /**
     * yyyy/MM/dd HH:mm:ss:SSS
     */
    public static final String PATTERN_YMD_HMS_S = "yyyy/MM/dd HH:mm:ss:SSS"; // pattern_ymdtimeMillisecond

    /**
     * yyyyMMddHHmmssSSS
     */
    public static final String PATTERNYMDHMS_S2 = "yyyyMMddHHmmssSSS"; // pattern_ymdtimeMillisecond

    /**
     * yyyy
     */
    public static final String PATTERN_YYYY = "yyyy"; // pattern_ymd

    /**
     * yyyy/MM
     */
    public static final String PATTERN_YYYY_MM = "yyyy/MM"; // pattern_ymd

    /**
     * yyyyMM
     */
    public static final String PATTERN_YYYYMM = "yyyyMM"; // pattern_ymd

    /**
     * EEE MMM dd HH:mm:ss Z yyyy
     */
    public static final String PATTERN_EEE_MMM = "EEE MMM dd HH:mm:ss Z yyyy"; // pattern_ymd

    /**
     * yyyyMMddHH0000
     */
    public static final String PATTERN_YYYYMMDDHH0000 = "yyyyMMddHH0000"; // pattern_ymdhh0000
    
    private DateUtil(){
        
    }
    
    /**
     * 格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化
     *
     * @param date
     * @return
     */
    public static Date format(String date) {
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YMD);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date format(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.UK);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return format(date);
        }
    }

    /**
     * 格式化
     *
     * @param date
     * @param parsePattern
     * @param returnPattern
     * @return
     */
    public static String format(String date, String parsePattern, String returnPattern) {
        return format(parse(date, parsePattern), returnPattern);
    }

    /**
     * 解析
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 解析
     *
     * @param dateStr
     * @return
     */
    public static Date parse(String dateStr) {
        try {
            return DateFormat.getDateTimeInstance().parse(dateStr);
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 日期区间分割
     *
     * @param start
     * @param end
     * @param splitCount
     * @return
     */
    public static List<Date> getDateSplit(Date start, Date end, long splitCount) {
        long startTime = start.getTime();
        long endTime = end.getTime();
        long between = endTime - startTime;

        long count = splitCount - 1l;
        long section = between / count;

        List<Date> list = new ArrayList<Date>();
        list.add(start);

        for (long i = 1l; i < count; i++) {
            long time = startTime + section * i;
            Date date = new Date();
            date.setTime(time);
            list.add(date);
        }

        list.add(end);

        return list;
    }

    public static int countMonths(Date date1, Date date2) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);

        // 开始日期若小月结束日期
        if (year < 0) {
            year = -year;
            return year * 12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        }

        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    public static int countYears(Date date1, Date date2) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(date1);
        c2.setTime(date2);
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        return year + 1;
    }

    /**
     * 返回两个日期之间隔了多少天
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDateDaySpace(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / (60 * 60 * 24 * 1000));
    }

    /**
     * 时间计算
     * 参数说明：1：当天 ，2：近一周，3：近一月，4：近三月，5：近一年
     * @param i
     * @return
     */
    public static String searchDateFormate(int i) {
        String returnDate;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        switch (i) {
            case 1: // 当天
                returnDate = sf.format(new Date());
                break;
            case 2:// 近一周
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -7);
                returnDate = sf.format(cal.getTime());
                break;
            case 3:// 近一月
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -1);
                returnDate = sf.format(cal.getTime());
                break;
            case 4: // 近三月
                cal.setTime(new Date());
                cal.add(Calendar.MONTH, -3);
                returnDate = sf.format(cal.getTime());
                break;
            case 5: // 近一年
                cal.setTime(new Date());
                cal.add(Calendar.YEAR, -1);
                returnDate = sf.format(cal.getTime());
                break;
            default:
                returnDate=null;
        }
        return returnDate;
    }

    /**
     * 获取当前时间
     *
     * @param partten
     * @return
     */
    public static String getCurrentDate(String partten) {
        SimpleDateFormat sf = new SimpleDateFormat(partten);
        return sf.format(new Date());
    }

    /**
     * 数据库只认java.sql.*
     *
     * @return
     */
    public static Timestamp getSqlTimestamp() {
        return getSqlTimestamp(new Date().getTime());
    }

    /**
     * 数据库只认java.sql.*
     *
     * @param date
     * @return
     */
    public static Timestamp getSqlTimestamp(Date date) {
        if (null == date) {
            return getSqlTimestamp();
        }
        return getSqlTimestamp(date.getTime());
    }

    /**
     * 数据库只认java.sql.*
     *
     * @param time
     * @return
     */
    public static Timestamp getSqlTimestamp(long time) {
        return new Timestamp(time);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 以时间段分割为起始的时间取得
     *
     * @param date
     * @param splitMiu
     * @return
     */
    public static Date getDateSplitByMiu(Date date, int splitMiu) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE, (minute / splitMiu) * splitMiu);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 时间以分钟数增加
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date addDateByMiu(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minuteIn = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.MINUTE,minuteIn + minute);
        return calendar.getTime();
    }

    /**
     * 时间以天增加
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDateByDay(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayIn = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,dayIn + day);
        return calendar.getTime();
    }

    /**
     * 根据日期月份，获取月份的开始和结束日期
     *
     * @param date
     * @return
     */
    public static String getMonthDateSplit(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 得到前一个月的第一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date start = calendar.getTime();

        // 得到前一个月的最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date end = calendar.getTime();
        return start + "&" + end;
    }

    /**
     * 根据日期月份，获取年的开始和结束日期
     *
     * @param date
     * @return
     */
    public static String getYearDateSplit(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 得到一月的第一天
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        Date start = calendar.getTime();

        // 得到12月的最后一天
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DATE, 31);
        Date end = calendar.getTime();
        return start + "&" + end;
    }

    /**
     * 时间以月增加
     *
     * @param date
     * @param month
     * @return
     */
    public static Date addDateByMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int monthCal = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month + monthCal);
        return calendar.getTime();
    }

    /**
     * 时间以小时增加
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date addDateByHour(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hourCal = calendar.get(Calendar.HOUR);
        calendar.set(Calendar.HOUR, hour + hourCal);
        return calendar.getTime();
    }

    /**
     * 时间以年增加
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addDateByYear(Date date, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int yearCal = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year + yearCal);
        return calendar.getTime();
    }

    /**
     * 返回两个日期之间隔了多少天，包含开始、结束时间
     *
     * @param start
     * @param end
     * @return
     */
    public static List<String> getDaySpaceDate(Date start, Date end) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(start);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(end);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        List<String> dateList = new LinkedList<String>();

        long dayCount = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
        if (dayCount < 0) {
            return dateList;
        }

        dateList.add(format(fromCalendar.getTime(), PATTERN_YMD));

        for (int i = 0; i < dayCount; i++) {
            fromCalendar.add(Calendar.DATE, 1);// 增加一天
            dateList.add(format(fromCalendar.getTime(), PATTERN_YMD));
        }

        return dateList;
    }

    /**
     * 一天的开始时间
     */
    public static Date getDateStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 一天的开始时间
     */
    public static Date getDateStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 一天的结束时间
     */
    public static Date getDateEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        return calendar.getTime();
    }

    /**
     * 一天的结束时间
     */
    public static Date getDateEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        return calendar.getTime();
    }

    /**
     * 获取时间： 时分
     *
     * @param start
     * @return
     */
    public static String formatHM(Date start) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        int minute = calendar.get(Calendar.MINUTE);
        String minuteStr = String.valueOf(minute);
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + minuteStr;
    }

    /**
     * 时间去除秒，分钟以00默认
     */
    public static String getDateMinte(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 00);
        SimpleDateFormat format = new SimpleDateFormat(PATTERN_YMD_HM);
        return format.format(calendar.getTime());
    }

    /**
     * 返回两个日期之间隔了多少小时
     *
     * @param start
     * @param end
     * @return
     */
    public static int getDateHourSpace(Date start, Date end) {
        return (int) ((end.getTime() - start.getTime()) / (60 * 60 * 1000));
    }
    
    public static long dateDiff(Date start, Date end, String pattern) {
        Date startDate = format(format(start, pattern), pattern);
        Date endDate = format(format(end, pattern), pattern);
        if(endDate!=null&&startDate!=null){
            return endDate.getTime() - startDate.getTime();
        }
        return 0;
    }

}
