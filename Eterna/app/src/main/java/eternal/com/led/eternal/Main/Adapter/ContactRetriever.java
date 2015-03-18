package eternal.com.led.eternal.Main.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;

import eternal.com.led.eternal.Main.Models.UserModel;

/**
 * Created by CrowdStar on 3/14/2015.
 */
public class ContactRetriever {

    public ArrayList getContact(Context context, boolean isMultiple) {
        ArrayList contactListName = new ArrayList();
        ArrayList checkerList = new ArrayList();
        ArrayList<UserModel> userModelArrayList = new ArrayList();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (!checkerList.contains(name)) {
                checkerList.add(name);
                if (isMultiple) {
                    UserModel userModel = new UserModel();
                    userModel.setPhone(phone);
                    userModel.setName(name);
                    userModelArrayList.add(userModel);
                } else {
                    contactListName.add(name.trim());
                }
            }
        }
        cursor.close();
        if (isMultiple)
            return userModelArrayList;
        else
            return contactListName;
    }
}
