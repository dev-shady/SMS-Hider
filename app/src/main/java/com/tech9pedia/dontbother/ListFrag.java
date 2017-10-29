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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import android.widget.ListView;


public class ListFrag extends ListFragment  {
	private Context contextMsg;
	private DBAdapter dbaMsg;
	ActionBarActivity aba;
	GetMessages msgs;
	ArrayList<String> list=new ArrayList<String>();
	ActionBar actionbar;
	String[] cols1;
	int[] views1;
	//private Boolean delete=false;
	  private Cursor c1; View v;
	  private  ListView listview;
	  private ListFragAdapter ada;
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
	  
	//  private static final String LIST_STATE="listState";
	  private Parcelable mListState=null;
	  
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		
		   v=inflater.inflate(R.layout.list_fragment,container,false) ;
		   listview=(ListView)v.findViewById(android.R.id.list);
					 // registerForContextMenu(listview);
		   AdView mAdView=(AdView)v.findViewById(R.id.adView2);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
					  return v;
		   }
		   
	
public void onActivtyCreated(Bundle state) {
	   super.onActivityCreated(state);
	  //Log.d("ListFrag","onActivityCreated");
	//  mListState=state.getParcelable(LIST_STATE);
	    }
public void onSaveInstanceState(Bundle state) {
	  super.onSaveInstanceState(state);
	//  mListState=getListView().onSaveInstanceState();
	  //state.putParcelable(LIST_STATE, mListState);
	  

}
@SuppressWarnings("deprecation")
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	 setHasOptionsMenu(true);

 } 

public void onPause() {
	 //Log.d("ListFragment","onPause");
	 mListState=listview.onSaveInstanceState();
	 super.onPause();
}

