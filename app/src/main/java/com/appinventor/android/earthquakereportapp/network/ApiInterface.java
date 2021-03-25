package com.appinventor.android.earthquakereportapp.network;

import com.appinventor.android.earthquakereportapp.ui.EarthquakeAdapter;
import com.appinventor.android.earthquakereportapp.variables.Global;

import io.reactivex.Completable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

//    @GET("fdsnws/event/1/query")
//    // Creates a call of ResponseBody
//    Call<ResponseBody> getEarthquakes(@Query("format") String format,
//                                      @Query("eventtype") String eventType,
//                                      @Query("starttime") String starttime,
//                                      @Query("endtime") String endtime,
//                                      @Query("orderby") String orderBy,
//                                      @Query("minmag") int minMag,
//                                      @Query("maxmag") int maxMag);

    @GET("earthquakes/feed/v1.0/summary/all_day.geojson")
        // Creates a call of ResponseBody
    Call<ResponseBody> getEarthquakes();

    @GET("w/api.php")
        // Creates a call of ResponseBody
    Call<ResponseBody> getEarthquakeTrivia(@Query("format") String format,
                                      @Query("formatversion") int formatVersion,
                                      @Query("action") String action,
                                      @Query("prop") String prop,
                                      @Query("exsentences") int exSentences,
                                      @Query("exintro") String exIntro,
                                      @Query("explaintext") String explainText,
                                      @Query("exlimit") int exLimit,
                                      @Query("titles") String titles);
}
