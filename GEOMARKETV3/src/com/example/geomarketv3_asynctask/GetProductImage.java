package com.example.geomarketv3_asynctask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.geomarketv3_uilogic.ViewProduct;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetProductImage extends AsyncTask<Object, Object, Object>{
	public String imgurl;
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		 try {
			 
			    URL myFileUrl = new URL (imgurl);
			    HttpURLConnection conn =
			      (HttpURLConnection) myFileUrl.openConnection();
			    conn.setDoInput(true);
			    conn.connect();
			 
			    InputStream is = (InputStream) conn.getInputStream();
			   
			    ViewProduct.image = BitmapFactory.decodeStream(is);
			 
			    is.close();
			 
			  } catch (MalformedURLException e) {
			    e.printStackTrace();
			  } catch (Exception e) {
			    e.printStackTrace();
			  }
		return null;
	}

	@Override
	protected void onPostExecute(Object arg0) {
		// TODO Auto-generated method stub
		//ViewProduct.setImage();
	}
	
	

}
