package com.ivanskyi.service.impl;

import com.ivanskyi.model.User;
import com.ivanskyi.repository.UserRepository;
import com.ivanskyi.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public User getUserByUsername(String userName) {
        return userRepository.findByUsername(userName).get();
    }
}
