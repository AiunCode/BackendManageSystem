package com.aiun.common.util;

import java.math.BigDecimal;

/**
 * @author lenovo
 */
public class BigDecimalUtil {

    public static BigDecimal add(double v1, double v2) {
        String strV1 = Double.toString(v1);
        String strV2 = Double.toString(v2);

        BigDecimal b1 = new BigDecimal(strV1);
        BigDecimal b2 = new BigDecimal(strV2);

        return b1.add(b2);
    }

    public static BigDecimal sub(double v1, double v2) {
        String strV1 = Double.toString(v1);
        String strV2 = Double.toString(v2);

        BigDecimal b1 = new BigDecimal(strV1);
        BigDecimal b2 = new BigDecimal(strV2);

        return b1.subtract(b2);
    }

    public static BigDecimal mul(double v1, double v2) {
        String strV1 = Double.toString(v1);
        String strV2 = Double.toString(v2);

        BigDecimal b1 = new BigDecimal(strV1);
        BigDecimal b2 = new BigDecimal(strV2);

        return b1.multiply(b2);
    }

    public static BigDecimal div(double v1, double v2) {
        String strV1 = Double.toString(v1);
        String strV2 = Double.toString(v2);

        BigDecimal b1 = new BigDecimal(strV1);
        BigDecimal b2 = new BigDecimal(strV2);
        //采用四舍五入模式,并且保留2位小数
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }
}
