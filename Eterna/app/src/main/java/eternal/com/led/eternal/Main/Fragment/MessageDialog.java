package eternal.com.led.eternal.Main.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import eternal.com.led.eternal.Main.Interfaces.DecisionCallBack;

/**
 * Created by CrowdStar on 3/15/2015.
 */
public class MessageDialog extends DialogFragment {

    static DecisionCallBack mDecisionCallBack;
    static String number;

    public MessageDialog newInstance(DecisionCallBack callBack,String phone) {
        MessageDialog messageDialog = new MessageDialog();
        mDecisionCallBack = callBack;
        number = phone;
        return messageDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setMessage("Be sure to enter a valid mobile number. This will change your login number to "+ number)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDecisionCallBack.onNoOption();
                    }
                })

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mDecisionCallBack.onYesOption();
                    }
                })
                .create();
    }
}
