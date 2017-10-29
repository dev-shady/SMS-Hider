package com.tech9pedia.dontbother;

import com.tech9pedia.dontbother.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PasswordManager extends Activity {
	static int counter=3;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(counter==0) System.exit(0);
		setContentView(R.layout.password_activity);
	}
 public void onClick(View view) {
	

	 EditText et=(EditText) findViewById(R.id.password);
	 SharedPreferences sharedPrefs=PreferenceManager.getDefaultSharedPreferences(this);
	
	 if(et.getText().toString().equals(sharedPrefs.getString("pinPref","1234"))) {
		 startActivity(new Intent("com.tech9pedia.Main"));
		 finish();
	 }else {
		 Toast.makeText(this,"wrong password",Toast.LENGTH_SHORT).show();
		 counter--;
	 }
 }

}
