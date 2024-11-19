package com.SW.IgE.service;


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

        if (user.getUser_ige() == null) {
            user.setUser_ige(List.of());
        }

        userRepository.save(user);
    }

    public User getUserInfo(String useremail) {
        return userRepository.findByUseremail(useremail).orElse(null);
    }

    public List<String> getUserAllergies(Integer userId) {
        return userRepository.findById(userId)
                .map(User::getUser_ige)
                .orElse(List.of());
    }



}
