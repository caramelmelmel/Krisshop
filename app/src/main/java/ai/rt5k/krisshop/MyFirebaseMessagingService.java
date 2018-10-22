package ai.rt5k.krisshop;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService";
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyFirebaseMessagingService.this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("fcm_token", token);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, remoteMessage.getData().toString());
    }
}
