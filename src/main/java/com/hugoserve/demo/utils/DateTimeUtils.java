package com.hugoserve.demo.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import com.google.protobuf.Timestamp;

public class DateTimeUtils {

    private DateTimeUtils() {}

    public static Instant googleTimestampInstant(Timestamp timestamp){
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }

    public static Timestamp instantToGoogleTimestamp(Instant instant) {

        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public static Instant stringtoInstant(String timestamp){
        return Instant.parse(timestamp);
    }

    public static int compareTimestamps(Timestamp timestamp1, Timestamp timestamp2) {
        Instant instant1 = Instant.ofEpochSecond(timestamp1.getSeconds(), timestamp1.getNanos());
        Instant instant2 = Instant.ofEpochSecond(timestamp2.getSeconds(), timestamp2.getNanos());
        return instant1.compareTo(instant2);
    }

    public static Timestamp convertToTimestamp(String dateString) {
        Instant instant = LocalDate.parse(dateString)
                .atStartOfDay(ZoneId.of("UTC"))
                .toInstant();

        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }


}
