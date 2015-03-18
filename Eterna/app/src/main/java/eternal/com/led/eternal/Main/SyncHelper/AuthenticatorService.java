package eternal.com.led.eternal.Main.SyncHelper;

import android.accounts.Account;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import eternal.com.led.eternal.Main.SharedPreference.UserPreference;

/**
 * Created by CrowdStar on 2/19/2015.
 */
public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private Authenticator mAuthenticator;
    public static final String AUTHORITY = "eternal.com.led.eternal.providers";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "eternal.appspot.com";
    // The account name


    public static Account GetAccount(Context context) {
        // Note: Normally the account name is set to the user's identity (username or email
        // address). However, since we aren't actually using any user accounts, it makes more sense
        // to use a generic string in this case.
        //
        // This string should *not* be localized. If the user switches locale, we would not be
        // able to locate the old account, and may erroneously register multiple accounts.
        final String accountName = new UserPreference(context).getName();
        return new Account(accountName, ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
