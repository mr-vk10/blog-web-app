package com.app.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.blog.models.Users;
import com.app.blog.repository.UserRepository;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Users loadUserByUserID(Integer id) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException("User ID not found");
    }

    @Override
    public Users loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByUserName(username);
        if (user.isPresent())
            return user.get();
        else
            throw new UsernameNotFoundException("User ID not found");
    }

}
