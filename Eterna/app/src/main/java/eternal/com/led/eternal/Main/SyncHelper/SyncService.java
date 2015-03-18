package eternal.com.led.eternal.Main.SyncHelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by CrowdStar on 2/19/2015.
 */
public class SyncService extends Service {

    private static BackgroundImageSync backgroundImageSync;
    private static final Object syncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (syncAdapterLock) {
            if (backgroundImageSync == null) {
                backgroundImageSync = new BackgroundImageSync(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return backgroundImageSync.getSyncAdapterBinder();
    }
}
