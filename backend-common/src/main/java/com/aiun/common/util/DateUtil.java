package com.aiun.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期和字符串转换的工具类
 * @author lenovo
 */
public class DateUtil {
    /**
     * 设置为常量，提高可扩展性
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";
//    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    // 1.将日期转换成字符串
    public static String dateToString(Date date, String pattern) {
        // 判断pattern是否为空,利用工具类
        if (pattern.isEmpty()) {
            // 如果为空，设置默认格式
            pattern = DEFAULT_PATTERN;
        }

        SimpleDateFormat sd = new SimpleDateFormat();
        sd.applyPattern(pattern);
        String res = sd.format(date);
        return res;
    }

    // 重载，只传一个参数
    public static String dateToString(Date date) {
        return DateUtil.dateToString(date, "DEFAULT_PATTERN");
    }

    // 2.字符串转成日期
    public static Date stringToDate(String str, String pattern) throws ParseException {
        // 判断pattern是否为空,利用工具类
        if (pattern.isEmpty()) {
            // 如果为空，设置默认格式
            pattern = DEFAULT_PATTERN;
        }

        SimpleDateFormat sd = new SimpleDateFormat();
        sd.applyPattern(pattern);
        Date res = sd.parse(str);
        return res;
    }
}
