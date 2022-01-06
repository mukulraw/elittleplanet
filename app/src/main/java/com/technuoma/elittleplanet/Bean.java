package com.technuoma.elittleplanet;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Bean extends Application {
    private static Context context;

    public String baseurl = "https://elittleplanet.com/";


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

    }
}
