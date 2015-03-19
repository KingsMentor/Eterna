package eternal.com.led.eternal.Main.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.internal.ph;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Animation;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.CustomViews.CustomTextView;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.ServerHelper.PhoneNumberVerificationHelper;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class PhoneVerificationFragment extends Fragment implements PhoneVerification {
    EditText phoneNumberEditText;
    EditText ccEditText;
    Button continueButton;
    Button lostContactButton;
    CustomTextView textView;
    CustomTextView orSeperatorTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.claim_phone_activity, container, false);
        phoneNumberEditText = (EditText) rootView.findViewById(R.id.phone_field);
        ccEditText = (EditText) rootView.findViewById(R.id.cc);

        textView = (CustomTextView) rootView.findViewById(R.id.phone_access_textView);
        orSeperatorTextView = (CustomTextView) rootView.findViewById(R.id.seperator);
        lostContactButton = (Button) rootView.findViewById(R.id.lost_contact);

        if (new UserPreference(getActivity()).isPhoneChanging()) {
            textView.setText(getString(R.string.change_phone));
            lostContactButton.setVisibility(View.GONE);
            orSeperatorTextView.setVisibility(View.GONE);
        }


        lostContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FragmentChanger(getFragmentManager(), new LostContactFragment(), true);
            }
        });


        continueButton = (Button) rootView.findViewById(R.id.continue_button);
        continueButton.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneNumberEditText.getText().toString().trim().isEmpty() || ccEditText.getText().toString().trim().isEmpty()) {
                    new CustomMessage(getActivity(), getString(R.string.empty_field));
                } else {
                    new CustomMessage(getActivity(), getString(R.string.request_message));
                    changeViewEnable(false);
                    new Animation().TransitBg(getActivity(), continueButton);
                    String phone = "+" + ccEditText.getText().toString().trim() + phoneNumberEditText.getText().toString().trim();
                    new PhoneNumberVerificationHelper(getActivity(), PhoneVerificationFragment.this).execute(phone);
                }
            }
        });
        return rootView;
    }


    public PhoneVerificationFragment newInstance(ArrayList arrayList) {
        PhoneVerificationFragment fragment = new PhoneVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("numberList", arrayList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userPreference = new UserPreference(getActivity());
    }

    @Override
    public void onPhoneVerified(boolean isNewUser) {
        resetPreferences();
        new Animation().endAnimation(getActivity(), continueButton);
        String phone = "+" + ccEditText.getText().toString().trim() + phoneNumberEditText.getText().toString().trim();
        userPreference.setPhoneNumber(phone);
        ArrayList numberList = getArguments().getStringArrayList("numberList");
        new FragmentChanger(getFragmentManager(), new PinVerificationFragment().newInstance(isNewUser, numberList), false);
        changeViewEnable(true);

    }

    @Override
    public void onUserExist() {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new CustomMessage(getActivity(), "Can not change number to that of a user that already exist");
    }

    public void changeViewEnable(boolean status) {
        continueButton.setEnabled(status);
        ccEditText.setEnabled(status);
        phoneNumberEditText.setEnabled(status);
    }

    @Override
    public void onRequestFailed() {
        new Animation().endAnimation(getActivity(), continueButton);
        userPreference.setPhoneChange(false);
        new CustomMessage(getActivity(), getResources().getString(R.string.error_message));
        changeViewEnable(true);
    }


    UserPreference userPreference;

    public void resetPreferences() {
        userPreference.setName("");
        userPreference.setLogin(false);
        userPreference.setEmail("");
        userPreference.setRegID("");
        userPreference.setImageUrl("");
        userPreference.setPhoneChangeKey("");
        userPreference.setRegID("");
        userPreference.setDetailsOnServer(false);
    }

}
