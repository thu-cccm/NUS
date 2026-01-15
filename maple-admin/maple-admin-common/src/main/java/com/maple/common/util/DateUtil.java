package com.maple.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    private DateUtil() {
    }

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDD = "yyMMdd";
    public static final String YYMMDDHHMMSS = "yyMMddHHmmss";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static List<String> allBetweenDate(String startDate, String endDate) {
        final List<String> allDate = new ArrayList<>();
        final SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        try {
            final Date dateStart = sdf.parse(startDate);
            final Date dateEnd = sdf.parse(endDate);
            final Calendar calStart = Calendar.getInstance();
            calStart.setTime(dateStart);
            final Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(dateEnd);
            calEnd.add(Calendar.DATE, 1);
            StringBuilder date = new StringBuilder();
            while (!calStart.equals(calEnd)) {
                date.append(calStart.get(Calendar.YEAR)).append("-");
                if ((calStart.get(Calendar.MONTH) + 1) < 10) {
                    date.append("0");
                }
                date.append((calStart.get(Calendar.MONTH) + 1)).append("-");
                if (calStart.get(Calendar.DAY_OF_MONTH) < 10) {
                    date.append("0");
                }
                date.append(calStart.get(Calendar.DAY_OF_MONTH));
                allDate.add(date.toString());
                calStart.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return allDate;
    }

    public static Date strToDate(String dateStr, String pattern) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            final Date datetime = dateFormat.parse(dateStr);
            final Calendar cal = Calendar.getInstance();
            cal.setTime(datetime);

            return cal.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToStr(Date date, String pattern) {
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String getCurrentDate(String formatType) {
        final Date now = Calendar.getInstance().getTime();
        return dateToStr(now, formatType);
    }

    public static Date getTime(int calendarType, int value) {
        final Calendar calDay = Calendar.getInstance();
        calDay.add(calendarType, value);
        return calDay.getTime();
    }

    public static Date getTime(Date time, int calendarType, int value) {
        final Calendar calDay = Calendar.getInstance();
        calDay.setTime(time);
        calDay.add(calendarType, value);
        return calDay.getTime();
    }

    public static Date getMonthAfter(Date date, int month) {
        try {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, month);
            date = calendar.getTime();
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static long minutesBetween(Date date1, Date date2) {

        long date1Time = 0;
        long date2Time = 0;
        if (date1 != null) {
            date1Time = date1.getTime();
        }
        if (date2 != null) {
            date2Time = date2.getTime();
        }
        return (date2Time - date1Time) / (1000 * 60);
    }

    public static Date addMonth(Date date, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();
    }

    public static Date addSecond(Date date, int second) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, second);
        return cal.getTime();
    }

    public static String dateToYyyyMmDd(Date date) {
        return dateToStr(date, YYYYMMDD);
    }

    public static String dateToYyMmDd(Date date) {
        return dateToStr(date, YYMMDD);
    }

    public static String dateToYyyyMmDdHhMmSs(Date date) {
        return dateToStr(date, YYYYMMDDHHMMSS);
    }
}
