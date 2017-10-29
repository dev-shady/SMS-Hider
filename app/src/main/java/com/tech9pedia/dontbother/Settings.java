package com.tech9pedia.dontbother;



import com.tech9pedia.dontbother.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class Settings extends PreferenceActivity{
	//private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
	//private SharedPreferences prefs;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.d("Settings","b4 ddpreferencefromresource");
		//getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#81a3d0")));
		addPreferencesFromResource(R.xml.settings);
		/* prefs=PreferenceManager.getDefaultSharedPreferences(this);
		prefListener=new SharedPreferences.OnSharedPreferenceChangeListener() {
			
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
					String key) {
				// TODO Auto-generated method stub
				if(key.equals("fontSizePref")) {
					int val=prefs.getInt("fontSizePref",-1);
					Log.d("val inPrefList",val+"");
					
			}
		};
		}; */
		
	}

}
