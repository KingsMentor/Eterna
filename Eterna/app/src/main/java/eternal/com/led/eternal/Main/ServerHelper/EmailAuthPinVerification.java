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
import eternal.com.led.eternal.Main.Interfaces.EmailAuthCallBack;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public class EmailAuthPinVerification extends AsyncTask {

    EmailAuthCallBack mAuthCallBack;
    Context mContext;

    public EmailAuthPinVerification(EmailAuthCallBack callBack, Context context) {
        mContext = context;
        mAuthCallBack = callBack;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Uri uri = new Uri.Builder()
                .scheme("http")
                .authority("my-eterna.appspot.com")
                .path("emailAuthVerify")
                .appendQueryParameter("key", new UserPreference(mContext).getPhoneChangeKey())
                .appendQueryParameter("phone", new UserPreference(mContext).getPhoneNumber())
                .appendQueryParameter("pin", params[0].toString())
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
            if (Integer.parseInt(o.toString()) == ServerResponseCode.SUCCESS) {
                mAuthCallBack.onEmailAuth();
            } else if (Integer.parseInt(o.toString()) == ServerResponseCode.INVALID_PIN) {
                mAuthCallBack.onEmailAuthRequestDenied();
            } else {
                mAuthCallBack.onRequestFailed(mContext.getString(R.string.error_message));
            }
        } catch (Exception ex) {
            mAuthCallBack.onRequestFailed(o.toString());
        }

    }
}
