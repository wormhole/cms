package net.stackoverflow.cms.util;

/**
 * 系统工具类
 *
 * @author 凉衫薄
 */
public class SysUtils {

    /**
     * 判断系统是不是windows
     *
     * @return true：win，false：other
     */
    public static Boolean isWin() {
        return System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0 ? true : false;
    }
}
