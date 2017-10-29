package com.tech9pedia.dontbother;




import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.tech9pedia.dontbother.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

   

public class AddContactForInterface extends Fragment{
	 View v;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	
		   v= inflater.inflate(R.layout.add_contact_interface,container,false);
		   AdView mAdView=(AdView)v.findViewById(R.id.adView);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
		   return v;
    }
		   
	
public void onActivtyCreated(Bundle savedInstanceState) {
	   super.onActivityCreated(savedInstanceState);
	   setHasOptionsMenu(true);
	 
	  
	    }

@SuppressWarnings("deprecation")
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
}

@Override
public void onActivityResult(int requestCode,int resultCode,Intent data) {
	
		if(requestCode==1) {
			 if(resultCode==Activity.RESULT_OK) {
				 
				 Uri contactData=data.getData();
				 Cursor c=getActivity().managedQuery(contactData,null,null,null,null);
				 if(c.moveToFirst()) {
					 String pickedNum=c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					 EditText et=(EditText)v.findViewById(R.id.edittext);
					 et.setText(pickedNum);
					 Toast.makeText(getActivity(),"do precede the contact with proper ISD code if not preceded already",Toast.LENGTH_LONG).show();
				 }
					 
			 }
		}
	}

public void onResume() {
	   super.onResume();
	 TextView tv=(TextView)v.findViewById(R.id.pickC);
	 Button add=(Button)v.findViewById(R.id.addnum);
			if(tv!=null) {
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent pick=new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
						pick.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
						
						startActivityForResult(pick,1);
						
					}
		 }) ;
			} 
			
			  add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 EditText numMain=(EditText)getActivity().findViewById(R.id.edittext);
				      Button butMain=(Button)getActivity().findViewById(R.id.addnum);
				  
			      	String contact=numMain.getText().toString();
			      	
				   
				     numMain.setText("");
				   
				   Intent i=new Intent(getActivity(),AddContact.class);
				   
				   i.putExtra("senderNo",contact);
				
				   getActivity().startService(i);
				  Toast.makeText(getActivity(),"contact added",Toast.LENGTH_SHORT).show();
				}
				  
			  });
}



public void onDestroy(){
	super.onDestroy();

}

 void pickContact() {
	
}
 
 }




