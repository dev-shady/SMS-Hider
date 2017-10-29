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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

 public class ConversationList_ginger extends ListFragment  {
	 private Context context1;
		private DBAdapter dba1;
		//ArrayList<String> list=new ArrayList<String>();
		private Boolean delete;
		ListView listview;
		  private Cursor c1; View v;
		ConversationListAdapter ada;
		//  private Boolean multiple=false;
		  SparseBooleanArray checked;
		  Boolean deleteMsg=false;
		  Boolean firstTime;
		  
		  static Date startD;
		static Date endD;
		  ArrayList<String> senders;
		  int filterSenderPosition;
		  String projection;
		  String filterMsg;
		  GetMessages msgs;
		  ArrayList<ConversationsListItem> listitems;
		  private Parcelable mListState=null;
		  
		public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		//	 Log.d("ConversationList","onCreateView");
			 
			   v=inflater.inflate(R.layout.conversation_list,container,false) ;
			   listview=(ListView)v.findViewById(android.R.id.list);
			 //  new DM();
			   AdView mAdView=(AdView)v.findViewById(R.id.adView);
			   AdRequest adRequest=new Builder().build();
				//adRequest.addTestDevice("B3EEABB8EE11C2BE770B684D95219ECB");
			   mAdView.loadAd(adRequest);
						 // registerForContextMenu(listview);
						  return v;
			   }
			   
		
	public void onActivtyCreated(Bundle savedInstanceState) {
		   super.onActivityCreated(savedInstanceState);
		  
		    }
	public void onPause() {
		 //Log.d("ListFragment","onPause");
		 super.onPause();
		 mListState=listview.onSaveInstanceState();
	}
	 
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
	
		 try{
			   Class.forName("android.os.AsyncTask");
		   }catch(Throwable ignore) {
			   //ignored
			   
		   }
		 //new DM();
	   super.onCreate(savedInstanceState);
	
		 delete=false;
		 setHasOptionsMenu(true);
		 firstTime=true;
		
		 projection=null;
		 filterMsg=null;
			 updateDate(null,null);
	 } 
	@Override
	public void onResume() {
		   super.onResume();
		 //  Log.d("CL ginger","onResume");
		   context1=getActivity();
		  listitems=new  ArrayList<ConversationsListItem>();
		  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			msgs=new GetMessages();
			
			msgs.execute(startD,endD);
		  
	}

	public class GetMessages extends AsyncTask<Date,Void,ConversationListAdapter> {
	   
		ProgressDialog dialog=new ProgressDialog(context1);
		@Override
		protected void onPreExecute() {
			dialog.setMessage("refreshing");
			if(firstTime) {
			 dialog.show();
			 firstTime=false;
			}
		}
		@Override
		protected ConversationListAdapter doInBackground(Date... params) {
			// TODO Auto-generated method stub
			//Log.d("GetMessages","do in background");
			 listview.setChoiceMode(ListView.CHOICE_MODE_NONE);
			listitems.clear();
			dba1=new DBAdapter(context1); 
			
			startD=params[0];
			endD=params[1];
			senders=new ArrayList<String>();
			if(delete) {
			
				ada=new ConversationListAdapter(context1,listitems,true);
			}else {
				
				ada=new ConversationListAdapter(context1,listitems,false);
			}
			
			try {
				dba1.open();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}     //for writable database
			
			c1=dba1.getDistincMessages(null,"_id DESC");
			// senders=new String[c1.getCount()+1];
		      senders.add("-sender-");
			
			if(c1.moveToFirst()) {
			
				
				do{
				 // if(!repeatSenders(c1.getString(2))) {
					senders.add(c1.getString(2));
					 
				}while(c1.moveToNext());
			}	
				c1=dba1.getAllMessages(projection,"_id DESC",null);
				
				SimpleDateFormat sdf2,sdf3;
				sdf2=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
				sdf3=new SimpleDateFormat("MMM dd hh:mm");
				if(c1.moveToFirst()) {
				do{
				
				
				
				String strcont=c1.getString(2);
				Date storeddate;
				
				if(!repeatListItems(strcont)) {
					String addit=strcont;
					String contName=contactName(strcont);
					if(contName!=null)
						  addit=contName;
					  try {
						storeddate=sdf2.parse(c1.getString(3));
						
						 if(storeddate.after(startD)&& storeddate.before(endD)) {
							listitems.add(new ConversationsListItem(addit,strcont,c1.getString(1),sdf3.format(storeddate),c1.getInt(0)));
						 }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  
				  }
				}while(c1.moveToNext());
				
				}	
				
			   return ada;
			
	}
		
		protected void onPostExecute(ConversationListAdapter adaPost) {
			
				setListAdapter(ada);
			  ada.notifyDataSetChanged();
			  if(mListState!=null) {
					// Log.d("CFFFFF","trying to restore");
					 listview.onRestoreInstanceState(mListState);
				}
			  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			if(dialog.isShowing()) {
			
				dialog.dismiss();
			}
			c1.close();
			dba1.close();
			
			
		}
	}	

		
		 
@Override
public void onListItemClick(ListView lv,View v,int position,long id) {
	//Log.d("CL","onListItemClick");
   listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	 
	ListFrag_ginger fr=new ListFrag_ginger();
	Bundle bundle=new Bundle();
	bundle.putString("contactCL",((ConversationsListItem)lv.getItemAtPosition(position)).getNum());
	bundle.putString("startDCL",startD.toString());
	bundle.putString("endDCL",endD.toString());
	bundle.putString("projectionCL",projection);
	fr.setArguments(bundle);
 FragmentTransaction ft=getFragmentManager().beginTransaction();
   ft.replace(R.id.main_layout, fr);
    ft.addToBackStack(null);
   ft.commit();
 
 
}


	
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	
		 inflater.inflate(R.menu.cl_ginger, menu);
	 
}
@Override
public void onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);
	// Log.d("CL_g","prepareOptions");
	
	if(delete) {
		menu.clear();
    MenuItem done=menu.add(0,1,0,"done"); 
	
	menu.add(0,2,0,"cancel");
		
	
	}
	// delete=false;
      
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	Intent newIntent=new Intent("com.tech9pedia.Main");
	newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	switch(item.getItemId()) {
	case R.id.deleteGingerMainLF:
		  delete=true;
		  listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		  getActivity().supportInvalidateOptionsMenu();
		   new GetMessages().execute(startD,endD);
		   break;
	case 1:
		 
		 delete=false;
		 Toast.makeText(context1, "done",Toast.LENGTH_SHORT).show();
			
			for(int i=0;i<listitems.size();i++) {
				if(ada.mCheckedStatesConversation.get(i)){
				
					
					ConversationsListItem delItem=(ConversationsListItem) listview.getItemAtPosition(i);
					String cont=delItem.getNum();
					try {
						dba1.open();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dba1.deleteMessageUsingContact(cont);
					dba1.close();
				}
			}
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			//getActivity().supportInvalidateOptionsMenu();
			
			startActivity(newIntent);
			
		    break;
	case 2:
		
		 listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			//getActivity().supportInvalidateOptionsMenu();
			
			startActivity(newIntent);
			 break;
		
	case R.id.filterMain:
		
		//Intent filter=new Intent("com.tech9pedia.Filter");
		//startActivity(filter);
		
		new ShowDialog().show(getFragmentManager(),"Filter by");
				  
	} 
	return true;
}

   public void onDestroy() {
	   super.onDestroy();
	   
   }
   public Boolean repeatListItems(String number) {
	      for(int j=0;j<listitems.size();j++) {
	    	  if(number.equals(listitems.get(j).getNum())){
			  
			  return true;
		   }
	      }
	   return false;
}
public Boolean repeatSenders(String num) {
	   for(int k=0;k<senders.size();k++) {
	    	  if(num.equals(senders.get(k))) {
			  
			  return true;
		   }
	      }
	   return false;
}

