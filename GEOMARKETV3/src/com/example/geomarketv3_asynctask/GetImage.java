package com.example.geomarketv3_asynctask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.geomarketv3_fragment.SalesMemberFragment;
import com.example.geomarketv3_uilogic.ViewSalesActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetImage extends AsyncTask<Object, Object, Object>{
	
	public String imgurl;
	
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			URL url = new URL(imgurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = 4;
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        input.close();
	        ViewSalesActivity.bitImage = myBitmap;
	     
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
	protected void onPostExecute(Object result) {
		//SalesMemberFragment.activity.setImage();
		ViewSalesActivity.setImage();
	}
	
	
}
