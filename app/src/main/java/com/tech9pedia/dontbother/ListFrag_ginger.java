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
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class ListFrag_ginger extends ListFragment  {
	private Context contextMsg;
	private DBAdapter dbaMsg;
	ActionBarActivity aba;
	ArrayList<String> list=new ArrayList<String>();
	ActionBar actionbar;
	String[] cols1;
	int[] views1;
	 CheckBox box;
	private Boolean delete=false;
	//private int rowId;
//	private Boolean done=false;
	  private Cursor c1; View v;
	  private  ListView listview;
	  private ListFragAdapter ada;
	//  private Boolean multiple=false;
	  SparseBooleanArray checked;
	  Boolean isCABDestroyed=true;
	  Boolean firstTime=true;
	  String projectionLF;
	  String filterMsgLF;
	  
	  String contactLF;
	  String startDLF;
	  String endDLF;
	  
	  int delId;
	  ArrayList<ListFragItem> listitems;
	  String sortOrder="_id DESC";
	  String dialog=null;
	  boolean deleteMsg=false;
	  GetMessages msgs;
	  private Parcelable mListState=null;
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		   v=inflater.inflate(R.layout.list_fragment,container,false) ;
		   listview=(ListView)v.findViewById(android.R.id.list);
		  AdView mAdView=(AdView)v.findViewById(R.id.adView2);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
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
	super.onCreate(savedInstanceState);
	try{
		   Class.forName("android.os.AsyncTask");
	   }catch(Throwable ignore) {
		   //ignored
		   
	   }
	 setHasOptionsMenu(true);
	 projectionLF=null;
//	Log.d("ListFrag","onCreate");
	
	
 } 
@Override
public void onResume() {
	 super.onResume();
	
	   contextMsg=getActivity();
	   contactLF=getArguments().getString("contactCL");
	   startDLF=getArguments().getString("startDCL");
	   endDLF=getArguments().getString("endDCL");
	   projectionLF=getArguments().getString("projectionCL");
	  // cols1=new String[] {"smsUI"};
	   listitems=new ArrayList<ListFragItem>();
		// views1=new int[] {android.R.layout.simple_list_item_1};
	  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	  
	//	 Log.d("ListFrag","b4 getmessages");
		msgs=new GetMessages();
		// Log.d("ListFrag","b4 getmessagesExecute");
		 try {
		msgs.execute();
		 }catch(IllegalStateException e) {
			 e.printStackTrace();
		 }catch(IndexOutOfBoundsException e) {
			 e.printStackTrace();
		 }
	  
}

public class GetMessages extends AsyncTask<Void,Void,ListFragAdapter> {
   
