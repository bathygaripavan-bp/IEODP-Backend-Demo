package com.pavan.jwtDemo.service;

import com.pavan.jwtDemo.entity.UserEntity;
import com.pavan.jwtDemo.repository.UserRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetails implements UserDetailsService {
    @Autowired
    private UserRepositroy repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repo.findByUserName(username).get();
        new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>()    //Admin USe
        );
        return null;
    }
}
