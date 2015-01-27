package com.example.geomarketv3_asynctask;

import java.util.Map;

import com.example.geomarketv3_uilogic.SetLocActivity;
import com.example.geomarketv3_uilogic.ViewSalesActivity;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class User_Login extends AsyncTask<Object, Object, Object> {
	private ProgressDialog dialog;
	private Activity activity;
	private Firebase ref;
	private String email, password;
	private String userid;
	public User_Login(Activity activity, String email, String password){
		this.activity = activity;
		this.email = email;
		this.password = password;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
			
			@Override
			public void onAuthenticationError(FirebaseError error) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.out.println(error);
			}
			
			@Override
			public void onAuthenticated(AuthData authdata) {
				// TODO Auto-generated method stub
				userid = authdata.getUid();
				
				String url = "https://mmarketnyp.firebaseio.com/user/";
				ref = new Firebase(url);
				ref.addListenerForSingleValueEvent(new ValueEventListener(){

					@Override
					public void onCancelled(FirebaseError arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onDataChange(DataSnapshot snapshot) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
						Map<String, Object> usersMaps = (Map<String, Object>) snapshot.getValue();
						
						for(String i : usersMaps.keySet()){
							
							if(i.equals(userid)){
								Map<String, Object> user = (Map<String, Object>) usersMaps.get(i);
								
								if(user.get("role").toString().equals("sales")){
									Intent intent = new Intent(activity, SetLocActivity.class);
									intent.putExtra("userid", userid);
									activity.startActivity(intent);
									activity.finish();
								}else{
									Intent intent = new Intent(activity, ViewSalesActivity.class);
									intent.putExtra("userid", userid);
									activity.startActivity(intent);
									activity.finish();
								}
							}
						}
					}
					
				});
				
			}
		});
		return userid;
	}


	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		Firebase.setAndroidContext(activity);
		ref = new Firebase("https://mmarketnyp.firebaseio.com/");
		dialog = ProgressDialog.show(activity, "logging in."," Please wait....", true);
	}
	
	

}
