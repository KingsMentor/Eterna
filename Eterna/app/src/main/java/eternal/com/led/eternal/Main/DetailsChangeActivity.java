package eternal.com.led.eternal.Main;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import eternal.com.led.eternal.Main.Adapter.BitMapCache;
import eternal.com.led.eternal.Main.Fragment.ContactPickerFragment;
import eternal.com.led.eternal.Main.Fragment.FeatureFragment;
import eternal.com.led.eternal.Main.Fragment.FragmentChanger;
import eternal.com.led.eternal.Main.Fragment.PhoneVerificationFragment;
import eternal.com.led.eternal.Main.MainActivityController.MajorController;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

public class DetailsChangeActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setContentView(R.layout.details_change);
        new FragmentChanger(getSupportFragmentManager(), new ContactPickerFragment(), false);

    }

}