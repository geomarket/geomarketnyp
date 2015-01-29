
package com.example.geomarketv3_asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cloudinary.Cloudinary;
import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3_uilogic.ViewSalesActivity;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetImageProduct extends AsyncTask<Void, Void, Void> {
	
	private Cloudinary cloudinary;
	private Activity activity;
	private String url;
	private Bitmap Image;
	private Product product;
	private ProductAdapter adapter;
	public GetImageProduct(Activity activity, String url,  Product product, ProductAdapter adapter){
		this.activity = activity;
		this.url = url;
		this.product = product;
		this.adapter = adapter;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		Image = loadImageFromURL(url);
		product.setImage(Image);
		
		return null;
	}

	
	@Override
	protected void onPostExecute(Void result) {
		adapter.add(product);
		
	}

	@Override
	protected void onPreExecute() {
		
		cloudinary  = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
	}

	public Bitmap loadImageFromURL(String fileUrl){
		  try {
		 
		    URL myFileUrl = new URL (fileUrl);
		    HttpURLConnection conn =
		      (HttpURLConnection) myFileUrl.openConnection();
		    conn.setDoInput(true);
		    conn.connect();
		 
		    InputStream is = (InputStream) conn.getInputStream();
		    Bitmap bitmap = BitmapFactory.decodeStream(is);
		    is.close();
		    
		    return bitmap;
		 
		  } catch (MalformedURLException e) {
		    e.printStackTrace();
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		 
		  return null;
		}

	public Bitmap getImage() {
		return Image;
	}

	public void setImage(Bitmap image) {
		Image = image;
	}
	
}
