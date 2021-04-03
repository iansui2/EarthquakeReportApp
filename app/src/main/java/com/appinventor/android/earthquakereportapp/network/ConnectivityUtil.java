package com.appinventor.android.earthquakereportapp.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.appinventor.android.earthquakereportapp.util.ContextGetter;

import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class ConnectivityUtil {

    private final AppExecutors appExecutors;
    @SuppressLint("StaticFieldLeak")
    private static Context context = ContextGetter.getAppContext();

    public ConnectivityUtil(AppExecutors appExecutors, Context context) {
        this.appExecutors = appExecutors;
        ConnectivityUtil.context = context;
    }

    public synchronized void checkInternetConnection(ConnectivityCallback callback) {
        appExecutors.getNetworkIO().execute(() -> {
            if (isNetworkAvailable()) {
                HttpsURLConnection connection = null;
                try {
                    connection = (HttpsURLConnection)
                            new URL("https://clients3.google.com/generate_204").openConnection();
                    connection.setRequestProperty("User-Agent", "Android");
                    connection.setRequestProperty("Connection", "close");
                    connection.setConnectTimeout(1000);
                    connection.connect();

                    boolean isConnected = connection.getResponseCode() == 204 && connection.getContentLength() == 0;
                    postCallback(callback, isConnected);
                    connection.disconnect();
                } catch (Exception e) {
                    postCallback(callback, false);
                    if(connection != null) connection.disconnect();
                }
            } else {
                postCallback(callback, false);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = cm.getNetworkCapabilities(cm.getActiveNetwork());
            if (cap == null) return false;
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = cm.getAllNetworks();
            for (Network n : networks) {
                NetworkInfo nInfo = cm.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        } else {
            NetworkInfo[] networks = cm.getAllNetworkInfo();
            for (NetworkInfo nInfo : networks) {
                if (nInfo != null && nInfo.isConnected()) return true;
            }
        }

        return false;
    }

    private void postCallback(ConnectivityCallback callBack, boolean isConnected) {
        appExecutors.mainThread().execute(() -> callBack.onDetected(isConnected));
    }

    public interface ConnectivityCallback {
        void onDetected(boolean isConnected);
    }

}


