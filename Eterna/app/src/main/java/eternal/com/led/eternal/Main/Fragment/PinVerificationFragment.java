package eternal.com.led.eternal.Main.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Animation;
import eternal.com.led.eternal.Main.CallFragmentActivity;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Interfaces.DecisionCallBack;
import eternal.com.led.eternal.Main.Interfaces.PhoneVerification;
import eternal.com.led.eternal.Main.Interfaces.PinVerification;
import eternal.com.led.eternal.Main.MainActivityController.MajorController;
import eternal.com.led.eternal.Main.ServerHelper.PhoneNumberVerificationHelper;
import eternal.com.led.eternal.Main.ServerHelper.PinVerificationHelper;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class PinVerificationFragment extends Fragment implements PinVerification, PhoneVerification, DecisionCallBack {

    EditText pinEditText;
    Button verifyButton;
    Button resendPinButton;
    View animatingView;
    TextView resendPinTextView;
    private static boolean isNewUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.pin_verify, container, false);
        pinEditText = (EditText) rootView.findViewById(R.id.pin_field);
        verifyButton = (Button) rootView.findViewById(R.id.continue_button);
        resendPinButton = (Button) rootView.findViewById(R.id.resend_button);
        resendPinTextView = (TextView) rootView.findViewById(R.id.resend_textView);

        resendPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewEnable(false);
                animatingView = resendPinButton;
                new Animation().TransitBg(getActivity(), animatingView);
                new CustomMessage(getActivity(), getString(R.string.request_message));
                new PhoneNumberVerificationHelper(getActivity(), PinVerificationFragment.this).execute(new UserPreference(getActivity()).getPhoneNumber());

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resendPinButton.setVisibility(View.VISIBLE);
                resendPinTextView.setVisibility(View.VISIBLE);
            }
        }, 5000);

        isNewUser = getArguments().getBoolean("isNewUser");

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animatingView = verifyButton;
                if (pinEditText.getText().toString().isEmpty()) {
                    new CustomMessage(getActivity(), getString(R.string.pin_empty_string));
                } else {
                    new CustomMessage(getActivity(), getString(R.string.request_message));
                    if (new UserPreference(getActivity()).isPhoneChanging()) {
                        MessageDialog messageDialog = new MessageDialog().newInstance(PinVerificationFragment.this, new UserPreference(getActivity()).getPhoneNumber());
                        messageDialog.show(getFragmentManager(), "");
                    } else {
                        callPinVerification();
                    }
                }
            }
        });


        return rootView;
    }

    public void changeViewEnable(boolean status) {
        resendPinButton.setEnabled(status);
        verifyButton.setEnabled(status);
        pinEditText.setEnabled(status);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pin = intent.getStringExtra("pin");
            pinEditText.setText("" + pin);
            animatingView = verifyButton;
            callPinVerification();
        }
    };

    public void callPinVerification() {
        changeViewEnable(false);
        new Animation().TransitBg(getActivity(), animatingView);
        if (isNewUser)
            new PinVerificationHelper(getActivity(), this).execute(pinEditText.getText().toString(), "new");
        else {
            ArrayList numberList = getArguments().getStringArrayList("numberList");
            if (numberList != null)
                new PinVerificationHelper(getActivity(), this, numberList).execute(pinEditText.getText().toString(), "old");
            else
                new PinVerificationHelper(getActivity(), this).execute(pinEditText.getText().toString(), "old");

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("eternal.com.led.eternal.Main.BroadcastReceivers"));

    }


    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    public PinVerificationFragment newInstance(boolean isNewUser, ArrayList arrayList) {
        PinVerificationFragment fragment = new PinVerificationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isNewUser", isNewUser);
        bundle.putStringArrayList("numberList", arrayList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPinVerified() {
        if (isNewUser) {
            new FragmentChanger(getFragmentManager(), new NameClaimFragment(), true);
        } else {
            new UserPreference(getActivity()).setLogin(true);
            startActivity(new Intent(getActivity(), MajorController.class));
            getActivity().finish();
        }
        if (new UserPreference(getActivity()).isPhoneChanging()) {
            new UserPreference(getActivity()).setPhoneChange(false);
        }
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(), animatingView);
    }

    @Override
    public void onInvalidPin() {
        new Animation().endAnimation(getActivity(), animatingView);
        changeViewEnable(true);
        new CustomMessage(getActivity(), "invalid pin");
    }


    @Override
    public void onPhoneVerified(boolean isNewUser) {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(), animatingView);
    }

    @Override
    public void onUserExist() {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(), animatingView);
        new CustomMessage(getActivity(), "Can not change number to that of a user that already exist");
    }

    @Override
    public void onRequestFailed() {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(), animatingView);
        new CustomMessage(getActivity(), getResources().getString(R.string.error_message));
    }

    @Override
    public void onYesOption() {
        callPinVerification();
    }

    @Override
    public void onNoOption() {

    }
}
