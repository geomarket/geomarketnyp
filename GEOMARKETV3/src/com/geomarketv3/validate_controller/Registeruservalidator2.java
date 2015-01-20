package com.geomarketv3.validate_controller;

import java.util.ArrayList;

import android.content.Intent;

import com.example.geomarketv3_asynctask.Register_User;
import com.example.geomarketv3_uilogic.RegisterUser2;
import com.geomarketv3.crouton.Crouton;
import com.geomarketv3.crouton.Style;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;


public class Registeruservalidator2 {
	private RegisterUser2 activity;

	public Registeruservalidator2(RegisterUser2 activity) {
		this.activity = activity;
	}
	
public void validateForm(Intent intent , Form mForm,ArrayList<Validate> validatorsArrList ){
		
		if(mForm.validate()){
			Register_User register = new Register_User(activity,activity.getEmail().toString(), activity.getPWD().toString(), activity.getCompanyET().toString(), activity.getContactET().toString(), activity.getNameET().toString(), activity.getRole(), activity.getTitle().toString());
			register.execute();
		}else{
			if(!validatorsArrList.get(0).isValid()){
				Crouton crouton = Crouton.makeText(activity,"Please fill in the details.",Style.ALERT);
				crouton.show();
			}
		}
	}
}
