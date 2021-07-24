package com.aiun.common.util;

import org.apache.tomcat.util.buf.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * 日期和字符串转换的工具类
 * @author lenovo
 */
public class DateUtils {
    /**
     * 设置为常量，提高可扩展性
     */
    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static Date stringToDate(String dateTimeStr, String formatStr) {
        org.joda.time.format.DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime(date);

        return dateTime.toString(formatStr);
    }

    public static Date stringToDate(String dateTimeStr) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);

        return dateTime.toDate();
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return "";
        }
        DateTime dateTime = new DateTime(date);

        return dateTime.toString(STANDARD_FORMAT);
    }
}
