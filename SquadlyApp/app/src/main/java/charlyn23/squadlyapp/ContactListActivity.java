package charlyn23.squadlyapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class ContactListActivity extends AppCompatActivity {

    private ContentAdapter contactsAdapter;
    private List<Contact> contactList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        //Move through device's contacts and fetch each id, name and phone number
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

            Log.i("CONTACT ", name);

            while (phoneCursor != null && phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.i("PHONE NUMBER ", phoneNumber);
                contactList.add(new Contact(id, name, phoneNumber));
            }
        }

        ListView allContactListView = (ListView) findViewById(R.id.allContactsListView);
        contactsAdapter = new ContentAdapter(this, R.layout.list_row, contactList);


        allContactListView.setAdapter(contactsAdapter);


        final AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent returnToAddContactsActivity = new Intent();
                String contactName = contactList.get(position).getName();
                String contactPhoneNumber = contactList.get(position).getPhoneNumber();

                returnToAddContactsActivity.putExtra("name", contactName);
                returnToAddContactsActivity.putExtra("phoneNumber", contactPhoneNumber);
                setResult(RESULT_OK, returnToAddContactsActivity);

                finish();
            }
        };
        allContactListView.setOnItemClickListener(listener);


    }
}

