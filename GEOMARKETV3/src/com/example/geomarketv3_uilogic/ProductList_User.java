package com.example.geomarketv3_uilogic;

import java.util.ArrayList;

import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetActiveProductList;
import com.example.geomarketv3_asynctask.GetProductList;
import com.firebase.client.Firebase;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProductList_User extends Activity {
	private String userid;
	private ProductAdapter adapter;
	private ListView list;
	private ArrayList<Product> productList;
	private Firebase ref;
	private String url;
	private String Userkey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list__user);
		productList = new ArrayList<Product>();
		userid = getIntent().getStringExtra("userid");
		Userkey = getIntent().getStringExtra("key");
		url ="https://mmarketnyp.firebaseio.com/user/" +userid+"/product/";
		list = (ListView) findViewById(R.id.productlistUserview);
		adapter = new ProductAdapter(this, productList);
		list.setAdapter(adapter);
		GetActiveProductList productList = new GetActiveProductList(this, userid, adapter);
		productList.execute();
		
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Product product = new Product();
				product.setId(adapter.getItem(position).getId());
				product.setName(adapter.getItem(position).getName());
				product.setDetail(adapter.getItem(position).getDetail());
				product.setPrice(adapter.getItem(position).getPrice());
				product.setImgURL(adapter.getItem(position).getImgURL());
				product.setImage(adapter.getItem(position).getImage());
				product.setSalerID(Userkey);
				product.setStatus(adapter.getItem(position).getStatus());
				Intent intent = new Intent(ProductList_User.this, ViewProduct.class);
				ViewProduct.product = product;
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_list__user, menu);
		return true;
	}

}
