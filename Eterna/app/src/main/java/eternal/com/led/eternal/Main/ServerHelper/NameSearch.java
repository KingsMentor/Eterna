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
import eternal.com.led.eternal.Main.Interfaces.NameSearchCallBack;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/26/2015.
 */
public class NameSearch extends AsyncTask {

    private NameSearchCallBack searchCallBack;
    private Context mContext;

    public NameSearch(Context context, NameSearchCallBack callBack) {
        mContext = context;
        searchCallBack = callBack;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("search")
                .appendQueryParameter("name", params[0].toString())
                .appendQueryParameter("key", new UserPreference(mContext).getKey())
                .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
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
            if (Integer.parseInt(o.toString()) == ServerResponseCode.NO_USER_FOUND) {
                searchCallBack.onNameNotFound();
            } else if (Integer.parseInt(o.toString()) == ServerResponseCode.INVALID_PIN) {
                searchCallBack.onInvalidToken();
            } else {
                searchCallBack.onRequestFailed(o.toString());
            }
        } catch (Exception ex) {
            readJson(o.toString());
        }
    }

    public void readJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            String name = jsonArray.getJSONObject(0).getString("name");
            String contact = jsonArray.getJSONObject(0).getString("phone");
            String imageUrl = jsonArray.getJSONObject(0).getString("imageUrl");
            searchCallBack.onNameFound(name, contact, imageUrl);
        } catch (JSONException e) {
            searchCallBack.onRequestFailed(jsonString);
        }
    }
}
