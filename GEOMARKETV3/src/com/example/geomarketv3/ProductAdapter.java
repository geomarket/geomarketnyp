package com.example.geomarketv3;

import java.util.ArrayList;
import java.util.List;

import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
	public ProductAdapter(Context context, int resource,int textViewResourceId,  ArrayList<Product> resultArray) {
		super(context, R.layout.activity_product_adapter, textViewResourceId, resultArray);
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.resultArray = resultArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.activity_product_adapter, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.productTitle);
		TextView txtPrice = (TextView) rowView.findViewById(R.id.productPrice);
		TextView txtDetail = (TextView) rowView.findViewById(R.id.productDetail);
		ImageView imgView = (ImageView) rowView.findViewById(R.id.productImg);
		
		txtTitle.setText(resultArray.get(position).getName());
		txtPrice.setText(Double.toString(resultArray.get(position).getPrice()));
		txtDetail.setText(resultArray.get(position).getDetail());
		
		return super.getView(position, convertView, parent);
	}

		

}
