package com.appinventor.android.earthquakereportapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeTrivia;

import java.util.List;

@Dao
public interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEarthquake(EarthquakeRoom earthquake);

    @Update
    void updateEarthquake(EarthquakeRoom earthquake);

    @Delete
    void deleteEarthquake(EarthquakeRoom earthquake);

    @Query("DELETE FROM earthquake")
    void deleteAllEarthquake();

    @Query("SELECT * FROM earthquake ORDER BY time DESC")
    LiveData<List<EarthquakeRoom>> getAllEarthquake();
}
