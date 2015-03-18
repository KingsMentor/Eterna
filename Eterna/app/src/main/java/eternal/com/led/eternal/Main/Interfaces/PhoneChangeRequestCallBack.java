package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public interface PhoneChangeRequestCallBack extends UnexpectedError{

    public void onRequestCompleted();
    public void onRequestDenied();
}
