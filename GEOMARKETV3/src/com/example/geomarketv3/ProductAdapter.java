package com.example.geomarketv3;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductAdapter extends ArrayAdapter<Product> {
	private Activity context;
	private ArrayList<Product> resultArray;
	public ImageView imgView;
	private TextView txtTitle;
	public ProductAdapter(Context context, ArrayList<Product> resultArray) {
		super(context, R.layout.activity_product_adapter, resultArray);
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.resultArray = resultArray;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.activity_product_adapter, null, true);
		//TextView txtTitle = (TextView) rowView.findViewById(R.id.productTitle);

		imgView = (ImageView) rowView.findViewById(R.id.productImg);
		imgView.setImageBitmap(resultArray.get(position).getImage());
		
		txtTitle = (TextView) rowView.findViewById(R.id.productTitle);
	
		txtTitle.setText(resultArray.get(position).getName() + " $" + resultArray.get(position).getPrice());
		txtTitle.setTextSize(25);
		if(resultArray.get(position).getStatus().equals("active")){
			rowView.setBackgroundColor(Color.LTGRAY);
		}
		
		return rowView;
	}

	

}
