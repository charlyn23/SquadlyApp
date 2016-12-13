package charlyn23.squadlyapp;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button newAlertButton;
    Button contactsButton;
    Button settingsButton;
    FragmentManager manager;
    int contactsPermissionRequest;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences squadPrefsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newAlertButton = (Button)findViewById(R.id.newAlertButton);
        contactsButton = (Button)findViewById(R.id.contactsButton);
        settingsButton = (Button)findViewById(R.id.settingsButton);

        contactsPermissionRequest = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        squadPrefsFile = getApplicationContext().getSharedPreferences("SquadPrefsFile", Context.MODE_PRIVATE);

        AddContactsActivity addContactsActivity = new AddContactsActivity();
        SettingsActivity settingsActivity = new SettingsActivity();
        NewAlertActivity newAlertActivity = new NewAlertActivity();

        newAlertButton.setEnabled(false);

        newAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.contains("userAddress")) {
                    Intent intent = new Intent(MainActivity.this, NewAlertActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                else {
                    AddressAlert alert = new AddressAlert();
                    alert.show(getFragmentManager(), "mainAddy");
                }
            }
        });

        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContactsPermission();
                Intent intent = new Intent(MainActivity.this, AddContactsActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(goToSettingsActivity);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        if (!squadPrefsFile.getAll().isEmpty()) {
            newAlertButton.setEnabled(true);
        }
        else  {
            newAlertButton.setEnabled(false);
            Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
            contactsButton.startAnimation(pulseAnimation);

        }
        super.onPostResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //show why we need permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(getBaseContext(), "we need it", Toast.LENGTH_SHORT).show();
            } else {
                //request permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        contactsPermissionRequest);

            }
        }


    }

    public boolean addressSaved() {
        if (sharedPreferences.contains("userAddress")){
            return true;
        }
        return false;
    }
}
