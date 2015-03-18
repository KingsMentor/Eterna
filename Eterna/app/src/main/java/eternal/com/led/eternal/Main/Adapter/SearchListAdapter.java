package eternal.com.led.eternal.Main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.ImageHandler.ImageLoader;
import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/15/2015.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<UserModel> userModelArrayList;

    public SearchListAdapter(Context context, ArrayList<UserModel> models) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        userModelArrayList = models;
    }


    @Override
    public int getCount() {
        return userModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {

            convertView = mLayoutInflater.inflate(R.layout.found_eternal, parent, false);
            holder = new Holder();
            holder.userIcon = (ImageView) convertView.findViewById(R.id.user_profile_image_view_search);
            holder.userName = (TextView) convertView.findViewById(R.id.user_name_text_view_search);
            holder.userPhone = (TextView) convertView.findViewById(R.id.user_phone_text_view_search);
            holder.moreUserOption = (ImageView) convertView.findViewById(R.id.more_user_option);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.moreUserOption.setTag(position);
        holder.moreUserOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.found_eternal_pop_up, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.option_call) {
                            callPhone(userModelArrayList.get(position).getPhone());
                        } else if (item.getItemId() == R.id.option_message) {
                            sendMessage(userModelArrayList.get(position).getPhone());
                        } else if (item.getItemId() == R.id.option_add_to_contact) {
                            addToContact(userModelArrayList.get(position).getPhone());
                        }
                        return false;
                    }
                });
            }
        });

        holder.userName.setText(userModelArrayList.get(position).getName());
        holder.userPhone.setText(userModelArrayList.get(position).getPhone());

        String imageUrl = userModelArrayList.get(position).getImageUrl();
        Drawable d = new ImageLoader(mContext).LoadImageFromWebOperations(imageUrl, holder.userIcon);
        convertView.setTag(holder);
        return convertView;
    }

    public void sendMessage(String number) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", number);
        smsIntent.putExtra("sms_body", "Body of Message");
        mContext.startActivity(smsIntent);
    }

    public void callPhone(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        mContext.startActivity(intent);
    }

    public void addToContact(String number) {
        Intent intentInsertEdit = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE, number);
        intentInsertEdit.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
        mContext.startActivity(intentInsertEdit);
    }
}

