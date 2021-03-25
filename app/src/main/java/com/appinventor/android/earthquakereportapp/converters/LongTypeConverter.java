package com.appinventor.android.earthquakereportapp.converters;

import androidx.room.TypeConverter;

public class LongTypeConverter {
    @TypeConverter
    public static Long integerToLong(Integer value) {
        if (value != null) {
            return value.longValue();
        } else {
            return null;
        }
    }

    @TypeConverter
    public static Integer longToInteger(Long value) {
        if (value != null) {
            return value.intValue();
        } else {
            return null;
        }
    }
}
