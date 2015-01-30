package com.example.geomarketv3_asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.geomarketv3.ProductDetailAdapter;
import com.example.geomarketv3_uilogic.ViewSalesActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class GetCartImage extends AsyncTask<Object, Object, Object>{
	private String imgurl;
	private ImageView img;
	private Bitmap myBitmap;
	public GetCartImage(String imgurl ,ImageView img){
		this.imgurl = imgurl;
		this.img = img;
	}
	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		try {
			
			URL url = new URL(imgurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        myBitmap = BitmapFactory.decodeStream(input);
	        input.close();
	        
	     
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object arg0) {
		img.setImageBitmap(myBitmap);
	}


	
	
}
