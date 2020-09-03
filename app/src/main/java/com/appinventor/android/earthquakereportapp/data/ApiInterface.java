package com.appinventor.android.earthquakereportapp.data;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiInterface {
    @GET("fdsnws/event/1/query")
    // Creates a call of ResponseBody
    Call<ResponseBody> getEarthquakes(@Query("format") String format,
                                      @Query("eventtype") String eventType,
                                      @Query("orderby") String orderBy,
                                      @Query("minmag") int minMag,
                                      @Query("limit") int limit);
}
