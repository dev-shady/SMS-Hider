package com.tech9pedia.dontbother;




import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.tech9pedia.dontbother.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;



public class AddedContacts extends ListFragment{
	DBAdapter dba4;
	Cursor c4;
	Context contextContact;
	ListView listviewContact;
	ArrayList<AddedContactsItem> contactsList;
	AddedContactsAdapter contactAdapter;
	Boolean deleteContact=false;
	boolean firstTimeContact=true;
	
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	//  Log.d("AddedContacts","onCreateView");
	//	  Log.d("AddedContacts","b4 inflate expression");
	     View vContact=inflater.inflate(R.layout.added_contacts,container,false) ;
	     listviewContact=(ListView) vContact.findViewById(android.R.id.list);
	     AdView mAdView=(AdView)vContact.findViewById(R.id.adView);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
	     return vContact;
    }
		  
		   
	
public void onActivtyCreated(Bundle savedInstanceState) {
	   super.onActivityCreated(savedInstanceState);
	  
	   //Log.d("AddedContacts","onActivityCreated");
	    }

@SuppressWarnings("deprecation")
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	//Log.d("AddedContacts","onCreate");
	 setHasOptionsMenu(true);
 } 
public void onResume() {
	   super.onResume();
	   contextContact=getActivity();
	  // Log.d("AddedContacts","onResume");
	   contactsList=new ArrayList<AddedContactsItem>();
	   new GetContacts().execute();
      }

public class GetContacts extends AsyncTask<Void,Void,AddedContactsAdapter> {

	
	ProgressDialog dialog=new ProgressDialog(contextContact);
	@Override
	protected void onPreExecute() {
		dialog.setMessage("refreshing");
		if(firstTimeContact) {
		 dialog.show();
		 firstTimeContact=false;
		}
	}
	@Override
	protected AddedContactsAdapter doInBackground(Void... params) {
		// TODO Auto-generated method stub
		dba4=new DBAdapter(contextContact); 
		SimpleDateFormat sdf2,sdf3;
		sdf2=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		sdf3=new SimpleDateFormat("MMM dd hh:mm");
		contactsList.clear();
		try {
			dba4.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     //for writable database
		c4=dba4.getAllContacts();
		
		if(c4.moveToFirst()) {
		// String strtext;
		do{	
		 int iddContact;
		
		String contact=c4.getString(c4.getColumnIndex("contact"));
		String contact2=contact.substring(1);
		Date time=new Date();
		try {
			time = sdf2.parse(c4.getString(2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		iddContact=c4.getInt(0);
	//	Log.d("LF",strtext);
		//Log.d("donInBackground","b4 adding listitems");
		contactsList.add(new AddedContactsItem(contact2,sdf3.format(time),iddContact));
		//Log.d("donInBackground","after adding listitems");
		}while(c4.moveToNext());
		//Log.d("Postexecute",idd+"");               
		
		if(!contactsList.isEmpty())
		
		if(!deleteContact) {
			contactAdapter=new AddedContactsAdapter(contextContact,contactsList,false);
		}else {
			contactAdapter=new AddedContactsAdapter(contextContact,contactsList,true);
		}
		
	   return contactAdapter;
	}else {
		
		return null;
	}
  }	

protected void onPostExecute(AddedContactsAdapter adaPost) {
	
	if(c4.moveToFirst()){
	setListAdapter(adaPost); 
	adaPost.notifyDataSetChanged();
	}else {
		setListAdapter(null);
	}
	
	if(dialog.isShowing()) {
		dialog.dismiss();
	}
	
	dba4.close();
	
	
}

	
}

@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	
		 inflater.inflate(R.menu.added_contacts_menu, menu);
		
		
	 }
@Override
public void onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);
	
	if(deleteContact) {
		menu.clear();
		getActivity().getMenuInflater().inflate(R.menu.del_main_ginger, menu);
	}else {
		//getActivity().getMenuInflater().inflate(R.menu.added_contacts_menu, menu);	
	}
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId()) {
	case R.id.deleteContact:
		
		
		  
		  listviewContact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		 // list.clear();
		 
		  deleteContact=true;
		  
		  getActivity().supportInvalidateOptionsMenu();
		   new GetContacts().execute();
		   break;
	case R.id.donelf:
		
		 Toast.makeText(contextContact, "done",Toast.LENGTH_SHORT).show();
			
			for(int i=0;i<contactsList.size();i++) {
				if(contactAdapter.mCheckedStatesContact.get(i)){
				
				
					AddedContactsItem delContact=(AddedContactsItem) listviewContact.getItemAtPosition(i);
					
					
					try {
						dba4.open();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dba4.deleteContact(delContact.getDatabaseIdContact());
					dba4.close();
				deleteContact=false;
				}
			}
			
			listviewContact.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			 getActivity().supportInvalidateOptionsMenu();
			new GetContacts().execute();
			
		    break;
	case R.id.cancellf:
		
		 deleteContact=false;
		 getActivity().supportInvalidateOptionsMenu();
		new GetContacts().execute();
			 break;
		} 
	return true;
}
public void onDestroy(){
	super.onDestroy();
	
}
}



