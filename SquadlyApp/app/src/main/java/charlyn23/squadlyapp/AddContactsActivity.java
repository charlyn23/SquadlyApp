package charlyn23.squadlyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class AddContactsActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SquadPrefsFile";
    public static SharedPreferences.Editor editor;
    public static SharedPreferences sharedPreferences;

    public static SquadAdapter squadAdapter;
    public static ArrayList<Contact> squad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        Button addContactButton = (Button) findViewById(R.id.addContactButton);
        ListView contactsList = (ListView) findViewById(R.id.contactsListView);

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        squad = new ArrayList<>();

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showContactsIntent = new Intent(AddContactsActivity.this, ContactListActivity.class);
                startActivityForResult(showContactsIntent, 1);
            }
        });

        squadAdapter = new SquadAdapter(this, R.layout.squad_item, squad);

        contactsList.setAdapter(squadAdapter);

        if(!sharedPreferences.contains(null)) {
            Map<String, ?> allEntries = sharedPreferences.getAll();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()){
                String name = entry.getKey();
                String phoneNumber = entry.getValue().toString();
                Contact contact = new Contact(name, phoneNumber);
                squadAdapter.add(contact);
                getSquad();
            }
        }
    }
    public static ArrayList<Contact> getSquad(){
        return squad;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("name")) {
                String name = data.getStringExtra("name");
                String phoneNumber = data.getStringExtra("phoneNumber");
                Log.i("DATA ", name + " " + phoneNumber);

                if(!sharedPreferences.contains(name) && !squad.contains(name)) {
                    Contact newContact = new Contact(name, phoneNumber);
                    editor.putString(name, phoneNumber);
                    editor.commit();
                    squadAdapter.add(newContact);

                    Toast.makeText(this, name + " was added to squad", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, name + " is already in the squad", Toast.LENGTH_LONG).show();


                }
                Log.i("SHARED PREFS ", sharedPreferences.getString(name, phoneNumber));

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

