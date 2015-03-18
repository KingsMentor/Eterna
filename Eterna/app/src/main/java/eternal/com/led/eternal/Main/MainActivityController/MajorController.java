package eternal.com.led.eternal.Main.MainActivityController;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.FrameLayout;

import eternal.com.led.eternal.Main.CallFragmentActivity;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Fragment.ProfileFragment;
import eternal.com.led.eternal.Main.SyncHelper.SyncUtils;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/5/2015.
 */
public class MajorController extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        setContentView(R.layout.activity_main);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_tab_frame);
        if (frameLayout != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_tab_frame, new ProfileFragment().newInstance(null, true)).commitAllowingStateLoss();
            findViewById(R.id.footer_view_layout).setVisibility(View.GONE);
        }
        SyncUtils.CreateSyncAccount(this);
        SyncUtils.TriggerRefresh();
        overridePendingTransition(R.animator.slide_left, R.animator.slide_right);
    }


    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
