package com.bing.news.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateUtil {

    /**
     * 中文版
     */
    private final static String dayNamesCN[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    /**
     * 英文版
     */
    private final static String dayNamesEN[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    /**
     * 包含年-秒
     */
    public static final String FOR_YEAR_SECOND = "yyyy-MM-dd HH:mm:ss";
    /**
     * 包含年-日
     */
    public static final String FOR_YEAR_DAY = "yyyy-MM-dd";
    /**
     * 包含月-日
     */
    public static final String FOR_MONTH_DAY = "MM-dd";
    /**
     * 包含月-分
     */
    public static final String FOR_MONTH_MINUTE = "MM-dd HH:mm";
    /**
     * 包含时-秒
     */
    public static final String FOR_HOUR_SECOND = "HH:mm:ss";
    /**
     * 包含时-分
     */
    public static final String FOR_HOUR_MINUTE = "HH:mm";
    /**
     * 包含年-秒，作为文件名
     */
    public static final String FOR_FILENAME = "yyyyMMdd_HHmmssSSS";

    /**
     * 获取制定格式的现在的日期
     *
     * @param format
     * @return
     * @author YOLANDA
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        String dateString = formatter.format(curDate);
        return dateString;
    }

    /**
     * 从指定的时间获取制定格式的日期
     *
     * @param date
     * @param format
     * @return
     * @author YOLANDA
     */
    public static String getDateTime(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取制定日期是星期几
     *
     * @param date
     * @return
     * @author YOLANDA
     */
    public static String getWeek(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat(FOR_YEAR_DAY);
        Calendar cal = Calendar.getInstance();
        int weekno = 0;
        try {
            cal.setTime(formatter.parse(date));
            weekno = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (weekno < 0)
                weekno = 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        result = dayNamesCN[weekno];
        return result;
    }

    /**
     * 把字符串日期按照制定格式，格式为Date
     *
     * @param date
     * @param format
     * @return
     * @author YOLANDA
     */
    public static Date formatStr2Date(String date, String format) {
        if (date.equals("")) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Date date1 = null;
        try {
            date1 = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 把年-秒转为Calendar
     *
     * @param dates
     * @return
     * @author YOLANDA
     */
    public static Calendar str2Calendar(String[] dates) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(dates[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(dates[1]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dates[2]));
        cal.set(Calendar.HOUR, Integer.parseInt(dates[3]));
        cal.set(Calendar.MINUTE, Integer.parseInt(dates[4]));
        cal.set(Calendar.SECOND, Integer.parseInt(dates[5]));
        return cal;
    }

    /**
     * 比较时间大->小
     *
     * @param object1 日期1
     * @param object2 日期2
     * @return date1 = date2 返回 0 date1 > date2 返回 1 date1 < date2 返回 -1
     */
    public static int compare(String object1, String object2, String fmt) {
        Date date1, date2;
        int result = 0;
        date1 = formatStr2Date(object1.toString(), fmt);
        date2 = formatStr2Date(object2.toString(), fmt);
        if (date1.equals(date2)) {
            return 0;
        }
        if (date1.before(date2)) {
            result = -1;
        } else {
            result = 1;
        }
        return result;
    }

    /**
     * 比较两个年份的大小
     *
     * @param year1
     * @param year2
     * @return
     * @author YOLANDA
     */
    public static int compareYear(String year1, String year2) {
        int object1 = Integer.parseInt(year1);
        int object2 = Integer.parseInt(year2);
        if (object1 == object2)
            return 0;
        if (object1 < object2)
            return -1;
        else
            return 1;
    }

    /**
     * 判断日期是否是现在之前的
     *
     * @param date
     * @return
     * @author YOLANDA
     */
    public static boolean isSmallThanNow(String date) {
        boolean result = false;
        date = date.replaceAll("\\/", "-");
        String dateString = getDateTime(FOR_YEAR_SECOND);
        if (compare(dateString, date, FOR_YEAR_SECOND) > 0)
            result = true;
        return result;
    }

    /**
     * 计算两个日期型的时间相差多少时间
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public static String twoDateDistance(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        long year = timeLong / (24 * 60 * 60 * 1000 * 365);
        String yearStr = String.valueOf(year);
        long month = timeLong % (24 * 60 * 60 * 1000 * 365) / (24 * 60 * 60 * 1000 * 30);
        String monthStr = String.valueOf(month);
        long day = timeLong % (24 * 60 * 60 * 1000 * 365) % (24 * 60 * 60 * 1000 * 30) / (24 * 60 * 60 * 1000);
        String dayStr = String.valueOf(day);
        long hour = timeLong % (24 * 60 * 60 * 1000 * 365) % (24 * 60 * 60 * 1000 * 30) % (24 * 60 * 60 * 1000) / (60 * 60 * 1000);
        String hourStr = String.valueOf(hour);
        long minute = timeLong % (24 * 60 * 60 * 1000 * 365) % (24 * 60 * 60 * 1000 * 30) % (24 * 60 * 60 * 1000) % (60 * 60 * 1000) / (60 * 1000);
        String minuteStr = String.valueOf(minute);
        String returnStr = null;
        if (year != 0) {
            returnStr = yearStr + "年";
        } else if (month != 0) {
            returnStr = monthStr + "月";
        } else if (day != 0) {
            returnStr = dayStr + "天";
        } else if (hour != 0) {
            returnStr = hourStr + "小时";
        } else if (minute != 0) {
            returnStr = minuteStr + "分钟";
        }
        return returnStr == null ? "1秒 前" : returnStr + " 前";
    }

    /**
     * 比较小时-秒的大小
     *
     * @param m1
     * @param m2
     * @return
     * @author YOLANDA
     */
    public static int compareHs(String m1, String m2) {
        m1 = m1.replace(":", "");
        m2 = m2.replace(":", "");
        int object1 = Integer.parseInt(m1);
        int object2 = Integer.parseInt(m2);
        if (object1 == object2)
            return 0;
        if (object1 < object2)
            return -1;
        else
            return 1;
    }

    /**
     * 集合内时间排序
     *
     * @param arrayList
     * @return
     * @author YOLANDA
     */
    public static List<String> InsertionSort(List<String> arrayList) {
        String date1, date2, dateTemp;
        for (int i = 0; i < arrayList.size(); i++) {
            date1 = arrayList.get(i);
            for (int j = i + 1; j < arrayList.size(); j++) {
                date2 = arrayList.get(j);
                if (compare(date1, date2, FOR_HOUR_MINUTE) == 1) {
                    dateTemp = date1;
                    arrayList.set(i, date2);
                    arrayList.set(j, dateTemp);
                }
            }
        }
        return arrayList;
    }

    public static enum Unit {
        yyyy, MM, dd, HH, mm, ss
    }

    /**
     * 取日期增加后的值
     *
     * @param date     原日期
     * @param state    增加单位
     * @param addvalue 增加值
     * @return
     */
    public static String date2Add(String date, Unit state, int addvalue) {
        String result = "";
        SimpleDateFormat formatter = new SimpleDateFormat(FOR_YEAR_SECOND);
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(formatter.parse(date));
            if (state.equals(Unit.yyyy)) {
                cal.add(Calendar.YEAR, addvalue);
            } else if (state.equals(Unit.MM)) {
                cal.add(Calendar.MONTH, addvalue);
            } else if (state.equals(Unit.dd)) {
                cal.add(Calendar.DAY_OF_MONTH, addvalue);
            } else if (state.equals(Unit.HH)) {
                cal.add(Calendar.HOUR, addvalue);
            } else if (state.equals(Unit.mm)) {
                cal.add(Calendar.MINUTE, addvalue);
            } else if (state.equals(Unit.ss)) {
                cal.add(Calendar.SECOND, addvalue);
            }

            Date fdate = cal.getTime();
            result = getDateTime(fdate, FOR_YEAR_SECOND);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
