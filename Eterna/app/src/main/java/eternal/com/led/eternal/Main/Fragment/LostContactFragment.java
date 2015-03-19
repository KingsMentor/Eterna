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
import eternal.com.led.eternal.Main.CustomViews.CustomTextView;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.Interfaces.RetrieveAccountCallback;
import eternal.com.led.eternal.Main.ServerHelper.ContactRetrieverHelper;
import eternal.com.led.eternal.Main.ServerHelper.PhoneNumberVerificationHelper;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class LostContactFragment extends Fragment implements RetrieveAccountCallback {
    EditText phoneEditText;
    EditText emailEditText;
    EditText ccEditText;
    Button continueButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lost_contact_view, container, false);

        phoneEditText = (EditText) rootView.findViewById(R.id.phone_field);
        emailEditText = (EditText) rootView.findViewById(R.id.email_field);
        ccEditText = (EditText) rootView.findViewById(R.id.cc);
        continueButton = (Button) rootView.findViewById(R.id.continue_button);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneEditText.getText().toString().trim().isEmpty() || ccEditText.getText().toString().trim().isEmpty() || emailEditText.getText().toString().trim().isEmpty())
                    new CustomMessage(getActivity(), getString(R.string.empty_field));
                else {
                    changeEnable(false);
                    new Animation().TransitBg(getActivity(), continueButton);
                    String phone = "+" + ccEditText.getText().toString().trim() + phoneEditText.getText().toString().trim();
                    new ContactRetrieverHelper(LostContactFragment.this, getActivity()).execute(emailEditText.getText().toString().toString(), phone);
                }
            }
        });

        return rootView;
    }

    public void changeEnable(boolean status) {
        phoneEditText.setEnabled(status);
        ccEditText.setEnabled(status);
        emailEditText.setEnabled(status);
        continueButton.setEnabled(status);
    }

    @Override
    public void onAccountRetrieved(String phone, String key, String regKey) {
        new UserPreference(getActivity()).setPhoneNumber(phone);
        new UserPreference(getActivity()).setKey(key);
        new UserPreference(getActivity()).setPhoneChangeKey(regKey);
        new UserPreference(getActivity()).setPhoneChange(true);
        new FragmentChanger(getFragmentManager(), new PhoneNumberChangerFragment().newInstance(new ArrayList()), false);
    }

    @Override
    public void onNoUserExist() {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new CustomMessage(getActivity(), getString(R.string.user_not_exist));
    }

    @Override
    public void onRequestFailed(String error) {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new CustomMessage(getActivity(), getString(R.string.error_message));
    }
}
