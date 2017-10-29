package com.tech9pedia.dontbother;

public class ListFragItem {
	
	private String text;
	//private String num;
	private String date;
	private int id;
	private boolean checked=false;
	public ListFragItem(){}

	public ListFragItem(String text, String date,int idd){
		//this.num = num;
		this.text = text;
		this.date=date;
		this.id=idd;
		//Log.d("lfItem",this.num);
	}
	
	/*public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}*/
	
	public String getText(){
		return this.text;
	}
	

	public String getDate(){
		return this.date;
	}
	public int getDatabaseId() {
		return id;
	}
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked=checked;
	}
	public void setText(String title){
		this.text = title;
	}
	
	
	public void setDate(String date){
		this.date =date;
	}
	

}
