package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.entity.User;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * UserDetails加载服务
 *
 * @author 凉衫薄
 */
@Component
@Slf4j
public class CmsUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user != null) {
            List<String> roles = roleService.findNamesByUserId(user.getId());
            List<String> menus = menuService.findKeysByUserId(user.getId());
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (String role : roles) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_" + role);
                authorities.add(sga);
            }
            for (String menu : menus) {
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority(menu);
                authorities.add(sga);
            }
            return new CmsUserDetails(user, authorities);
        } else {
            log.error("找不到对应的用户:{}", username);
            throw new UsernameNotFoundException(username);
        }
    }
}
