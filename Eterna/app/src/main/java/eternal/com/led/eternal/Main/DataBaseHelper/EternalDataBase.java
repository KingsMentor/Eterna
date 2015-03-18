package eternal.com.led.eternal.Main.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class EternalDataBase extends SQLiteOpenHelper {

    public EternalDataBase(Context context) {
        super(context, EternalContract.DATABASE_NAME, null, EternalContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        new EternalDataBaseHelper().onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        new EternalDataBaseHelper().onUpgrade(db, oldVersion, newVersion);
    }
}
