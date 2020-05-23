package net.stackoverflow.cms.util;

import java.math.BigDecimal;

/**
 * 格式化工具类
 *
 * @author 凉衫薄
 */
public class FormatUtils {

    /**
     * double保留n位小数输出
     *
     * @param num
     * @param n
     * @return
     */
    public static double doubleFormat(double num, int n) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
