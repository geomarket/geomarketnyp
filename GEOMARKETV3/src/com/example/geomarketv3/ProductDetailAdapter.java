package com.example.geomarketv3;

import java.util.ArrayList;
import java.util.List;

import com.example.geomarketv3_asynctask.GetCartImage;
import com.geomarketv3.entity.Product;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailAdapter extends ArrayAdapter<Product> {
	private Activity context;
	private List<Product> resultArray;
	private TextView txtTitle, txtPrice;
	private CheckBox mShowCheckbox;
	private static ImageView imgView;
	public static Bitmap image;
	public ProductDetailAdapter(Context context, List<Product> resultArray) {
		super(context,R.layout.activity_product_detail_adapter, resultArray);
		// TODO Auto-generated constructor stub
		this.context = (Activity) context;
		this.resultArray = resultArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.activity_product_detail_adapter, null, true);
		
		txtTitle = (TextView) rowView.findViewById(R.id.TextViewItem);
		txtTitle.setText(resultArray.get(position).getName());
		txtPrice = (TextView) rowView.findViewById(R.id.TextView01);
		txtPrice.setText(Double.toString(resultArray.get(position).getPrice()));
		imgView = (ImageView) rowView.findViewById(R.id.ImageViewItem);
		GetCartImage getImage = new GetCartImage(resultArray.get(position).getImgURL(), imgView);
		getImage.execute();
		return rowView;
	}


}
