package com.example.geomarketv3_uilogic;

import java.util.ArrayList;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetSaleMemberDetail;
import com.example.geomarketv3_fragment.SalesMemberFragment;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.User;
import com.geomarketv3.geofencing.GeofenceRequester;
import com.geomarketv3.geofencing.SimpleGeofence;
import com.geomarketv3.geofencing.SimpleGeofenceStore;
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
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSalesActivity extends FragmentActivity implements  GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
LocationListener{
	private GoogleMap gMap;
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private Firebase ref;
    private SimpleGeofenceStore mPrefs;
    private List<Geofence> mCurrentGeofences;
    private GeofenceRequester mGeofenceRequester;
	private SimpleGeofence UiGeofence;
	private LatLng current_location;
	private int radius = 1000;
	private ArrayList<Marker> markerList;
	private Marker marker;
    private MapFragment mf;
    private FrameLayout fl;
    private RelativeLayout rl;
    private static ImageView imgIV;
    public static Bitmap bitImage = null;
    public static User user;
    public BootstrapButton GoBtn;
    private static TextView nameTV, titleTV, emailTV, contactTV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_sales);
		mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		fl = (FrameLayout) findViewById(R.id.saleMemberFragment);
		rl = (RelativeLayout) findViewById(R.id.rlView);
		rl.setVisibility(View.GONE);
		user = new User();
		//rl.getLayoutParams().height = 800;
		imgIV= (ImageView) findViewById(R.id.imgIV);
		nameTV = (TextView) findViewById(R.id.nameTV);
		titleTV = (TextView) findViewById(R.id.titleTV);
		emailTV = (TextView) findViewById(R.id.emailTV);
		contactTV = (TextView) findViewById(R.id.contactTV);
		GoBtn = (BootstrapButton) findViewById(R.id.GoBtn);
		markerList = new ArrayList<Marker>();
		gMap = mf.getMap();
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setCompassEnabled(true);
		mCurrentGeofences = new ArrayList<Geofence>();
		mPrefs = new SimpleGeofenceStore(ViewSalesActivity.this);
		mGeofenceRequester = new GeofenceRequester(ViewSalesActivity.this);
		mLocationClient = new LocationClient(this, this, this);
		 
        // 4. create & set LocationRequest for Location update
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(1000 * 5);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(1000 * 1);
        ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
        ref.addChildEventListener(new ChildEventListener(){

			@Override
			public void onCancelled(FirebaseError error) {
				// TODO Auto-generated method stub
				System.out.println(error);
			}

			@Override
			public void onChildAdded(DataSnapshot snapshot, String str) {
				// TODO Auto-generated method stub
				Map<String, Object> addSalePMaps = (Map<String, Object>) snapshot.getValue();
				if(addSalePMaps.get("role").equals("sales")){
					
					double lat = Double.parseDouble(addSalePMaps.get("lat").toString());
					double lng = Double.parseDouble(addSalePMaps.get("lng").toString());
					if(lat != 0 && lng != 0){
						marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(addSalePMaps.get("name").toString()+ " " +addSalePMaps.get("title").toString()).snippet("Click here to view the offer!!"));
						marker.showInfoWindow();
						markerList.add(marker);
						gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

							@Override
							public void onInfoWindowClick(Marker marker) {
								// TODO Auto-generated method stub
								if(rl.getVisibility() == View.GONE){
									//savePreferences("salesid", marker.getTitle());
									GetSaleMemberDetail getMem = new GetSaleMemberDetail(ViewSalesActivity.this, marker.getTitle());
									getMem.execute();
									rl.setVisibility(View.VISIBLE);
								}
								//Fragment SalesMemFragment = new SalesMemberFragment();
								//ViewSalesActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.saleMemberFragment, SalesMemFragment, "salesMember").commit();
							
							}
							
						});
						UiGeofence = new SimpleGeofence(addSalePMaps.get("title").toString(), lat, lng, radius,Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER);
						mPrefs.setGeofence("There is some offer Near you find them now!!!", UiGeofence);
						mCurrentGeofences.add(UiGeofence.toGeofence());
						try{
							mGeofenceRequester.addGeofences(mCurrentGeofences, "GeoMarket", "there is a offer near you!! Don't miss it", 0);
							
						}catch(UnsupportedOperationException e){
	
							Toast.makeText(getApplicationContext(), R.string.add_geofences_already_requested_error, Toast.LENGTH_LONG).show();
						}
					}
				}
			}

			@Override
			public void onChildChanged(DataSnapshot snapshot, String str) {
				// TODO Auto-generated method stub
					Map<String, Object> addSalePMaps = (Map<String, Object>) snapshot.getValue();
									
									if(addSalePMaps.get("role").equals("sales")){
										double lat = Double.parseDouble(addSalePMaps.get("lat").toString());
										double lng = Double.parseDouble(addSalePMaps.get("lng").toString());
										
										if(lat != 0 && lng != 0){
											marker = gMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(addSalePMaps.get("name").toString()+ " " +addSalePMaps.get("title").toString()).snippet("Click here to view the offer!!"));
											marker.showInfoWindow();
											markerList.add(marker);
											
											gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
						
												@Override
												public void onInfoWindowClick(Marker marker) {
													// TODO Auto-generated method stub
													if(rl.getVisibility() == View.GONE){
														//savePreferences("salesid", marker.getTitle());
														GetSaleMemberDetail getMem = new GetSaleMemberDetail(ViewSalesActivity.this, marker.getTitle());
														getMem.execute();
														rl.setVisibility(View.VISIBLE);
													}
													//Fragment SalesMemFragment = new SalesMemberFragment();
													//ViewSalesActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.saleMemberFragment, SalesMemFragment, "salesMember").commit();
												}
												
											});
											
											UiGeofence = new SimpleGeofence(addSalePMaps.get("title").toString(), lat, lng, radius,Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER);
											mPrefs.setGeofence("There is some offer Near you find them now!!!", UiGeofence);
											mCurrentGeofences.add(UiGeofence.toGeofence());
											try{
												mGeofenceRequester.addGeofences(mCurrentGeofences, "GeoMarket", "there is a offer near you!! Don't miss it", 0);
												
											}catch(UnsupportedOperationException e){
						
												Toast.makeText(getApplicationContext(), R.string.add_geofences_already_requested_error, Toast.LENGTH_LONG).show();
											}
										}else{
											System.out.println("removing " + addSalePMaps.get("title"));
											
											for(Marker marker : markerList){
												System.out.println("marker title 1 " + marker.getTitle()+ " " + addSalePMaps.get("title"));
												String title = marker.getTitle().substring(marker.getTitle().indexOf(" ")+1, marker.getTitle().length());
												System.out.println(title);
												if(title.equals(addSalePMaps.get("title").toString())){
													System.out.println("marker title 2 " + marker.getTitle());
													marker.remove();
													markerList.remove(marker);
													for(int i=0; i<mCurrentGeofences.size(); i++){
														if(mCurrentGeofences.get(i).getRequestId().equals(addSalePMaps.get("title"))){
															mCurrentGeofences.remove(i);
														}
													}
												}
											}
											
											
										}
									}
				
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
       
        GoBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=1.379268,103.849559&daddr=1.379468,103.848559"));
				
				intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
				//startActivity(intent);
				ViewSalesActivity.this.startActivityForResult(intent, 1);
			}
        	
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_sales, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
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

	@Override
	public void onBackPressed() {
		if(rl.getVisibility() == View.VISIBLE){
			rl.setVisibility(View.GONE);
		}else{
			Toast.makeText(getApplicationContext(), "You wan to quit? not yet done ahahaha", Toast.LENGTH_SHORT).show();
		}
		/*
		Fragment fragment = ViewSalesActivity.this.getSupportFragmentManager().findFragmentByTag("salesMember");
		if(fragment != null){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.remove(fragment);
			ft.commit();
		}else{
			Toast.makeText(getApplicationContext(), "You wan to quit? not yet done ahahaha", Toast.LENGTH_LONG).show();
		}*/
	}
	
	private void savePreferences(String key, String value){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	Editor edit = sp.edit();
    	edit.putString(key, value);
    	edit.commit();
    }
	
	public static void setImage(){
		imgIV.setImageBitmap(bitImage);
	}
	public static void setUser(){
		titleTV.setText(user.getTitle());
		nameTV.setText(user.getName());
		emailTV.setText(user.getEmail());
		System.out.println("contact " + user.getContact());
		contactTV.setText(Integer.toString(user.getContact()));
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
}
