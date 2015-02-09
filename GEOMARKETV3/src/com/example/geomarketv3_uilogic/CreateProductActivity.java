package com.example.geomarketv3_uilogic;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.geomarketv3.validate_controller.CreateProductvalidatior;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;
import com.geomarketv3.validation.validator.NotEmptyValidator;
import com.geomarketv3.validation.validator.NumericValidator;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

public class CreateProductActivity extends Activity {
	private BootstrapButton uploadBTN;
	private Uri selectedImage;
	private ImageView imgView;
	private Bitmap Image = null;
	private String uriOfImage = null;
	private EditText nameET, priceET, DescET;
	private String userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_product);
		imgView = (ImageView) findViewById(R.id.imageView);
		nameET = (EditText) findViewById(R.id.nameET);
		priceET = (EditText) findViewById(R.id.priceET);
		DescET = (EditText) findViewById(R.id.DescET);
		
		userid = getIntent().getStringExtra("userid");
		
		uploadBTN = (BootstrapButton) findViewById(R.id.uploadBtn);
		uploadBTN.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(CreateProductActivity.this);
				builder.setTitle("How do you wish to upload your Image?");
			builder.setItems(R.array.imguploadchoice, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									switch(which){
									case 0:
										Intent galleryPicker = new Intent(
												Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
												startActivityForResult(galleryPicker, 0);
										break;
									case 1:
										Intent cameraPicker = new Intent("android.media.action.IMAGE_CAPTURE");
										startActivityForResult(cameraPicker, 1);
										break;
									}
								}
							}).create().show();
						}
						
					});
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_product, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_addproduct:
			
			Intent intent = new Intent();
			Form mForm = new Form();
			Validate validName = new Validate(nameET);
			Validate validPrice = new Validate(priceET);
			Validate validDesc = new Validate(DescET);
			
			validName.addValidator(new NotEmptyValidator(CreateProductActivity.this));
			validPrice.addValidator(new NumericValidator(CreateProductActivity.this));
			validDesc.addValidator(new NotEmptyValidator(CreateProductActivity.this));
			
			mForm.addValidates(validName);
			mForm.addValidates(validDesc);
			mForm.addValidates(validPrice);
			ArrayList<Validate> validList = new ArrayList<Validate>();
			validList.add(validName);
			validList.add(validPrice);
			validList.add(validDesc);
			CreateProductvalidatior  create = new CreateProductvalidatior(CreateProductActivity.this);
			create.validateForm(intent, mForm, validList);
			
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 0 && resultCode == RESULT_OK){
				Uri mImageUri = data.getData();
			
			try {
				Image = Media.getBitmap(this.getContentResolver(), mImageUri);
				uriOfImage = getRealPathFromURI(mImageUri);
				imgView.setVisibility(View.VISIBLE);
				Image = decodeSampledBitmapFromResource(uriOfImage, 140, 300);
				imgView.setImageBitmap(Image);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(requestCode == 1 && resultCode == RESULT_OK){
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imgView.setImageBitmap(photo);
			imgView.setVisibility(View.VISIBLE);
			Uri mImageUri = getImageUri(CreateProductActivity.this,photo);
			uriOfImage = getRealPathFromURI(mImageUri);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
		public String getRealPathFromURI(Uri contentUri) {
	
	        // can post image
	        String [] proj={MediaStore.Images.Media.DATA};
	        Cursor cursor = managedQuery( contentUri,
	                        proj, // Which columns to return
	                        null,       // WHERE clause; which rows to return (all rows)
	                        null,       // WHERE clause selection arguments (none)
	                        null); // Order-by clause (ascending by name)
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	
	        return cursor.getString(column_index);
	}
	
	public static Bitmap decodeSampledBitmapFromResource(String pathName,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public Uri getSelectedImage() {
		return selectedImage;
	}

	public void setSelectedImage(Uri selectedImage) {
		this.selectedImage = selectedImage;
	}

	public ImageView getImgView() {
		return imgView;
	}

	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}

	public Bitmap getImage() {
		return Image;
	}

	public void setImage(Bitmap image) {
		Image = image;
	}

	public String getUriOfImage() {
		return uriOfImage;
	}

	public void setUriOfImage(String uriOfImage) {
		this.uriOfImage = uriOfImage;
	}

	

	public EditText getNameET() {
		return nameET;
	}

	public void setNameET(EditText nameET) {
		this.nameET = nameET;
	}

	public EditText getPriceET() {
		return priceET;
	}

	public void setPriceET(EditText priceET) {
		this.priceET = priceET;
	}

	public EditText getDescET() {
		return DescET;
	}

	public void setDescET(EditText descET) {
		DescET = descET;
	}
	
	
	
}
