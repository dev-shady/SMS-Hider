package com.tech9pedia.dontbother;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tech9pedia.dontbother.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity  {
	 private DrawerLayout mDrawerLayout;
	    private ListView mDrawerList;
	    private ActionBarDrawerToggle mDrawerToggle;
	  private FragmentTransaction ft;
	  String startDate=null;
	  String endDate=null;
	// slide menu items
		private String[] navMenuTitles;
		private TypedArray navMenuIcons;
       private Context context;
		private ArrayList<NavDrawerItem> navDrawerItems;
		private NavDrawerListAdapter adapter;
		
	   @Override
 protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);  
   //     Log.d("main","before setContentView");
	  // 
        context=this;
        setContentView(R.layout.activity_main);
     //   Log.d("main","after setContentView");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
     
         mDrawerList = (ListView) findViewById(R.id.left_drawer);
      // load slide menu items
 		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

 		// nav drawer icons from resources
 		navMenuIcons = getResources()
 				.obtainTypedArray(R.array.nav_drawer_icons);

         // set a custom shadow that overlays the main content when the drawer opens
       //  mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
         // set up the drawer's list view with items and click listener
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,GravityCompat.START);
        //  mDrawerLayout.setScrimColor(Color.TRANSPARENT);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		
		// Recycle the typed array
		navMenuIcons.recycle();

		// setting the nav drawer list adapter
				adapter = new NavDrawerListAdapter(getApplicationContext(),
						navDrawerItems);
				
				mDrawerList.setAdapter(adapter);
       
         mDrawerList.setOnItemClickListener(new DrawerItemClickListener()); 
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setHomeButtonEnabled(true);
       //  //$
         mDrawerList.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#81a3d0")));//$ also in xml
       // Log.d("main","ok till here");
        
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.drawable.ic_action_drawer,
        		                                    R.string.drawer_open,R.string.app_name) 
        
        {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(R.string.app_name);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.drawer_open);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
      ft=getSupportFragmentManager().beginTransaction();
      //Log.d("main","b4 replace main  with ConversationList");
     if(android.os.Build.VERSION.SDK_INT >android.os.Build.VERSION_CODES.HONEYCOMB) {
	     ft.replace(R.id.main_layout, new ConversationList());
      }else {
    	  //Log.d("main","in api <11");
    	//  Log.d("main","in api <11");
    	//  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B0C4DE")));
      ft.replace(R.id.main_layout, new ConversationList_ginger());
      }
    
	     
	     ft.commit();
	    
	    
	   }
	   @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	         // The action bar home/up action should open or close the drawer.
	         // ActionBarDrawerToggle will take care of this.
	        if (mDrawerToggle.onOptionsItemSelected(item)) {
	            return true;
	        }
	        // Handle action bar buttons
	        
	            return super.onOptionsItemSelected(item);
	        
	    }
	   @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);
	        // Sync the toggle state after onRestoreInstanceState has occurred.
	        //Log.d("main","in onPostCreate");
	        mDrawerToggle.syncState();
	    }
	   @Override
	   public void onConfigurationChanged(Configuration newConfig) {
		   super.onConfigurationChanged(newConfig);
		  // Log.d("main","in onConfigurationChanged");
		   mDrawerToggle.onConfigurationChanged(newConfig);
	   }
	   
	
	 
	 public void onResume() {

		super.onResume();  
		//Log.d("b4 theme","main");
		 theme();
	   }
	 
	 
 public void onPause() {
     super.onPause();
	  }

	 
 public void onDestroy() {
		 super.onDestroy();
	
 }
 @SuppressWarnings("deprecation")
