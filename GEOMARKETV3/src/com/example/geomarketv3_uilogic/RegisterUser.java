package com.example.geomarketv3_uilogic;

import java.util.ArrayList;

import com.example.geomarketv3.R;

import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.geomarketv3.validate_controller.Registeruservalidatior;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;
import com.geomarketv3.validation.validator.EmailValidator;
import com.geomarketv3.validation.validator.NotEmptyValidator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class RegisterUser extends Activity {
	private EditText emailET, passwordET, passwordCFMET;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bg1);
		rLayout.setBackgroundDrawable(drawable);
		emailET = (EditText) findViewById(R.id.emailET);
		emailET.setTextSize(20);
		emailET.setTextColor(getResources().getColor(R.color.abs__background_holo_dark));
		emailET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		passwordET = (EditText) findViewById(R.id.passwordET);
		passwordCFMET = (EditText) findViewById(R.id.passwordCFMET);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		
		case R.id.action_next:
			Form mForm = new Form();
			if(passwordET.getText().toString().equals(passwordCFMET.getText().toString())){
				Validate validEmail = new Validate(emailET);
				Validate validpwd = new Validate(passwordET);
				validEmail.addValidator(new EmailValidator(RegisterUser.this));
				validpwd.addValidator(new NotEmptyValidator(RegisterUser.this));
				mForm.addValidates(validEmail);
				mForm.addValidates(validpwd);
				ArrayList<Validate> validList = new ArrayList<Validate>();
				validList.add(validEmail);
				validList.add(validpwd);
				Intent intent = new Intent();
				 Registeruservalidatior registerconttoller = new Registeruservalidatior(RegisterUser.this);
				 registerconttoller.validateForm(intent, mForm, validList);
			}
			
			
		}
		
		return super.onOptionsItemSelected(item);
	}

	public EditText getEmailET() {
		return emailET;
	}

	public void setEmailET(EditText emailET) {
		this.emailET = emailET;
	}

	public EditText getPasswordET() {
		return passwordET;
	}

	public void setPasswordET(EditText passwordET) {
		this.passwordET = passwordET;
	}
	
	

}
