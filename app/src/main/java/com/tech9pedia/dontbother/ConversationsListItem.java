package com.tech9pedia.dontbother;


public class ConversationsListItem {
	
	private String textConversation;
	private String num;
	private String dateConversation;
	private String msgConversation;
	private int idConversation;
	private boolean checkedConversation=false;
	
//	private String count = "0";
	// boolean to set visiblity of the counter
	//private boolean isCounterVisible = false;
	
	public ConversationsListItem(){}

	public ConversationsListItem(String text,String num,String msg, String date,int idd){
		this.textConversation = text;
		this.num = num;
		this.dateConversation=date;
		this.idConversation=idd;
		this.msgConversation=msg;
		//Log.d("lfItem",this.num);
	}
	
	/*public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}*/
	
	public String getTextConversation(){
		return this.textConversation;
	}
	

	public String getDateConversation(){
		return this.dateConversation;
	}
	public int getDatabaseIdConversation() {
		return idConversation;
	}
	public String getMsgConversation() {
		return msgConversation;
	}
	public boolean isCheckedConversation() {
		return checkedConversation;
	}
	public String getNum(){
		return num;
	}
	/*
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}*/
	public void setCheckedContact(boolean checked) {
		this.checkedConversation=checked;
	}
	public void setTextConversation(String title){
		this.textConversation = title;
	}
	
	
	public void setDateConversation(String date){
		this.dateConversation =date;
	}
	
	public void setNum(String nm){
		this.num = nm;
	}/*
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}*/
}
