package com.appinventor.android.earthquakereportapp.context;

import android.app.Application;
import android.content.Context;

public class ContextGetter extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
