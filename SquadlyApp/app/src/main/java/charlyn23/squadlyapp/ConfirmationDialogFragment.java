package charlyn23.squadlyapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class ConfirmationDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final String phone = NewAlertActivity.contactPhoneNumber;
        final String recipientName = NewAlertActivity.contactName;
        String textMessage = R.string.confirmation_dialog_text1 + recipientName + R.string.confirmation_dialog_text2;
        String textMessageHardCoded = "Should I text " + recipientName + " when you get home?";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(textMessageHardCoded)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //what happens when they press OK
                        Log.i("Dialog", "Ok pressed");
                        Intent confirmationIntent = new Intent(getActivity(), NewAlertActivity.class);
                        confirmationIntent.putExtra("confirmationResult", true);
                        startActivity(confirmationIntent);
                        sendSquadAlert(phone);


                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //what happens when they press Cancel
                Log.i("Dialog", "Cancel was pressed");
            }
        });
        return builder.create();
    }

    public void sendSquadAlert(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        String SENT = "SMS Sent";
        PendingIntent sentPI = PendingIntent.getBroadcast(getActivity(), 0, new Intent(SENT), 0);
        if (phoneNumber.charAt(0) != '1') {
            phoneNumber = "1" + phoneNumber;
        }
        try {
            smsManager.sendTextMessage(phoneNumber, null, String.valueOf(R.string.default_text_message), sentPI, null);
            Toast.makeText(getActivity(), "Text sent to " + phoneNumber, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
}

