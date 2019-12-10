package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.pojo.entity.Permission;
import net.stackoverflow.cms.pojo.entity.Role;
import net.stackoverflow.cms.pojo.entity.User;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * UserDetails加载服务
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class CmsUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:" + username);
        List<User> users = userService.selectByCondition(new HashMap<String, Object>(16) {{
            put("username", username);
        }});
        if (!CollectionUtils.isEmpty(users)) {
            User user = users.get(0);
            List<Role> roles = userService.getRoleByUserId(user.getId());
            List<Permission> permissions = userService.getPermissionByUserId(user.getId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : roles) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + role.getName());
                authorities.add(sga);
            }
            for (Permission permission : permissions) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority(permission.getName());
                authorities.add(sga);
            }
            return new CmsUserDetails(user.getUsername(), user.getPassword(), user.getEnabled(), user.getEmail(), user.getTelephone(), authorities);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
