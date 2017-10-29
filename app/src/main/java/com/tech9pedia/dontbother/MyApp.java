package com.tech9pedia.dontbother;



import android.app.Application;

public class MyApp extends Application {
	
	public void onCreate() {
		super.onCreate();
		 try{
			   Class.forName("android.os.AsyncTask");
		   }catch(Throwable ignore) {
			   //ignored
			   
		   }
	}

}
