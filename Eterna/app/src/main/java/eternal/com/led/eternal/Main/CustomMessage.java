package eternal.com.led.eternal.Main;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by CrowdStar on 2/15/2015.
 */
public class CustomMessage {
    public CustomMessage(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
