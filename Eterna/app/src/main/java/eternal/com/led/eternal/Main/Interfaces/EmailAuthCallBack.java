package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public interface EmailAuthCallBack extends UnexpectedError {

    public void onEmailAuth();
    public void onEmailAuthRequestDenied();
}
