package net.stackoverflow.cms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间日期工具类
 *
 * @author 凉衫薄
 */
public class TimeUtils {

    /**
     * 日期转时间
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 时间转换日期
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parse(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(time);
    }

    /**
     * 获取日期格式路径
     *
     * @return
     */
    public static String pathWithDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("/yyyy/MM/dd/");
        return sdf.format(new Date());
    }
}
