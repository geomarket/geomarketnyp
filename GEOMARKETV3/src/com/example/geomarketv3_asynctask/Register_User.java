package com.example.geomarketv3_asynctask;

import java.util.HashMap;
import java.util.Map;

import com.example.geomarketv3_uilogic.Login;
import com.example.geomarketv3_uilogic.SetLocActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class Register_User extends AsyncTask<Object, Object, Object>{
	private ProgressDialog dialog;
	private Activity activity;
	private Firebase ref;
	private String email, password, company, contact, name, role, title;
	
	public Register_User(Activity activity, String email, String password,
			String company, String contact, String name, String role,
			String title) {
		super();
		this.activity = activity;
		this.email = email;
		this.password = password;
		this.company = company;
		this.contact = contact;
		this.name = name;
		this.role = role;
		this.title = title;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		createUser();
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		dialog.dismiss();
		Intent intent = new Intent(activity, Login.class);
		activity.startActivity(intent);
		activity.finish();
	}

	@Override
	protected void onPreExecute() {
		Firebase.setAndroidContext(activity);
		ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
		dialog = ProgressDialog.show(activity, "logging in."," Please wait....", true);
	}
	
	private void createUser(){
		ref.createUser(email, password, new Firebase.ResultHandler() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
					
					@Override
					public void onAuthenticationError(FirebaseError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(activity, arg0.toString(), Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onAuthenticated(AuthData snapshot) {
						// TODO Auto-generated method stub
						
						Firebase createuserref = ref.child(snapshot.getUid());
						Map<String, Object> createUser = new HashMap<String, Object>();
						createUser.put("company", company);
						createUser.put("contact", contact);
						createUser.put("email", email);
						createUser.put("name", name);
						createUser.put("role", role);
						createUser.put("title", title);
						createuserref.updateChildren(createUser);
					}
				});
			}
			
			@Override
			public void onError(FirebaseError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(activity, arg0.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
