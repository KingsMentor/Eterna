package eternal.com.led.eternal.Main.GCM_Handler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import eternal.com.led.eternal.Main.NotificationActivity;
import eternal.com.led.eternal.R;


/**
 * Created by CrowdStar on 2/16/2015.
 */
public class InfoNotificationManager {
    NotificationManager mNotificationManager;

    public InfoNotificationManager(Context context, String name, String phone) {
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = null;
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("play_sound", false)) {
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(phone)
                            .setContentText(name + " changed his contact number. Update your list to stay connected.");
        } else {
            mBuilder =
                    new NotificationCompat.Builder(context)
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle(phone)
                            .setContentText(name + " changed his contact number. Update your list to stay connected.");
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, NotificationActivity.class), 0);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(2, mBuilder.build());
    }
}
