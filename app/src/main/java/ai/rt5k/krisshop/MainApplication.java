package ai.rt5k.krisshop;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashMap;

import ai.rt5k.krisshop.ModelObjects.Product;

public class MainApplication extends Application {
    public static final String SERVER_URL = "http://www.krucible.online:8080";
    public RequestQueue mainQueue;

    String uid = "";
    String sessionId = "";
    String name = "";
    HashMap<Product, Integer> cart = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
