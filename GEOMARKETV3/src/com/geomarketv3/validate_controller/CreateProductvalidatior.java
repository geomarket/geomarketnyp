package com.geomarketv3.validate_controller;

import java.util.ArrayList;

import android.content.Intent;

import com.example.geomarketv3_asynctask.CreateProduct;
import com.example.geomarketv3_uilogic.CreateProductActivity;
import com.geomarketv3.crouton.Crouton;
import com.geomarketv3.crouton.Style;
import com.geomarketv3.entity.Product;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;

public class CreateProductvalidatior {
	
	private CreateProductActivity activity;
	private Product product;
	
	public CreateProductvalidatior(CreateProductActivity activity){
		this.activity = activity;
	}
	
	public void validateForm(Intent intent, Form mForm, ArrayList<Validate> validatorsArrList){
		if(mForm.validate()){
			product = new Product();
			product.setDetail(activity.getDescET().getText().toString());
			product.setName(activity.getNameET().getText().toString());
			product.setPrice(Double.parseDouble(activity.getPriceET().getText().toString()));
			
			
			if(!activity.getUriOfImage().equals(null)){
				product.setImgURL(activity.getUriOfImage());
			}
			
			CreateProduct create = new CreateProduct(activity, product, activity.getUserid());
			create.execute();
		}else{
			if(!validatorsArrList.get(0).isValid()){
				Crouton crouton = Crouton.makeText(activity,"Please fill in the details.",Style.ALERT);
				crouton.show();
			}else{
				System.out.println("NOTHING");
			}
		}
	}
	
}
