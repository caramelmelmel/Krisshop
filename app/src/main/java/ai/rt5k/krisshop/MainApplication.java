package ai.rt5k.krisshop;

import android.app.Application;

import com.android.volley.RequestQueue;

public class MainApplication extends Application {
    public static final String SERVER_URL = "http://10.12.34.153:5000";
    public RequestQueue mainQueue;
}
