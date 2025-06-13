package com.hugoserve.demo.provider.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import com.hugoserve.demo.Exception.DatabaseException;
import com.hugoserve.demo.Exception.DuplicateKeyException;
import com.hugoserve.demo.proto.MySQlRequest;
import com.hugoserve.demo.provider.MySqlProvider;
import com.hugoserve.demo.utils.MySqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

@Repository
public class MySqlProviderImpl implements MySqlProvider {
    private Connection connection;
    private final Logger logger = LoggerFactory.getLogger(MySqlProviderImpl.class);

    MySqlProviderImpl(DataSource dataSource) {
        this.connection = DataSourceUtils.getConnection(dataSource);
    }


    public PreparedStatement getPreparedStatement(String sql, Map<String, Object> paramMap) throws SQLException {
        PreparedStatement preparedStatement;
        MySQlRequest request = MySqlUtils.parseSQL(sql);
        preparedStatement = connection.prepareStatement(request.getSql());
        preparedStatement = MySqlUtils.getPreparedStatement(preparedStatement, paramMap, request.getParamList());
        logger.info("Got prepared Statement for Execution:{}", preparedStatement);
        return preparedStatement;
    }

    @Override
    public int create(String sql, Map<String, Object> paramMap) throws DuplicateKeyException {
        int no_of_row = 0;
        try {
            PreparedStatement preparedStatement = getPreparedStatement(sql, paramMap);
            no_of_row = preparedStatement.executeUpdate();
            logger.info("Executed MySQL query {} and no of rows affected are {}", preparedStatement, no_of_row);
        } catch (Exception e) {
            logger.error("Error in Executing query :", e);
            if (e.getMessage().contains("Duplicate")) {
                throw new DuplicateKeyException(e);
            } else {
                throw new DatabaseException(e);
            }
        }
        return no_of_row;
    }

    @Override
    public int update(String sql, Map<String, Object> paramMap) {
        int no_of_row = 0;
        try {
            PreparedStatement preparedStatement = getPreparedStatement(sql, paramMap);
            no_of_row = preparedStatement.executeUpdate();
            logger.info("Executed MySQL query {} and no of rows affected are {}", preparedStatement, no_of_row);
        } catch (Exception e) {
            logger.error("Error in Executing update :", e);
            throw new DatabaseException(e);
        }
        return no_of_row;
    }

    @Override
    public List<Map<String, Object>> query(String sql, Map<String, Object> paramMap) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getPreparedStatement(sql, paramMap);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                int columnCount = rsMetaData.getColumnCount();
                int count = 1;
                Map<String, Object> row = new HashMap<>();
                while (count <= columnCount) {
                    String columnName = rsMetaData.getColumnLabel(count);
                    Object columnValue = resultSet.getObject(count);

                    // Convert based on SQL data types
                    if (columnValue instanceof String) {
                        row.put(columnName, columnValue); // Keep String as String
                    } else if (columnValue instanceof BigDecimal) {
                        row.put(columnName, ((BigDecimal) columnValue).doubleValue());
                    } else if (columnValue instanceof Integer) {
                        row.put(columnName, columnValue); // Keep Integer as Integer
                    } else {
                        row.put(columnName, columnValue); // Keep other types as is
                    }

                    count++;
                }
                resultList.add(row);
                logger.info("Executed MySQL query {} and no of rows affected are {}", preparedStatement, resultList.size());
            }
        } catch (Exception e) {
            logger.error("Error in Executing query :", e);
            throw new DatabaseException(e);
        }
        return resultList;
    }


}
