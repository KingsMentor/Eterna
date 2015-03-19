package eternal.com.led.eternal.Main.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import eternal.com.led.eternal.Main.Adapter.BitMapCache;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.CustomViews.CircularImageView;
import eternal.com.led.eternal.Main.Service.EmailAddService;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.Main.DetailsChangeActivity;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/17/2015.
 */
public class ProfileFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    fragmentRemovedListener mFragmentRemovedListener;
    Button userPhoneTextView;
    TextView userNameTextView;
    UserPreference userPreference;
    Button userEmailTextView;
    CircularImageView circularImageView;
    boolean isChangingDetails = false;

    GoogleApiClient mGoogleApiClient;

    private BroadcastReceiver proImageCacheReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()) != null) {
                circularImageView.setImageDrawable(new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()));
            }
        }
    };


    @Override
    public void onConnected(Bundle bundle) {
        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        new UserPreference(getActivity()).setEmail(email);
        new UserPreference(getActivity()).setImageUrl(currentPerson.getImage().getUrl().replace("sz=50", "sz=500"));
        userEmailTextView.setText(email);
        getActivity().startService(new Intent(getActivity(), EmailAddService.class).putExtra("email", email).putExtra("imageUrl", userPreference.getImageUrl()));


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(proImageCacheReceiver, new IntentFilter(".BroadcastReceivers"));
    }


    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            getActivity().startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                    0, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            mGoogleApiClient.connect();
        }
    }

    public interface fragmentRemovedListener {
        public void onProfileFragmentRemoved();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_activity, container, false);

        userPreference = new UserPreference(getActivity());

        circularImageView = (CircularImageView) rootView.findViewById(R.id.pro_pic_image);
        if (new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()) != null) {
            circularImageView.setImageDrawable(new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()));
        } else {
            if (!userPreference.getImageUrl().trim().isEmpty()) {
                getActivity().startService(new Intent(getActivity(), EmailAddService.class).putExtra("email", userPreference.getEmail()).putExtra("imageUrl", userPreference.getImageUrl()));
            }
        }

        userPhoneTextView = (Button) rootView.findViewById(R.id.userphone);
        userPhoneTextView.setText(new UserPreference(getActivity()).getPhoneNumber());
        userPhoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPreference.isServerDetailsSet()) {
                    if (userPreference.getEmail().isEmpty() && userPreference.getImageUrl().isEmpty()) {
                        new CustomMessage(getActivity(), "An Email has not yet been attached to this account");
                    } else {
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), DetailsChangeActivity.class));
                        isChangingDetails = true;
                    }
                } else {

                    new CustomMessage(getActivity(), "An Email has not yet been attached to this account");
                }
            }
        });

        userNameTextView = (TextView) rootView.findViewById(R.id.username);
        userNameTextView.setText(new UserPreference(getActivity()).getName());


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        userEmailTextView = (Button) rootView.findViewById(R.id.useremail);
        if (!userPreference.getEmail().trim().isEmpty()) {
            userEmailTextView.setText(userPreference.getEmail());
        }
        userEmailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userPreference.isServerDetailsSet()) {
                    if (userPreference.getEmail().isEmpty() && userPreference.getImageUrl().isEmpty()) {
                        mGoogleApiClient.connect();
                        new CustomMessage(getActivity(), "service");
                    } else {
                        //getActivity().startService(new Intent(getActivity(), EmailAddService.class).putExtra("email", userPreference.getEmail()).putExtra("imageUrl", userPreference.getImageUrl()));
                        new CustomMessage(getActivity(), "service");
                    }
                }

            }
        });


        return rootView;
    }

    static boolean isTab;

    public ProfileFragment newInstance(fragmentRemovedListener mListener, boolean status) {
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.mFragmentRemovedListener = mListener;
        isTab = status;
        return profileFragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //y null pointer here
        if (!(isTab || isChangingDetails)) {
            mFragmentRemovedListener.onProfileFragmentRemoved();
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(proImageCacheReceiver);
        }
    }
}
