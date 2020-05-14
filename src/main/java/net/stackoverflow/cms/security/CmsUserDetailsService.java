package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDetails加载服务
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsUserDetailsService implements UserDetailsService {

    private UserService userService;

    public CmsUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user != null) {
            List<Role> roles = userService.findRoleByUserId(user.getId());
            List<Permission> permissions = userService.findPermissionByUserId(user.getId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Role role : roles) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + role.getName());
                authorities.add(sga);
            }
            for (Permission permission : permissions) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority(permission.getName());
                authorities.add(sga);
            }
            log.info("加载完用户信息:{}", username);
            return new CmsUserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getEnabled(), user.getEmail(), user.getTelephone(), user.getDeletable(), authorities);
        } else {
            log.error("找不到对应的用户:{}", username);
            throw new UsernameNotFoundException(username);
        }
    }
}
