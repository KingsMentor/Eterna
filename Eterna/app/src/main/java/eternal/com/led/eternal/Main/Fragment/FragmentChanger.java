package eternal.com.led.eternal.Main.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/18/2015.
 */
public class FragmentChanger {
    public FragmentChanger(FragmentManager fragmentManager, Fragment fragment, boolean addToStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_left, R.animator.slide_right, R.animator.slide_left, R.animator.slide_right);
        if (addToStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.content_frame, fragment).commitAllowingStateLoss();

    }
}
