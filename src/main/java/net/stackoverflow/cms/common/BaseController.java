package net.stackoverflow.cms.common;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.security.CmsUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controller基类
 *
 * @author 凉衫薄
 */
@Slf4j
public class BaseController {

    private final String telRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    private final String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private final Pattern telPattern = Pattern.compile(telRegex);
    private final Pattern emailPattern = Pattern.compile(emailRegex);

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

    /**
     * 校验电话号码
     *
     * @param telephone
     * @return
     */
    protected boolean validateTelephone(String telephone) {
        if (telephone == null) {
            return false;
        }
        Matcher m = telPattern.matcher(telephone);
        return m.matches();
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    protected boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }

    /**
     * 校验密码
     *
     * @param password
     * @return
     */
    protected boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        } else {
            return password.length() >= 6;
        }
    }
}
