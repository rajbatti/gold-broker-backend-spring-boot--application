package com.hugoserve.demo.provider;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.hugoserve.demo.Exception.DuplicateKeyException;

public interface MySqlProvider {
    int create(String sql, Map<String, Object> paramMap) throws DuplicateKeyException;

    int update(String sql, Map<String, Object> paramMap);

    List<Map<String, Object>> query(String sql, Map<String, Object> paramMap);
}
