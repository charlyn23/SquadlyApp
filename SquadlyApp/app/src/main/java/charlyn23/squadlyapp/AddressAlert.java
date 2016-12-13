package charlyn23.squadlyapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class AddressAlert extends DialogFragment {

    public AlertDialog.Builder builder;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.address_alert_title)
                .setMessage(R.string.address_alert)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent getToSettingsIntent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(getToSettingsIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "we need it", Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
}