package eternal.com.led.eternal.Main;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import eternal.com.led.eternal.Main.Adapter.InfoAdapter;
import eternal.com.led.eternal.Main.DataBaseHelper.EternalContract;
import eternal.com.led.eternal.Main.GCM_Handler.InfoNotificationManager;
import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.Main.Providers.ContactedEternalProviders;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/16/2015.
 */
public class NotificationActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    InfoAdapter infoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (InfoNotificationManager.mNotificationManager != null) {
            InfoNotificationManager.mNotificationManager.cancelAll();
            InfoNotificationManager.position = 1;
        }
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.notification_list);

        ListView listView = (ListView) findViewById(R.id.info_list);
        listView.setEmptyView(findViewById(android.R.id.empty));

        getSupportLoaderManager().initLoader(0, savedInstanceState, this);
        infoAdapter = new InfoAdapter(NotificationActivity.this, null);
        listView.setAdapter(infoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(NotificationActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.update_contact, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        UserModel userModel = (UserModel) view.getTag();
                        Intent intent = new Intent(Intent.ACTION_INSERT);
                        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                        intent.putExtra(ContactsContract.Intents.Insert.NAME, userModel.getName());
                        intent.putExtra(ContactsContract.Intents.Insert.PHONE, userModel.getPhone());
                        startActivity(intent);
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {"" + EternalContract.ID_KEY, EternalContract.NAME_KEY, EternalContract.PHONE_KEY, EternalContract.IMAGE_URL};
        Uri uri = Uri.parse(ContactedEternalProviders.CONTENT_URI + "/" + ContactedEternalProviders.allEternal);
        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        infoAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        infoAdapter.swapCursor(null);
    }
}