package com.SW.IgE.service;

import com.SW.IgE.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.SW.IgE.entity.User;
import com.SW.IgE.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    public boolean existsByUseremail(String email) {
        return userRepository.existsByUseremail(email);
    }

    public void joinUser(User user) {
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        user.setRole(User.Role.USER);
        user.setAge(user.getAge());
        if (user.getUser_ige() == null) {
            user.setUser_ige(List.of());
        }

        userRepository.save(user);
    }

    public User getUserInfo(String useremail) {
        return userRepository.findByUseremail(useremail)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public List<String> getUserAllergies(Integer userId) {
        return userRepository.findById(userId)
                .map(User::getUser_ige)
                .orElse(List.of());
    }

    public User getUserWithAllDetails(String useremail) {
        return userRepository.findUserWithAllDetailsByEmail(useremail).orElse(null);
    }

    public void updateUser(User updatedUser) {
        // 사용자 정보를 업데이트
        User existingUser = userRepository.findById(updatedUser.getId()).orElse(null);
        if (existingUser != null) {
            // 업데이트된 필드를 반영
            existingUser.setName(updatedUser.getName());
            existingUser.setAge(updatedUser.getAge());
            existingUser.setUser_ige(updatedUser.getUser_ige());

            // 비밀번호를 변경할 경우, 새 비밀번호로 업데이트
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                String rawPassword = updatedUser.getPassword();
                String encPassword = bCryptPasswordEncoder.encode(rawPassword);
                existingUser.setPassword(encPassword);
            }

            userRepository.save(existingUser);
        } else {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }





}
