package net.stackoverflow.cms.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据校验工具
 *
 * @author 凉衫薄
 */
public class ValidateUtils {

    private static final String telRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
    private static final String emailRegex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final Pattern telPattern = Pattern.compile(telRegex);
    private static final Pattern emailPattern = Pattern.compile(emailRegex);

    /**
     * 校验电话号码
     *
     * @param telephone
     * @return
     */
    public static boolean validateTelephone(String telephone) {
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
    public static boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher m = emailPattern.matcher(email);
        return m.matches();
    }

    /**
     * 校验用户名
     *
     * @param username
     * @return
     */
    public static boolean validateUsername(String username) {
        return !StringUtils.isEmpty(username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return
     */
    public static boolean validatePassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        } else {
            return password.length() >= 6;
        }
    }

    /**
     * 校验名称
     *
     * @param name
     * @return
     */
    public static boolean validateName(String name) {
        return !StringUtils.isEmpty(name);
    }
}
