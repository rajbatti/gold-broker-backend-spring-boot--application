package com.hugoserve.demo.utils;

import static com.google.common.base.Preconditions.checkArgument;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.google.protobuf.Timestamp;
import com.hugoserve.demo.proto.MySQlRequest;
import com.hugoserve.demo.proto.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlUtils {
    private static final Logger LOGGER= LoggerFactory.getLogger(MySqlUtils.class);

    private MySqlUtils() {}
    public static MySQlRequest parseSQL(String sql) {
        StringBuilder parsedSql = new StringBuilder();
        int length = sql.length();
        MySQlRequest.Builder request = MySQlRequest.newBuilder();

        int index = 1;
        for (int i = 0; i < length; i++) {
            char c = sql.charAt(i);
            if (':' == c) {
                int j = i + 1;
                while (j < length && Character.isJavaIdentifierPart(sql.charAt(j))) {
                    j++;
                }
                String parameterName = sql.substring(i + 1, j);
                checkArgument(!parameterName.isEmpty(), "Named parameters in SQL statement must not be empty.");
                request.addParam(Param.newBuilder().setIndex(index++).setParamname(parameterName).build());
                i = j - 1;
                parsedSql.append('?');
            } else {
                parsedSql.append(c);
            }
        }

        return request.setSql(parsedSql.toString()).build();
    }

    public static PreparedStatement getPreparedStatement(PreparedStatement preparedStatement, Map<String, Object> paramMap,
                                                         List<Param> paramListWithIndex) {
        try {
            for (Param param : paramListWithIndex) {
                Object value = paramMap.get(param.getParamname());
                Class<?> type = value.getClass();
                if (type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)) {
                    preparedStatement.setInt(param.getIndex(), (int) value);
                } else if (type.isAssignableFrom(long.class) || type.isAssignableFrom(Long.class)) {
                    preparedStatement.setLong(param.getIndex(), (long) value);
                } else if (type.isAssignableFrom(boolean.class) || type.isAssignableFrom(Boolean.class)) {
                    preparedStatement.setBoolean(param.getIndex(), (Boolean) value);
                } else if (type.isAssignableFrom(float.class) || type.isAssignableFrom(Float.class)) {
                    preparedStatement.setFloat(param.getIndex(), (Float) value);
                } else if (type.isAssignableFrom(double.class) || type.isAssignableFrom(Double.class)) {
                    preparedStatement.setDouble(param.getIndex(), (Double) value);
                } else if (type.isAssignableFrom(Timestamp.class)) {
                    preparedStatement.setTimestamp(param.getIndex(),
                            SqlConverter.instantToSqlTimestamp(DateTimeUtils.googleTimestampInstant((Timestamp) value)));
                } else {
                    preparedStatement.setString(param.getIndex(), value.toString());
                }
            }
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
        return preparedStatement;

    }
}
