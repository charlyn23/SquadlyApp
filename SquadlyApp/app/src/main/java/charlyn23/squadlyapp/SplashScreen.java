package charlyn23.squadlyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class SplashScreen extends AppCompatActivity {

    SharedPreferences newPrefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newPrefs = getSharedPreferences("newPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = newPrefs.edit();
        boolean  firstTime = newPrefs.getBoolean("first", true);
        if(firstTime) {
            Log.i("SplashScreen", "splash shown");
            editor.putBoolean("first",false);
            editor.commit();
            Intent intent = new Intent(SplashScreen.this, InstructionActivity.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            Log.i("SplashScreen", "splash NOT shown");
        }
    }
}
