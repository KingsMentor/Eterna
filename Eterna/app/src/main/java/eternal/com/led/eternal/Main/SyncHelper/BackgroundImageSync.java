package eternal.com.led.eternal.Main.SyncHelper;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

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
import eternal.com.led.eternal.Main.Constant.UserConstant;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class BackgroundImageSync extends AbstractThreadedSyncAdapter {
    private ContentResolver mContentResolver;
    Context mContext;

    public BackgroundImageSync(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContext = context;
    }

    public BackgroundImageSync(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        if (!new UserPreference(mContext).getImageUrl().isEmpty()) {
            try {

                URL url = new URL(new UserPreference(mContext).getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setUseCaches(true);
                InputStream is = connection.getInputStream();
                Bitmap img = BitmapFactory.decodeStream(is);
                new BitMapCache().addProfileImageBitmapToMemoryCache(mContext, img);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (new UserPreference(mContext).getPushID().toString().isEmpty()) {
            regPush();
        }
    }

    GoogleCloudMessaging gcm;

    public void regPush() {
        String regId = "";
        if (gcm == null) {
            gcm = GoogleCloudMessaging.getInstance(mContext);
        }
        assert gcm != null;
        try {
            regId = gcm.register(UserConstant.APP_PROJECT_NUMBER);
            new UserPreference(mContext).setPushID(regId);
            pushToServer(regId);
        } catch (IOException e) {
        }
    }

    public void pushToServer(String regID) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("registerPush")
                .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                .appendQueryParameter("key", new UserPreference(mContext).getKey())
                .appendQueryParameter("regID", regID)
                .build();
        HttpPost regIDHttpPost = new HttpPost(uri.toString());
        HttpClient regIdHttpClient = new DefaultHttpClient();
        ResponseHandler<String> stringResponseHandler = new BasicResponseHandler();
        try {
            regIdHttpClient.execute(regIDHttpPost, stringResponseHandler);
        } catch (IOException e) {
        }
    }
}
