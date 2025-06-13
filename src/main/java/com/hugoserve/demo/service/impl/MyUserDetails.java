package com.hugoserve.demo.service.impl;

import java.util.Optional;

import com.hugoserve.demo.Exception.UserException;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.dao.UserDAO;
import com.hugoserve.demo.facade.UserFacade;
import com.hugoserve.demo.model.UserData;
import com.hugoserve.demo.proto.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {
    private final
    UserFacade userFacade;

    MyUserDetails(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public UserData loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userFacade.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(username+" is not exist");
        }
        return new UserData(user.get());
    }
}
