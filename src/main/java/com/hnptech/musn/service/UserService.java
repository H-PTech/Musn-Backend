package com.hnptech.musn.service;

import com.hnptech.musn.entity.User;
import com.hnptech.musn.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User getUserByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }
    public User updatePush(User userDetails) {
        User user = userRepository.findByProviderId(userDetails.getProviderId());
        if(user != null) {
            user.setPush(userDetails.isPush());
        } else {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user);
    }

    public User updateNickName(User userDetails) {
        User user = userRepository.findByProviderId(userDetails.getProviderId());
        if(user != null) {
            user.setNickname(userDetails.getNickname());
        } else {
            throw new RuntimeException("User not found");
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
