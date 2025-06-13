package com.hugoserve.demo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.hugoserve.demo.Exception.DuplicateKeyException;
import com.hugoserve.demo.Exception.UserException;
import com.hugoserve.demo.Exception.UserExistException;
import com.hugoserve.demo.constants.DB_Constants;
import com.hugoserve.demo.constants.SqlQueryConstants;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.dao.UserDAO;
import com.hugoserve.demo.dao.helper.RdstoProtoMapper;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.provider.MySqlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDAOimpl implements UserDAO {
    private final Logger logger = LoggerFactory.getLogger(UserDAOimpl.class);
    private final MySqlProvider mySqlProvider;

    UserDAOimpl(MySqlProvider mySqlProvider) {
        this.mySqlProvider = mySqlProvider;
    }

    @Override
    public Optional<UserEntity> findByUsername(String inputUsername) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(DB_Constants.USERS_USERNAME, inputUsername);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_USER, userMap);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(RdstoProtoMapper.deserializetoUserEntity(rows.getFirst()));
    }

    @Override
    public void createUser(UserEntity user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put(DB_Constants.USERS_USERNAME, user.getUsername());
        userMap.put(DB_Constants.USERS_PASSWORD, user.getPassword());
        try {
            mySqlProvider.create(SqlQueryConstants.INSERT_USER, userMap);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
            throw new UserExistException();
        }

    }


}
