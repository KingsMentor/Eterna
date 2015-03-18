package eternal.com.led.eternal.Main.Service;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import eternal.com.led.eternal.Main.Adapter.BitMapCache;
import eternal.com.led.eternal.Main.Constant.ServerResponseCode;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/23/2015.
 */
public class EmailAddService extends IntentService {


    public EmailAddService() {
        super("EmailAddService");
    }

    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, startId, startId);
        return START_STICKY;
    }

    LocalBroadcastManager broadCast = LocalBroadcastManager.getInstance(this);
    Intent intent = new Intent(".BroadcastReceivers");

    @Override
    protected void onHandleIntent(Intent intent) {
        try {

            URL url = new URL(new UserPreference(this).getImageUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            InputStream is = connection.getInputStream();
            Bitmap img = BitmapFactory.decodeStream(is);
            new BitMapCache().addProfileImageBitmapToMemoryCache(this, img);
            changeEmail(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeEmail(Intent intentInfo) {
        String result = changeEmailRequest(intentInfo);
        if (result.toString().trim().equalsIgnoreCase("" + ServerResponseCode.FAILURE)) {
            broadCast.sendBroadcast(intent.putExtra("responseCode", ServerResponseCode.FAILURE));
        } else if (result.toString().trim().equalsIgnoreCase("" + ServerResponseCode.INVALID_PIN)) {
            broadCast.sendBroadcast(intent.putExtra("responseCode", ServerResponseCode.INVALID_PIN));
        } else {
            new UserPreference(this).setDetailsOnServer(true);
            broadCast.sendBroadcast(intent.putExtra("responseCode", ServerResponseCode.SUCCESS));
        }
    }


    public String changeEmailRequest(final Intent intent) {


        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("changeEmail")
                .appendQueryParameter("key", new UserPreference(EmailAddService.this).getKey())
                .appendQueryParameter("phone", new UserPreference(EmailAddService.this).getPhoneNumber())
                .appendQueryParameter("email", intent.getExtras().getString("email"))
                .appendQueryParameter("imageUrl", intent.getExtras().getString("imageUrl"))
                .build();
        HttpPost httpPost = new HttpPost(uri.toString());
        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> stringResponseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpPost, stringResponseHandler);
        } catch (IOException e) {
            return "" + ServerResponseCode.FAILURE;
        }


    }


}
