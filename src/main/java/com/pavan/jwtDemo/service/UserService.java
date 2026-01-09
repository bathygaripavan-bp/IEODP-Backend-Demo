package com.pavan.jwtDemo.service;

import com.pavan.jwtDemo.config.JwtUtil;
import com.pavan.jwtDemo.dto.UserSignInResponseDto;
import com.pavan.jwtDemo.entity.UserEntity;
import com.pavan.jwtDemo.repository.UserRepositroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepositroy repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = repo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + username));

        return new User(
                user.getUserName(),
                user.getPassword(),
                new ArrayList<>()  // roles
        );
    }

    public UserSignInResponseDto createUser(UserEntity userEntity) {

        UserEntity savedUser = repo.save(
                UserEntity.builder()
                        .userName(userEntity.getUserName())
                        .email(userEntity.getEmail())
                        .password(encoder.encode(userEntity.getPassword()))
                        .build()
        );

        return UserSignInResponseDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .jwtToken(jwtUtil.generateJwtToke(savedUser.getUserName()))
                .build();
    }
}
