package com.hugoserve.demo.utils;

import java.sql.Timestamp;
import java.time.Instant;

public class SqlConverter {

    private SqlConverter() {}

    public static Timestamp instantToSqlTimestamp(Instant instant) {
        return Timestamp.from(instant);
    }


}
