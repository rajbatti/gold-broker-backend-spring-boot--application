package com.hugoserve.demo.dao;

import java.util.Optional;

import com.hugoserve.demo.proto.entity.UserEntity;

public interface UserDAO {
    Optional<UserEntity> findByUsername(String username);

    void createUser(UserEntity user);
}
