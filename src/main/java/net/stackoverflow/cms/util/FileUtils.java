package net.stackoverflow.cms.util;

import net.stackoverflow.cms.constant.FileTypeConst;

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

    /**
     * 获取文件类型
     *
     * @param ext
     * @return
     */
    public static Integer getType(String ext) {
        if (FileTypeConst.IMAGE.contains(ext.toLowerCase())) {
            return 1;
        } else if (FileTypeConst.VIDEO.contains(ext.toLowerCase())) {
            return 2;
        } else if (FileTypeConst.AUDIO.contains(ext.toLowerCase())) {
            return 3;
        } else {
            return 0;
        }
    }
}
