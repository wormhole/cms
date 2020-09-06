package net.stackoverflow.cms.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误
 *
 * @author 凉衫薄
 */
public class CaptchaException extends AuthenticationException {

    public CaptchaException() {
        super(null);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}
