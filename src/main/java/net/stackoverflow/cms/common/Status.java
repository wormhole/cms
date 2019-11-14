package net.stackoverflow.cms.common;

/**
 * 状态常量类
 *
 * @author 凉衫薄
 */
public class Status {

    //成功
    public static final Integer SUCCESS = 0;
    //普通的业务错误
    public static final Integer FAILURE = 1;
    //服务器错误
    public static final Integer SERVER_ERROR = 2;
    //验证码错误
    public static final Integer VCODE_ERROR = 3;
    //字段校验错误
    public static final Integer VALIDATE_ERROR = 4;
}
