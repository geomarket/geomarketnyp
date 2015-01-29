package com.example.geomarketv3_uilogic;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.id;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetProductImage;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewProduct extends Activity {
	public static Product product = new Product();
	private static ImageView imgView;
	private TextView detailTV, PriceTV, nameTV;
	public static Bitmap image;
	private LinearLayout llbtn;
	private BootstrapButton purchaseBTN;
	private Firebase ref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_product);

		System.out.println("saler " + product.getSalerID());
		String url = "https://mmarketnyp.firebaseio.com/user/"+product.getSalerID()+"/product/"+product.getId();
		System.out.println("url: " + url);
		ref = new Firebase(url);
		ref.addListenerForSingleValueEvent(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0);
			}

			@Override
			public void onDataChange(DataSnapshot data) {
				// TODO Auto-generated method stub
				SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy"); 
				Date date = new Date();
				String formattedDate = curFormater.format(date);
				Map<String, Object> productMaps =  (Map<String, Object>) data.getValue();
				if(productMaps.get(formattedDate) != null){
					Map<String, Object> viewCountMap = (Map<String, Object>) productMaps.get(formattedDate);
					int count = Integer.parseInt(viewCountMap.get("viewcount").toString()) + 1;
					Firebase postRef = ref.child(formattedDate);
					Map<String, String> postView = new HashMap<String, String>();
					postView.put("viewcount", Integer.toString(count));
					postRef.setValue(postView);
				}else{
					System.out.println("null" +formattedDate);
					Firebase postRef = ref.child(formattedDate);
					Map<String, String> postView = new HashMap<String, String>();
					postView.put("viewcount", "1");
					postRef.setValue(postView);
				}
				
				
			}
			
		});

	
		imgView = (ImageView) findViewById(R.id.imgIV);
		imgView.setImageBitmap(product.getImage());
		detailTV = (TextView) findViewById(R.id.descTV);
		detailTV.setText(product.getDetail());
		PriceTV =  (TextView) findViewById(R.id.PriceTV);
		PriceTV.setText("Price: " + Double.toString(product.getPrice()));
		PriceTV.setTextSize(20);
		nameTV = (TextView) findViewById(R.id.nameTV);
		nameTV.setText(product.getName());
		nameTV.setTextSize(35);
		llbtn = (LinearLayout) findViewById(R.id.llbtn);
		llbtn.setBackgroundColor(Color.WHITE);
		llbtn.setAlpha(15);
		
		purchaseBTN = (BootstrapButton) findViewById(R.id.purchaseBtn);
		purchaseBTN.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_product, menu);
		return true;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	

}
