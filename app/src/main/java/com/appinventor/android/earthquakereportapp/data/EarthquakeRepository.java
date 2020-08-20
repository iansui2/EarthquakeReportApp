package com.appinventor.android.earthquakereportapp.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.appinventor.android.earthquakereportapp.R;
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

public class EarthquakeRepository {

    private TextView mEmptyStateTextView;
    public List<Earthquake> allEarthquakes = new ArrayList<>();

    private static OkHttpClient okHttpClientBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        return httpClient.readTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS).build();
    }


    public LiveData<List<Earthquake>> callWebService() {
        final MutableLiveData<List<Earthquake>> earthquakeList = new MutableLiveData<>();
        if (networkAvailable()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL) // This is the base url: https://earthquake.usgs.gov/
                    .addConverterFactory(GsonConverterFactory.create()) // Adds a GsonConverterFactory
                    .client(okHttpClientBuilder())
                    .build(); // Builds up Retrofit

            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            Call<ResponseBody> call = apiInterface.getEarthquakes(Constants.FORMAT, Constants.EVENT_TYPE, Global.orderBy,
                    Global.minMag, Global.limit);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            assert response.body() != null;
                            InputStream inputStream = response.body().byteStream();
                            String jsonResponse = readFromStream(inputStream);
                            allEarthquakes = parseJson(jsonResponse);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        earthquakeList.postValue(allEarthquakes);
                    } else {
                        Log.e("EarthquakeActivity", String.valueOf(R.string.failure));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("EarthquakeActivity", String.valueOf(R.string.failure));
                }
            });
        }
        return earthquakeList;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private List<Earthquake> parseJson (String response) throws JSONException {
        List<Earthquake> earthquakeResults = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);

        try {
            JSONArray earthquakeArray = jsonObject.getJSONArray("features");

            for(int i=0; i < earthquakeArray.length(); i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                Double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                Long time = properties.getLong("time");

                Earthquake earthquake = new Earthquake(magnitude, location, time);
                earthquakeResults.add(earthquake);
            }
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
        return earthquakeResults;
    }

    private Boolean networkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
