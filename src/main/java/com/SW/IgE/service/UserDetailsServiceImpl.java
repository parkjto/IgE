package com.SW.IgE.service;

import com.SW.IgE.repository.UserRepository;
import com.SW.IgE.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private static final String DEFAULT_ROLE = "USER";

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String useremail) throws UsernameNotFoundException {
        if (useremail == null || useremail.trim().isEmpty()) {
            logger.error("이메일이 입력되지 않았습니다.");
            throw new UsernameNotFoundException("이메일이 입력되지 않았습니다.");
        }

        logger.debug("로그인 시도 - 이메일: {}", useremail);

        // 사용자 조회
        User user = userRepository.findByUseremail(useremail)
                .orElseThrow(() -> {
                    logger.error("사용자를 찾을 수 없습니다: {}", useremail);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + useremail);
                });

        logger.info("사용자 찾음: {}", user.getUseremail());

        // 사용자의 알레르기 목록을 권한으로 설정
        Set<SimpleGrantedAuthority> authorities = user.getUser_ige().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        // 권한이 없는 경우 기본 역할 추가
        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE));
            logger.debug("기본 권한 '{}' 추가", DEFAULT_ROLE);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUseremail(),
                user.getPassword(),  // 암호화된 비밀번호
                authorities
        );
    }
}
