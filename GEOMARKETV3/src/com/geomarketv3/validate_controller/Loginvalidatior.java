package com.geomarketv3.validate_controller;

import java.util.ArrayList;

import android.content.Intent;

import com.example.geomarketv3_asynctask.User_Login;
import com.example.geomarketv3_uilogic.Login;
import com.geomarketv3.crouton.Crouton;
import com.geomarketv3.crouton.Style;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;

public class Loginvalidatior {
	private Login activity;
	
	public Loginvalidatior( Login activity){
		this.activity = activity;
	}
	
	public void validateForm(Intent intent, Form mForm, ArrayList<Validate> validatorsArrList){
		if(mForm.validate()){
			User_Login login = new User_Login(activity, activity.getEmailET().getText().toString(), activity.getPasswordET().getText().toString());
			login.execute();
		}else{
			if(!validatorsArrList.get(0).isValid()){
				Crouton crouton = Crouton.makeText(activity,"Please fill in the details.",Style.ALERT);
				crouton.show();
			}
		}
	}
}
