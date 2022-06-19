package com.example.myproject.Settings;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.myproject.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onPause() {
            super.onPause();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CheckPreferences();
    }

    private void CheckPreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String size = pref.getString("size", null);
        if(size != null){
            Resources res = getResources();
            Configuration configuration = new Configuration(res.getConfiguration());
            switch (size){
                case "маленький":{
                    configuration.fontScale = 0.8f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
                case "средний":{
                    configuration.fontScale = 1f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
                case "большой":{
                    configuration.fontScale = 1.2f;
                    res.updateConfiguration(configuration, res.getDisplayMetrics());
                    break;
                }
            }
        }
    }
}