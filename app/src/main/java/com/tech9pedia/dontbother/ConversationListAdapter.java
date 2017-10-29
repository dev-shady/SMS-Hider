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

public class ConversationListAdapter extends BaseAdapter implements OnCheckedChangeListener {
	
	private Context contextConversation;
	SparseBooleanArray mCheckedStatesConversation;
	
	private ArrayList<ConversationsListItem> conversationItems;;
	private boolean boxVisible=false;
	
	public ConversationListAdapter(Context context, ArrayList<ConversationsListItem> items,boolean box){
		this.contextConversation = context;
		this.conversationItems=items;
		
		this.boxVisible=box;
		mCheckedStatesConversation=new SparseBooleanArray(conversationItems.size());
		
	}

	@Override
	public Object getItem(int position) {		
		return conversationItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		  //Log.d("CAdapter","getview");
		  ConversationsItemHolder holderConversation=null;
	        
		if (convertView == null || boxVisible) {
			//Log.d("CA","b4 inflate CLA data");
			 LayoutInflater mInflater = (LayoutInflater)
	         contextConversation.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			 convertView = mInflater.inflate(R.layout.conversation_list_item,null);
			 holderConversation=new ConversationsItemHolder();
			  holderConversation.conversation=(TextView) convertView.findViewById(R.id.nameConversation);
	           
			  holderConversation.dateConversation=(TextView) convertView.findViewById(R.id.dateConversation);
	            
	           holderConversation.checkSelectConversation=(CheckBox)convertView.findViewById(R.id.checkboxConversation);
	           holderConversation.msgConversation=(TextView)convertView.findViewById(R.id.textConversation);
	           
	             convertView.setTag(holderConversation);
        	//Log.d("CA","b4 inflate lf data");
           
        	
        	
            
           
        }else {
        	holderConversation=(ConversationsItemHolder) convertView.getTag();
        }
         
        
       
        if(boxVisible) {
       // 	Log.d("AddedContactsAdapter","checkbox visible");
          holderConversation.checkSelectConversation.setVisibility(View.VISIBLE);
        }else {
        	 holderConversation.checkSelectConversation.setVisibility(View.INVISIBLE);
        }
       
        
		
        try{
      //  ConversationsListItem rowItem=conversationItems.get(position);      
        holderConversation.conversation.setText(conversationItems.get(position).getTextConversation());
        holderConversation.msgConversation.setText(conversationItems.get(position).getMsgConversation());
      holderConversation.dateConversation.setText(conversationItems.get(position).getDateConversation());
      holderConversation.checkSelectConversation.setChecked(true); //$
      holderConversation.checkSelectConversation.setTag(position);
      holderConversation.checkSelectConversation.setChecked(mCheckedStatesConversation.get(position, false));
      holderConversation.checkSelectConversation.setOnCheckedChangeListener(this);
        }catch(ArrayIndexOutOfBoundsException e) {
        	e.printStackTrace();
        }
     //Log.d("CA","getView finished");
        return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return conversationItems.size();
	}
	public boolean isCheckedContact(int position) {
		return mCheckedStatesConversation.get(position,false);
	}
	public void setCheckedContact(int position,boolean isChecked) {
		 mCheckedStatesConversation.put(position,false);
		
	}
	static class ConversationsItemHolder {
		TextView conversation;
		CheckBox checkSelectConversation;
		TextView dateConversation;
		TextView msgConversation;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		mCheckedStatesConversation.put((Integer) buttonView.getTag(),isChecked);
	}

	

	
	
}
