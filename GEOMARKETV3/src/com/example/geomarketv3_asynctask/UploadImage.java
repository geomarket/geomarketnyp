package com.example.geomarketv3_asynctask;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.example.geomarketv3_uilogic.Login;
import com.example.geomarketv3_uilogic.SetLocActivity;
import com.geomarketv3.entity.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

public class UploadImage extends AsyncTask<Void, Void, Void>{
	private Cloudinary cloudinary;
	private Activity activity;
	private String url, id;
	private int end;

	
	public UploadImage(Activity activity, String url,String id, int end){
		this.activity = activity;
		this.url = url;
		this.id = id;
		this.end = end;
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		imageUpload(id, url);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		if(end == 0){
			Intent intent = new Intent(activity, Login.class);
			activity.startActivity(intent);
			activity.finish();
		}else if(end == 1){
			Intent intent = new Intent(activity, SetLocActivity.class);
			intent.putExtra("userid", id);
			activity.startActivity(intent);
			activity.finish();
		}
	}

	@Override
	protected void onPreExecute() {
		cloudinary  = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
	
	}
	
private void imageUpload(String id, String path){
		
		File file = new File(path);
		
		try {

			Map params = Cloudinary.asMap("public_id", id);
			cloudinary.uploader().upload(file,params);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error " +  e);
			e.printStackTrace();
		}
	}

	
}
