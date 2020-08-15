package net.stackoverflow.cms.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误
 *
 * @author 凉衫薄
 */
public class VerifyCodeException extends AuthenticationException {

    public VerifyCodeException() {
        super(null);
    }

    public VerifyCodeException(String msg) {
        super(msg);
    }
}
