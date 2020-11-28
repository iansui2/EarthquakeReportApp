package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

@Database(entities = {EarthquakeTrivia.class}, version = 9, exportSchema = false)
public abstract class EarthquakeTriviaDatabase extends RoomDatabase {

    private static EarthquakeTriviaDatabase instance;
    public abstract EarthquakeTriviaDAO earthquakeTriviaDAO();

    public static synchronized EarthquakeTriviaDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    EarthquakeTriviaDatabase.class, "earthquake_trivia.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
