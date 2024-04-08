package org.apache.cordova.firebase;
import org.apache.cordova.firebase.FirebasePluginMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import android.util.Log;
import java.util.Map;

public class CustomFirebaseMessagingService extends FirebasePluginMessagingService {
    private static final String TAG = "custom-fcm-service";

    static boolean includesWebEngage() {
        try {
            Class.forName("com.webengage.sdk.android.WebEngage");
            return true;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "WebEngage Not Found", e);
        } catch (Throwable t) {
            Log.e(TAG, "Error while checking for WebEngage", t);
        }
        return false;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Add your custom native code here
        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            if (includesWebEngage()) {
                com.webengage.sdk.android.WebEngage.engage(getApplicationContext());
                if (data.containsKey("source") && "webengage".equals(data.get("source"))) {
                    com.webengage.sdk.android.WebEngage.get().receive(data);
                }
            }
            else {
                super.onMessageReceived(remoteMessage);
               
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        // Send token to server
        if (includesWebEngage()) {
            com.webengage.sdk.android.WebEngage.get().setRegistrationID(s);
        }
        else
        {
            super.onNewToken(s);
        }
    }
}