package com.appinventor.android.earthquakereportapp.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Earthquake {

    @SerializedName("mag")
    @Expose
    private Double mMag;

    @SerializedName("place")
    @Expose
    private String mPlace;

    @SerializedName("time")
    @Expose
    private long mTime;

    @SerializedName("url")
    @Expose
    private String mUrl;

    @SerializedName("felt")
    @Expose
    private String mFelt;

    @SerializedName("tsunami")
    @Expose
    private int mTsunami;

    @SerializedName("longitude")
    @Expose
    private double mLongitude;

    @SerializedName("latitude")
    @Expose
    private double mLatitude;

    @SerializedName("depth")
    @Expose
    private double mDepth;

    public Earthquake(Double mag, String place, long time, String url, String felt, int tsunami,
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

    public long getTime() {
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
