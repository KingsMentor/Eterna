package eternal.com.led.eternal.Main;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.internal.co;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Adapter.BitMapCache;
import eternal.com.led.eternal.Main.Adapter.ContactAdapter;
import eternal.com.led.eternal.Main.Adapter.ContactRetriever;
import eternal.com.led.eternal.Main.Adapter.SearchListAdapter;
import eternal.com.led.eternal.Main.CustomViews.CircularImageView;
import eternal.com.led.eternal.Main.Fragment.ProfileFragment;
import eternal.com.led.eternal.Main.Interfaces.NameSearchCallBack;
import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.Main.ServerHelper.NameSearch;
import eternal.com.led.eternal.Main.SharedPreference.UserPreference;
import eternal.com.led.eternal.R;

public class CallFragmentActivity extends Fragment implements NameSearchCallBack, ProfileFragment.fragmentRemovedListener {
    LinearLayout linearLayout;
    CardView cardView;
    CircularImageView footerCircularImageView;
    EditText searchNameEditText;
    ListView listView;
    ContactAdapter contactAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.call_fragment, container, false);

        listView = (ListView) rootView.findViewById(R.id.search_listview);
        contactAdapter = new ContactAdapter(getActivity(), new ContactRetriever().getContact(getActivity(), true));
        listView.setAdapter(contactAdapter);
        listView.setItemsCanFocus(false);
        listView.setFastScrollEnabled(true);
        listView.setEmptyView(rootView.findViewById(android.R.id.empty));


        linearLayout = (LinearLayout) rootView.findViewById(R.id.background_layout);

        cardView = (CardView) rootView.findViewById(R.id.footer_view_layout);
        footerCircularImageView = (CircularImageView) rootView.findViewById(R.id.profile_footer_avater);


        setFooterProImage();


        rootView.findViewById(R.id.profile_footer_avater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
                cardView.startAnimation(animation);
                cardView.setVisibility(View.GONE);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.add(R.id.content_frame, new ProfileFragment().newInstance(CallFragmentActivity.this, false)).commit();
            }
        });

        rootView.findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }
        });

        rootView.findViewById(R.id.continue_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, searchNameEditText.getText().toString());
                startActivity(intent);

            }
        });
        searchNameEditText = (EditText) rootView.findViewById(R.id.searchName);
        searchNameEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_SEARCH);
        searchNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                         @Override
                                                         public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                             if (!searchNameEditText.getText().toString().trim().isEmpty()) {
                                                                 hideSoftKeyboard(getActivity());
                                                                 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                                                 if (prefs.getBoolean("my_search", false)) {
                                                                     new NameSearch(getActivity(), CallFragmentActivity.this).execute(searchNameEditText.getText().toString());
                                                                     TransitBg();
                                                                 } else {
                                                                     if (searchNameEditText.getText().toString().trim().equalsIgnoreCase(new UserPreference(getActivity()).getName().trim()))
                                                                         new CustomMessage(getActivity(), "Enable in settings to be able to search for yourself");
                                                                     else {
                                                                         new NameSearch(getActivity(), CallFragmentActivity.this).execute(searchNameEditText.getText().toString());
                                                                         TransitBg();
                                                                     }

                                                                 }
                                                                 return true;
                                                             } else
                                                                 new CustomMessage(getActivity(), getString(R.string.empty_search));

                                                             return false;

                                                         }
                                                     }

        );

        searchNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SharedPreferences prefs =
                        PreferenceManager.getDefaultSharedPreferences(getActivity());
                if (prefs.getBoolean("contact_search", false)) {
                    if (!(listView.getAdapter() instanceof ContactAdapter)) {
                        listView.setAdapter(contactAdapter);
                    }
                    contactAdapter.getFilter().filter(s);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return rootView;
    }

    ObjectAnimator animator;

    public void TransitBg() {
        animator = ObjectAnimator.ofInt(linearLayout, "backgroundColor", getResources().getColor(R.color.blue), getResources().getColor(R.color.bg_color), getResources().getColor(R.color.blue), getResources().getColor(R.color.bg_blue)).setDuration(3000);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();


    }

    public void changeAnimation() {
        animator.end();
        linearLayout.setBackgroundColor(getResources().getColor(R.color.bg_blue));
    }


    public void setFooterProImage() {
        if (new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()) != null)
            footerCircularImageView.setImageDrawable(new BitMapCache().getProfileImageBitmapFromMemCache(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        searchNameEditText.setText(new UserPreference(getActivity()).getSearchText());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getActivity().findViewById(R.id.content_tab_frame) == null) {
            inflater.inflate(R.menu.menu_main, menu);
        } else {
            inflater.inflate(R.menu.menu_main_tab, menu);
        }
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider actionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if (actionProvider != null) {
            actionProvider.setShareIntent(shareIntent());
        }
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        } else if (id == R.id.notification) {
            startActivity(new Intent(getActivity(), NotificationActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        new UserPreference(getActivity()).setSearchText(searchNameEditText.getText().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        new UserPreference(getActivity()).setSearchText(searchNameEditText.getText().toString());
    }

    @Override
    public void onStart() {
        super.onStart();
        searchNameEditText.setText(new UserPreference(getActivity()).getSearchText());
    }

    private Intent shareIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Its Eterna");
        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I claimed " + new UserPreference(getActivity()).getName() + " on eterna. I urge you to claim yours to always stay connected.Visit www.my-eternal.appspot.com");
        return sendIntent;
    }

    @Override
    public void onProfileFragmentRemoved() {
        cardView.setVisibility(View.VISIBLE);
        setFooterProImage();
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        cardView.startAnimation(animation);
    }

    @Override
    public void onNameFound(String name, String phone, String image) {
        changeAnimation();
        ArrayList<UserModel> userModels = new ArrayList<>();
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setPhone(phone);
        userModel.setImageUrl(image.trim());
        userModels.add(userModel);
        listView.setAdapter(new SearchListAdapter(getActivity(), userModels));


    }


    @Override
    public void onNameNotFound() {
        changeAnimation();
        new CustomMessage(getActivity(), "not found");
    }

    @Override
    public void onInvalidToken() {
        changeAnimation();
        new CustomMessage(getActivity(), "invalid");
    }

    @Override
    public void onRequestFailed(String error) {
        changeAnimation();
        new CustomMessage(getActivity(), error);
    }
}
