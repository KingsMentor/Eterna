package eternal.com.led.eternal.Main.GCM_Handler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Constant.UserConstant;
import eternal.com.led.eternal.Main.NotificationActivity;
import eternal.com.led.eternal.R;


/**
 * Created by CrowdStar on 2/16/2015.
 */
public class InfoNotificationManager {
    static public NotificationManager mNotificationManager;
    static public int position = 1;
    static ArrayList contacts = new ArrayList();

    public InfoNotificationManager(Context context, String name, String phone) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = null;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        contacts.add(name);
        if (position > 1) {
            phone = "Updates from your Eterna's";
            for (Object names : contacts) {
                inboxStyle.addLine(name);
            }
        } else {
            phone = "Updates from an Eterna";
            inboxStyle.addLine(name);
        }
        inboxStyle.setBigContentTitle(phone);
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("play_sound", false)) {


            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(phone)
                            .setNumber(position)
                            .setStyle(inboxStyle)
                            .setContentText(name);


        } else {
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setStyle(inboxStyle)
                            .setContentTitle(phone)
                            .setNumber(position)
                            .setContentText(name);


        }

        position++;

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, NotificationActivity.class), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(UserConstant.INFO_KEY, mBuilder.build());
    }
}
