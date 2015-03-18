package eternal.com.led.eternal.Main.ServerHelper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.IntegerRes;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.Interfaces.PinVerification;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class PinVerificationHelper extends AsyncTask {
    private PinVerification mPinVerification;
    private Context mContext;

    public PinVerificationHelper(Context context, PinVerification mVerification) {
        mPinVerification = mVerification;
        mContext = context;
    }

    String numbers;

    public PinVerificationHelper(Context context, PinVerification mVerification, ArrayList numberList) {
        mPinVerification = mVerification;
        mContext = context;
        for (Object number : numberList) {
            numbers += number.toString() + "_";
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = null;
        HttpPost httpPost;
        try {

            if (new UserPreference(mContext).isPhoneChanging()) {
                uri = new Uri.Builder()
                        .scheme("http")
                        .authority("my-eterna.appspot.com")
                        .path("verifyPin")
                        .appendQueryParameter("pin", params[0].toString())
                        .appendQueryParameter("key", new UserPreference(mContext).getVerifyKey())
                        .appendQueryParameter("userType", params[1].toString())
                        .appendQueryParameter("oldUserKey", new UserPreference(mContext).getKey())
                        .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                        .appendQueryParameter("isChange", "Yes")
                        .build();
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("alertNumbers", "" + numbers));
                httpPost = new HttpPost(uri.toString());
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            } else {
                uri = new Uri.Builder()
                        .scheme("http")
                        .authority("my-eterna.appspot.com")
                        .path("verifyPin")
                        .appendQueryParameter("pin", params[0].toString())
                        .appendQueryParameter("key", new UserPreference(mContext).getVerifyKey())
                        .appendQueryParameter("userType", params[1].toString())
                        .appendQueryParameter("oldUserKey", new UserPreference(mContext).getKey())
                        .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                        .build();
                httpPost = new HttpPost(uri.toString());
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            ResponseHandler<String> integerResponseHandler = new BasicResponseHandler();

            return httpClient.execute(httpPost, integerResponseHandler);
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        try {
            int intResult = Integer.parseInt(o.toString());
            if (intResult == ServerResponseCode.SUCCESS) {
                mPinVerification.onPinVerified();
            } else if (intResult == ServerResponseCode.INVALID_PIN) {
                mPinVerification.onInvalidPin();
            } else {
                mPinVerification.onRequestFailed();
            }
        } catch (Exception ex) {
            decodeResponse(o.toString());
        }
    }

    public void decodeResponse(String response) {
        UserPreference userPreference = new UserPreference(mContext);
        try {
            JSONArray jsonArray = new JSONArray(response);
            String phone = jsonArray.getJSONObject(0).getString("phone");
            userPreference.setPhoneNumber(phone);

            String email = jsonArray.getJSONObject(0).getString("email");
            userPreference.setEmail(email);

            String name = jsonArray.getJSONObject(0).getString("name");
            userPreference.setName(name);

            String imageUrl = jsonArray.getJSONObject(0).getString("imageUrl");
            userPreference.setImageUrl(imageUrl);

            if (email.trim().isEmpty() && imageUrl.trim().isEmpty()) {
                userPreference.setDetailsOnServer(false);
            } else {
                userPreference.setDetailsOnServer(true);
            }

            String key = jsonArray.getJSONObject(0).getString("key");
            userPreference.setKey(key);

            mPinVerification.onPinVerified();
        } catch (JSONException e) {
            mPinVerification.onRequestFailed();
        }
    }
}