public void onViewCreated(final View view,Bundle state) {
	super.onViewCreated(view, state);
}
@Override
public void onResume() {
	   super.onResume();
	   //Log.d("ListFrag","onResume");
	 /*  if(mListState!=null) {
			  getListView().onRestoreInstanceState(mListState);
			  mListState=null;
			  
		}else {*/
	   contextMsg=getActivity();
	   contactLF=getArguments().getString("contactCL");
	   startDLF=getArguments().getString("startDCL");
	   endDLF=getArguments().getString("endDCL");
	   projectionLF=getArguments().getString("projectionCL");
	  // cols1=new String[] {"smsUI"};
	   listitems=new ArrayList<ListFragItem>();
		// views1=new int[] {android.R.layout.simple_list_item_1};
	  listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	  
	   if(android.os.Build.VERSION.SDK_INT >android.os.Build.VERSION_CODES.HONEYCOMB) {
		   listview.setOnItemLongClickListener(lcListener);
	       listview.setMultiChoiceModeListener(mcListener); }
		 //Log.d("ListFrag","b4 getmessages");
		msgs=new GetMessages();
		 //Log.d("ListFrag","b4 getmessagesExecute");
		 try {
		msgs.execute();
		 }catch(Exception e) {
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
		//Log.d("GetMessages LF","do in background");
		listview.setChoiceMode(ListView.CHOICE_MODE_NONE);
		
		listitems.clear();
		SimpleDateFormat sdf1=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
		SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd hh:mm");
		//Log.d("GetMessages LF","do in background");
		dbaMsg=new DBAdapter(contextMsg);   
		Date d1=null,d2=null;;
		try {
			d1 = sdf1.parse(startDLF);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			d2 = sdf1.parse(endDLF);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(projectionLF==null) 
			projectionLF="contact='"+contactLF+"'";
		else projectionLF+="AND contact='"+contactLF+"'";
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
		//Log.d("donInBackground LF","after getAllMessages");
		
		if(c1.moveToFirst()) {
			//Log.d("donInBackground LF","c1 exists");
			do{
			 String strtext;
			 int idd;
			//String strcont=c1.getString(2);
			 strtext=c1.getString(c1.getColumnIndex("sms"));
			idd=c1.getInt(0);
			//Date dt;
			Date storeddate=null;
			//Log.d("LF",c1.getString(3));
			try {
				storeddate = sdf1.parse(c1.getString(3));
				//Log.d("LF storedate",storeddate.toString());
				if(storeddate.after(d1)&& storeddate.before(d2)) {
					//Log.d("LF startDLF",startDLF.toString());
				//Log.d("donInBackground","b4 adding listitems");
				listitems.add(new ListFragItem(strtext,sdf2.format(storeddate),idd));
				} 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			//Log.d("Postexecute",idd+"");               
			}while(c1.moveToNext());
			
			
				//Log.d("donInBackground","delete not exists");
				ada=new ListFragAdapter(contextMsg,listitems,false);
			
			
			
		   return ada;
		}else {
			//Log.d("donInBackground","c1 not exists");
			return null;
		}
      }	
	
	protected void onPostExecute(ListFragAdapter adaPost) {
		//Log.d("GetMessages","in postExecute");
		//if(c1.moveToFirst() && !ada.isEmpty()) {
	 //   Log.d("GetMessages","c1 not null");
		setListAdapter(ada); 
		if(mListState!=null) {
			// Log.d("LFFFFF","trying to restore");
			 listview.onRestoreInstanceState(mListState);
		}
		ada.notifyDataSetChanged();
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		if(dialog.isShowing()) {
			//Log.d("do in background","in dailog is showing");
			dialog.dismiss();
		}
		//} else {
		//Log.d("GetMessages","c1  null"); 
		dbaMsg.close();
		
		
	}
}	

	
	 

@Override
public void onListItemClick(ListView lv,View v,int position,long id) {
	//Log.d("ListFrag","onListItemClick");
	msgs.cancel(true);
	//Log.d("onListItemClick",position+"");
	if(isCABDestroyed) {
	//	listitems.clear();
		try {
   listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);	
	OnListClick fr=new OnListClick();
	Bundle bundle=new Bundle();
	ListFragItem child=(ListFragItem) listview.getItemAtPosition(position);
	bundle.putInt("id",child.getDatabaseId());
	bundle.putInt("count",listview.getCount());
	fr.setArguments(bundle);
 FragmentTransaction ft=getFragmentManager().beginTransaction();
   ft.replace(R.id.main_layout, fr);
    ft.addToBackStack(null);
   ft.commit();
		}catch(Exception e) {
	  //  	Log.d("ARRAYINDEXOUTOFBOUND","ARRAYINDEXOUTOFBOUND");
	    }
	}else {
		// Log.d("onListItemClick","cab active");
	
	}
 
}
private OnItemLongClickListener lcListener=new OnItemLongClickListener() {
	
	 public boolean onItemLongClick(AdapterView<?> parent,View view,int position,long id) {
		 //Log.d("ListFrag","long click");
		 msgs.cancel(true);
		 listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		 //Log.d("Longclick listener","after choic mode multiple modal");
		// setListAdapter(getListAdapter());
		 isCABDestroyed=false;
		 delId=(int) id;
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
					ListFragItem delItem=(ListFragItem) listview.getItemAtPosition(pos);
					
				
					int id=delItem.getDatabaseId();
			//		Log.d(i+" id pos"+pos,+id+"");
					try {
						dbaMsg.open();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dbaMsg.deleteMessage(id);
					dbaMsg.close();
				}
				}
		//	Log.d("ListFrag","b4 mode finish func");
			mode.finish();
			//Log.d("ListFrag","after mode finish func");
			new GetMessages().execute();
			return false;
		default:
			  
			mode.finish();
			return false;
		
		}
	
	}
	@Override
	public void onDestroyActionMode(android.view.ActionMode mode) {
		// TODO Auto-generated method stub
		 //Log.d("ListFrag","destroy action mode1");
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
			mode.setSubtitle(checkedCount+"selected"); break;
		}
		
	}
	
  };
@Override
public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
	super.onCreateOptionsMenu(menu, inflater);
		 inflater.inflate(R.menu.list_frag_menu, menu);
		
		

}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	switch(item.getItemId()) {

	case R.id.sortGingerMain:
		
		dialog="sort";
		new ShowDialog().show(getFragmentManager(), "Sort by");
		 break;
		 

	} 
	return true;
}



protected Cursor createCursor() {
	// TODO Auto-generated method stub
	return null;
}



   public void onDestroy() {
	   super.onDestroy();
	  
   }
   
   class ShowDialog extends DialogFragment {
	   @Override
	public Dialog onCreateDialog(Bundle savedInstanceSate) {
		  //Log.d("in ShowDialog","onCreateDialog");
		  //Log.d("in ShowDialog",dialog);
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
   
 
