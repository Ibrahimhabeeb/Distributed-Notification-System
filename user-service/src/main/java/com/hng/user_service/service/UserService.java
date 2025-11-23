package com.hng.user_service.service;

import  com.hng.dtos.UserDetailsDto;
import com.hng.user_service.models.User;
import com.hng.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;



@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

 
    public UserDetailsDto getUserDetails(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));

        return new UserDetailsDto(
                user.getEmail(),
                user.getName(),
                user.getPushToken()
        );
    }
}
