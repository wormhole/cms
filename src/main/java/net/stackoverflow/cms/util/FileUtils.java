package net.stackoverflow.cms.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件工具类
 *
 * @author 凉衫薄
 */
public class FileUtils {

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
