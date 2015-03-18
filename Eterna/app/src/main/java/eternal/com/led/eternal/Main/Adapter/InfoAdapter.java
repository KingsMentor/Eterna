package eternal.com.led.eternal.Main.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import eternal.com.led.eternal.Main.ImageHandler.ImageLoader;
import eternal.com.led.eternal.Main.Models.UserModel;
import eternal.com.led.eternal.R;

/**
 * Created by CrowdStar on 3/16/2015.
 */
public class InfoAdapter extends CursorAdapter {


    public InfoAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.noti_info, parent, false);

        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewPersonName = (TextView) view.findViewById(R.id.user_name_text_view_search);
        textViewPersonName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

        TextView textViewPersonPhone = (TextView) view.findViewById(R.id.user_phone_text_view_search);
        textViewPersonPhone.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

        ImageView imageViewPersonImageUrl = (ImageView) view.findViewById(R.id.user_profile_image_view_search);
        String url = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3)));
        new ImageLoader(context).LoadImageFromWebOperations(url, imageViewPersonImageUrl);

        UserModel userModel = new UserModel();
        userModel.setName(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        userModel.setPhone(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));
        view.setTag(userModel);
    }
}
