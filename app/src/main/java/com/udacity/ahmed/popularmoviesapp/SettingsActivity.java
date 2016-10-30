package com.udacity.ahmed.popularmoviesapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by ahmed on 10/20/2016.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