public void onClick(View v) {
		//Log.d("main","in onClick");
	switch(v.getId()) {
		
	case R.id.startDate:
	case R.id.endDate:		
		showDialog(v.getId());
		
		break;
	
			
		}
	} 
 
 public Dialog onCreateDialog(final int id) {
	// Log.d("main","crateDialog");
	 Calendar cal = Calendar.getInstance();
		DatePickerDialog.OnDateSetListener dateListener =
			    new DatePickerDialog.OnDateSetListener() {
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						
		//				Log.d("main onCreateDialog","date listener");
						SimpleDateFormat sdf=new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
						
						if (id ==R.id.startDate) {
			//				Log.d("onFiltrDialog","startDate id");
						
							
							GregorianCalendar cal=new GregorianCalendar(year,monthOfYear,dayOfMonth);
							Date startdate=cal.getTime();
							startDate=sdf.format(startdate);
							
							ConversationList.updateDate(startDate, endDate);
							ConversationList_ginger.updateDate(startDate, endDate);
							//criteriaStartDt = txt;
							//((TextView)filterDialog.findViewById(R.id.textView1)).setText(criteriaStartDt);
						} else if (id ==R.id.endDate) {
				//			Log.d("onFiltrDialog","endDate id");
							GregorianCalendar cal1=new GregorianCalendar(year,monthOfYear,dayOfMonth);
							Date enddate=cal1.getTime();
							endDate=sdf.format(enddate);
					//		Log.d("endDate",endDate);
							 ConversationList.updateDate(startDate, endDate);
							 ConversationList_ginger.updateDate(startDate, endDate);
							//criteriaEndDt = txt;
							//((TextView)filterDialog.findViewById(R.id.textView2)).setText(criteriaEndDt);
						}
					}
				};
			return new DatePickerDialog(this, dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
			
	 
 }
 /* The click listner for ListView in the navigation drawer */
 private class DrawerItemClickListener implements ListView.OnItemClickListener {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//Log.d("main","in drawerList click listener");
		switch(position) {
		case 0:
			//Log.d("main",position+"");
			//home
			Intent home=new Intent("com.tech9pedia.Main");
			 home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		     mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList);
		     startActivity(home);
		     break;
		case 1:   
			//Log.d("main",position+"");
			 ft=getSupportFragmentManager().beginTransaction();
		     ft.replace(R.id.main_layout, new AddContactForInterface());
		     ft.addToBackStack(null);
		     ft.commit();
		     mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList);
		     break;
		case 2:
			//Log.d("main",position+"");
			  ft=getSupportFragmentManager().beginTransaction();
		     ft.replace(R.id.main_layout, new AddedContacts());
		     ft.addToBackStack(null);
		     ft.commit();
		     mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList);
		     
		     break;
		case 3:
			 Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/*");// text/plain
				shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey,I found this cool app,SMS Hider on play store.It hides and stors messages from other numbers(even promotional) \n  Check this Out on play store. ");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SMS Hider"); // TODO
				mDrawerLayout.closeDrawer(mDrawerList);
				startActivity(Intent.createChooser(shareIntent, "Share app via"));
				
				break;	
		case 4:
			
			//Log.d("main",position+"");
		  Intent setting=new Intent("com.tech9pedia.Settings");
			startActivity(setting);
			 mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList);
		     break;
		     //diplayUserSettings();
       case 5:
			
			//Log.d("main",position+"");
			 ft=getSupportFragmentManager().beginTransaction();
		     ft.replace(R.id.main_layout, new About());
		     ft.addToBackStack(null);
		     ft.commit();
		     mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList); 
		    break;
		    
       case 6:
    	   //Log.d("main",position+"");
		     mDrawerList.setItemChecked(position, true);
		     mDrawerLayout.closeDrawer(mDrawerList); 
		     DialogInterface.OnClickListener dcl=new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				switch(which) {
				case DialogInterface.BUTTON_POSITIVE:
					System.exit(0);
					break;
					
				case DialogInterface.BUTTON_NEGATIVE:
					
					break;
				}
				}
			};
			AlertDialog.Builder builder=new AlertDialog.Builder(context);
			builder.setMessage("Are you sure ?").setPositiveButton("ok", dcl)
			.setNegativeButton("cancel",dcl).show();
			
			
		    break;
		}
	}
     
 }

 public void theme() {
	 SharedPreferences sharedPrefs=PreferenceManager.getDefaultSharedPreferences(this);
	String font=sharedPrefs.getString("fontSizePref","medium");
	//Log.d("theme", font);
	switch(font.charAt(0)) {
	case '0'    :
	case 's':
		 setTheme(R.style.FontSizeSmall);
		//	Log.d("in theme", font);
		 break;
	case '1':	 
	case 'm':
		setTheme(R.style.FontSizeMedium);
		//Log.d("in theme", font);
		break;
	case '2':	
	case 'l':	
		 setTheme(R.style.FontSizeLarge);
		 //Log.d("in theme", font);
		 break;
	}	 
 }
 
  
 
  }

