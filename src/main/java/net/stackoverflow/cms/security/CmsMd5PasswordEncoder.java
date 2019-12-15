package net.stackoverflow.cms.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * Md5密码编码
 *
 * @author 凉衫薄
 */
public class CmsMd5PasswordEncoder implements PasswordEncoder {

    private final String salt = "4F2016C6B934D55BD7120E5D0E62CCE3";

    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.md5DigestAsHex((rawPassword + salt).getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(DigestUtils.md5DigestAsHex((rawPassword + salt).getBytes()));
    }
}
