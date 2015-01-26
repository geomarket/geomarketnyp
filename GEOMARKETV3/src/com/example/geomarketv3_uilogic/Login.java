package com.example.geomarketv3_uilogic;
import java.util.ArrayList;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.geomarketv3.R;
import com.example.geomarketv3_asynctask.User_Login;
import com.geomarketv3.validate_controller.Loginvalidatior;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class Login extends Activity {
	private EditText emailET, passwordET;
	private BootstrapButton btnlogin, btnregister;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bg1);
		rLayout.setBackgroundDrawable(drawable);
		
		
		emailET = (EditText) findViewById(R.id.emailET);
		passwordET = (EditText) findViewById(R.id.passwordET);
		emailET.setTextSize(20);
		emailET.setTextColor(getResources().getColor(R.color.abs__background_holo_dark));
		passwordET.setTextSize(20);
		passwordET.setTextColor(getResources().getColor(R.color.abs__background_holo_dark));
		emailET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		//emailET.setText("fuhchang123@gmail.com");
		emailET.setText("test123@gmail.com");
		passwordET.setText("123123");
		btnregister = (BootstrapButton) findViewById(R.id.registerBtn);
		btnlogin = (BootstrapButton) findViewById(R.id.loginBtn);
		btnregister.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login.this,RegisterUser.class);
				startActivity(intent);
			}
			
		});
		btnlogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Form mForm = new Form();
				Validate validEmail = new Validate(emailET);
				Validate validpwd = new Validate(passwordET);
				validEmail.addValidator(new EmailValidator(Login.this));
				validpwd.addValidator(new NotEmptyValidator(Login.this));
				mForm.addValidates(validEmail);
				mForm.addValidates(validpwd);
				
				ArrayList<Validate> validList = new ArrayList<Validate>();
				validList.add(validEmail);
				validList.add(validpwd);
				Intent intent = new Intent();
				
				Loginvalidatior loginController = new Loginvalidatior(Login.this);
				loginController.validateForm(intent, mForm, validList);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
