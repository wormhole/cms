package net.stackoverflow.cms.common;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.security.CmsUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;

/**
 * Controller基类
 *
 * @author 凉衫薄
 */
@Slf4j
public class BaseController {

    /**
     * 获取UserDetails
     *
     * @return
     */
    protected CmsUserDetails getUserDetails() {
        return (CmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * double保留n位小数输出
     *
     * @param num
     * @param n
     * @return
     */
    protected double doubleFormat(double num, int n) {
        BigDecimal b = new BigDecimal(num);
        return b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
