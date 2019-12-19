package net.stackoverflow.cms.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * UserDetails实现类
 *
 * @author 凉衫薄
 */
public class CmsUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final Integer enabled;
    private final String email;
    private final String telephone;
    private final Integer root;
    private final List<GrantedAuthority> authorities;

    public CmsUserDetails(String username, String password, Integer enabled, String email, String telephone, Integer root, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.email = email;
        this.telephone = telephone;
        this.authorities = authorities;
        this.root = root;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTelephone() {
        return this.telephone;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (enabled == null) {
            return false;
        } else {
            return enabled == 1;
        }
    }
}
