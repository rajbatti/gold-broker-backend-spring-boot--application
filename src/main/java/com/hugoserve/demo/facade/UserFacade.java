package com.hugoserve.demo.facade;

import java.util.Optional;

import com.hugoserve.demo.dao.UserDAO;
import com.hugoserve.demo.proto.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    private final UserDAO userDAO;

    UserFacade(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public void createUser(UserEntity user) {
        userDAO.createUser(user);
    }
}
