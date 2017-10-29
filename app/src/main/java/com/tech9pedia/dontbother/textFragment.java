package com.tech9pedia.dontbother;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;



public class textFragment extends ListFragment{

	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
		Log.d("textfrag","in onCreateView");
		ArrayAdapter<String> ad=new ArrayAdapter<String>(inflater.getContext(),android.R.layout.simple_list_item_1,new String[] {"a","b"});
		setListAdapter(ad);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

}
