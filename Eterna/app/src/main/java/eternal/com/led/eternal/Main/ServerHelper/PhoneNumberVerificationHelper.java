package eternal.com.led.eternal.Main.ServerHelper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import eternal.com.led.eternal.Main.Constant.ServerResponseCode;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class PhoneNumberVerificationHelper extends AsyncTask {


    private PhoneVerification mPhoneVerification;
    private Context mContext;
    private String numbers = "";


    public PhoneNumberVerificationHelper(Context context, PhoneVerification mVerification) {
        mPhoneVerification = mVerification;
        mContext = context;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Uri uri = null;
            HttpPost httpPost = null;

            if (new UserPreference(mContext).isPhoneChanging()) {
                uri = new Uri.Builder()
                        .scheme("http")
                        .authority("my-eterna.appspot.com")
                        .appendQueryParameter("oldKey", new UserPreference(mContext).getKey())
                        .path("authPhone")
                        .appendQueryParameter("phone", params[0].toString())
                        .build();
            } else {
                uri = new Uri.Builder()
                        .scheme("http")
                        .authority("my-eterna.appspot.com")
                        .path("authPhone")
                        .appendQueryParameter("phone", params[0].toString())
                        .build();
            }

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);

            httpPost = new HttpPost(uri.toString());
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            return httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            return ServerResponseCode.FAILURE;
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        try {
            int response = Integer.parseInt(result.toString());
            if (response == ServerResponseCode.USER_EXIST)
                mPhoneVerification.onUserExist();
        } catch (Exception ex) {
            decodeResponse(result.toString());
        }
    }

    public void decodeResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.getJSONObject(0).getString("type").trim().equalsIgnoreCase("new")) {
                mPhoneVerification.onPhoneVerified(true);
                String verifyKey = jsonArray.getJSONObject(0).getString("regkey");
                new UserPreference(mContext).setVerifyKey(verifyKey);
            } else {
                mPhoneVerification.onPhoneVerified(false);
                String verifyKey = jsonArray.getJSONObject(0).getString("regkey");
                new UserPreference(mContext).setVerifyKey(verifyKey);
                String key = jsonArray.getJSONObject(0).getString("oldkey");
                new UserPreference(mContext).setKey(key);
            }
        } catch (JSONException e) {
            mPhoneVerification.onRequestFailed();
        }

    }
}
