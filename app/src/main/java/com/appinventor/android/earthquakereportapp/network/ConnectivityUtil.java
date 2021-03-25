package com.appinventor.android.earthquakereportapp.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.appinventor.android.earthquakereportapp.util.ContextGetter;

public class ConnectivityUtil {

    @SuppressLint("StaticFieldLeak")
    public static Context appContext = ContextGetter.getAppContext();
    // Creates a ConnectivityManager Object
    public static ConnectivityManager connectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    public static boolean internetConnection;

    public static boolean networkAvailable() {
        assert connectivityManager != null;
        // Creates a NetworkInfo Object and assigns it to getting the Active Network Information
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // internetConnection is assigned to network info that is not null and is connected or connecting
        internetConnection = networkInfo != null && networkInfo.isConnectedOrConnecting();

        return internetConnection;
    }
}
