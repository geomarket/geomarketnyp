package com.geomarketv3.validate_controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.example.geomarketv3_asynctask.Register_User;
import com.example.geomarketv3_asynctask.UploadImage;
import com.example.geomarketv3_uilogic.RegisterUser2;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.geomarketv3.crouton.Crouton;
import com.geomarketv3.crouton.Style;
import com.geomarketv3.entity.User;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;


public class Registeruservalidator2 {
	private RegisterUser2 activity;
	private ProgressDialog dialog;
	private Firebase ref;
	private String id;
	private User user;
	private Cloudinary cloudinary;
	
	public Registeruservalidator2(RegisterUser2 activity) {
		this.activity = activity;
	}
	
public void validateForm(Intent intent , Form mForm,ArrayList<Validate> validatorsArrList ){
		
		if(mForm.validate()){
			user = new User();
			user.setEmail(activity.getEmail());
			user.setPwd(activity.getPWD().toString());
			user.setCompany(activity.getCompanyET());
			user.setContact(Integer.parseInt(activity.getContactET()));
			user.setName(activity.getNameET());
			user.setRole(activity.getRole());
			user.setImgURL(activity.getUriOfImage());
			user.setTitle(activity.getTitle().toString());
			
			Register_User register = new Register_User(activity,user);
			register.execute();
		}else{
			if(!validatorsArrList.get(0).isValid()){
				Crouton crouton = Crouton.makeText(activity,"Please fill in the details.",Style.ALERT);
				crouton.show();
			}
		}
	}




}
