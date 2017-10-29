package com.tech9pedia.dontbother;




import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.tech9pedia.dontbother.R;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class About extends Fragment{
	 View v;
	 String tag="help";
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	
		   v= inflater.inflate(R.layout.about,container,false);
		   AdView mAdView=(AdView)v.findViewById(R.id.adView);
		   AdRequest adRequest=new Builder().build();
		   mAdView.loadAd(adRequest);
		   return v;
    }
		   
	

 }




