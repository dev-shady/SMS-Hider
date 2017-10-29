package com.tech9pedia.dontbother;

public class AddedContactsItem {
	
	private String textContact;
	//private String num;
	private String dateContact;
	private int idContact;
	private boolean checkedContact=false;
	public AddedContactsItem(){}

	public AddedContactsItem(String text, String date,int idd){
		//this.num = num;
		this.textContact = text;
		this.dateContact=date;
		this.idContact=idd;
		//Log.d("lfItem",this.num);
	}
	
	public String getTextContact(){
		return this.textContact;
	}
	

	public String getDateContact(){
		return this.dateContact;
	}
	public int getDatabaseIdContact() {
		return idContact;
	}
	public boolean isCheckedContact() {
		return checkedContact;
	}
	public void setCheckedContact(boolean checked) {
		this.checkedContact=checked;
	}
	public void setTextContact(String title){
		this.textContact = title;
	}
	
	public void setDateContact(String date){
		this.dateContact =date;
	}
	
}
