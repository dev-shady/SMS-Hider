package com.tech9pedia.dontbother;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.telephony.SmsMessage;


public class SMSReceiver extends BroadcastReceiver{
     
      String str;
      String senderNo;
      DBAdapter dba2;
      Context context2;
      boolean flag=false,priv=false;
      GregorianCalendar cal;
     
      @Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Bundle bundle=intent.getExtras();
		SmsMessage[] msg=null;
		str="";
		senderNo="";
		context2=context;
		if(bundle!=null) {
			Object[] pduObj=(Object[])bundle.get("pdus");
			msg=new SmsMessage[pduObj.length];
			cal=new GregorianCalendar();
            
            
			for(int i=0;i<msg.length;i++) {
			    msg[i]=SmsMessage.createFromPdu((byte[])pduObj[i]);
			  if(i==0) {
				  senderNo=msg[i].getOriginatingAddress();
				  if(!senderNo.matches("[0-9]*"))
					  priv=true;
				
			  } 
			  str+=msg[i].getMessageBody().toString();
			}
			
			}
		
		 dba2=new DBAdapter(context2);
		  try {
			dba2.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  Cursor c2=dba2.getAllContacts();
			if(c2.moveToFirst()) {
			
				 do{  
					String num=c2.getString(1).substring(1);
				   if(num.equals(senderNo) || (priv&&senderNo.contains(num))) {
					   flag=true;
					 
					   SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
					   Date date=cal.getTime();
					  
					   dba2.insertMessage(str,senderNo,sdf.format(date));
					 
					      break;
				   }else {
					   
					  
				   }
				  }while(c2.moveToNext());
			}
	
			 if(flag) {
				 abortBroadcast();
				 flag=false;
				 priv=false;
				
			 }
			
	       dba2.close();
		
	      
	   }
}