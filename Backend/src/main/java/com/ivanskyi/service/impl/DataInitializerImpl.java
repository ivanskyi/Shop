package com.ivanskyi.service.impl;

import com.ivanskyi.model.User;
import com.ivanskyi.model.UserRole;
import com.ivanskyi.repository.UserRepository;
import com.ivanskyi.service.DataInitializer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class DataInitializerImpl implements DataInitializer {

    private static final String MANAGER_USER_USERNAME = "manager";
    private static final String MANAGER_USER_PASSWORD = "manager";
    private static final String MANAGER_USER_EMAIL = "manager@email.com";

    private static final String CLIENT_USER_USERNAME = "client";
    private static final String CLIENT_USER_PASSWORD = "client";
    private static final String CLIENT_USER_EMAIL = "client@email.com";

    private final UserSecurityServiceImpl userSecurityServiceImpl;
    private final UserRepository userRepository;

    @Override
    public void initData() {
        try {
            initializeUser(MANAGER_USER_USERNAME, MANAGER_USER_PASSWORD, MANAGER_USER_EMAIL, UserRole.ROLE_MANAGER);
            initializeUser(CLIENT_USER_USERNAME, CLIENT_USER_PASSWORD, CLIENT_USER_EMAIL, UserRole.ROLE_CLIENT);
            log.info("Start data was successfully initialized");
        } catch (Exception e) {
            log.error("Got error when trying to initialize start data, Got exception: ", e);
        }
    }

    private void initializeUser(final String username, final String password, final String email, final UserRole userRole) {
        if (getUserByUserName(username).isEmpty()) {
            final User user = createUser(username, password, email, userRole);
            userSecurityServiceImpl.signup(user);
        }
    }

    private User createUser(final String username, final String password, final String email, final UserRole userRole) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
        return user;
    }

    private Optional<User> getUserByUserName(final String userName) {
        return userRepository.findByUsername(userName);
    }
}
