package eternal.com.led.eternal.Main.Fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import java.util.ArrayList;

import eternal.com.led.eternal.Main.Adapter.ContactRetriever;
import eternal.com.led.eternal.Main.Animation;
import eternal.com.led.eternal.Main.CustomMessage;
import eternal.com.led.eternal.Main.Interfaces.PhoneChangeRequestCallBack;
import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.Main.ServerHelper.PhoneChangeHelper;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/13/2015.
 */
public class ContactPickerFragment extends Fragment implements PhoneChangeRequestCallBack {
    ArrayList<UserModel> userModelArrayAdapter;
    ListView listView;
    ArrayList selectedNumbers;
    ArrayList selectedIndex;
    Button continueButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.contact_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.contact_list);
        continueButton = (Button) rootView.findViewById(R.id.continue_button);


        userModelArrayAdapter = new ContactRetriever().getContact(getActivity(), true);
        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_multiple_choice, new ContactRetriever().getContact(getActivity(), false));
        listView.setAdapter(adapter);
        listView.setItemsCanFocus(false);
        listView.setFastScrollEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isSelect = true;
                for (int x = 0; x < selectedIndex.size(); x++)
                    if (Integer.parseInt(selectedIndex.get(x).toString()) == position) {
                        isSelect = false;
                        selectedIndex.remove(x);
                    }
                if (isSelect)
                    selectedIndex.add(position);
            }
        });

        selectedNumbers = new ArrayList();
        selectedIndex = new ArrayList();

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Object position : selectedIndex) {
                    String number = userModelArrayAdapter.get(Integer.parseInt(position.toString())).getPhone();
                    selectedNumbers.add(number);
                }
                new PhoneChangeHelper(ContactPickerFragment.this, getActivity()).execute();
                new Animation().TransitBg(getActivity(), continueButton);
                changeEnable(false);
            }
        });
    }

    ObjectAnimator animator;

    public void changeEnable(boolean status) {
        listView.setEnabled(status);
        continueButton.setEnabled(status);
    }


    @Override
    public void onRequestCompleted() {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new FragmentChanger(getFragmentManager(), new PhoneNumberChangerFragment().newInstance(selectedNumbers), false);
    }

    @Override
    public void onRequestDenied() {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new CustomMessage(getActivity(), getString(R.string.error_message));
    }

    @Override
    public void onRequestFailed(String error) {
        changeEnable(true);
        new Animation().endAnimation(getActivity(), continueButton);
        new CustomMessage(getActivity(), getString(R.string.error_message));
    }
}

