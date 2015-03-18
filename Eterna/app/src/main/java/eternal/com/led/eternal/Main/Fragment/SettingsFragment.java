package eternal.com.led.eternal.Main.Fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/15/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
