package com.neuedu.util;

import java.math.BigDecimal;

/**
 * 价格计算工具类
 */
public class BigDecimalUtils {
    /**
     * 加法
     */
    public  static BigDecimal add(double d1,double d2){
        //转换
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecima2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.add(bigDecima2);
    }
     /**
     * 减法
     */
    public  static BigDecimal sub(double d1,double d2){
        //转换
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecima2 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.subtract(bigDecima2);
    }
       /**
         * 乘法
         */
        public  static BigDecimal mul(double d1,double d2){
            //转换
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
            BigDecimal bigDecima2 = new BigDecimal(String.valueOf(d2));
            return bigDecimal.multiply(bigDecima2);
        }
       /**
         * 除法
         */
        public  static BigDecimal div(double d1,double d2){
            //转换
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
            BigDecimal bigDecima2 = new BigDecimal(String.valueOf(d2));
                                        /*保留两位小数  四舍五入*/
            return bigDecimal.divide(bigDecima2,2,BigDecimal.ROUND_HALF_UP);
        }

}
