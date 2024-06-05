package com.zhang.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhang
 * &#064;date  2024/2/10
 * &#064;Description  时间戳转换标准时间工具类
 */
public class TimeChangeUtils {
    public static String timeStampDate(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
