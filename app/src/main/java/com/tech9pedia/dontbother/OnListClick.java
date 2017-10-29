package com.tech9pedia.dontbother;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.tech9pedia.dontbother.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;



public class OnListClick extends Fragment{
//	private static final String CLIPBOARD ="clipboard";
	SharedPreferences sp;
	ShowMessage onClickMsg;
	Context context2;
	DBAdapter dba2;
	 String[] cols2;
	 String contact;
	 int[] views2; 
	 View v;
	  Cursor c2;
	  String str;
	  int id;
	  int count;
	  TextView tv;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	
		  v=inflater.inflate(R.layout.on_list_click,container,false) ;
		  AdView mAdView=(AdView)v.findViewById(R.id.adView3);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
		   return v;
    }
		   
	
public void onActivtyCreated(Bundle savedInstanceState) {
	   super.onActivityCreated(savedInstanceState);
	   setHasOptionsMenu(true);
	//   Log.d("OnListClick","onActivityCreated");
	    }

@SuppressWarnings("deprecation")
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setHasOptionsMenu(true);
	//Log.d("ListFrag","onCreate");
	
 } 
public void onResume() {
	   super.onResume();
	  // Log.d("OnListClick","onResume");
	   context2=getActivity();	
	   id=getArguments().getInt("id");
	  count = getArguments().getInt("count");
	   //Log.d("onclick b4 showMEssage",id+""+count+"count");
	  onClickMsg= new ShowMessage();
	   onClickMsg.execute(id);
}

	 
public class ShowMessage extends AsyncTask<Integer,Void,String> {
    
	@Override
	protected String doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf2,sdf3;
		sdf2=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		sdf3=new SimpleDateFormat("MMM dd hh:mm");
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
		
		c2=dba2.getMessage(id);
		//c2.moveToPosition(params[0]);
		//int id2=c2.getInt(c2.getColumnIndex("_id"));
		//c2=dba2.getMessage(id2);		
		 str=c2.getString(1);
		contact=c2.getString(2);
		Date time=new Date();
		try {
			 time=sdf2.parse(c2.getString(3));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 id=c2.getInt(0);
		 str+="\n\n";
		 str+=("from "+contact+"\n"+sdf3.format(time));
		 //Log.d("onclick",id+"");
		dba2.close();
		 
	     return str;
	}		
	protected void onPostExecute(String strPost) {
		
	    tv=(TextView)getActivity().findViewById(R.id.textView);
	   tv.setText(strPost);
	   onClickMsg.cancel(true);
	} 
		
  }
public void onDestroy() {
	super.onDestroy();
}
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.mainclick, menu);
	}
@SuppressLint("NewApi") @SuppressWarnings("deprecation")
public boolean onOptionsItemSelected(MenuItem item) {
	Intent smsIntent;
	  switch(item.getItemId()) {
		  case R.id.shareMsg: 
			  Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/*");// text/plain
				shareIntent.putExtra(Intent.EXTRA_TEXT,tv.getText().toString());
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject"); // TODO
				startActivity(Intent.createChooser(shareIntent, "Share via"));
				break;			
		  case R.id.copyMsg:
			 if(android.os.Build.VERSION.SDK_INT <android.os.Build.VERSION_CODES.HONEYCOMB) {
			//	 Log.d("OLC","in<hc");
				 android.text.ClipboardManager clipboard=( android.text.ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				 clipboard.setText(tv.getText().toString());
				
			 }else {
				
				 android.content.ClipboardManager clipboard=( android.content.ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
				 android.content.ClipData clip= android.content.ClipData.newPlainText("Shared via DontBother",tv.getText().toString());
				 clipboard.setPrimaryClip(clip);
			 }
			  Toast.makeText(context2, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
			  break;	
			  
	  case R.id.replyMsg: 
		  //Log.d("OLC","in reply");
		   smsIntent=new Intent(Intent.ACTION_VIEW);
		  smsIntent.setType("vnd.android-dir/mms-sms");
		  smsIntent.putExtra("address",contact);
		  startActivity(smsIntent);
		  break;	
	      
		   
	  case R.id.deleteMsg:
		 try {
			dba2.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  dba2.deleteMessage(id);
		    if(count!=1)
		   getFragmentManager().popBackStack();
		    else {
		    	Intent newIntent=new Intent("com.tech9pedia.Main");
				newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(newIntent);
		    }
	  } 
	  
	  
  return false;
 }

/*
private void replaceFragment(Fragment fr) {
	// TODO Auto-generated method stub
	String backStateName=fr.getClass().getName();
	getFragmentManager().popBackStackImmediate(backStateName,0);
	
}*/
}

