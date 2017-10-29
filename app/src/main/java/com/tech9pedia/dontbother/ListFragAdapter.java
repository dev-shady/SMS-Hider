package com.tech9pedia.dontbother;


import java.util.ArrayList;

import com.tech9pedia.dontbother.R;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ListFragAdapter extends BaseAdapter implements OnCheckedChangeListener {
	
	private Context context;
	SparseBooleanArray mCheckedStates;
	
	private ArrayList<ListFragItem> listFragItems;
	private boolean checkbox=false;
	public ListFragAdapter(Context context, ArrayList<ListFragItem> items,boolean box){
		this.context = context;
		this.listFragItems=items;
		this.checkbox=box;
		mCheckedStates=new SparseBooleanArray(listFragItems.size());
		//Log.d("lfAdapter","constructor");
	}

	@Override
	public Object getItem(int position) {		
		return listFragItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  //Log.d("ListFragAdapter","getview");
	        ListFragItemHolder holder=null;
	        
		if (convertView == null || checkbox) {
			//Log.d("LFA","b4 inflate lf data");
			 LayoutInflater mInflater = (LayoutInflater)
	         context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			 convertView = mInflater.inflate(R.layout.list_frag_data, null);
			 holder=new ListFragItemHolder();
			  holder.textviewmsg=(TextView) convertView.findViewById(R.id.text);
	           
			  holder.textview=(TextView) convertView.findViewById(R.id.date);
	            
	             holder.checkSelect=(CheckBox)convertView.findViewById(R.id.checkbox);
	           
	             convertView.setTag(holder);
        	//Log.d("LFA","b4 inflate lf data");
           
        	//Log.d("LFA","b4 inflate lf data");
        	
            
           
        }else {
        	holder=(ListFragItemHolder) convertView.getTag();
        }
         
        
       
        if(checkbox) {
          holder.checkSelect.setVisibility(View.VISIBLE);
        }else {
        	 holder.checkSelect.setVisibility(View.INVISIBLE);
        }
       
        
		
        try{
       // ListFragItem rowItem=listFragItems.get(position);      
        holder.textviewmsg.setText(listFragItems.get(position).getText());
      holder.textview.setText(listFragItems.get(position).getDate());
        
      holder.checkSelect.setChecked(true);
      holder.checkSelect.setTag(position);
      holder.checkSelect.setChecked(mCheckedStates.get(position, false));
      holder.checkSelect.setOnCheckedChangeListener(this);
        }catch(ArrayIndexOutOfBoundsException e) {
        	e.printStackTrace();
        }
     //Log.d("LFA","getView finished");
        return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listFragItems.size();
	}
	public boolean isChecked(int position) {
		return mCheckedStates.get(position,false);
	}
	public void setChecked(int position,boolean isChecked) {
		 mCheckedStates.put(position,false);
		
	}
	static class ListFragItemHolder {
		TextView textview;
		CheckBox checkSelect;
		TextView textviewmsg;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		mCheckedStates.put((Integer) buttonView.getTag(),isChecked);
	}

	
	
}
