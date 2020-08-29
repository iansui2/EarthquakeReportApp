package com.appinventor.android.earthquakereportapp.context;

import android.app.Application;
import android.content.Context;

public class ContextGetter extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        // Gets the context from Application
        context = getApplicationContext();
    }

    // Returns the context
    public static Context getAppContext() {
        return context;
    }
}
