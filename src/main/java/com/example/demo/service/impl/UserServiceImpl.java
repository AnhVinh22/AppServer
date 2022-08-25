package com.example.demo.service.impl;

import com.example.demo.constant.AccountStatus;
import com.example.demo.domain.User;
import com.example.demo.exception.NewsNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.user.UpdateUserRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, UpdateUserRequest updateUserRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NewsNotFoundException("Can't find userId = " + userId);
        }
        User user = userOptional.get();
        user.setName(updateUserRequest.getName());
        user.setAvatar(updateUserRequest.getAvatar());
        return userRepository.save(user);
    }

    @Override
    public User setStatusUserAccount(String id, AccountStatus accountStatus) {
        User user = userRepository.findById(id).get();
        if (user == null) {
            throw new NewsNotFoundException("User id does not exist");
        }
        user.setAccountStatus(accountStatus);
        userRepository.save(user);
        return user;
    }

}
