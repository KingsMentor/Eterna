package eternal.com.led.eternal.Main.Fragment;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class FeatureFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sliding_fragment, container, false);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title_textview);
        titleTextView.setText(getArguments().getString("title"));

        TextView descriptionTextView = (TextView) rootView.findViewById(R.id.description_textview);
        descriptionTextView.setText(getArguments().getString("desc"));


        return rootView;
    }

    public FeatureFragment newInstance(String title, String description, int position) {
        FeatureFragment featureFragment = new FeatureFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("desc", description);
        bundle.putInt("position", position);
        featureFragment.setArguments(bundle);
        return featureFragment;
    }


}
