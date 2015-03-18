package eternal.com.led.eternal.Main.SharedPreference;

import android.content.Context;

import eternal.com.led.eternal.Main.Constant.UserConstant;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class UserPreference {

    private Context mContext;

    public UserPreference(Context context) {
        mContext = context;
    }

    public void setPhoneNumber(String number) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".number", number).commit();
    }

    public void setEmail(String email) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".email", email).commit();

    }

    public void setLogin(boolean isLogin) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putBoolean(getClass().getSimpleName() + ".login", isLogin).commit();

    }

    /**
     * setName of the user
     *
     * @param name name of the user
     */
    public void setName(String name) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".name", name).commit();

    }

    public String getPhoneNumber() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".number", "");
    }

    public boolean isLogin() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getBoolean(getClass().getSimpleName() + ".login", false);

    }

    public String getEmail() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".email", "");

    }


    public String getName() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".name", "");

    }


    public void setKey(String key) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".key", key).commit();

    }

    public void setVerifyKey(String key) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".verifyKey", key).commit();

    }

    public String getKey() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".key", "");

    }

    public String getVerifyKey() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".verifyKey", "");

    }


    public void setImageUrl(String imageUrl) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".imageUrl", imageUrl).commit();

    }

    public String getImageUrl() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".imageUrl", "");

    }


    public void setRegID(String regID) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".regID", regID).commit();

    }

    public String getRegID() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".regID", "");

    }


    public void setPhoneChangeKey(String PhoneChangeKey) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".PhoneChangeKey", PhoneChangeKey).commit();

    }

    public String getPhoneChangeKey() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".PhoneChangeKey", "");

    }

    public void setPushID(String PushID) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".PushID", PushID).commit();

    }

    public String getPushID() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".PushID", "");

    }

    public void setPhoneChange(boolean status) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putBoolean(getClass().getSimpleName() + ".isPhoneChange", status).commit();

    }

    public boolean isPhoneChanging() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getBoolean(getClass().getSimpleName() + ".isPhoneChange", false);

    }

    public void setDetailsOnServer(boolean status) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putBoolean(getClass().getSimpleName() + ".isServerDetailsSet", status).commit();

    }

    public boolean isServerDetailsSet() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getBoolean(getClass().getSimpleName() + ".isServerDetailsSet", false);

    }

    public void setSearchText(String SearchText) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".SearchText", SearchText).commit();

    }

    public String getSearchText() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".SearchText", "");

    }
}
