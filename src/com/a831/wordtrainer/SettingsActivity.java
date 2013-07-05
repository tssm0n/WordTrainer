package com.a831.wordtrainer;

import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Button;


public class SettingsActivity extends PreferenceActivity {
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        // Display the fragment as the main content.
	        getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new Prefs1Fragment()).commit();
	        
	    }

	    /**
	     * Populate the activity with the top-level headers.
	     */
	    @Override
	    public void onBuildHeaders(List<Header> target) {

	    }

	    /**
	     * This fragment shows the preferences for the first header.
	     */
	    public static class Prefs1Fragment extends PreferenceFragment {
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	            super.onCreate(savedInstanceState);

	            // Load the preferences from an XML resource
	            addPreferencesFromResource(R.xml.pref_general);
	        }
	    }
}
