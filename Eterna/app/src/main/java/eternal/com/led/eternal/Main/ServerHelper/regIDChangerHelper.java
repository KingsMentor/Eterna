package eternal.com.led.eternal.Main.ServerHelper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

import eternal.com.led.eternal.Main.Constant.ServerResponseCode;
import eternal.com.led.eternal.Main.Interfaces.InfoChangeListener;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/22/2015.
 */
public class regIDChangerHelper extends AsyncTask {

    private Context mContext;
    private InfoChangeListener mInfoChangeListener;
    private static String regId;

    public regIDChangerHelper(Context context, InfoChangeListener listener) {
        mContext = context;
        mInfoChangeListener = listener;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        regId = params[0].toString();
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appsot.com")
                .path("changeRegId")
                .appendQueryParameter("key", new UserPreference(mContext).getKey())
                .appendQueryParameter("phone", new UserPreference(mContext).getEmail())
                .appendQueryParameter("oldregId",new UserPreference(mContext).getRegID())
                .appendQueryParameter("regId",regId)
                .build();
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
        HttpConnectionParams.setSoTimeout(httpParameters, 15000);

        HttpPost httpPost = new HttpPost(uri.toString());
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        ResponseHandler<String> stringResponseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpPost, stringResponseHandler);
        } catch (IOException e) {
            return ServerResponseCode.FAILURE;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o.toString().trim().equalsIgnoreCase("" + ServerResponseCode.FAILURE)) {
            mInfoChangeListener.failedRequest();
        } else if (o.toString().trim().equalsIgnoreCase("" + ServerResponseCode.INVALID_PIN)) {
            mInfoChangeListener.onInvalidKey();
        } else {
            new UserPreference(mContext).setKey(o.toString());
            new UserPreference(mContext).setRegID(regId);
            mInfoChangeListener.onSuccess();
        }
    }
}
