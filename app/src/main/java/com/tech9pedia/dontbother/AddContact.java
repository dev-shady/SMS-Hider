package com.tech9pedia.dontbother;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;


public class AddContact extends IntentService{
	
	Context context3;
	DBAdapter dba3;
	  Cursor c3;
	  public AddContact() {
			super("AddContact");
			//context3=getApplicationContext();
			// TODO Auto-generated constructor stub
		}
  protected void onHandleIntent(Intent intent) {
		  String senderNum="$";
		  senderNum+=intent.getStringExtra("senderNo");
		  Date now=new Date();
	
		 SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		
		  dba3=new DBAdapter(getApplicationContext());
		  try {
			dba3.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		 
		   
		   Long id=dba3.insertContact(senderNum,sdf.format(now));
		  
			 c3=dba3.getAllContacts();
			//  if(c3.moveToFirst()) {
		//	 do{   
			//	   Log.d("AddContact",c3.getString(1));
			//  }while(c3.moveToNext());
			//}
			  dba3.close();
		  }
		
	  }
	 
 