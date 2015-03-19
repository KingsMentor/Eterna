package eternal.com.led.eternal.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import eternal.com.led.eternal.Main.Adapter.BitMapCache;
import eternal.com.led.eternal.Main.Fragment.FeatureFragment;
import eternal.com.led.eternal.Main.Fragment.FragmentChanger;
import eternal.com.led.eternal.Main.Fragment.PhoneVerificationFragment;
import eternal.com.led.eternal.Main.MainActivityController.MajorController;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

public class MainActivity extends FragmentActivity {

    ViewPager sliding_pager;
    FeaturePager mFeaturePager;
    FrameLayout mFrameLayout;
    Button continueButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new UserPreference(this).isLogin()) {
            startActivity(new Intent(this, MajorController.class));
            finish();
        } else if (new UserPreference((this)).isPhoneChanging()) {
            setContentView(R.layout.main_splach_screen);
            new UserPreference(this).setPhoneChange(false);

            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = shre.edit();
            edit.putString("profileImage","");

            new FragmentChanger(getSupportFragmentManager(), new PhoneVerificationFragment().newInstance(null), false);
        } else {
            setContentView(R.layout.main_splach_screen);
            sliding_pager = (ViewPager) findViewById(R.id.home_slide);
            mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
            mFeaturePager = new FeaturePager(getSupportFragmentManager());
            sliding_pager.setAdapter(mFeaturePager);
            sliding_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    //mFrameLayout.setBackground(getBackground(position));
                    if (position == 3) {
                        continueButton.setText(getString(R.string.Continue));
                    } else {
                        continueButton.setText(getString(R.string.next));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            continueButton = (Button) findViewById(R.id.continue_button);

            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sliding_pager.getCurrentItem() == 3)
                        new FragmentChanger(getSupportFragmentManager(), new PhoneVerificationFragment().newInstance(null), false);
                    else
                        sliding_pager.setCurrentItem(sliding_pager.getCurrentItem() + 1, true);

                }
            });
        }

    }



    public class FeaturePager extends FragmentPagerAdapter {
        public FeaturePager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return new FeatureFragment().newInstance(getResources().getStringArray(R.array.tile)[position], getResources().getStringArray(R.array.description)[position], position);
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.tile).length;
        }
    }
}
