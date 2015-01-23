package com.example.geomarketv3_asynctask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3.ProductList;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ListView;

public class GetProductList extends AsyncTask<Object, Object, Object> {
	private ArrayList<Product> productList;
	private String url = "https://mmarketnyp.firebaseio.com/user";
	private String userid;
	private ProgressDialog dialog;
	private ProductAdapter adapter;
	private Firebase ref;
	private Activity activity;
	private Cloudinary cloudinary;
	
	public GetProductList(Activity activity, String userid, ProductAdapter adapter){
		this.activity = activity;
		this.userid = userid;
		this.adapter = adapter;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		
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
			
		});
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
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

	public Bitmap loadImageFromURL(String fileUrl){
		  try {
		 
		    URL myFileUrl = new URL (fileUrl);
		    HttpURLConnection conn =
		      (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();
		 
		    InputStream is = (InputStream) conn.getInputStream();
		    
		 
		    return BitmapFactory.decodeStream(is);
		 
		  } catch (MalformedURLException e) {
		    e.printStackTrace();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		 
		  return null;
		}

}
