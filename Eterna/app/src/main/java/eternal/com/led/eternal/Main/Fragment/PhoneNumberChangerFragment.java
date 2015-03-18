package eternal.com.led.eternal.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Animation;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Interfaces.EmailAuthCallBack;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.ServerHelper.EmailAuthPinVerification;
import eternal.com.led.eternal.Main.ServerHelper.PhoneNumberVerificationHelper;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class PhoneNumberChangerFragment extends Fragment implements EmailAuthCallBack {
    EditText phoneNumberEditText;
    Button verifyButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.phone_change_activity, container, false);
        phoneNumberEditText = (EditText) rootView.findViewById(R.id.phone_field);
        verifyButton = (Button) rootView.findViewById(R.id.continue_button);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Animation().TransitBg(getActivity(), verifyButton);
                changeEnable(false);
                new EmailAuthPinVerification(PhoneNumberChangerFragment.this, getActivity()).execute(phoneNumberEditText.getText().toString());

            }
        });
        return rootView;
    }

    public void changeEnable(boolean status) {
        verifyButton.setEnabled(status);
        phoneNumberEditText.setEnabled(status);

    }

    public PhoneNumberChangerFragment newInstance(ArrayList numberList) {
        PhoneNumberChangerFragment fragment = new PhoneNumberChangerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("numberList", numberList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onEmailAuth() {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), verifyButton);
        new UserPreference(getActivity()).setPhoneChange(true);
        new FragmentChanger(getFragmentManager(), new PhoneVerificationFragment().newInstance(getArguments().getStringArrayList("numberList")), false);
    }

    @Override
    public void onEmailAuthRequestDenied() {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), verifyButton);
        new CustomMessage(getActivity(), "deny");
    }

    @Override
    public void onRequestFailed(String error) {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), verifyButton);
        new CustomMessage(getActivity(), "error");
    }
}
