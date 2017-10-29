package com.tech9pedia.dontbother;






import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.tech9pedia.dontbother.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class AudioManagerMainActivity extends ActionBarActivity  implements OnLongClickListener{
	private SeekBar seekBarMedia,seekBarRinger,seekBarNotif,seekBarSystem,seekBarAlarm,seekBarCall;
	private TextView media,ringer,notif,system,alarm,call;
	private AudioManager audioManager=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	//	Log.d("Seekbar","onCreate");
		// if(android.os.Build.VERSION.SDK_INT<android.os.Build.VERSION_CODES.HONEYCOMB)
        getSupportActionBar().hide();
		checkDisguise();
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.setVolumeControlStream(AudioManager.STREAM_RING);
		this.setVolumeControlStream(AudioManager.STREAM_SYSTEM);
		this.setVolumeControlStream(AudioManager.STREAM_ALARM);
		this.setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
		this.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
		
		
		setContentView(R.layout.audio_manager_main_activitiy);
		initStream();
		 	}
	
	
  protected void initStream() {
	//Log.d("Seekbar","in initStream");
	//Return the handle to a system-level service - 'AUDIO'.
	//Typeface tf=Typeface.createFromAsset(getAssets(),"fonts/Roboto-BoldItalic.ttf");
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			TextView tv=(TextView) findViewById(R.id.editText1);
			tv.setOnLongClickListener(this);
		//	tv.setTypeface(tf);
	seekBarMedia = (SeekBar) findViewById(R.id.seekBarMedia);
	seekBarMedia.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
	seekBarMedia.setProgress(audioManager
		
			.getStreamVolume(AudioManager.STREAM_MUSIC));
	
	media =(TextView) findViewById(R.id.media);
	
	seekBarRinger = (SeekBar) findViewById(R.id.seekBarRinger);
	seekBarRinger.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_RING));
	seekBarRinger.setProgress(audioManager
			.getStreamVolume(AudioManager.STREAM_RING));
	ringer =(TextView) findViewById(R.id.ringer);
	
	seekBarNotif = (SeekBar) findViewById(R.id.seekBarNotif);
	seekBarNotif.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
	seekBarNotif.setProgress(audioManager
			.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
	notif =(TextView) findViewById(R.id.notif);
	
	seekBarSystem = (SeekBar) findViewById(R.id.seekBarSystem);
	seekBarSystem.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
	seekBarSystem.setProgress(audioManager
			.getStreamVolume(AudioManager.STREAM_SYSTEM));
	system =(TextView) findViewById(R.id.system);
	
	seekBarAlarm = (SeekBar) findViewById(R.id.seekBarAlarm);
	seekBarAlarm.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_ALARM));
	seekBarAlarm.setProgress(audioManager
			.getStreamVolume(AudioManager.STREAM_ALARM));
	alarm =(TextView) findViewById(R.id.alarm);
	
	seekBarCall = (SeekBar) findViewById(R.id.seekBarCall);
	seekBarCall.setMax(audioManager
			.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
	seekBarCall.setProgress(audioManager
			.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
	call =(TextView) findViewById(R.id.call);
	
	media.setText("Media Volume ("+seekBarMedia.getProgress() + "/" + seekBarMedia.getMax()+")");
	ringer.setText("Ringer Volume ("+seekBarRinger.getProgress() + "/" + seekBarRinger.getMax()+")");
	system.setText("System Volume ("+seekBarSystem.getProgress() + "/" + seekBarSystem.getMax()+")");
	notif.setText("Notification Volume ("+seekBarNotif.getProgress() + "/" + seekBarNotif.getMax()+")");
	alarm.setText("Alarm Volume ("+seekBarAlarm.getProgress() + "/" + seekBarAlarm.getMax()+")");
	call.setText("Voice Call ("+seekBarCall.getProgress() + "/" + seekBarCall.getMax()+")");
	
	try {
	seekBarMedia.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int mediaProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			mediaProgress = progresValue;
			 
			 audioManager.setStreamVolume(
						AudioManager.STREAM_MUSIC, mediaProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			
			media.setText("Media Volume ("+mediaProgress+ "/" + seekBarMedia.getMax()+")");
		}
	});
	seekBarRinger.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int ringerProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			ringerProgress = progresValue;
			
			 audioManager.setStreamVolume(
						AudioManager.STREAM_RING, ringerProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			
			ringer.setText("Ringer Volume ("+ringerProgress+ "/" + seekBarRinger.getMax()+")");
		}
	});
	seekBarNotif.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int notifProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			notifProgress = progresValue;
		
			 audioManager.setStreamVolume(
						AudioManager.STREAM_NOTIFICATION, notifProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			
			notif.setText("Notifications Volume ("+notifProgress+ "/" + seekBarNotif.getMax()+")");
		}
	});
	seekBarSystem.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int systemProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			systemProgress = progresValue;
			
			 audioManager.setStreamVolume(
						AudioManager.STREAM_SYSTEM, systemProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			
			system.setText("System Volume ("+systemProgress+ "/" + seekBarSystem.getMax()+")");
		}
	});
	seekBarAlarm.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int alarmProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			alarmProgress = progresValue;
			
			 audioManager.setStreamVolume(
						AudioManager.STREAM_ALARM, alarmProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			 
			alarm.setText("Alarm Volume ("+alarmProgress+ "/" + seekBarAlarm.getMax()+")");
		}
	});
	seekBarCall.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		int callProgress = 0;
        
		@Override
		public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			callProgress = progresValue;
			
			 audioManager.setStreamVolume(
						AudioManager.STREAM_ALARM, callProgress, 0);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// Do something here, if you want to do anything at the start of
			// touching the seekbar
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// Display the value in textview
			 
			call.setText("Voice Call ("+callProgress+ "/" + seekBarCall.getMax()+")");
		}
	});
	}catch(Exception e) {
		e.printStackTrace();
	}
}
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		
		 SharedPreferences sharedPrefs=PreferenceManager.getDefaultSharedPreferences(this);
		 String time=sharedPrefs.getString("longPressPref","3");
		
		 int timeInt=(Integer.parseInt(time)-1);
		if(v.getId()==R.id.editText1) {
			try {
				Thread.sleep(timeInt*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(v.isPressed()) {
				Log.d("AMMA","pressed");
			Intent i=new Intent("com.tech9pedia.PasswordManager");
			startActivity(i);
			finish();
			return true;
			}else{
				Log.d("AMMA","opressed 4 less time");
				return false;
			}
		}
		return false;
	}
	
	void checkDisguise() {
		SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("useDisguisePref",false)) 
			return;
		else {
			Intent i=new Intent("com.tech9pedia.Main");
			startActivity(i);
			finish();
		}
	}

}

