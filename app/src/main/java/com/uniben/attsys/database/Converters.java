package com.uniben.attsys.database;

import android.arch.persistence.room.TypeConverter;

import com.uniben.attsys.models.Token;

import java.util.Date;

public class Converters {

    /**
     * Convert a long value to a date
     * @param value the long value
     * @return the date
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert a date to a long value
     * @param date the date
     * @return the long value
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Token fromToken(String token){
        return token == null ? null : new Token(token);
    }


    @TypeConverter
    public static String toStringFromToken(Token token){
        return token == null ? null : token.getToken();
    }

}
