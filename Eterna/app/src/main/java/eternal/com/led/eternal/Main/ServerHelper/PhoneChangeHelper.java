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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import eternal.com.led.eternal.Main.Constant.ServerResponseCode;
import eternal.com.led.eternal.Main.Interfaces.PhoneChangeRequestCallBack;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public class PhoneChangeHelper extends AsyncTask {

    PhoneChangeRequestCallBack mRequestCallBack;
    Context mContext;

    public PhoneChangeHelper(PhoneChangeRequestCallBack callBack, Context context) {
        mContext = context;
        mRequestCallBack = callBack;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("emailAuth")
                .appendQueryParameter("key", new UserPreference(mContext).getKey())
                .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                .appendQueryParameter("email", new UserPreference(mContext).getEmail())
                .build();

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
        HttpConnectionParams.setSoTimeout(httpParameters, 15000);

        HttpPost httpPost = new HttpPost(uri.toString());
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            return ServerResponseCode.FAILURE;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            if (Integer.parseInt(o.toString()) == ServerResponseCode.INVALID_PIN) {
                mRequestCallBack.onRequestDenied();
            } else {
                mRequestCallBack.onRequestFailed(mContext.getString(R.string.error_message));
            }

        } catch (Exception ex) {
            decodeJson(o.toString());
        }
    }

    public void decodeJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            String regKey = jsonArray.getJSONObject(0).getString("regKey");
            new UserPreference(mContext).setPhoneChangeKey(regKey);
            mRequestCallBack.onRequestCompleted();
        } catch (JSONException e) {
            mRequestCallBack.onRequestFailed(mContext.getString(R.string.error_message));
        }
    }
}
