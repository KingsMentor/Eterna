package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 3/18/2015.
 */
public interface RetrieveAccountCallback extends UnexpectedError {

    public void onAccountRetrieved(String phone, String key, String regKey);

    public void onNoUserExist();

    @Override
    void onRequestFailed(String error);
}
