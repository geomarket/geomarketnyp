package com.example.geomarketv3_asynctask;

import java.util.HashMap;
import java.util.Map;

import com.firebase.client.Firebase;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class CreateProduct extends AsyncTask<Object, Object, Object> {
	private ProgressDialog dialog;
	private Activity activity;
	private Firebase ref;
	private Product product;
	private String userid;
	private Firebase productRef;
	private String url = "https://mmarketnyp.firebaseio.com/user";
	
	public CreateProduct(Activity activity, Product product, String userid){
		this.activity = activity;
		this.product = product;
		this.userid = userid;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		Firebase createProduct = ref.push();
		Map<String, Object> productMap = new HashMap<String, Object>();
		productMap.put("name", product.getName());
		productMap.put("price", Double.toString(product.getPrice()));
		productMap.put("detail", product.getDetail());
		createProduct.setValue(productMap);
		
		System.out.println("key " + createProduct.getKey());
		
		UploadImage uploadimg = new UploadImage(activity, product.getImgURL(), createProduct.getKey(), 1);
		uploadimg.execute();
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		dialog.dismiss();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		
		url = url +"/"+userid+"/product";
		
		ref = new Firebase(url);
		
		
		dialog = ProgressDialog.show(activity, "creating."," Please wait....", true);
	}
	
	

}
