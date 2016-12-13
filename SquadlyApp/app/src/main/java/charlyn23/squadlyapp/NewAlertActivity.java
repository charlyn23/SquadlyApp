package charlyn23.squadlyapp;

import android.Manifest;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class NewAlertActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback {

    private static SquadAdapter squadAdapter;
    public static ArrayList<Contact> squad;
    public SharedPreferences squadPrefsFile;
    public SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    String defaultText;
    double longitude;
    double latitude;
    double savedLatitude;
    double savedLongitude;
    public static String contactPhoneNumber;
    public static String contactName;
    float geofenceRadiusInMeters;
    long durationInMilliseconds;
    GoogleApiClient googleApiClient;
    Geofence homeGeofence;
    PendingIntent geofencePendingIntent;
    int contactsPermissionRequest;
    int locationPermissionRequest;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alert);

        final ListView newAlertContactListview = (ListView) findViewById(R.id.newAlerContactListView);
        TextView noSquad = (TextView) findViewById(R.id.noSquadTextView);
        TextView pickSquad = (TextView) findViewById(R.id.pickSquadTextView);

        defaultText = "Hi, I got home OK. Good night!";

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        squadPrefsFile = getApplicationContext().getSharedPreferences("SquadPrefsFile", Context.MODE_PRIVATE);
        squad = new ArrayList<>();

        squadAdapter = new SquadAdapter(this, R.layout.squad_item, squad);
        newAlertContactListview.setAdapter(squadAdapter);


        //If squadPrefsFile has contacts, dump all contacts in squadPrefsFile into the listView
        if (!squadPrefsFile.contains(null)) {

            Map<String, ?> allEntries = squadPrefsFile.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                String name = entry.getKey();
                String phoneNumber = entry.getValue().toString();
                Contact contactToBeAddedToListView = new Contact(name, phoneNumber);
                if (!checkIfContactWasAddedToListView(squadAdapter, contactToBeAddedToListView)) {
                    squadAdapter.add(contactToBeAddedToListView);
                }
            }
        } else {
            noSquad.setText("No Squad Yet. \n Go to Contacts to build your squad.");
            noSquad.setVisibility(View.VISIBLE);
            Toast.makeText(this, "no squad", Toast.LENGTH_SHORT).show();
        }

        newAlertContactListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                contactName = squad.get(position).getName();
                contactPhoneNumber = squad.get(position).getPhoneNumber();
                Log.i("Text: ", String.valueOf(contactPhoneNumber));
                if (!sharedPreferences.contains("userAddress")) {
                    AddressAlert alert = new AddressAlert();
                    FragmentManager manager = getFragmentManager();
                    alert.show(manager, "addy");
//                    Toast.makeText(getApplicationContext(), "Please save your address in settings.", Toast.LENGTH_LONG).show();
                } else {
//                    sendSquadAlert(contactPhoneNumber);

                    ConfirmationDialogFragment confirmation = new ConfirmationDialogFragment();
                    FragmentManager manager = getFragmentManager();
                    confirmation.show(manager, "confDialog");
//
//                    createGeofence();
//                    addGeofence();
                    savedLatitude = sharedPreferences.getFloat("latitude", (float) latitude);
                    savedLongitude = sharedPreferences.getFloat("longitude", (float) longitude);
                    Log.i("Saved LatLong", String.valueOf(savedLatitude) + ", " + String.valueOf(savedLongitude));

                }
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addScope(new Scope("scope"))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();

        locationPermissionRequest = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Intent intent = getIntent();
        boolean confirmed = intent.getBooleanExtra("confirmationResult", true);
        if (confirmed == true) {
            createGeofence();
            getGeofencingRequest();
            getGeofencePendingIntent();
            addGeofence();
        }
    }

    public boolean checkIfContactWasAddedToListView(SquadAdapter squadAdapter, Contact contactToBeAdded) {
        for (int i = 0; i < squadAdapter.getCount(); i++) {
            Contact previouslyAddedContact = squadAdapter.getItem(i);
            if (contactToBeAdded.equals(previouslyAddedContact)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("NewAlert", "onConnected()");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("NewAlert", "onConnectionSuspended");
    }



    //1.Make geofence object
    public Geofence createGeofence() {

        geofenceRadiusInMeters = (float) 10;
        durationInMilliseconds = 300000;

        return new Geofence.Builder()
                .setRequestId("userAddress")
                .setCircularRegion(savedLatitude, savedLongitude, 100)
                .setExpirationDuration(durationInMilliseconds)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .setLoiteringDelay(3000)
                .build();
    }

    //2. Make geofencing request
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(createGeofence());
        return builder.build();
    }

    //3. Define  Intent
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {

            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionHandler.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    //4. Add geofence
    public void addGeofence() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        locationPermissionRequest);

                return;
            }
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
        }
    }


    public void onResult(@NonNull MediaBrowserService.Result result) {

    }

    public void onReceiveResult(Object result) throws RemoteException {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Result result) {

    }
}



