package com.example.finalproject.service.security;

import com.example.finalproject.entity.User;
import com.example.finalproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> userOptional = userRepository.findUserByUsername(username);
//        if(userOptional.isPresent())
//        {
//            User user = userOptional.get();
//            return new UserDetailsImpl(user);
//        }
//        else
//        {
//            throw new UsernameNotFoundException("Invalid username");
//        }
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() ->new UsernameNotFoundException("User not found with username " + username));

        return new UserDetailsImpl(user);

    }
    }