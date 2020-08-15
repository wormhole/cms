package net.stackoverflow.cms.common;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.security.CmsUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

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
    protected UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取WebAuthenticationDetails
     *
     * @return
     */
    protected WebAuthenticationDetails getDetails() {
        return (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    /**
     * 获取用户
     *
     * @return
     */
    protected User getUser() {
        User user = null;
        CmsUserDetails userDetails = (CmsUserDetails) getUserDetails();
        if (userDetails != null) {
            user = userDetails.getUser();
        }
        return user;
    }

    /**
     * 获取用户主键
     *
     * @return
     */
    protected String getUserId() {
        String userId = null;
        User user = getUser();
        if (user != null) {
            userId = user.getId();
        }
        return userId;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    protected String getUsername() {
        String username = null;
        User user = getUser();
        if (user != null) {
            username = user.getUsername();
        }
        return username;
    }
}
