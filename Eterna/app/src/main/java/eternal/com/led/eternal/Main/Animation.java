package eternal.com.led.eternal.Main;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/15/2015.
 */
public class Animation {


    static ObjectAnimator animator;
    public void TransitBg(Context context, View view) {
        animator = ObjectAnimator.ofInt(view, "backgroundColor", view.getResources().getColor(R.color.blue), view.getResources().getColor(R.color.bg_color), view.getResources().getColor(R.color.blue), view.getResources().getColor(R.color.bg_blue)).setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();


    }

    public void endAnimation(Context context,View view) {
        animator.end();
        view.setBackgroundColor(context.getResources().getColor(R.color.bg_blue));
    }
}
