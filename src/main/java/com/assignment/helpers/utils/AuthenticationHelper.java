package com.assignment.helpers.utils;


import com.assignment.dto.users.CustomUserDetails;
import com.assignment.helpers.ErrorConstants;
import com.assignment.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationHelper {

    static UserRepository userRepository;

    @Autowired
    public AuthenticationHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static CustomUserDetails getUserDetails(String userId) {
        return userRepository.findByUsername(userId).map(CustomUserDetails::new)
                .or(() -> userRepository.findByEmail(userId).map(CustomUserDetails::new))
                .orElseThrow(() -> new UsernameNotFoundException(ErrorConstants.USER_NOT_FOUND));
    }
}
