package eternal.com.led.eternal.Main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import eternal.com.led.eternal.Main.Fragment.SettingsFragment;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/15/2015.
 */
public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
