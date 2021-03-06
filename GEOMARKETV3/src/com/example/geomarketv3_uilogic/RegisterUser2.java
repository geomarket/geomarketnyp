package com.example.geomarketv3_uilogic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.example.geomarketv3.R;

import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.geomarketv3.validate_controller.Registeruservalidator2;
import com.geomarketv3.validation.Form;
import com.geomarketv3.validation.Validate;
import com.geomarketv3.validation.validator.NotEmptyValidator;
import com.geomarketv3.validation.validator.NumericValidator;
import com.geomarketv3.validation.validator.PhoneValidator;

import android.media.ExifInterface;
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
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class RegisterUser2 extends Activity {
	private EditText titleET, nameET, contactET, companyET;
	private BootstrapButton uploadbtn;
	private Spinner typeSelector;
	private ImageView imgView;
	private String role;
	private Uri selectedImage;
	private Bitmap Image = null;
	private String uriOfImage = null;
	private String selectedType = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user2);
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.rLayout);
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.bg1);
		rLayout.setBackgroundDrawable(drawable);
		typeSelector = (Spinner) findViewById(R.id.spinnerType);
		typeSelector.setBackgroundColor(Color.WHITE);
		typeSelector.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				setSelectedType(typeSelector.getItemAtPosition(position).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		
		});
		titleET = (EditText) findViewById(R.id.titleET);
		nameET = (EditText) findViewById(R.id.nameET);
		contactET = (EditText) findViewById(R.id.contactET);
		companyET = (EditText) findViewById(R.id.companyET);
		uploadbtn = (BootstrapButton) findViewById(R.id.uploadBtn);
		imgView =   (ImageView) findViewById(R.id.imageView);
		imgView.setVisibility(View.GONE);
		role = "sales";
		uploadbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterUser2.this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_user2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case R.id.action_register:
				Form mForm = new Form();
				Validate validtitle= new Validate(titleET);
				Validate validname= new Validate(nameET);
				Validate validcontact= new Validate(contactET);
				Validate validcompany= new Validate(companyET);
				
				validtitle.addValidator(new NotEmptyValidator(RegisterUser2.this));
				validname.addValidator(new NotEmptyValidator(RegisterUser2.this));
				validcontact.addValidator(new PhoneValidator(RegisterUser2.this));
				validcompany.addValidator(new NotEmptyValidator(RegisterUser2.this));
				mForm.addValidates(validtitle);
				mForm.addValidates(validtitle);
				mForm.addValidates(validcontact);
				mForm.addValidates(validcompany);
				
				ArrayList<Validate> validList = new ArrayList<Validate>();
				validList.add(validtitle);
				validList.add(validname);
				validList.add(validcontact);
				validList.add(validcompany);
				
				Intent intent = new Intent();
				Registeruservalidator2 registeruser2controller = new Registeruservalidator2(RegisterUser2.this);
				registeruser2controller.validateForm(intent, mForm, validList);
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
			Uri mImageUri = getImageUri(RegisterUser2.this,photo);
			uriOfImage = getRealPathFromURI(mImageUri);
		}
		
	}

	private void setImageBitmap(Bitmap image2) {
		// TODO Auto-generated method stub
		
	}

	public EditText getTitleET() {
		return titleET;
	}

	public void setTitleET(EditText titleET) {
		this.titleET = titleET;
	}

	public String getNameET() {
		return nameET.getText().toString();
	}

	public void setNameET(EditText nameET) {
		this.nameET = nameET;
	}

	public String getContactET() {
		return contactET.getText().toString();
	}

	public void setContactET(EditText contactET) {
		this.contactET = contactET;
	}

	public String getCompanyET() {
		return companyET.getText().toString();
	}

	public void setCompanyET(EditText companyET) {
		this.companyET = companyET;
	}

	public String getEmail(){
		return getIntent().getStringExtra("email");
	}
	
	public String getPWD(){
		return getIntent().getStringExtra("pwd");
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	
	public String getUriOfImage() {
		return uriOfImage;
	}

	public void setUriOfImage(String uriOfImage) {
		this.uriOfImage = uriOfImage;
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
	
	public int getCameraPhotoOrientation(Context context, Uri imageUri,
			String imagePath) {
		int rotate = 0;
		try {
			context.getContentResolver().notifyChange(imageUri, null);
			File imageFile = new File(imagePath);

			ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			default:
				rotate = 0;
				break;
			}

			Log.i("RotateImage", "Exif orientation: " + orientation);
			Log.i("RotateImage", "Rotate value: " + rotate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rotate;
	}
	
	public Uri getImageUri(Context inContext, Bitmap inImage) {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
	    String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
	    return Uri.parse(path);
	}

	public String getSelectedType() {
		return selectedType;
	}

	public void setSelectedType(String selectedType) {
		this.selectedType = selectedType;
	}
}
