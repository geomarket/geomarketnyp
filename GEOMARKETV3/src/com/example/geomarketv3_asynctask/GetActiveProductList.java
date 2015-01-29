package com.example.geomarketv3_asynctask;

import java.util.ArrayList;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.geomarketv3.ProductAdapter;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetActiveProductList extends AsyncTask<Object, Object, Object> {
	private ArrayList<Product> productList;
	private String url = "https://mmarketnyp.firebaseio.com/user";
	private String userid;
	private ProgressDialog dialog;
	private ProductAdapter adapter;
	private Firebase ref;
	private Activity activity;
	private Cloudinary cloudinary;
	
	public GetActiveProductList(Activity activity, String userid, ProductAdapter adapter){
		this.activity = activity;
		this.userid = userid;
		this.adapter = adapter;
	}
	@Override
	protected Object doInBackground(Object... arg0) {
		ref.addValueEventListener(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError error) {
				// TODO Auto-generated method stub
				System.out.println("Error: " + error);
			}

			@Override
			public void onDataChange(DataSnapshot data) {
				// TODO Auto-generated method stub
				adapter.clear();
				Map<String, Object> productsMap = (Map<String, Object>) data.getValue();
				
				
				for(String i : productsMap.keySet()){
					
					Map<String, Object> productMap = (Map<String, Object>) productsMap.get(i);
					
						if(productMap.get("status").toString().equals("active")){
							String url = cloudinary.url().format("jpg").transformation(new Transformation().width(300).crop("fit")).generate(i);
							Product product = new Product();
							product.setId(i);
							product.setName(productMap.get("name").toString());
							product.setPrice(Double.parseDouble(productMap.get("price").toString()));
							product.setDetail(productMap.get("detail").toString());
							product.setImgURL(url);
							product.setStatus(productMap.get("status").toString());
							GetImageProduct getImage = new GetImageProduct(activity,url, product, adapter);
							getImage.execute();	
						}
				}
			}
			
		});
		return null;
	}

	@Override
	protected void onPostExecute(Object arg0) {
		adapter.notifyDataSetChanged();
		dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		url = url +"/"+userid+"/product";
		ref = new Firebase(url);
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
		dialog = ProgressDialog.show(activity,"Retrieving Product", "Please wait...", true);
	}
	
	
}
