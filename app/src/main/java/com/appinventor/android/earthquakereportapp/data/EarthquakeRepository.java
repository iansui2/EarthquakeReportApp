package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;

import com.appinventor.android.earthquakereportapp.converters.LongTypeConverter;
import com.appinventor.android.earthquakereportapp.network.ApiInterface;
import com.appinventor.android.earthquakereportapp.network.ConnectivityInterceptor;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;
import com.appinventor.android.earthquakereportapp.pojo.EarthquakeRoom;
import com.appinventor.android.earthquakereportapp.variables.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.appinventor.android.earthquakereportapp.network.ConnectivityUtil.*;
import static com.appinventor.android.earthquakereportapp.variables.Constants.LOG_TAG;

public class EarthquakeRepository {

    public static ConnectivityInterceptor interceptor = new ConnectivityInterceptor();
    private Call<ResponseBody> retryCall;
    private Callback<ResponseBody> callback;
    public static int seconds = 30;

    private static EarthquakeDatabase database;
    private static LiveData<List<EarthquakeRoom>> allEarthquakeLiveData;

    public EarthquakeRepository(Context context) {
        database = EarthquakeDatabase.getInstance(context);
        allEarthquakeLiveData = database.earthquakeDAO().getAllEarthquake();
    }

    private static OkHttpClient okHttpClientBuilder() {
        // Building of OkHttpClient
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.readTimeout(seconds, TimeUnit.SECONDS)
                .connectTimeout(seconds, TimeUnit.SECONDS)
                .writeTimeout(seconds, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .build(); // Builds up OkHttpClient
    }

    public void insertEarthquake() {
            // Building of Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_EARTHQUAKE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder())
                .build();

            // Creates an instance of Retrofit Interface
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            // Creates a call of Response Body
            Call<ResponseBody> call = apiInterface.getEarthquakes();

            // Executing the call on a background thread
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            // Creates an InputStream
                            InputStream inputStream = null;
                            try {
                                // Assigning the byte stream body response into the InputStream
                                inputStream = response.body().byteStream();
                                // Creates a String jsonResponse and assigns it to the method readFromStream taking an input of inoutStream
                                String jsonResponse = readFromStream(inputStream);
                                // Assigning the list of earthquakes to the method parseJson taking an input of the String jsonResponse
                                List<Earthquake> allEarthquakeList = parseJson(jsonResponse);
                                Completable.fromAction(() -> {
                                    for(int i=0; i < allEarthquakeList.size(); i++) {
                                        Double magnitude = allEarthquakeList.get(i).getMag();
                                        String location = allEarthquakeList.get(i).getPlace();
                                        Long time = allEarthquakeList.get(i).getTime();
                                        String url = allEarthquakeList.get(i).getUrl();
                                        String felt = allEarthquakeList.get(i).getFelt();
                                        int tsunami = allEarthquakeList.get(i).getTsunami();
                                        Double longitude = allEarthquakeList.get(i).getLongitude();
                                        Double latitude = allEarthquakeList.get(i).getLatitude();
                                        Double depth = allEarthquakeList.get(i).getDepth();

                                        EarthquakeRoom earthquakeRoom = new EarthquakeRoom(magnitude, location, time,
                                                url, felt, tsunami, longitude, latitude, depth);
                                        database.earthquakeDAO().insertEarthquake(earthquakeRoom);
                                    }
                                }).subscribeOn(Schedulers.io())
                                .subscribe();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            } finally {
                                if (inputStream != null) {
                                    try {
                                        // Close the inputStream
                                        inputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            Log.i(LOG_TAG, response.code() + ": Success of loading the earthquakes");
                            // Clone the call and assign it to a variable
                            // Set the callback to this call using this function
                        } else {
                            Log.e(LOG_TAG, response.code() + ": Failure of loading the earthquakes");
                            // Clone the call and assign it to a variable
                            // Set the callback to this call using this function
                        }
                    retryCall = call.clone();
                    setCallback(this);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(LOG_TAG, "onFailure: Failure of loading the earthquakes");
                    // Clone the call and assign it to a variable
                    retryCall = call.clone();
                    // Set the callback to this call using this function
                    setCallback(this);
                }
            });
    }

    public LiveData<List<EarthquakeRoom>> getAllEarthquake() {
        return allEarthquakeLiveData;
    }

    private void setCallback(Callback<ResponseBody> responseBodyCallback) {
        callback = responseBodyCallback;
    }

    public void getRetryCallback() {
        // Execute the call on a background thread using the callback
        retryCall.enqueue(callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String readFromStream(InputStream inputStream) throws IOException {
        // Creates a StringBuilder that will build up a String
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            // Creates an InputStreamReader that will read data one byte at a time
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            // Creates a BufferedReader and put the InputStreamReader on the BufferedReader
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // Reads the line and assigns it to a String line
            String line = reader.readLine();

            while (line != null) {
                // Append the String into the String Builder
                output.append(line);
                // Reads the line and assigns it to a String line
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Earthquake> parseJson(String response) throws JSONException {
        List<Earthquake> earthquakeResults = new ArrayList<>();
        // Creates a JSONObject with the value coming from the String response
        JSONObject jsonObject = new JSONObject(response);

        try {
            // Creates a JSONArray and gets the name of the JSONArray
            JSONArray earthquakeArray = jsonObject.getJSONArray("features");

            for(int i=0; i < earthquakeArray.length(); i++) {
                // Creates a JSONObject and gets the current JSONObject
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // Creates a JSONObject and gets the name of the JSONObject
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // Assigning the values of the properties based on the name of the key of the JSONObject
                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                String felt = properties.getString("felt");
                int tsunami = properties.getInt("tsunami");

                JSONObject geometry = currentEarthquake.getJSONObject("geometry");
                JSONArray coordinates = geometry.getJSONArray("coordinates");

                Double longitude = coordinates.getDouble(0);
                Double latitude = coordinates.getDouble(1);
                Double depth = coordinates.getDouble(2);

                // Creates an Earthquake Object
                Earthquake earthquake = new Earthquake(magnitude, location, time, url, felt, tsunami,
                        longitude, latitude, depth);
                // Adds the Earthquake Object to the list.
                earthquakeResults.add(earthquake);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return earthquakeResults;
    }
}
