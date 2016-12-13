package charlyn23.squadlyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class SettingsActivity extends AppCompatActivity {

    String userName;
    public  String street;
    public  String city;
    public  String state;
    public  String zip;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static String userAddress;
    static ArrayList<String> states;
    public static double longitude;
    public static double latitude;
    public Context context = this.getBaseContext();
    public String result;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //we will compare the user entry for state to this list to validate.
        states = new ArrayList<>();
        states.add("AL");
        states.add("AK");
        states.add("AZ");
        states.add("AR");
        states.add("CA");
        states.add("CO");
        states.add("CT");
        states.add("DE");
        states.add("FL");
        states.add("GA");
        states.add("HI");
        states.add("ID");
        states.add("IL");
        states.add("IN");
        states.add("IA");
        states.add("KS");
        states.add("KY");
        states.add("LA");
        states.add("ME");
        states.add("MD");
        states.add("MA");
        states.add("MI");
        states.add("MN");
        states.add("MS");
        states.add("MO");
        states.add("MT");
        states.add("NE");
        states.add("NV");
        states.add("NH");
        states.add("NJ");
        states.add("NM");
        states.add("NY");
        states.add("NC");
        states.add("ND");
        states.add("OH");
        states.add("OK");
        states.add("OR");
        states.add("PA");
        states.add("RI");
        states.add("SC");
        states.add("SD");
        states.add("TN");
        states.add("TX");
        states.add("UT");
        states.add("VT");
        states.add("VA");
        states.add("WA");
        states.add("WV");
        states.add("WI");
        states.add("WI");
        states.add("WY");


        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        final EditText streetEditText = (EditText) findViewById(R.id.streetEditText);
        final EditText cityEditText = (EditText) findViewById(R.id.cityEditText);
        final EditText stateEditText = (EditText) findViewById(R.id.stateEditText);
        final EditText zipEditText = (EditText) findViewById(R.id.zipEditText);
        TextView defaultTextMessageTextView = (TextView) findViewById(R.id.textMessageTextView);
        defaultTextMessageTextView.setText(R.string.default_text_message);
        Button saveButton = (Button) findViewById(R.id.saveButton);

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //Populate fields with user info if it was previously saved
        if (sharedPreferences.contains("userAddress")) {
            String savedName = sharedPreferences.getString("userName", userName);
            nameEditText.setText(savedName, TextView.BufferType.EDITABLE);
            Log.i("Saved Name: ", savedName);

            String savedStreet = sharedPreferences.getString("street", street);
            streetEditText.setText(savedStreet);
            Log.i("Saved Street: ", savedStreet);

            String savedCity = sharedPreferences.getString("city", city);
            cityEditText.setText(savedCity);
            Log.i("Saved City : ", savedCity);

            String savedState = sharedPreferences.getString("state", state);
            stateEditText.setText(savedState);
            Log.i("Saved State: ", savedState);

            String savedZip = sharedPreferences.getString("zip", zip);
            zipEditText.setText(savedZip);
            Log.i("Saved Zip: ", savedZip);

            new AsyncClass().execute();

        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //User Values
                userName = String.valueOf(nameEditText.getText());
                street = String.valueOf(streetEditText.getText());
                city = String.valueOf(cityEditText.getText());
                state = String.valueOf(stateEditText.getText());
                zip = String.valueOf(zipEditText.getText());

                //Validate state entries
                if (!states.contains(state)) {
                    Toast.makeText(getBaseContext(), "Please enter a valid state", Toast.LENGTH_SHORT).show();
                }

                new AsyncClass().execute();

                Intent saveToSharedPrefs = new Intent();
                saveToSharedPrefs.putExtra("userName", userName);
                saveToSharedPrefs.putExtra("street", street);
                saveToSharedPrefs.putExtra("city", city);
                saveToSharedPrefs.putExtra("state", state);
                saveToSharedPrefs.putExtra("zip", zip);
                userAddress = street + " " + city + ", " + state + " " + zip;
                saveToSharedPrefs.putExtra("userAddress", userAddress);

                editor.putString("userName", userName);
                editor.putString("street", street);
                editor.putString("city", city);
                editor.putString("state", state);
                editor.putString("zip", zip);
                editor.putString("userAddress", userAddress);
                editor.commit();

                Log.i("Saved to Prefs: ", sharedPreferences.getAll().toString());
                Log.i("User Address: ", userAddress);

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

//        if (nameEditText.getText() == null){
//            Toast.makeText(getApplicationContext(), "Please save your name.", Toast.LENGTH_SHORT).show();
//        }
//        if (street.equals("")) {
//            Toast.makeText(getApplicationContext(), "Please enter a valid building number and street.", Toast.LENGTH_SHORT).show();
//        }
//        if (city.equals("")){
//            Toast.makeText(getApplicationContext(), "Please enter a valid city.", Toast.LENGTH_SHORT).show();
//        }
//        if (state.equals("")){
//            Toast.makeText(getApplicationContext(), "Please enter a valid state.", Toast.LENGTH_SHORT).show();
//        }
//        if (zip == null){
//            Toast.makeText(getApplicationContext(), "Please enter a valid zip code.", Toast.LENGTH_SHORT).show();
//        }

    }


    class AsyncClass extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground (Void...params){
            String result = "";
            String modifiedStreet = sharedPreferences.getString("street", street).replace(' ', '+');
            String modifiedCity = sharedPreferences.getString("city", city).replace(' ', '+');
            String modifiedAddress = modifiedStreet + ",+" + modifiedCity + ",+" + sharedPreferences.getString("state", state);
            //1600+Amphitheatre+Parkway,+Mountain+View,+CA
            String jsonURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + modifiedAddress
                    + "&key=" + R.string.API_KEY;


            Log.i("JSON URL" , jsonURL);
            URL url = null;
            try {
                url = new URL(jsonURL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                if ((connection != null) || (url != null)) {
                    Log.v("status", "CONNECTED");
                }
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                Log.d("json: ", result);
                connection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("JSON", "Malformed url: " + e.toString());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JSON", "IOException url" + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result = s;
            Log.v("JSON", s);
            getLatitude();
            getLongitude();
            Log.i("Shared Prefs = ", String.valueOf(sharedPreferences.getAll()));
        }
    }

    public double getLatitude(){
        try {
            JSONObject initialJson = new JSONObject(result);
            JSONArray resultsArray = initialJson.getJSONArray("results");
            JSONObject jsonBlob = (JSONObject) resultsArray.get(0);
            JSONObject geometry = (JSONObject) jsonBlob.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            latitude = (double) location.get("lat");
            Log.i("Lat= ", String.valueOf(latitude)); //it works!
//            editor.put("latitude", latitude);
            editor.putFloat("latitude", (float) latitude);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return latitude;
    }

    public double getLongitude(){
        try {
            JSONObject initialJson = new JSONObject(result);
            JSONArray resultsArray = initialJson.getJSONArray("results");
            JSONObject jsonBlob = (JSONObject) resultsArray.get(0);
            JSONObject geometry = (JSONObject) jsonBlob.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            longitude = (double) location.get("lng");
            Log.i("Lng= ", String.valueOf(longitude));
            editor.putFloat("longitude", (float) longitude);
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return longitude;
    }

}


