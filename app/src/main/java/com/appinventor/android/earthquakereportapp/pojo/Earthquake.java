package com.appinventor.android.earthquakereportapp.pojo;

public class Earthquake {

    private Double mMag;

    private String mPlace;

    private long mTime;

    private String mUrl;

    private int mFelt;

    private int mTsunami;

    public Earthquake(Double mag, String place, long time) {
        mMag = mag;
        mPlace = place;
        mTime = time;
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

    public int getFelt() {
        return mFelt;
    }

    public int getTsunami() {
        return mTsunami;
    }

}
