package eternal.com.led.eternal.Main.ServerHelper;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import eternal.com.led.eternal.Main.Constant.ServerResponseCode;
import eternal.com.led.eternal.Main.Interfaces.NameVerification;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class NameClaimerHelper extends AsyncTask {

    private NameVerification mNameVerification;
    private Context mContext;

    public NameClaimerHelper(Context context, NameVerification verification) {
        mNameVerification = verification;
        mContext = context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("claimName")
                .appendQueryParameter("name", params[0].toString())
                .appendQueryParameter("key", new UserPreference(mContext).getVerifyKey())
                .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                .build();
        HttpPost httpPost = new HttpPost(uri.toString());
        HttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            return httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            return ServerResponseCode.FAILURE;
        }

    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (result.toString().trim().equalsIgnoreCase("" + ServerResponseCode.NAME_TAKEN)) {
            mNameVerification.onNameTaken();
        } else if (result.toString().trim().equalsIgnoreCase("" + ServerResponseCode.FAILURE)) {
            mNameVerification.onRequestFailed();
        } else {
            readJson(result.toString());
        }
    }

    public void readJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            String key = jsonArray.getJSONObject(0).getString("key");
            new UserPreference(mContext).setKey(key);
            mNameVerification.onNameClaimed();
        } catch (JSONException e) {
            mNameVerification.onRequestFailed();
        }
    }
}

