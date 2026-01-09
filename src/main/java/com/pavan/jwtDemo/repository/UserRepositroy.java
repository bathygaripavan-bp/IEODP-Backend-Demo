package com.pavan.jwtDemo.repository;

import com.pavan.jwtDemo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositroy extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUserName(String userName);
}
