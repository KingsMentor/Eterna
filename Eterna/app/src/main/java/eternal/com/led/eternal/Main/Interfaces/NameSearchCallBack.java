package eternal.com.led.eternal.Main.Interfaces;

/**
 * Created by CrowdStar on 2/26/2015.
 */
public interface NameSearchCallBack extends UnexpectedError {
    public void onNameFound(String name, String phone, String image);

    public void onNameNotFound();

    public void onInvalidToken();
}
