package com.tech9pedia.dontbother;

import java.lang.reflect.InvocationTargetException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	private final static String KEY_MSGROWID="_id";
	private final static  String KEY_MSGSMS="sms";
	private final static String KEY_MSGCONTACT="contact";
	private final static String KEY_MSGTIME="time";
	
	private final static String KEY_CONTACTSROWID="_id";
	private final static String KEY_CONTACTSCONTACT="contact";
	private final static String KEY_CONTACTSTIME="time";
	
	//private final static String TAG="DBAdapter";
	
	private final static String DATABASE_NAME="DontBotherDatabase";
	private final static String DATABASE_MSGTABLE="messages";
	private final static String DATABASE_CONTACTSTABLE="contacts";
	private final static int DATABASE_VERSION=1;

	private final static String DATABASE_MSGCREATE=
			"create table messages (_id Integer primary key autoincrement,"+"sms String not null,contact String not null,time String not null);";
	private final static String DATABASE_CONTACTSCREATE=
			"create table contacts (_id Integer primary key autoincrement,"+"contact String not null,time String not null);";
	final Context context;
	DatabaseHelper DBHelper; 
	SQLiteDatabase db;
	public DBAdapter(Context context) {
		//Log.d("DBAdapter","in DBAdapter constructor");
		this.context=context;
		DBHelper=new DatabaseHelper(context);
	}
	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			//Log.d("DBAdapter","in DatabaseHelper constructor");
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			//Log.w(TAG,"Upgrading database from "+oldVersion+" to "+newVersion+" which will destroy all data");
			db.execSQL("DROP TABLE IF EXISTS messages");
			db.execSQL("DROP TABLE IF EXISTS contacts");
			onCreate(db);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try {
				db.execSQL(DATABASE_MSGCREATE);
				db.execSQL(DATABASE_CONTACTSCREATE);
				//Log.d("DBAdapter","in onCreate()");
				}catch(SQLException e) {
					e.printStackTrace();
				}
		}
	}
	public DBAdapter open() throws SQLException, InvocationTargetException {
		//Log.d("DBAdapter","in open()");
		db=DBHelper.getWritableDatabase();
		//Log.d("DBAdapter","after messagesgetWritableDatabase()");
		return this;
	}

	public void close() {
		//Log.d("DBAdapter","in close()");
		DBHelper.close();
	}

	public long insertMessage(String sms,String contact,String time) {
		//Log.d("DBAdapter","in insertMessage()");
		ContentValues initialValues=new ContentValues();
		initialValues.put(KEY_MSGSMS, sms);
		initialValues.put(KEY_MSGCONTACT, contact);
		initialValues.put(KEY_MSGTIME,time);
		return db.insert(DATABASE_MSGTABLE, null, initialValues);
	}
	public boolean deleteMessage(int rowId) {
		//Log.d("DBAdapter",rowId+" id in delete message");
		return db.delete(DATABASE_MSGTABLE, KEY_MSGROWID + "=" + rowId, null)>0;
	}
	public boolean deleteMessageUsingContact(String cont) {
		//Log.d("DBAdapter",cont+" in delete message");
		return db.delete(DATABASE_MSGTABLE, KEY_MSGCONTACT + "=" +"'"+cont+"'", null)>0;
	}
	
	public Cursor getAllMessages(String projection,String sortOrder,String groupBy) {
		//Log.d("DBAdapter","in getAllMessags");
		Cursor cur= db.query(DATABASE_MSGTABLE, new String[] {KEY_MSGROWID, KEY_MSGSMS,KEY_MSGCONTACT, KEY_MSGTIME },projection,null,groupBy,null,sortOrder);
	    
	    return cur;
	}
	public Cursor getDistincMessages(String projection,String sortOrder) {
		//Log.d("DBAdapter","in getDistinctMessags");
		Cursor cur= db.query(true,DATABASE_MSGTABLE, new String[] {KEY_MSGROWID, KEY_MSGSMS,KEY_MSGCONTACT, KEY_MSGTIME },projection,null,KEY_MSGCONTACT,null,sortOrder,null);
	    
	    return cur;
	}
	public Cursor sortingBySender(String sender) {
		//Log.d("DBAdapter","in getAllMessags");
		Cursor cur= db.query(DATABASE_MSGTABLE, new String[] {KEY_MSGROWID, KEY_MSGSMS,KEY_MSGCONTACT, KEY_MSGTIME },"contact=?",new String[] {sender},null,null,null,"_id DESC");
	    
	    return cur;
	}
	public Cursor getAllMessagesSorted() {
		//Log.d("DBAdapter","in getAllMessags");
		Cursor cur= db.rawQuery("SELECT _id, sms, contact "+
				"FROM messages ORDER BY _id DESC",
				null);
	   
	    return cur;
	}
	public Cursor getMessage(int rowId) throws SQLException {
		Cursor mCursor=db.query(true,DATABASE_MSGTABLE, new String[] {KEY_MSGROWID,KEY_MSGSMS,KEY_MSGCONTACT, KEY_MSGTIME},KEY_MSGROWID+"="+rowId,null,null,null,null,null);
		if(mCursor!=null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor getMessage(String contact) throws SQLException {
		Cursor mCursor=db.query(true,DATABASE_MSGTABLE, new String[] {KEY_MSGROWID,KEY_MSGSMS,KEY_MSGCONTACT,KEY_MSGTIME},KEY_MSGCONTACT+"="+contact,null,null,null,null,null);
		if(mCursor!=null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public long insertContact(String contact,String time) {
		//Log.d("DBAdapter","in insertContacts()");
		ContentValues initialValues=new ContentValues();
		initialValues.put(KEY_CONTACTSCONTACT, contact);
		initialValues.put(KEY_CONTACTSTIME, time);
		//initialValues.put(KEY_CONTACTSTIME, time);
		return db.insert(DATABASE_CONTACTSTABLE, null, initialValues);
	}
	public boolean deleteContact(int rowId) {
		return db.delete(DATABASE_CONTACTSTABLE, KEY_CONTACTSROWID + "=" + rowId, null)>0;
	}
	public Cursor getAllContacts() {
	//	Log.d("DBAdapter","in getAllContacts");
		Cursor cur= db.query(DATABASE_CONTACTSTABLE, new String[] {KEY_CONTACTSROWID,KEY_CONTACTSCONTACT,KEY_CONTACTSTIME},null,null,null,null,null);
	    //if(cur.moveToFirst()) {
	    //	Log.d("DBAdapter","movetoFirst exists");
	    
	       return cur;
   }
	public Cursor getContact(int rowId) throws SQLException {
		Cursor mCursor=db.query(true,DATABASE_CONTACTSTABLE, new String[] {KEY_CONTACTSROWID,KEY_CONTACTSCONTACT,KEY_CONTACTSTIME},KEY_CONTACTSROWID+"="+rowId,null,null,null,null,null);
		
		return mCursor;
	}
	
}	
	
	
