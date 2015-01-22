package com.example.geomarketv3;

import java.util.ArrayList;
import java.util.Map;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ProductList extends Activity {
	private ListView list;
	private String userid;
	private ProductAdapter adapter;
	private ArrayList<Product> productList = new ArrayList<Product>();
	private String url = "https://mmarketnyp.firebaseio.com/user";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		userid = getIntent().getStringExtra("userid");
		list = (ListView) findViewById(R.id.productlistview);
		
		
		url = url +"/"+userid+"/product";
		Firebase ref = new Firebase(url);
		ref.addValueEventListener(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}

			@Override
			public void onDataChange(DataSnapshot data) {
				// TODO Auto-generated method stub
				Map<String, Object> productsMap = (Map<String, Object>) data.getValue();
					for(String i : productsMap.keySet()){
						Map<String, Object> productMap = (Map<String, Object>) productsMap.get(i);
						if(productMap.get("status").equals("active")){
							Product product = new Product();
							product.setId(productMap.get("status").toString());
							product.setName(productMap.get("name").toString());
							product.setPrice(Double.parseDouble(productMap.get("price").toString()));
							product.setDetail(productMap.get("detail").toString());
							productList.add(product);
						}
					}
					adapter = new ProductAdapter(ProductList.this,productList );
					list.setAdapter(adapter);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.product_list, menu);
		return true;
	}

}
