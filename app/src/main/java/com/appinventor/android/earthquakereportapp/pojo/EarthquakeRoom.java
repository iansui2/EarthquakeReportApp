package com.appinventor.android.earthquakereportapp.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.appinventor.android.earthquakereportapp.converters.DoubleTypeConverter;
import com.appinventor.android.earthquakereportapp.converters.LongTypeConverter;

@Entity(tableName = "earthquake", indices = {@Index(value = {"mag", "place", "time", "url",
"felt", "tsunami", "longitude", "latitude", "depth"}, unique = true)})
public class EarthquakeRoom {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int mId;

    @ColumnInfo(name = "mag")
    @TypeConverters(DoubleTypeConverter.class)
    private Double mMag;

    @ColumnInfo(name = "place")
    private String mPlace;

    @ColumnInfo(name = "time")
    @TypeConverters(LongTypeConverter.class)
    private Long mTime;

    @ColumnInfo(name = "url")
    private String mUrl;

    @ColumnInfo(name = "felt")
    private String mFelt;

    @ColumnInfo(name = "tsunami")
    private int mTsunami;

    @ColumnInfo(name = "longitude")
    @TypeConverters(DoubleTypeConverter.class)
    private Double mLongitude;

    @ColumnInfo(name = "latitude")
    @TypeConverters(DoubleTypeConverter.class)
    private Double mLatitude;

    @ColumnInfo(name = "depth")
    @TypeConverters(DoubleTypeConverter.class)
    private Double mDepth;

    public EarthquakeRoom(Double mag, String place, Long time, String url, String felt, int tsunami,
                          Double longitude, Double latitude, Double depth) {
        mMag = mag;
        mPlace = place;
        mTime = time;
        mUrl = url;
        mFelt = felt;
        mTsunami = tsunami;
        mLongitude = longitude;
        mLatitude = latitude;
        mDepth = depth;
    }

    public Double getMag() {
        return mMag;
    }

    public String getPlace() { return mPlace; }

    public Long getTime() {
        return mTime;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFelt() {
        return mFelt;
    }

    public int getTsunami() {
        return mTsunami;
    }

    public Double getLongitude() { return mLongitude; }

    public Double getLatitude() {
        return mLatitude;
    }

    public Double getDepth() {
        return mDepth;
    }
}
