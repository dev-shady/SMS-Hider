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

import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

 public class ConversationList extends ListFragment  {
	private Context context1;
	private DBAdapter dba1;
	GetMessages msgs;
	//ArrayList<String> list=new ArrayList<String>();
//	private Boolean delete=false;
	private ListView listview;
	//private int rowId;
	//private Boolean done=false;
	 private Cursor c1;
	  View v;
	ConversationListAdapter ada;
	  //private Boolean multiple=false;
	  SparseBooleanArray checked;
	  Boolean isCABDestroyed=true;
	  Boolean firstTime;
	String dialog=null;
	ArrayList<String> senders;
	static Date startD;
	static Date endD;
	 int filterSenderPosition;
	 String projection;
	String filterMsg;
	 String name;
	 
	  ArrayList<ConversationsListItem> listitems;
	  
	
	  private Parcelable mListState=null;
	  public ConversationList(Date s,Date e) {
		  startD=s;
		  endD=e;
	  }
	  public ConversationList() {
		  
	  }
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		//  Log.d("ConversationList","onCreateView");
		 
		   v=inflater.inflate(R.layout.conversation_list,container,false) ;
		   listview=(ListView)v.findViewById(android.R.id.list);
		  
		   AdView mAdView=(AdView)v.findViewById(R.id.adView);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
					 // registerForContextMenu(listview);
					  return v;
		   }
		   
	
public void onActivtyCreated(Bundle state) {
	   super.onActivityCreated(state);
	  // mListState=state.getParcelable(LIST_STATE);
	  }
public void onSaveInstanceState(Bundle state) {
	  super.onSaveInstanceState(state);
	 //mListState=getListView().onSaveInstanceState();
	//  state.putParcelable(LIST_STATE, mListState);
	  

}
public void onPause() {
	 //Log.d("ListFragment","onPause");
	 super.onPause();
	 mListState=listview.onSaveInstanceState();
}

@SuppressWarnings("deprecation")
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 setHasOptionsMenu(true);
	 firstTime=true;
	 projection=null;
	 filterMsg=null;
		 updateDate(null,null);
 } 
@Override
public void onResume() {
	   super.onResume();
	   //Log.d("CL","onResume");
	   context1=getActivity();
	  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	  listitems=new  ArrayList<ConversationsListItem>();
	  ada=new ConversationListAdapter(context1,listitems,false);
	  listview.setMultiChoiceModeListener(mcListener);
	  listview.setOnItemLongClickListener(lcListener);
		msgs=new GetMessages();
		 //Log.d("ListFrag","b4 getmessagesExecute");
		msgs.execute(startD,endD);
		//Log.d("CL","B4 MLISTSTATE CHECKING NULL");
	/*if(mListState!=null) {
		  listview.onRestoreInstanceState(mListState);
		  mListState=null;
	}*/
	  
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
		
		//ada.notifyDataSetChanged();
		dba1=new DBAdapter(context1); 
		//Log.d("",params[0].toString());
		startD=params[0];
		endD=params[1];
		senders=new ArrayList<String>();
		//Log.d("",startD.toString());
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
	      
	    	    
		//Log.d("donInBackground","after getAllMessages");
		
		if(c1.moveToFirst()) {
		
			//Log.d("donInBackground","c1 exists");
			do{
			  //if(!repeatSenders(c1.getString(2))) {
				//  if(contactName(c1.getString(2))!=null)
				//	  senders.add(name);
				 // else
				  senders.add((c1.getString(2)));
				//Log.d("senders","addded");
				
			  
			}while(c1.moveToNext());
		}	
			c1=dba1.getAllMessages(projection,"_id DESC",null);
			//do{ Log.d("c1 distinct",c1.getString(2));
		//	}while(c1.moveToNext());
			
			if(c1.moveToFirst()) {
				SimpleDateFormat sdf2,sdf3;
				
				sdf2=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
				sdf3=new SimpleDateFormat("MMM dd hh:mm");
			do{
			//int idd=c1.getPosition();
			//Date date=new Date();
				String strcont=c1.getString(2);
		if(!repeatListItems(strcont)) {
			
			Date storeddate;
			
			 //Log.d("CL",strcont+"");
			//if(!repeatListItems(strcont)) {
				String addit=strcont;
				String contName=contactName(strcont);
				if(contName!=null)
					  addit=contName;
				  try {
					storeddate=sdf2.parse(c1.getString(3));
							
					 if(storeddate.after(startD)&& storeddate.before(endD)) {
						listitems.add(new ConversationsListItem(addit,strcont,c1.getString(1),sdf3.format(storeddate),c1.getInt(0)));
					// Log.d("CL",addit+"\t"+strcont+"added");
					 }
				  
				
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				
		} 
			  
			  
			}while(c1.moveToNext());
			
		     
			//if(delete) {
		   //		Log.d("donInBackground","delete exists");
				//ada=new ArrayAdapter<String>(context1,android.R.layout.simple_list_item_multiple_choice,list); //
			//}else {
				//Log.d("donInBackground","delete not exists");
			
				//ada=new ConversationListAdapter(context1,listitems,false);
			
			//}
			
		//	if(ada.isEmpty()) Log.d("donInBackground","empty ada");
		 
			}/*else {
				
			Log.d("donInBackground","c1 not exists");
			return null;
		}*/
			  return ada;
}
	
	protected void onPostExecute(ConversationListAdapter adaPost) {
		//Log.d("GetMessages","in postExecute");
	//	Log.d("GetMessages","c1  null"); 
		 
		//if(c1.moveToFirst() && ada!=null) {
			//Log.d("donInBackground","ada  exists");
			setListAdapter(ada);
			if(mListState!=null) {
				// Log.d("CFFFFF","trying to restore");
				 listview.onRestoreInstanceState(mListState);
			}
		  ada.notifyDataSetChanged();
		  dba1.close();
		  c1.close();
		  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//}else {
			
		
		
		//}
		if(dialog.isShowing()) {
			//Log.d("do in background","in dailog is showing");
			dialog.dismiss();
		}
		
		
		
		
	}
}	

	
	 

