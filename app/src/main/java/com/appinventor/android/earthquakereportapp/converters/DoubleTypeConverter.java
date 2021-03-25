package com.appinventor.android.earthquakereportapp.converters;

import androidx.room.TypeConverter;

public class DoubleTypeConverter {
    @TypeConverter
    public static Double integerToDouble(Integer value) {
        if (value != null) {
            return value.doubleValue();
        } else {
            return null;
        }
    }

    @TypeConverter
    public static Integer doubleToInteger(Double value) {
        if (value != null) {
            return value.intValue();
        } else {
            return null;
        }
    }
}
