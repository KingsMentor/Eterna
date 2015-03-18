package eternal.com.led.eternal.Main.DataBaseHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class EternalDataBaseHelper {


    public EternalDataBaseHelper() {

    }


    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE " + EternalContract.TABLE_NAME + " ( " +
                EternalContract.ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EternalContract.NAME_KEY + " VARCHAR(45)  NOT NULL," +
                EternalContract.PHONE_KEY + " INTEGER(15) NOT NULL," +
                EternalContract.IMAGE_URL + " VARCHAR(45) NOT NULL" +
                ");";
        db.execSQL(sql);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
