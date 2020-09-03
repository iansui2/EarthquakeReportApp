package com.appinventor.android.earthquakereportapp.network;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder().build();
        Response response = chain.proceed(request);
        if (response.code() != 200) {
            Response anotherResponse = null;
            anotherResponse = makeRefreshCall(request, chain);
            return response;
        }
        return response;
    }

    private Response makeRefreshCall (Request request, Chain chain) throws IOException {
        Request newRequest;
        newRequest = request.newBuilder().build();
        Response newResponse = chain.proceed(newRequest);
        while (newResponse.code() != 200) {
            makeRefreshCall(newRequest, chain);
        }
        return newResponse;
    }

}
