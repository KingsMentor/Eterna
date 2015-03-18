package eternal.com.led.eternal.Main.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by CrowdStar on 2/20/2015.
 */
public class PinReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] puds = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) puds[0]);
        LocalBroadcastManager broadCast = LocalBroadcastManager.getInstance(context);
        if (messages.getMessageBody().startsWith("Eternal Code: ")) {
            String pin = messages.getMessageBody().replace("Eternal Code: ", "");
            intent = new Intent("eternal.com.led.eternal.Main.BroadcastReceivers").putExtra("pin", pin);
            broadCast.sendBroadcast(intent);
        }
    }
}
