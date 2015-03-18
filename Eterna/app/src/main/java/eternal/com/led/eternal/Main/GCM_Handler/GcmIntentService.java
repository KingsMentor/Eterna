package eternal.com.led.eternal.Main.GCM_Handler;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;

import eternal.com.led.eternal.Main.DataBaseHelper.EternalContract;
import eternal.com.led.eternal.Main.Providers.ContactedEternalProviders;

/**
 * Created by CrowdStar on 2/16/2015.
 */
public class GcmIntentService extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * Used to name the worker thread, important only for debugging.
     */
    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.
                MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            decodeMessage(extras.getString("name"),extras.getString("phone"),extras.getString("imageUrl"));
        }
    }

    public void decodeMessage(String name, String phone, String imageUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(EternalContract.NAME_KEY, name);
        contentValues.put(EternalContract.PHONE_KEY, phone);
        contentValues.put(EternalContract.IMAGE_URL, imageUrl);
        getContentResolver().insert(ContactedEternalProviders.CONTENT_URI, contentValues);

        new InfoNotificationManager(getBaseContext(), name, phone);
    }
}
