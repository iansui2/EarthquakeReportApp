package com.appinventor.android.earthquakereportapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

import java.util.List;

@Dao
public interface EarthquakeTriviaDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrivia(EarthquakeTrivia trivia);

    @Update
    void updateTrivia(EarthquakeTrivia trivia);

    @Delete
    void deleteTrivia(EarthquakeTrivia trivia);

    @Query("DELETE FROM trivia")
    void deleteAllTrivia();

    @Query("SELECT * FROM trivia")
    LiveData<List<EarthquakeTrivia>> getAllTrivia();
}
