package com.example.geomarketv3_uilogic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3.ProductDetailAdapter;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.geomarketv3.entity.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ShoppingCart extends Activity {
	public List<Product> productList;
	private ListView list;
	private ProductDetailAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		productList = new ArrayList<Product>();
		productList.addAll(GetArrayListSharedPreferenced());
		adapter = new ProductDetailAdapter(this, productList);
		list = (ListView) findViewById(R.id.ListViewProduct);
		list.setAdapter(adapter);
		System.out.println("size " + productList.size());
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				final int selposition = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCart.this);
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case DialogInterface.BUTTON_POSITIVE:
							
							adapter.remove(productList.get(selposition));
							SaveArrayListSharedPreferenced(productList);
							break;
						case DialogInterface.BUTTON_NEGATIVE:
							break;
						}
					}
				};
				builder.setMessage("Do you wish to remove from cart?").setPositiveButton("yes", dialogClickListener).setNegativeButton("no", dialogClickListener).show();
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shopping_cart, menu);
		return true;
	}
	
	private List<Product> GetArrayListSharedPreferenced(){
		 SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		 Editor prefsEditor = appSharedPrefs.edit();
	      Gson gson = new Gson();
	      String json = appSharedPrefs.getString("MyProductArray", null);
	      Type type = new TypeToken<List<Product>>(){}.getType();
	      List<Product> productList = gson.fromJson(json, type);
	      return productList;
	}
	
private void SaveArrayListSharedPreferenced(List<Product> product){
		
		 SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		 Editor prefsEditor = appSharedPrefs.edit();
		 Gson gson = new Gson();
		 String json = gson.toJson(productList);
		 prefsEditor.putString("MyProductArray", json);
		 prefsEditor.commit(); 
	}

}
