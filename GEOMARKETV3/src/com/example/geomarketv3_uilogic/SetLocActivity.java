package com.example.geomarketv3_uilogic;

import java.io.IOException;




import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.geomarketv3.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.geofencing.GeofenceRequester;
import com.geomarketv3.geofencing.SimpleGeofence;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class SetLocActivity extends Activity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
LocationListener {
	private GoogleMap gMap;
	private LatLng myLocation;
	private BootstrapButton setLoc;
	private Firebase ref, rmref;
	private String userid;
	private String url;
	private double lat, lng;
	private Marker marker;
	private ArrayList<Marker> markerList;
    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private GeofenceRequester mGeofenceRequester;
	private SimpleGeofence UiGeofence;
	List<Geofence> mCurrentGeofences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_loc);
	
		userid = getSharedPrefernces();
		url = "https://mmarketnyp.firebaseio.com/user/" + userid;
		ref = new Firebase(url+"/location");
		MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		gMap = mf.getMap();
		gMap.setMyLocationEnabled(true);
		markerList = new ArrayList<Marker>();
		ref.addListenerForSingleValueEvent(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), arg0.toString(), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				if(snapshot.getValue() != null){
					Map<String, Object> updateMaps = (Map<String, Object>) snapshot.getValue();
					for(String i: updateMaps.keySet()){
						Map<String, Object> updateMap = (Map<String, Object>) updateMaps.get(i);
						Geocoder geocoder = new Geocoder(SetLocActivity.this, Locale.getDefault()); 
			        	 try {
							List<Address> address = geocoder.getFromLocation(Double.parseDouble(updateMap.get("lat").toString()), Double.parseDouble(updateMap.get("lng").toString()), 1);
							marker = gMap.addMarker(new MarkerOptions().position(new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude())).title(updateMap.get("markertitle").toString()).snippet(address.get(0).getAddressLine(0) + " " + address.get(0).getAddressLine(1)));
							marker.showInfoWindow();
							markerList.add(marker);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		mLocationClient = new LocationClient(this, this, this);
		
		gMap.setOnMapLongClickListener(new OnMapLongClickListener(){

			@Override
			public void onMapLongClick(LatLng latlng) {
				// TODO Auto-generated method stub
				final LatLng setlatlng = latlng;
				AlertDialog.Builder builder = new AlertDialog.Builder(SetLocActivity.this);
				
				// TODO Auto-generated method stub
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        	 Geocoder geocoder = new Geocoder(SetLocActivity.this, Locale.getDefault()); 
				        	 try {
								List<Address> address = geocoder.getFromLocation(setlatlng.latitude, setlatlng.longitude, 1);
								marker = gMap.addMarker(new MarkerOptions().position(setlatlng).title("marker"+ (markerList.size()+1)).snippet(address.get(0).getAddressLine(0) + " " + address.get(0).getAddressLine(1)));
								marker.showInfoWindow();
								markerList.add(marker);
								getDate(ref, marker);
								
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				        	
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				        	Toast.makeText(getApplicationContext(), "No!!! "+ setlatlng, Toast.LENGTH_SHORT).show();
				            break;
				        }
				    }
				};
				builder.setMessage("Do you wish to set your shop here?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
			}
			
		});
		gMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				marker.setDraggable(true);
				
				return false;
			}
			
		});
		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			@Override
			public void onInfoWindowClick(final Marker marker) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SetLocActivity.this);
				DialogInterface.OnClickListener removedialogClickListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						 case DialogInterface.BUTTON_POSITIVE:
							 	marker.remove();
							 		for(int i=0; i<markerList.size(); i++){
							 			if(markerList.get(i).getTitle().equals(marker.getTitle())){
							 				markerList.remove(i);
							 			}
							 		}
							 		ref.addListenerForSingleValueEvent(new ValueEventListener(){

										@Override
										public void onCancelled(
												FirebaseError arg0) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onDataChange(
												DataSnapshot arg0) {
											// TODO Auto-generated method stub
											
											Map<String, Object> getMarkerMaps = (Map<String, Object>) arg0.getValue();
											for(String i : getMarkerMaps.keySet()){
												Map<String, Object> getMarkerMap =  (Map<String, Object>) getMarkerMaps.get(i);
													if(getMarkerMap.get("markertitle").toString().equals(marker.getTitle())){
														String markerurl = url+"/location/"+ i;
														rmref = new Firebase(markerurl);
														rmref.removeValue();
													}
											}
										}
					 					
					 				});
							 break;
						 case DialogInterface.BUTTON_NEGATIVE:
							 break;
						}
					}
					
				};
				builder.setMessage("Do you wish to remove this shop?").setPositiveButton("Yes", removedialogClickListener).setNegativeButton("No", removedialogClickListener).show();
			}
			
		});
		gMap.setOnMarkerDragListener(new OnMarkerDragListener(){

			@Override
			public void onMarkerDrag(Marker arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMarkerDragEnd(Marker marker) {
				// TODO Auto-generated method stub
				Map<String, Object> updateMap = new HashMap<String, Object>();
				updateMap.put("lat", marker.getPosition().latitude);
				updateMap.put("lng", marker.getPosition().longitude);
			}

			@Override
			public void onMarkerDragStart(Marker arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
        // 4. create & set LocationRequest for Location update
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000 * 5);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000 * 1);
       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_loc, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
		
		if (searchView != null) {
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
		}
		
		searchView.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextChange(String result) {
				// TODO Auto-generated method stub
				
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String result) {
				// TODO Auto-generated method stub
					 Geocoder geocoder = new Geocoder(SetLocActivity.this, Locale.getDefault()); 
					 try {
						 List<Address> address = geocoder.getFromLocationName(result, 1);
						 CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude()), 16);
						 gMap.animateCamera(cameraUpdate); 
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				return false;
			}
			
		});
		return true;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		if(mLocationClient != null)
            mLocationClient.requestLocationUpdates(mLocationRequest,  this);
 
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
         
        if(mLocationClient != null){
            // get location
            mCurrentLocation = mLocationClient.getLastLocation();
                try{
                     
                	CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 16);
					gMap.animateCamera(cameraUpdate); 

                }catch(NullPointerException npe){
                     
                    Toast.makeText(this, "Failed to Connect", Toast.LENGTH_SHORT).show();
 
                    // switch on location service intent
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
        }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mLocationClient.connect();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationClient.disconnect();
	}

	public String getDate(final Firebase ref, final Marker marker){
		
		Calendar c = Calendar.getInstance();
		final int mYear = c.get(Calendar.YEAR);
		final int mMonth = c.get(Calendar.MONTH);
		final int mDay = c.get(Calendar.DAY_OF_MONTH);
    
        	final Map<String, Object> updateMap = new HashMap<String, Object>();
        	updateMap.put("lat", marker.getPosition().latitude);
        	updateMap.put("lng", marker.getPosition().longitude);
			DatePickerDialog dpd = new DatePickerDialog(SetLocActivity.this,  new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					// TODO Auto-generated method stub
					
					if(mYear < year || mYear == year){
						if(mMonth < monthOfYear || mMonth == monthOfYear){
							if(mDay < dayOfMonth || mDay == dayOfMonth){
								
								String date = dayOfMonth+"/"+(monthOfYear+1)+"/"+ year;
								SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
								Date selected_date;
								long epoch = 0;
								try {
									selected_date = sdf.parse(date);
									epoch = selected_date.getTime();
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								updateMap.put("date", epoch);
								updateMap.put("markertitle", marker.getTitle());
								ref.push().setValue(updateMap);
							}else{
								Toast.makeText(getApplicationContext(), "Please check your day", Toast.LENGTH_SHORT).show();
								marker.remove();
							}
						}else{
							Toast.makeText(getApplicationContext(), "Please check your month", Toast.LENGTH_SHORT).show();
							marker.remove();
						}
					}else{
						Toast.makeText(getApplicationContext(), "Please check your year", Toast.LENGTH_SHORT).show();
						marker.remove();
					}
					
				}
			}, mYear, mMonth,  mDay);
			dpd.show();
        
		return null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_addproduct:
			Intent addproductIntent = new Intent(SetLocActivity.this, CreateProductActivity.class);
			addproductIntent.putExtra("userid", userid);
			startActivity(addproductIntent);
			break;
		
		case R.id.action_viewproduct:
			Intent ViewProductListIntent = new Intent(SetLocActivity.this, ProductList.class);
			ViewProductListIntent.putExtra("userid", userid);
			startActivity(ViewProductListIntent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public String getSharedPrefernces(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SetLocActivity.this);
		return pref.getString("userid", "null");
	}
}
