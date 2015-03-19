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
import eternal.com.led.eternal.Main.Interfaces.EmailAuthCallBack;
import eternal.com.led.eternal.Main.Interfaces.RetrieveAccountCallback;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public class ContactRetrieverHelper extends AsyncTask {

    RetrieveAccountCallback mAccountCallback;
    Context mContext;

    public ContactRetrieverHelper(RetrieveAccountCallback callBack, Context context) {
        mContext = context;
        mAccountCallback = callBack;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("lostContact")
                .appendQueryParameter("email", params[0].toString())
                .appendQueryParameter("phone", params[1].toString())
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
            if (Integer.parseInt(o.toString()) == ServerResponseCode.USER_NOT_EXIST) {
                mAccountCallback.onNoUserExist();
            } else {
                mAccountCallback.onRequestFailed(o.toString());
            }
        } catch (Exception ex) {
            decodeServerResponse(o.toString());
        }
    }

    public void decodeServerResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            String phone = jsonArray.getJSONObject(0).getString("phone");
            String key = jsonArray.getJSONObject(0).getString("oldKey");
            String regKey = jsonArray.getJSONObject(0).getString("regKey");
            mAccountCallback.onAccountRetrieved(phone, key, regKey);
        } catch (JSONException e) {
            mAccountCallback.onRequestFailed(e.getMessage());
        }
    }
}
