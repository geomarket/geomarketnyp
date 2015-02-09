package com.example.geomarketv3_uilogic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.id;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetProductList;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.Product;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ProductList extends Activity {
	private ListView list;
	private String userid;
	private ProductAdapter adapter;
	private ArrayList<Product> productList;
	private Firebase ref;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		productList = new ArrayList<Product>();
		userid = getIntent().getStringExtra("userid");
		url ="https://mmarketnyp.firebaseio.com/user/" +userid+"/product/";
		list = (ListView) findViewById(R.id.productlistview);
		adapter = new ProductAdapter(this, productList);
		list.setAdapter(adapter);
		
		 GetProductList productList = new GetProductList(this, userid, adapter);
		 productList.execute();
		 list.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,final int position, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(ProductList.this);
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        	Product product = new Product();
				        	product.setId(adapter.getItem(position).getId());
				        	product.setName(adapter.getItem(position).getName());
				        	product.setPrice(adapter.getItem(position).getPrice());
				        	product.setDetail(adapter.getItem(position).getDetail());
				        	Intent intent = new Intent(ProductList.this, UpdateProductActivity.class);
				        	UpdateProductActivity.product = product;
				        	intent.putExtra("userid", userid);
				        	startActivity(intent);
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				        	
				            break;
				        }
				    }
				};
				
				builder.setMessage("You wish to update this product?").setPositiveButton("yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
				return false;
			}
			 
		 });
		 list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, final int position,long arg3) {
				// TODO Auto-generated method stub
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ProductList.this);
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        		if(adapter.getItem(position).getStatus().equals("unactive")){
				        			System.out.println("activing");
				        			ref = new Firebase(url);
				        			Firebase hopperRef = ref.child(adapter.getItem(position).getId());
				        			Map<String, Object> statusMap = new HashMap<String, Object>();
				        			statusMap.put("status", "active");
				        			hopperRef.updateChildren(statusMap);
				        		}else{
				        			System.out.println("Product is already unactive");
				        		}
				        	
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				        	if(adapter.getItem(position).getStatus().equals("active")){
			        			System.out.println("unactiving");
			        			ref = new Firebase(url);
			        			Firebase hopperRef = ref.child(adapter.getItem(position).getId());
			        			Map<String, Object> statusMap = new HashMap<String, Object>();
			        			statusMap.put("status", "unactive");
			        			hopperRef.updateChildren(statusMap);
			        		}else{
			        			System.out.println("Product is already active");
			        		}
				            break;
				        }
				    }
				};
				builder.setMessage("You product is " + adapter.getItem(position).getStatus()).setPositiveButton("active", dialogClickListener).setNegativeButton("Unactive", dialogClickListener).show();
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
