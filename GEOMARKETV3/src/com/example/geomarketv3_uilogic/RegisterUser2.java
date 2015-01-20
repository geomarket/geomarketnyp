package com.example.geomarketv3_uilogic;

import java.util.ArrayList;

import com.example.geomarketv3.R;

import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.geomarketv3.validate_controller.Registeruservalidator2;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;
import com.geomarketv3.validation.validator.NotEmptyValidator;
import com.geomarketv3.validation.validator.NumericValidator;
import com.geomarketv3.validation.validator.PhoneValidator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class RegisterUser2 extends Activity {
	private EditText titleET, nameET, contactET, companyET;
	private String role;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user2);
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bg1);
		rLayout.setBackgroundDrawable(drawable);
		titleET = (EditText) findViewById(R.id.titleET);
		nameET = (EditText) findViewById(R.id.nameET);
		contactET = (EditText) findViewById(R.id.contactET);
		companyET = (EditText) findViewById(R.id.companyET);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.action_register:
				Form mForm = new Form();
				Validate validtitle= new Validate(titleET);
				Validate validname= new Validate(nameET);
				Validate validcontact= new Validate(contactET);
				Validate validcompany= new Validate(companyET);
				
				validtitle.addValidator(new NotEmptyValidator(RegisterUser2.this));
				validname.addValidator(new NotEmptyValidator(RegisterUser2.this));
				validcontact.addValidator(new PhoneValidator(RegisterUser2.this));
				validcompany.addValidator(new NotEmptyValidator(RegisterUser2.this));
				mForm.addValidates(validtitle);
				mForm.addValidates(validtitle);
				mForm.addValidates(validcontact);
				mForm.addValidates(validcompany);
				
				ArrayList<Validate> validList = new ArrayList<Validate>();
				validList.add(validtitle);
				validList.add(validname);
				validList.add(validcontact);
				validList.add(validcompany);
				
				Intent intent = new Intent();
				Registeruservalidator2 registeruser2controller = new Registeruservalidator2(RegisterUser2.this);
				registeruser2controller.validateForm(intent, mForm, validList);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public EditText getTitleET() {
		return titleET;
	}

	public void setTitleET(EditText titleET) {
		this.titleET = titleET;
	}

	public String getNameET() {
		return nameET.getText().toString();
	}

	public void setNameET(EditText nameET) {
		this.nameET = nameET;
	}

	public String getContactET() {
		return contactET.getText().toString();
	}

	public void setContactET(EditText contactET) {
		this.contactET = contactET;
	}

	public String getCompanyET() {
		return companyET.getText().toString();
	}

	public void setCompanyET(EditText companyET) {
		this.companyET = companyET;
	}

	public String getEmail(){
		return getIntent().getStringExtra("email");
	}
	
	public String getPWD(){
		return getIntent().getStringExtra("pwd");
	}

	public String getRole() {
		return "sales";
	}

	public void setRole(String role) {
		this.role = role;
	}
}
