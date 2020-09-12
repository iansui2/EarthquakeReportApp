package com.appinventor.android.earthquakereportapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtil {
    public static Boolean networkAvailable(Context context) {
        boolean internetConnection;

        // Creates a ConnectivityManager Object
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        // Creates a NetworkInfo Object and assigns it to getting the Active Network Information
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // internetConnection is assigned to network info that is not null and is connected or connecting
        internetConnection = networkInfo != null && networkInfo.isConnectedOrConnecting();

        return internetConnection;
    }
}
