package com.example.geomarketv3_asynctask;

import java.io.IOException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.Url;
import com.example.geomarketv3.R;
import com.example.geomarketv3_fragment.SalesMemberFragment;
import com.example.geomarketv3_uilogic.ViewSalesActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class GetSaleMemberDetail extends AsyncTask<Object, Object, Object>{
	private FragmentActivity activity;
	private String name;
	private Firebase ref;
	private ProgressDialog dialog;
	private Cloudinary cloudinary;
	private ImageView imgIV;
	private String imgurl;
	private Bitmap image;
	private String url = "https://mmarketnyp.firebaseio.com/";
	
	public GetSaleMemberDetail(FragmentActivity activity, String name){
		this.activity = activity;
		this.name = name;
	}
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		ref.addValueEventListener(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError error) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, error.toString(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				
				
				Map<String, Object> usersMaps = (Map<String, Object>) snapshot.getValue();
				
				
				
				for(String i : usersMaps.keySet()){
					
					Map<String, Object> userMap = (Map<String, Object>) usersMaps.get(i);
					
					if(userMap.get("role").toString().equals("sales")){
						if(userMap.get("title").toString().equals(name)){
								imgurl = cloudinary.url().format("jpg").transformation(new Transformation().width(1800).crop("fit")).generate(i);
								
								GetImage getImg = new GetImage();
								getImg.imgurl = imgurl;
								getImg.execute();	
								ViewSalesActivity.user.setTitle(userMap.get("title").toString());
								
								ViewSalesActivity.user.setName(userMap.get("name").toString());
								ViewSalesActivity.user.setEmail(userMap.get("email").toString());
								ViewSalesActivity.user.setContact(Integer.parseInt(userMap.get("contact").toString()));
						}
					}
				}
				
				
			}
			
		});
		dialog.dismiss();
		return null;
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(activity,"Retrieving details", "Please wait...", true);
		ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
	}
	
	
	@Override
	protected void onPostExecute(Object result) {
		ViewSalesActivity.setUser();
	}

	


}
