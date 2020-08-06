package com.mdaftabalam.todolist.util;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Created by Aftab Alam on 06-08-2020.
 **/
public class DateRoomConverter {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}