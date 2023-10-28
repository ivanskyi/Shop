package com.ivanskyi.security;

import com.ivanskyi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.ivanskyi.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    if (userRepository.findByUsername(username).isEmpty()) {
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }
    final User user = userRepository.findByUsername(username).get();
    return org.springframework.security.core.userdetails.User
        .withUsername(username)
        .password(user.getPassword())
        .authorities(user.getRoles())
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
  }
}
