package com.example.geomarketv3_dialog;

import java.util.ArrayList;

import com.example.geomarketv3.R;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ImageDialog extends DialogFragment{
	
	int dialogType;
	
	public void setDialogType(int type){
		dialogType = type;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	
		if(dialogType == 0){
			builder.setTitle("Image upload choice");
			builder.setItems(R.array.imguploadchoice, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					System.out.println(which);
				}
			});
			
		}
		
		return builder.create();
	}


	
	
}
