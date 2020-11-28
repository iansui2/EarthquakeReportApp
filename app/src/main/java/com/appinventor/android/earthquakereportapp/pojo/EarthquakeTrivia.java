package com.appinventor.android.earthquakereportapp.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "trivia", indices = {@Index(value = {"title", "extract"}, unique = true)})
public class EarthquakeTrivia {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "title")
    public String mTitle;

    @ColumnInfo(name = "extract")
    private String mExtract;

    public EarthquakeTrivia(String title, String extract) {
        mTitle = title;
        mExtract = extract;
    }

    public int getId() { return mId; }

    public String getTitle() {
        return mTitle;
    }

    public String getExtract() {
        return mExtract;
    }
}
