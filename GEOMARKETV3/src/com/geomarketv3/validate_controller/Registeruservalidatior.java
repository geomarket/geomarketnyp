package com.geomarketv3.validate_controller;

import java.util.ArrayList;

import android.content.Intent;
import com.example.geomarketv3_uilogic.RegisterUser;
import com.example.geomarketv3_uilogic.RegisterUser2;
import com.geomarketv3.crouton.Crouton;
import com.geomarketv3.crouton.Style;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;

public class Registeruservalidatior {
	private RegisterUser activity;
	
	public Registeruservalidatior(RegisterUser activity){
		this.activity = activity;
	}
	
	public void validateForm(Intent intent, Form mForm, ArrayList<Validate> validatorsArrList){
		if(mForm.validate()){
			intent = new Intent(activity, RegisterUser2.class);
			intent.putExtra("email", activity.getEmailET().getText().toString());
			intent.putExtra("pwd", activity.getPasswordET().getText().toString());
			activity.startActivity(intent);
			activity.finish();
		}else{
			if(!validatorsArrList.get(0).isValid()){
				Crouton crouton = Crouton.makeText(activity,"Please fill in the details.",Style.ALERT);
				crouton.show();
			}
		}
	}
}
