package charlyn23.squadlyapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class GeofenceTransitionHandler extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionHandler(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e("Geofence Error", "error");
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Toast.makeText(this, "You've returned home", Toast.LENGTH_SHORT).show();
            Log.i("Geofence", "home");
            String phoneNumber = NewAlertActivity.contactPhoneNumber;
            sendSquadAlert(phoneNumber);

        }
    }

    public void sendSquadAlert(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        String SENT = "SMS Sent";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        if (phoneNumber.charAt(0) != '1') {
            phoneNumber = "1" + phoneNumber;
        }
        try {
            smsManager.sendTextMessage(phoneNumber, null, String.valueOf(R.string.default_text_message), sentPI, null);
            Toast.makeText(getApplicationContext(), "Text sent to " + phoneNumber, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


}

