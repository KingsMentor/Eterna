package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public interface PhoneVerification {

    public void onPhoneVerified(boolean isNewUser);

    public void onUserExist();
    public void onRequestFailed();

}
