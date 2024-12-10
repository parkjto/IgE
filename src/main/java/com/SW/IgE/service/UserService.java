package com.SW.IgE.service;

import com.SW.IgE.DTO.UserUpdateDTO;
import com.SW.IgE.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

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
        logger.info("[Service] 사용자 업데이트 시작 - ID: {}", userUpdateDTO.getId());
        
        User existingUser = userRepository.findById(userUpdateDTO.getId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        logger.info("[Service] 기존 사용자 정보: {}", existingUser);
        
        // 기본 정보 업데이트
        existingUser.setName(userUpdateDTO.getName());
        existingUser.setAge(userUpdateDTO.getAge());
        
        if (userUpdateDTO.getAllergies() != null) {
            logger.info("[Service] 알레르기 정보 업데이트: {}", userUpdateDTO.getAllergies());
            existingUser.setUser_ige(userUpdateDTO.getAllergies());
        }

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()) {
            logger.info("[Service] 비밀번호 업데이트");
            String encryptedPassword = bCryptPasswordEncoder.encode(userUpdateDTO.getPassword());
            existingUser.setPassword(encryptedPassword);
        }

        User savedUser = userRepository.save(existingUser);
        logger.info("[Service] 사용자 정보 저장 완료 - ID: {}", savedUser.getId());
        
        return savedUser;
    }

    public String resetPassword(String useremail, String name, int age, List<String> allergies, String newPassword) {
        logger.info("[ResetPassword] 비밀번호 재설정 시도 - 이메일: {}", useremail);
        
        User user = userRepository.findByUseremail(useremail)
                .orElseThrow(() -> {
                    logger.error("[ResetPassword] 사용자를 찾을 수 없음 - 이메일: {}", useremail);
                    return new UserNotFoundException("사용자를 찾을 수 없습니다.");
                });
                
        // 사용자 정보 검증
        if (!user.getName().equals(name) || !user.getAge().equals(age)) {
            logger.error("[ResetPassword] 사용자 정보 불일치 - 이름/나이 불일치");
            throw new UserNotFoundException("입력하신 정보와 일치하는 사용자가 없습니다.");
        }
        
        // 알레르기 정보 검증
        if (!user.getUser_ige().containsAll(allergies) || !allergies.containsAll(user.getUser_ige())) {
            logger.error("[ResetPassword] 알레르기 정보 불일치");
            throw new UserNotFoundException("알레르기 정보가 일치하지 않습니다.");
        }
        
        // 새 비밀번호 설정
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("[ResetPassword] 비밀번호 재설정 완료");
        
        return "비밀번호가 성공적으로 변경되었습니다.";
    }

}
