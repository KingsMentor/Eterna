package eternal.com.led.eternal.Main.ImageHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/12/2015.
 */
public class ImageLoader {
    Context mContext;

    public ImageLoader(Context context) {
        mContext = context;
        drawable = context.getResources().getDrawable(R.drawable.ic_avater);
    }

    Drawable drawable;

    public Drawable LoadImageFromWebOperations(final String link, final ImageView imageView) {

        new AsyncTask() {


            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setUseCaches(true);
                    InputStream is = connection.getInputStream();
                    Bitmap img = BitmapFactory.decodeStream(is);
                    drawable = new BitmapDrawable(mContext.getResources(), img);
                    return drawable;
                } catch (Exception e) {
                    Log.e("message", e.getMessage());
                    return drawable;
                }

            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                imageView.setImageDrawable(drawable);
            }
        }.execute();

        return drawable;

    }
}

