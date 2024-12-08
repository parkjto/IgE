package com.SW.IgE.service;

import com.SW.IgE.DTO.UserUpdateDTO;
import com.SW.IgE.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.SW.IgE.entity.User;
import com.SW.IgE.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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

    public User updateUser(UserUpdateDTO userUpdateDTO) {
        User existingUser = userRepository.findById(userUpdateDTO.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        existingUser.setName(userUpdateDTO.getName());
        existingUser.setAge(userUpdateDTO.getAge());
        existingUser.setUser_ige(userUpdateDTO.getAllergies());

        // 비밀번호가 변경되면, 비밀번호 강도 체크 후 업데이트
        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            if (userUpdateDTO.getPassword().length() < 2) {
                throw new RuntimeException("비밀번호는 최소 2자 이상이어야 합니다.");

            }
            String encPassword = bCryptPasswordEncoder.encode(userUpdateDTO.getPassword());
            existingUser.setPassword(encPassword);
        }

        existingUser.setName(userUpdateDTO.getName());
        existingUser.setUser_ige(userUpdateDTO.getAllergies() != null ? userUpdateDTO.getAllergies() : null);

        return userRepository.save(existingUser);
    }








}
