package com.example.geomarketv3_asynctask;

import java.util.ArrayList;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

public class GetProductList extends AsyncTask<Object, Object, Object> {
	private ArrayList<Product> productList = new ArrayList<Product>();
	private String url = "https://mmarketnyp.firebaseio.com/";
	private String userid;
	private ProgressDialog dialog;
	private Firebase ref;
	private Activity activity;
	public GetProductList(Activity activity, String userid){
		this.activity = activity;
		this.userid = userid;
	}
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		ref.addChildEventListener(new ChildEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildAdded(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String str) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		productList.clear();
		url = url +"/"+userid+"/product";
		ref = new Firebase(url);
		dialog = ProgressDialog.show(activity,"Retrieving Product", "Please wait...", true);
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

}
