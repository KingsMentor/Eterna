package eternal.com.led.eternal.Main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import eternal.com.led.eternal.Main.Animation;
import eternal.com.led.eternal.Main.CallFragmentActivity;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Interfaces.NameVerification;
import eternal.com.led.eternal.Main.MainActivityController.MajorController;
import eternal.com.led.eternal.Main.ServerHelper.NameClaimerHelper;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class NameClaimFragment extends Fragment implements NameVerification {
    EditText nameField;
    Button claimNameButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.claim_name_activity, container, false);
        nameField = (EditText) rootView.findViewById(R.id.name_field);
        claimNameButton = (Button) rootView.findViewById(R.id.continue_button);
        claimNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().toString().isEmpty()) {
                    new CustomMessage(getActivity(), getString(R.string.name_empty_string));
                } else {
                    changeViewEnable(false);
                    new Animation().TransitBg(getActivity(),claimNameButton);
                    new CustomMessage(getActivity(), getString(R.string.request_message));
                    new NameClaimerHelper(getActivity(), NameClaimFragment.this).execute(nameField.getText().toString().trim());
                }
            }
        });
        return rootView;
    }

    public void changeViewEnable(boolean status) {
        nameField.setEnabled(status);
        claimNameButton.setEnabled(status);
    }

    @Override
    public void onNameClaimed() {
        new UserPreference(getActivity()).setLogin(true);
        new UserPreference(getActivity()).setName(nameField.getText().toString().trim());
        startActivity(new Intent(getActivity(), MajorController.class));
        getActivity().finish();
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(),claimNameButton);
    }

    @Override
    public void onNameTaken() {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(),claimNameButton);
        new CustomMessage(getActivity(), "Aww.., the name " + nameField.getText() + " has been taken. The good news is you can pick a more style one");
    }

    @Override
    public void onRequestFailed() {
        changeViewEnable(true);
        new Animation().endAnimation(getActivity(),claimNameButton);
        new CustomMessage(getActivity(), getResources().getString(R.string.error_message));
    }

}
