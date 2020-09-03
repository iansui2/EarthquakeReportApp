package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appinventor.android.earthquakereportapp.R;
import com.appinventor.android.earthquakereportapp.network.ConnectivityInterceptor;
import com.appinventor.android.earthquakereportapp.network.NetworkUtil;
import com.appinventor.android.earthquakereportapp.pojo.Earthquake;
import com.appinventor.android.earthquakereportapp.variables.Constants;
import com.appinventor.android.earthquakereportapp.variables.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.appinventor.android.earthquakereportapp.context.ContextGetter.*;
import static com.appinventor.android.earthquakereportapp.network.NetworkUtil.*;
import static com.appinventor.android.earthquakereportapp.network.NetworkUtil.networkAvailable;

public class EarthquakeRepository {

    public List<Earthquake> allEarthquakes = new ArrayList<>();
    static ConnectivityInterceptor interceptor = new ConnectivityInterceptor();

    private static OkHttpClient okHttpClientBuilder() {
        // Building of OkHttpClient
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.readTimeout(50, TimeUnit.SECONDS)
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(55, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build(); // Builds up OkHttpClient
    }


    public LiveData<List<Earthquake>> callWebService() {
        final MutableLiveData<List<Earthquake>> earthquakeList = new MutableLiveData<>();
            // Building of Retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL) // This is the base url: https://earthquake.usgs.gov/
                    .addConverterFactory(GsonConverterFactory.create()) // Adds a GsonConverterFactory
                    .client(okHttpClientBuilder())
                    .build(); // Builds up Retrofit

            // Creates an instance of Retrofit Interface
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            // Creates a call of Response Body
            Call<ResponseBody> call = apiInterface.getEarthquakes(Constants.FORMAT, Constants.EVENT_TYPE, Global.orderBy,
                    Global.minMag, Global.limit);

            // Executing the call on a background thread
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        // Creates an InputStream
                        InputStream inputStream = null;
                        try {
                            assert response.body() != null;
                            // Assigning the byte stream body response into the InputStream
                            inputStream = response.body().byteStream();
                            // Creates a String jsonResponse and assigns it to the method readFromStream taking an input of inoutStream
                            String jsonResponse = readFromStream(inputStream);
                            // Assigning the list of earthquakes to the method parseJson taking an input of the String jsonResponse
                            allEarthquakes = parseJson(jsonResponse);
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

                        try {
                            // Make the main thread sleep
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Post the value of the list to the LiveData
                        earthquakeList.postValue(allEarthquakes);
                    } else {
                        Log.e("EarthquakeRepository", "onResponse: Failure of loading the earthquakes");
                        // Post the value of the list to the LiveData
                        earthquakeList.postValue(allEarthquakes);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("EarthquakeRepository", "onFailure: Failure of loading the earthquakes");
                    // Post the value of the list to the LiveData
                    earthquakeList.postValue(allEarthquakes);
                }
            });
        return earthquakeList;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        // Creates a StringBuilder that will build up a String
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            // Creates an InputStreamReader that will read data one byte at a time
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
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

    private List<Earthquake> parseJson (String response) throws JSONException {
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
                Long time = properties.getLong("time");

                // Creates an Earthquake Object
                Earthquake earthquake = new Earthquake(magnitude, location, time);
                // Adds the Earthquake Object to the list.
                earthquakeResults.add(earthquake);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return earthquakeResults;
    }
}
