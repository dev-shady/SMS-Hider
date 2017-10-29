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

public class AddedContactsAdapter extends BaseAdapter implements OnCheckedChangeListener {
	
	private Context contextContact;
	SparseBooleanArray mCheckedStatesContact;
	
	private ArrayList<AddedContactsItem> addedContactItems;
	private boolean checkboxContact=false;
	
	public AddedContactsAdapter(Context context, ArrayList<AddedContactsItem> items,boolean box){
		this.contextContact = context;
		this.addedContactItems=items;
		
		this.checkboxContact=box;
		mCheckedStatesContact=new SparseBooleanArray(addedContactItems.size());
	}

	@Override
	public Object getItem(int position) {		
		return addedContactItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
	        AddedContactsItemHolder holder=null;
	        
		if (convertView == null || checkboxContact) {
			
			 LayoutInflater mInflater = (LayoutInflater)
	         contextContact.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			 convertView = mInflater.inflate(R.layout.list_frag_data, null);
			 holder=new AddedContactsItemHolder();
			  holder.contact=(TextView) convertView.findViewById(R.id.text);
	           
			  holder.dateContact=(TextView) convertView.findViewById(R.id.date);
	            
	             holder.checkSelectContact=(CheckBox)convertView.findViewById(R.id.checkbox);
	           
	             convertView.setTag(holder);
        	
           
        }else {
        	holder=(AddedContactsItemHolder) convertView.getTag();
        }
         
        
       //sub.setSingleLine(true);
      //  sub.setEllipsize(TruncateAt.END);
        
        
        
	
        if(checkboxContact) {
        	
          holder.checkSelectContact.setVisibility(View.VISIBLE);
        }else {
        	 holder.checkSelectContact.setVisibility(View.INVISIBLE);
        }
       
        
		
        try {
        AddedContactsItem rowItem=addedContactItems.get(position);      
        holder.contact.setText(addedContactItems.get(position).getTextContact());
      holder.dateContact.setText(addedContactItems.get(position).getDateContact());
      holder.checkSelectContact.setChecked(true);
      holder.checkSelectContact.setTag(position);
      holder.checkSelectContact.setChecked(mCheckedStatesContact.get(position, false));
      holder.checkSelectContact.setOnCheckedChangeListener(this);
        }catch(ArrayIndexOutOfBoundsException e) {
        	e.printStackTrace();
        }
    
        return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return addedContactItems.size();
	}
	public boolean isCheckedContact(int position) {
		return mCheckedStatesContact.get(position,false);
	}
	public void setCheckedContact(int position,boolean isChecked) {
		 mCheckedStatesContact.put(position,false);
		
	}
	static class AddedContactsItemHolder {
		TextView contact;
		CheckBox checkSelectContact;
		TextView dateContact;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		mCheckedStatesContact.put((Integer) buttonView.getTag(),isChecked);
	}

	

	
	
}
