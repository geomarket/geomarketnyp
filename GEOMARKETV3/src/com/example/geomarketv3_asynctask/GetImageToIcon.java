package com.example.geomarketv3_asynctask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cloudinary.Cloudinary;
import com.example.geomarketv3_uilogic.SetLocActivity;
import com.geomarketv3.entity.Product;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetImageToIcon extends AsyncTask<Void, Void, Void>{
	private Cloudinary cloudinary;
	private SetLocActivity activity;
	private String url;
	private Bitmap Image;
	private String userid;
	
	public GetImageToIcon(SetLocActivity activity,  String userid) {
		this.activity = activity;
		this.userid = userid;
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		Image = loadImageFromURL(url);
		activity.iconImg = Image;
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
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
	
}
