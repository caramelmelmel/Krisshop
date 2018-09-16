package ai.rt5k.krisshop;

import android.app.Application;

import com.android.volley.RequestQueue;

public class MainApplication extends Application {
    public static final String SERVER_URL = "http://192.168.1.14";
    public RequestQueue mainQueue;
}
