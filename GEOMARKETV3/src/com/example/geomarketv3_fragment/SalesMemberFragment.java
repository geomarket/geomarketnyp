package com.example.geomarketv3_fragment;

import java.nio.charset.CharsetEncoder;

import com.example.geomarketv3.R;


import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetSaleMemberDetail;


import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SalesMemberFragment extends Fragment {
	private RelativeLayout rl1 , rlp, rlg, rlIM;
	private TextView nameTV, emailTV, contactTV;
	private String saleID;
	private ImageView imgView;
	public static Bitmap bitImage = null;
	public static SalesMemberFragment activity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.activity_sales_member_fragment, container, false);
		rootView.setBackgroundColor(Color.TRANSPARENT);
		activity = this;
		rl1 = (RelativeLayout) rootView.findViewById(R.id.rlIM);
		rl1.setBackgroundColor(Color.LTGRAY);
		rlp = (RelativeLayout) rootView.findViewById(R.id.rlPersonal);
		rlp.setBackgroundColor(Color.LTGRAY);
		rlg = (RelativeLayout) rootView.findViewById(R.id.rlG);
		rlg.setBackgroundColor(Color.LTGRAY);
		loadSavedPreferences();
		imgView = (ImageView) rootView.findViewById(R.id.imgIV);
		imgView.setBackgroundColor(Color.TRANSPARENT);
		nameTV = (TextView) rootView.findViewById(R.id.nameTV);
		nameTV.setText("\u2022 name");
		emailTV = (TextView) rootView.findViewById(R.id.emailTV);
		emailTV.setText("\u2022 email");
		contactTV = (TextView) rootView.findViewById(R.id.contactTV);
		contactTV.setText("\u2022 email");
		GetSaleMemberDetail getMem = new GetSaleMemberDetail(this.getActivity(),saleID);
		getMem.execute();
		
		return rootView;
	}

	private void loadSavedPreferences(){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		saleID = sp.getString("salesid", "sale name not found");
	}
	
	public void setImage(){
		imgView.setImageBitmap(bitImage);
	}
}