	ProgressDialog dialog=new ProgressDialog(contextMsg);
	@Override
	protected void onPreExecute() {
		dialog.setMessage("refreshing");
		if(firstTime) {
		 dialog.show();
		 firstTime=false;
		}
	}
	@Override
	protected ListFragAdapter doInBackground(Void... params) {
		// TODO Auto-generated method stub
		//Log.d("GetMessages","do in background");
		listitems.clear();

		SimpleDateFormat sdf1=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd hh:mm");
		dbaMsg=new DBAdapter(contextMsg);   
		Date d1=null,d2=null;
		if(projectionLF==null) 
			projectionLF="contact='"+contactLF+"'";
		else projectionLF+="AND contact='"+contactLF+"'";
		try {
			d1=sdf1.parse(startDLF);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			d2=sdf1.parse(endDLF);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dbaMsg.open();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     //for writable database
		
		c1=dbaMsg.getAllMessages(projectionLF,sortOrder,null);
		//Log.d("donInBackground","after getAllMessages");
		
		if(c1.moveToFirst()) {
			//Log.d("donInBackground","c1 exists");
			do{
			 String strtext;
			 int idd;
			
			//String strcont=c1.getString(c1.getColumnIndex("contact"));
			 strtext=c1.getString(c1.getColumnIndex("sms"));
			
			idd=c1.getInt(0);
			
		//	Date dt;
			Date storeddate=null;
			//Log.d("LF",c1.getString(3));
			try {
				storeddate = sdf1.parse(c1.getString(3));
				//Log.d("LF storedate",storeddate.toString());
				if(storeddate.after(d1)&& storeddate.before(d2)) {
					//Log.d("LF storedate",startDLF.toString());
				//Log.d("donInBackground","b4 adding listitem");
				listitems.add(new ListFragItem(strtext,sdf2.format(storeddate),idd));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			//Log.d("Postexecute",idd+"");               
			}while(c1.moveToNext());
			
			if(delete) {
			//	Log.d("lisfrag_ginger","delete exits -checkbox true");
				ada=new ListFragAdapter(contextMsg,listitems,true);
			}else {
				//Log.d("lisfrag_ginger","delete not exits -checkbox false");
				ada=new ListFragAdapter(contextMsg,listitems,false);
			}
			
		
		   return ada;
		}else {
		//	Log.d("donInBackground","c1 not exists");
			return null;
		}
      }	
	
	protected void onPostExecute(ListFragAdapter adaPost) {
		//Log.d("GetMessages","in postExecute");

		setListAdapter(adaPost); 
		ada.notifyDataSetChanged();
		if(mListState!=null) {
			// Log.d("CFFFFF","trying to restore");
			 listview.onRestoreInstanceState(mListState);
		}
		if(dialog.isShowing()) {
			//Log.d("do in background","in dailog is showing");
			dialog.dismiss();
		}
		c1.close();
		dbaMsg.close();
		
		
	}
}	

	
	
@Override
public void onListItemClick(ListView lv,View v,int position,long id) {
	
	if(isCABDestroyed) {
	//	listitems.clear();
   listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	
	OnListClick fr=new OnListClick();
	Bundle bundle=new Bundle();
	ListFragItem child=(ListFragItem) listview.getItemAtPosition(position);
	bundle.putInt("id",child.getDatabaseId());
	fr.setArguments(bundle);
 FragmentTransaction ft=getFragmentManager().beginTransaction();
   ft.replace(R.id.main_layout, fr);
   ft.addToBackStack(null);
   ft.commit();
	}else {
	//	 Log.d("onListItemClick","cab active");
	
	}
 
}



	
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
	
		 inflater.inflate(R.menu.main_for_ginger, menu);
		
		
	 }
 @Override
public void onPrepareOptionsMenu(Menu menu) {
	super.onPrepareOptionsMenu(menu);
	//Log.d("LF","in onPrepareOptionsMenu");
	if(delete) {
		menu.clear();
	getActivity().getMenuInflater().inflate(R.menu.del_main_ginger,menu);
	
      }
}


public void onCreateContextMenu(ContextMenu menu,View view,ContextMenuInfo info) {
	 super.onCreateContextMenu(menu, view, info);
  getActivity().getMenuInflater().inflate(R.menu.context, menu);
 
	
} 

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId()) {
	case R.id.deleteGingerMain:
		
		//  Log.d("LFOptionsitem","del");
		  delete=true;
		  listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		 // list.clear();
		  //Log.d("createOptions menu","delete");
		  deleteMsg=true;
		  
		  getActivity().supportInvalidateOptionsMenu();
		   new GetMessages().execute();
		   break;
	case R.id.donelf:
		// Log.d("LFOptionsitem","done");
		 Toast.makeText(contextMsg, "done",Toast.LENGTH_SHORT).show();
		 delete=false;
			//Log.d("count",listitems.size()+"");
			for(int i=0;i<listitems.size();i++) {
				if(ada.mCheckedStates.get(i)){
				
				//	Log.d(i+"",1+"");
					ListFragItem delItem=(ListFragItem) listview.getItemAtPosition(i);
					
					//Log.d(i+"",delItem.getDatabaseId()+"");
					try {
						dbaMsg.open();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dbaMsg.deleteMessage(delItem.getDatabaseId());
					dbaMsg.close();
				}
			}
			listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			 getActivity().supportInvalidateOptionsMenu();
			msgs=new GetMessages();
			msgs.execute();
			
		    break;
	case R.id.cancellf:
		 //Log.d("LFOptionsitem","cancel");
		 delete=false;
		 getActivity().supportInvalidateOptionsMenu();
		 msgs=new GetMessages();
		 msgs.execute();
			 break;
			 
	case R.id.sortGingerMain:
		//Log.d("in Lf","id sortMain");
		dialog="sort";
		new ShowDialog().show(getFragmentManager(), "Sort by");
		 break;			 
		} 
	return true;
}
	
   public void onDestroy() {
	   super.onDestroy();
	   
   }
class ShowDialog extends DialogFragment {
	   @Override
	public Dialog onCreateDialog(Bundle savedInstanceSate) {
		  
			if(dialog.equals("sort")) {
			//	Log.d("in SortDialog","onCreateDialog");
				return new AlertDialog.Builder(contextMsg)
				   .setTitle("Sort Options")
				   .setItems(R.array.sort_opts_arr, new  DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch(which) {
							case 0:
								sortOrder = "_id ASC";
				//				Log.d("OCD",sortOrder);
							
								break;
							case 1:
								sortOrder = "_id DESC";
					//			Log.d("OCD",sortOrder);
								break;				
							}
							msgs.cancel(true);
					msgs=new GetMessages();
					msgs.execute();
						}

						
					}).create();
			}
			return null;
   }
 }
}