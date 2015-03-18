package eternal.com.led.eternal.Main.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/9/2015.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
        init(context, null, R.styleable.CustomTextViewStyle_CustomTextViewStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.styleable.CustomTextViewStyle_CustomTextViewStyle);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyle, 0);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/" + attributes.getString(R.styleable.CustomTextView_font) + ".ttf");
        setTypeface(tf);
    }
}
