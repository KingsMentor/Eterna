package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public interface PinVerification {
    public void onPinVerified();

    public void onInvalidPin();
    void onRequestFailed();
}