View filterLayout() {

	final View root= getActivity().getLayoutInflater().inflate(R.layout.filter,null);
//Log.d("CL","filterLayout");
	ArrayAdapter<String> spinnerAda=new ArrayAdapter<String>(context1,android.R.layout.simple_spinner_item,senders);
	 spinnerAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	Spinner spn=(Spinner) root.findViewById(R.id.spinnerview);
	spn.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long id) {
			// TODO Auto-generated method stub
			filterSenderPosition=position;
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	EditText filtertext=(EditText) root.findViewById(R.id.message);
	filtertext.addTextChangedListener(new TextWatcher() {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			filterMsg=s.toString();
		}
		
	});
	
if(spinnerAda!=null && spn!=null)
	spn.setAdapter(spinnerAda);
	//Log.d("CL","filterLayout");
	return root;
}


class ShowDialog extends DialogFragment {
	   @Override
	public Dialog onCreateDialog(Bundle savedInstanceSate) {
		
				
				return new AlertDialog.Builder(context1)
						.setTitle("Filter")
						 .setView(filterLayout())
					      .setCancelable(true)
					      .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	  // ((SimpleCursorAdapter)getListAdapter()).changeCursor(createCursor(true));
					        	   
					        	   if(senders.get(filterSenderPosition).equals("-sender-"))
					        		   projection=null;
					        	   else projection=("contact='"+senders.get(filterSenderPosition)+"'");
					        	  
					        	   if(projection!=null && filterMsg!=null) 
					        		     projection+=" AND sms LIKE '%"+filterMsg+"%'";
					        	   else if(projection==null && filterMsg!=null)
					        		   projection=" sms LIKE '%"+filterMsg+"%'";
					        	     
					        	  msgs.cancel(true);
					        	    msgs=new GetMessages();
					        	   msgs.execute(startD,endD);
					        	  
					        	   
					        	  // new GetMessages().execute(startD,endD);
					           }
					       })
					       .setNegativeButton("Clear", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   updateDate(null,null);
					        	   filterSenderPosition=0;
					        	   filterMsg=null;
					        	   projection=null;
					        	   new GetMessages().execute(startD,endD);
					           }
					       })
					       .create();		
			
			
			
   } 
}
static public void updateDate(String start,String end) {
	 
	//  Log.d("updateDate","b4 start");
	   SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
	  
	   if(start==null && end==null) {
		 //  Log.d("initialisation","both null");
		   startD=new Date(100);
			endD=new Date(2050,00,01);
	   }else if(start==null) {
		
		  try {
			endD=sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  return;
	  } 
	  else if(end==null){
		
		  try {
			startD=sdf.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  return;
	  }
	  else  {
		 
		  try {
			startD=sdf.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			endD=sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		  return;
	  } 
	  
		//startD=new Date(100);
		//endD=new Date(2050,00,01);
	  
}
String contactName(String phoneNumber) {
	   String name=null;
	   Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
	   ContentResolver cr=context1.getContentResolver();
	   if(cr!=null) {
		
		   Cursor cur=cr.query(uri,new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME},null,null,null);
		   if(cur.moveToFirst()) {
			   name=cur.getString(cur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
			 
		   }
	   }
	   return name;
}
}

