package eternal.com.led.eternal.Main.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 2/15/2015.
 */
public class ContactAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<UserModel> contactList;
    private ArrayList<UserModel> contactFilterList;

    public ContactAdapter(Context context, ArrayList<UserModel> models) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contactList = models;
        contactFilterList = models;
    }


    @Override
    public int getCount() {
        return contactList.size();
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

            convertView = mLayoutInflater.inflate(R.layout.contact, parent, false);
            holder = new Holder();
            holder.userName = (TextView) convertView.findViewById(R.id.user_name_text_view_search);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.userName.setText(contactList.get(position).getName());
        holder.userName.setTag(position);
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.contact_pop, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.option_call) {
                            callPhone(contactList.get(position).getPhone());
                        } else if (item.getItemId() == R.id.option_message) {
                            sendMessage(contactList.get(position).getPhone());
                        }
                        return false;
                    }
                });
            }
        });

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


    ValueFilter valueFilter;

    @Override
    public android.widget.Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<UserModel> filterList = new ArrayList<>();
                for (int i = 0; i < contactFilterList.size(); i++) {
                    if (contactFilterList.get(i).getName().toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        UserModel userModel = new UserModel();
                        userModel.setName(contactFilterList.get(i).getName());
                        userModel.setPhone(contactFilterList.get(i).getPhone());
                        filterList.add(userModel);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = contactFilterList.size();
                results.values = contactFilterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contactList = (ArrayList<UserModel>) results.values;
            notifyDataSetChanged();
        }
    }
}

