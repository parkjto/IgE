package com.SW.IgE.service;

import com.SW.IgE.entity.User;
import com.SW.IgE.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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

    public void updateUserAllergies(String useremail, List<String> allergies) {
        User user = getUserInfo(useremail);
        if (user != null) {
            user.setUser_ige(allergies);
            userRepository.save(user);
        }
    }
}
