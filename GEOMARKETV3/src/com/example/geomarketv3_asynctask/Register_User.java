package com.example.geomarketv3_asynctask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.Url;
import com.example.geomarketv3_uilogic.Login;
import com.example.geomarketv3_uilogic.SetLocActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.geomarketv3.entity.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

public class Register_User extends AsyncTask<Object, Object, Object>{
	private ProgressDialog dialog;
	private Activity activity;
	private Firebase ref;
	private User user;

	
	public Register_User(Activity activity, User user) {
		this.activity = activity;
		this.user = user;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		ref.createUser(user.getEmail(), user.getPwd(), new Firebase.ResultHandler() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						ref.authWithPassword(user.getEmail(), user.getPwd(), new Firebase.AuthResultHandler() {
							
							@Override
							public void onAuthenticationError(FirebaseError arg0) {
								// TODO Auto-generated method stub
								Toast.makeText(activity, arg0.toString(), Toast.LENGTH_SHORT).show();
							}
							
							@Override
							public void onAuthenticated(AuthData snapshot) {
								// TODO Auto-generated method stub
								
								Firebase createuserref = ref.child(snapshot.getUid());
								
								user.setId(snapshot.getUid());
								Map<String, Object> createUser = new HashMap<String, Object>();
								createUser.put("company", user.getCompany());
								createUser.put("contact", user.getContact());
								createUser.put("email", user.getEmail());
								createUser.put("name", user.getName());
								createUser.put("role", user.getRole());
								createUser.put("title", user.getTitle());
								createuserref.updateChildren(createUser);
								

									UploadImage uploadimg = new UploadImage(activity, user.getImgURL(), user.getId(), 0);
									uploadimg.execute();
							}
						});
					}
					
					@Override
					public void onError(FirebaseError arg0) {
						// TODO Auto-generated method stub
						Toast.makeText(activity, arg0.toString(), Toast.LENGTH_SHORT).show();
					}
				});
		
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
	

	
	


}
