package eternal.com.led.eternal.Main.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.LruCache;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by CrowdStar on 2/16/2015.
 */
public class BitMapCache {


    public BitMapCache() {
        super();
    }





    public void addProfileImageBitmapToMemoryCache(Context context, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = shre.edit();
        edit.putString("profileImage", encodedImage);
        edit.commit();

    }


    public Drawable getProfileImageBitmapFromMemCache(Context context) {
        SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(context);
        String previouslyEncodedImage = shre.getString("profileImage", "");

        if (!previouslyEncodedImage.equalsIgnoreCase("")) {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return new BitmapDrawable(context.getResources(), bitmap);
        }
        return null;
    }
}
