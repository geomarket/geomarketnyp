package com.example.geomarketv3;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.geomarketv3_asynctask.GetProductImage;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewProduct extends Activity {
	public static Product product = new Product();
	private static ImageView imgView;
	private TextView detailTV, PriceTV, nameTV;
	public static Bitmap image;
	private LinearLayout llbtn;
	private Cloudinary cloudinary;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_product);
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
		String url = cloudinary.url().format("jpg").transformation(new Transformation().width(1500).crop("fit")).generate(product.getId());
	
		imgView = (ImageView) findViewById(R.id.imgIV);
		GetProductImage getImage = new GetProductImage();
		getImage.imgurl = url;
		getImage.execute();
		
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
	
	public static void setImage(){
		imgView.setImageBitmap(image);
	}
	

}
