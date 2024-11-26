package com.SW.IgE.service;

import com.SW.IgE.repository.UserRepository;
import com.SW.IgE.entity.User;
import com.SW.IgE.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

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

        // CustomUserDetails 반환
        return new CustomUserDetails(user);  // CustomUserDetails 객체 반환
    }
}
