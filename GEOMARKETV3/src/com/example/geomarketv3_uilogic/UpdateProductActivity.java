package com.example.geomarketv3_uilogic;

import java.util.HashMap;
import java.util.Map;

import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.firebase.client.Firebase;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class UpdateProductActivity extends Activity {
	public static Product product = new Product();
	private EditText nameET, priceET, detailET;
	private String userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_product);
		userid = getIntent().getStringExtra("userid");
		nameET = (EditText) findViewById(R.id.nameET);
		priceET = (EditText) findViewById(R.id.priceET);
		detailET = (EditText) findViewById(R.id.DescET);
		nameET.setText(product.getName());
		priceET.setText(Double.toString(product.getPrice()));
		detailET.setText(product.getDetail());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_product, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_updateproduct:
			String url = "https://mmarketnyp.firebaseio.com/user/"+userid+"/product"; 
			Firebase Ref = new Firebase(url);
			Firebase updateRef = Ref.child(product.getId());
			Map<String, Object> updateMap = new HashMap<String, Object>();
			 updateMap.put("name", nameET.getText().toString());
			 updateMap.put("price", priceET.getText().toString());
			 updateMap.put("detail", detailET.getText().toString());
			 updateRef.updateChildren(updateMap);
			 UpdateProductActivity.this.finish();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
}
