package com.SW.IgE.security;

import com.SW.IgE.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails implements UserDetails {

    private final Integer id; // id를 Integer로 변경
    private final String email;
    private final String password;
    private final Set<GrantedAuthority> authorities;
    private final Integer age;

    // User 객체만 받아 필요한 정보를 설정하는 생성자
    public CustomUserDetails(User user) {
        this.id = user.getId(); // id 저장
        this.email = user.getUseremail();
        this.password = user.getPassword();
        this.authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        this.age = user.getAge(); // age 저장
    }

    public Integer getId() { // id Getter
        return id;
    }

    public Integer getAge() { // age Getter
        return age;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }
}
