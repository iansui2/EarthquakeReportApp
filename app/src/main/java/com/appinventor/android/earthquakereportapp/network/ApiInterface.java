package com.appinventor.android.earthquakereportapp.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("earthquakes/feed/v1.0/summary/all_day.geojson")
        // Creates a call of ResponseBody
    Call<ResponseBody> getEarthquakes();

}
