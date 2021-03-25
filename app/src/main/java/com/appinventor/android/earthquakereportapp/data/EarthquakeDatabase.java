package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.appinventor.android.earthquakereportapp.converters.DoubleTypeConverter;
import com.appinventor.android.earthquakereportapp.converters.LongTypeConverter;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

@Database(entities = {EarthquakeRoom.class, EarthquakeTrivia.class}, version = 2, exportSchema = false)
@TypeConverters({LongTypeConverter.class, DoubleTypeConverter.class})
public abstract class EarthquakeDatabase extends RoomDatabase {

    private static EarthquakeDatabase instance;
    public abstract EarthquakeDAO earthquakeDAO();
    public abstract EarthquakeTriviaDAO earthquakeTriviaDAO();

    public static synchronized EarthquakeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EarthquakeDatabase.class, "earthquake.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