@Override
public void onListItemClick(ListView lv,View v,int position,long id) {
	//Log.d("CL","onListItemClick");
	//Log.d("onListItemClick",position+"");
	if(isCABDestroyed) {
	//	listitems.clear();
    try {listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	
   SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
   msgs.cancel(true);
	ListFrag fr=new ListFrag();
	Bundle bundle=new Bundle();
	bundle.putString("contactCL",((ConversationsListItem)lv.getItemAtPosition(position)).getNum());
	bundle.putString("startDCL",sdf.format(startD));
	bundle.putString("endDCL",sdf.format(endD));
	bundle.putString("projectionCL",projection);
	fr.setArguments(bundle);
 FragmentTransaction ft=getFragmentManager().beginTransaction();
   ft.replace(R.id.main_layout, fr);
    ft.addToBackStack(null);
   ft.commit(); 
    }catch(Exception e) {
    	//Log.d("ARRAYINDEXOUTOFBOUND","ARRAYINDEXOUTOFBOUND");
    }
  }else {
		 //Log.d("onListItemClick","cab active");
	
  }
 
}

private OnItemLongClickListener lcListener=new OnItemLongClickListener() {
	 public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id) {
		 //Log.d("ListFrag","long click");
		 listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		 //Log.d("Longclick listener","after choic mode multiple modal");
		// setListAdapter(getListAdapter());
		 isCABDestroyed=false;
		 listview.setItemChecked(position,true);
		 //Log.d("ListFrag","position checked");
		 return true;
	 } 
	 };
	 
 private MultiChoiceModeListener mcListener=new MultiChoiceModeListener() {
	
	@Override
	public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","create action mode");
		MenuInflater inflater=mode.getMenuInflater();
		inflater.inflate(R.menu.cab, menu);
		return true;
		
	}
	@Override
	public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","prepare action mode");
		return false;
	}
	@Override
	public boolean onActionItemClicked(android.view.ActionMode mode,
			MenuItem item) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","action item clicked");
		switch(item.getItemId()) {
		case R.id.msgdel:
		
			SparseBooleanArray sba=listview.getCheckedItemPositions();
			//Log.d("sba size",sba.size()+"");
			for(int i=0;i<sba.size();i++) {
				if(sba.valueAt(i)) {
					int pos=sba.keyAt(i);
				//	Log.d(i+" key/pos",pos+"");
					//int id=(int)listview.getItemIdAtPosition(pos);
				ConversationsListItem child=(ConversationsListItem) listview.getItemAtPosition(pos); 
				String cont=child.getNum();
				//int id=child.getDatabaseIdConversation();
					//Log.d(i+" id",cont);
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
			Intent newIntent=new Intent("com.tech9pedia.Main");
			newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			mode.finish();
			startActivity(newIntent);
			return false;
		default:
			  
			mode.finish();
			return false;
		
		}
	
	}
	@Override
	public void onDestroyActionMode(android.view.ActionMode mode) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","destroy action mode");
		isCABDestroyed=true; //ready to switch back to single choice
	}
	@Override
	public void onItemCheckedStateChanged(android.view.ActionMode mode,
			int position, long id, boolean checked) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","Item Check State Changed");
		final int checkedCount=listview.getCheckedItemCount();
		switch(checkedCount) {
		case 0:
			mode.setSubtitle(null); break;
		case 1:
			mode.setSubtitle("1 Selected"); break;
		default:
			mode.setSubtitle(checkedCount+" Selected"); break;
		}
		
	}
	
  };
  
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	
	   inflater.inflate(R.menu.cl, menu);
	 
}
/*
public void onCreateContextMenu(ContextMenu menu,View view,ContextMenuInfo info) {
	 super.onCreateContextMenu(menu, view, info);
  getActivity().getMenuInflater().inflate(R.menu.context, menu);
 
	
} */

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId()) {
	
	
	case R.id.filterMainCL:
		//Log.d("ConversationList","filter main");
		//Intent filter=new Intent("com.tech9pedia.Filter");
		//startActivity(filter);
		dialog="filter";
		new ShowDialog().show(getFragmentManager(),"Filter by");
		
	}
	
	return true;
}
View filterLayout() {
	//Log.d("CL","filterLayout");
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
	//Log.d("CL","filterLayout");
if(spinnerAda!=null && spn!=null)
	spn.setAdapter(spinnerAda);
	//Log.d("CL","spiner ada set");
	return root;
}


   public void onDestroy() {
	   super.onDestroy();
	   
   }
   public Boolean repeatListItems(String number) {
	      for(int j=0;j<listitems.size();j++) {
	    	  if(number.equals(listitems.get(j).getNum())){
		//	   Log.d("repeatListItems","equals");
			  return true;
		   }
	      }
	   return false;
   }
   public Boolean repeatSenders(String num) {
	   for(int k=0;k<senders.size();k++) {
	    	  if(num.equals(senders.get(k))) {
			//   Log.d("repeatSenders","equals");
			  return true;
		   }
	      }
	   return false;
   }
   class ShowDialog extends DialogFragment {
	   @Override
	public Dialog onCreateDialog(Bundle savedInstanceSate) {
		 
			if(dialog.equals("filter")) {
				//Log.d("in FilterDialog","onCreateDialog");
				return new AlertDialog.Builder(context1)
						.setTitle("Filter")
						 .setView(filterLayout())
					      .setCancelable(true)
					      .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	  // ((SimpleCursorAdapter)getListAdapter()).changeCursor(createCursor(true));
					        	
					        	
					        	   if(senders.get(filterSenderPosition).equals("-sender-"))
					        		   projection=null;
					        	   else {
					        		   projection=("contact='"+senders.get(filterSenderPosition)+"'");
					        	//       Log.d("",projection);
					        	   }
					        	  
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
			return null;
      } 
     
   }
   static public void updateDate(String start,String end) {
	 
	  // Log.d("updateDate","b4 start");
	   SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
	  
	 //  Date test=new Date(100);
	 //  Log.d("god",test.toString());
	   if(start==null && end==null) {
		//   Log.d("initialisation","both null");
		   startD=new Date(100);
			endD=new Date(2050,00,01);
	   }else if(start==null) {
		  //Log.d("updateDate","in start null");
		
		  try {
			endD=sdf.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		  return;
	  } 
	  else if(end==null){
		 // Log.d("updateDate","in end null");
		
		  try {
			startD=sdf.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  //Log.d("startD",startD.toString()+"");
		  return;
	  }
	  else  {
		  //Log.d("CL updateDate","last else for both");
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
		  //Log.d(startD.toString(),endD.toString());
		  return;
	  } 
	  
		//startD=new Date(100);
		//endD=new Date(2050,00,01);
	  
   }
   String contactName(String phoneNumber) {
	  name=null;
	   Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(phoneNumber));
	   ContentResolver cr=context1.getContentResolver();
	   if(cr!=null) {
		   //Log.d("contactName","b4 query");
		   Cursor cur=cr.query(uri,new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME},null,null,null);
		   if(cur.moveToFirst()) {
			   name=cur.getString(cur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
			 //  Log.d("contactName",phoneNumber+"\t"+name);
		   }
	   }
	   return name;
   }
  
 @Override
 public void onActivityResult(int requestCode,int resultCode,Intent data) {
 	//	super.onActivityResult(requestCode, resultCode, data);
 		 //Log.d("addedContacsFor Interface","in activity result");
 	
 	}
 }
 /*  String contactNumber(String contactName) {
		String  num=null;
		   Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,Uri.encode(contactName));
		   ContentResolver cr=context1.getContentResolver();
		   if(cr!=null) {
			   Log.d("contactName","b4 query");
			   Cursor cur=cr.query(uri,new String[] {ContactsContract.PhoneLookup.NUMBER},null,null,null);
			   if(cur.moveToFirst()) {
				   num=cur.getString(cur.getColumnIndexOrThrow(ContactsContract.PhoneLookup.NUMBER));
				   Log.d("contactName",contactName+"\t"+num);
				   return num;
			   }
		   }
		   return contactName;
	   }
*/
