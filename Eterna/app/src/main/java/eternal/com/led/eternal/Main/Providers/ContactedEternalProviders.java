package eternal.com.led.eternal.Main.Providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

import eternal.com.led.eternal.Main.DataBaseHelper.EternalContract;
import eternal.com.led.eternal.Main.DataBaseHelper.EternalDataBase;
import eternal.com.led.eternal.Main.DataBaseHelper.EternalDataBaseHelper;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class ContactedEternalProviders extends ContentProvider {

    private static final String PROVIDER_NAME = "eternal.contentProvider";
    private static final String URI = "content://" + PROVIDER_NAME + "/" + EternalContract.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URI);
    private static EternalDataBase mDATA_BASE_HELPER;

    public final static int anEternal = 1;
    public final static int allEternal = 2;

    static final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, EternalContract.TABLE_NAME, allEternal);
        uriMatcher.addURI(PROVIDER_NAME, EternalContract.TABLE_NAME + "/#", anEternal);
    }

    @Override
    public boolean onCreate() {
        mDATA_BASE_HELPER = new EternalDataBase(getContext());
        Log.e("mf","jere");
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteDatabase = new SQLiteQueryBuilder();
        checkColumns(projection);
        sqLiteDatabase.setTables(EternalContract.TABLE_NAME);

        SQLiteDatabase db = mDATA_BASE_HELPER.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case anEternal:
                return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.led.eternal";
            case allEternal:
                return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.led.eternal";
            default:
                return null;
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDATA_BASE_HELPER.getWritableDatabase().insert(EternalContract.TABLE_NAME, "", values);
        if (rowID > 1) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowDeleted = mDATA_BASE_HELPER.getWritableDatabase().delete(EternalContract.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int choice = uriMatcher.match(uri);
        int rowUpdated = -1;
        switch (choice) {
            case allEternal:
                rowUpdated = mDATA_BASE_HELPER.getWritableDatabase().update(EternalContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case anEternal:
                rowUpdated = mDATA_BASE_HELPER.getWritableDatabase().update(EternalContract.TABLE_NAME, values, EternalContract.NAME_KEY + " = " + uri.getLastPathSegment(), selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {"" + EternalContract.ID_KEY, EternalContract.NAME_KEY,
                EternalContract.PHONE_KEY, EternalContract.IMAGE_URL,
        };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
