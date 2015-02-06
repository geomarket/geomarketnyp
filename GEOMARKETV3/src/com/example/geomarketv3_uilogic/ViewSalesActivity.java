package com.example.geomarketv3_uilogic;

import java.lang.reflect.Type;

import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.example.geomarketv3.ProductAdapter;
import com.example.geomarketv3.R;
import com.example.geomarketv3.R.layout;
import com.example.geomarketv3.R.menu;
import com.example.geomarketv3_asynctask.GetImage;
import com.example.geomarketv3_asynctask.GetImageProduct;
import com.example.geomarketv3_asynctask.GetProductList;
import com.example.geomarketv3_asynctask.GetSaleMemberDetail;
import com.example.geomarketv3_fragment.SalesMemberFragment;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.geomarketv3.entity.FavItem;
import com.geomarketv3.entity.Product;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSalesActivity extends FragmentActivity implements  GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, 
LocationListener{
	private GoogleMap gMap;
	private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private Firebase ref, updateFav, updateSaleFav;
    private String userid;
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
    public BootstrapButton GoBtn, FavBtn, ViewBtn;
    private static TextView nameTV, titleTV, emailTV, contactTV;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private Cloudinary cloudinary;
    private ListView list;
    private ArrayList<FavItem> favList;
    private NotificationManager notifyManager;
    private NotificationCompat.Builder notifyBuilder;
    private int MY_NOTIFICATION_ID = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_sales);
		Firebase.setAndroidContext(this);
		mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		fl = (FrameLayout) findViewById(R.id.saleMemberFragment);
		rl = (RelativeLayout) findViewById(R.id.rlView);
		rl.setVisibility(View.GONE);
		userid = getSharedPrefernces();
		user = new User();
		favList = new ArrayList<FavItem>();
		getFav(userid, favList);
		productList = new ArrayList<Product>();
		mCurrentGeofences = new ArrayList<Geofence>();
		mPrefs = new SimpleGeofenceStore(ViewSalesActivity.this);
		mGeofenceRequester = new GeofenceRequester(ViewSalesActivity.this);
		
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
		
		imgIV= (ImageView) findViewById(R.id.imgIV);
		nameTV = (TextView) findViewById(R.id.nameTV);
		titleTV = (TextView) findViewById(R.id.titleTV);
		emailTV = (TextView) findViewById(R.id.emailTV);
		contactTV = (TextView) findViewById(R.id.contactTV);
		ViewBtn = (BootstrapButton) findViewById(R.id.viewBtn);
		GoBtn = (BootstrapButton) findViewById(R.id.GoBtn);
		GoBtn.setVisibility(View.GONE);
		FavBtn = (BootstrapButton) findViewById(R.id.favBTN);
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_sales, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search_icon).getActionView();
		if (searchView != null) {
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
				for(int i=0; i< markerList.size(); i++){
					String str = markerList.get(i).getTitle().toLowerCase();
					if(str.contains(result)){
						CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(markerList.get(i).getPosition(), 16);
						gMap.animateCamera(cameraUpdate); 
					}
				}
				return false;
			}
			
		});
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_cart:
			if(GetArrayListSharedPreferenced() != null){
				Intent intent = new Intent(this, ShoppingCart.class);
				intent.putExtra("userid", userid);
				startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "You have nothing in your cart", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.action_view_date:
			DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
					// TODO Auto-generated method stub
					gMap.clear();
					markerList.clear();
					GetSalesLocByDate(year,monthOfYear+1, dayOfMonth);
				}
			}, 2015, 1, 11);
			
			dpd.show();
			break;
		}
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
					
					   ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
				       ref.addChildEventListener(new ChildEventListener(){

						@Override
						public void onCancelled(FirebaseError arg0) {
							// TODO Auto-generated method stub
							System.out.println(arg0); 
						}

						@Override
						public void onChildAdded(DataSnapshot data, String arg1) {
							// TODO Auto-generated method stub
							GetSalesLoc(data, mCurrentLocation);
						}

						@Override
						public void onChildChanged(DataSnapshot data, String arg1) {
							// TODO Auto-generated method stub
							GetSalesLoc(data, mCurrentLocation);	
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
			GoBtn.setVisibility(View.GONE);
		}else{
			Toast.makeText(getApplicationContext(), "You wan to quit? not yet done ahahaha", Toast.LENGTH_SHORT).show();
		}
		
	}
		
	public static void setImage(){
		imgIV.setImageBitmap(bitImage);
	}
	public static void setUser(){
		titleTV.setText(user.getTitle());
		nameTV.setText(user.getName());
		emailTV.setText(user.getEmail());
		contactTV.setText(Integer.toString(user.getContact()));
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void GetSalesLoc(DataSnapshot data, final Location mCurrentLocation){
		final String userKey = data.getKey();
		Map<String, Object> saleUserMaps = (Map<String, Object>) data.getValue();
		if(saleUserMaps.get("role").equals("sales")){
			Map<String, Object> saleLocMaps = (Map<String, Object>) saleUserMaps.get("location");
			if(saleLocMaps != null){
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				for(String i: saleLocMaps.keySet()){
						
						Map<String, Object> saleLocMap = (Map<String, Object>) saleLocMaps.get(i);
							String currentDate = dateFormat.format(date);
							
							Long epoch = Long.parseLong(saleLocMap.get("date").toString());
							Date convertdate = new Date(epoch);
							String saleDate = dateFormat.format(convertdate);
							
						if(currentDate.equals(saleDate)){
							LatLng latlng = new LatLng(Double.parseDouble(saleLocMap.get("lat").toString()), Double.parseDouble(saleLocMap.get("lng").toString()));
							
							marker = gMap.addMarker(new MarkerOptions().position(latlng));
							marker.setTitle(saleUserMaps.get("title").toString());
							marker.setSnippet("sales on Date: " + saleDate);
							
							marker.showInfoWindow();
							markerList.add(marker);
							UiGeofence = new SimpleGeofence(i + " has some offer faster find them!!", latlng.latitude, latlng.longitude, radius,Geofence.NEVER_EXPIRE, Geofence.GEOFENCE_TRANSITION_ENTER);
							mPrefs.setGeofence("There is some offer Near you find them now!!!", UiGeofence);
							mCurrentGeofences.add(UiGeofence.toGeofence());
							try{
								mGeofenceRequester.addGeofences(mCurrentGeofences, "HELLO", "there is a offer near you!! test", 0);
								
							}catch(UnsupportedOperationException e){

								Toast.makeText(getApplicationContext(), R.string.add_geofences_already_requested_error, Toast.LENGTH_LONG).show();
							}
							ViewBtn.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(ViewSalesActivity.this, ProductList_User.class);
									intent.putExtra("userid", userKey);
									intent.putExtra("key", user.getId());
									startActivity(intent);
								}
								
							});
							gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
	
								@Override
								public void onInfoWindowClick(final Marker marker) {
									// TODO Auto-generated method stub
									if(rl.getVisibility() == View.GONE){
										GoBtn.setVisibility(View.VISIBLE);
										getSaler(marker.getTitle());
										
										final double lat = marker.getPosition().latitude;
										final double lng = marker.getPosition().longitude;
										if(favList.size() > 0){
											for(int a=0; a<favList.size(); a++){
												if(favList.get(a).getFavID().equals(userKey)){
													setNotification(marker.getTitle());
													FavBtn.setBootstrapType("success");
												}
											}
										}
										
										FavBtn.setOnClickListener(new OnClickListener(){

											@Override
											public void onClick(View view) {
												// TODO Auto-generated method stub
												if(favList.size() > 0){
													for(int a=0; a<favList.size(); a++){
														if(favList.get(a).getFavID().equals(userKey)){
															String url = "https://mmarketnyp.firebaseio.com/user/"+ userid+"/favourite/" + favList.get(a).getId();
															updateFav = new Firebase(url);
															updateFav.removeValue();
															favList.remove(a);
															FavBtn.setBootstrapType("primary");
														}else{
															String url = "https://mmarketnyp.firebaseio.com/user/"+ userid;
															updateFav = new Firebase(url);
															Firebase FavRef = updateFav.child("favourite");
															Date date = new Date();
															
																Long epoch = date.getTime();
															
															
															Map<String, String> favMap = new HashMap<String, String>();
															favMap.put("userfav", userKey);
															favMap.put("date", epoch.toString());
															FavRef.push().setValue(favMap);
															FavBtn.setBootstrapType("success");
														}
													}
												}else{
													String url = "https://mmarketnyp.firebaseio.com/user/"+ userid;
													updateFav = new Firebase(url);
													Firebase FavRef = updateFav.child("favourite");
													Date date = new Date();
													SimpleDateFormat curFormater = new SimpleDateFormat("dd/M/yyyy"); 
													Map<String, String> favMap = new HashMap<String, String>();
													favMap.put("userfav", userKey);
													favMap.put("date", curFormater.format(date));
													FavRef.push().setValue(favMap);
													FavBtn.setBootstrapType("success");
												}
											}
								        	
								        });
										
										
										GoBtn.setOnClickListener(new OnClickListener(){

											@Override
											public void onClick(View view) {
												// TODO Auto-generated method stub
												Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+lat+","+lng+"&daddr="+mCurrentLocation.getLatitude()+","+ mCurrentLocation.getLongitude()));
												intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
												ViewSalesActivity.this.startActivityForResult(intent, 1);
											}
								        	
								        });
										rl.setVisibility(View.VISIBLE);
									}
								}
								
							});
						}else{
							System.out.println("not today date: " + dateFormat.format(date));
						}
						
				}
			}
		}
		
	}
	
	private void GetSalesLocByDate(int year, int month, int day){
		ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
		final int Year = year;
		final int Month = month;
		final int Day = day;
		ref.addChildEventListener(new ChildEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildAdded(DataSnapshot snapshot, String str) {
				// TODO Auto-generated method stub
				final String userKey = snapshot.getKey();
				Map<String, Object> UserMaps = (Map<String, Object>) snapshot.getValue();
				if(UserMaps.get("role").equals("sales")){
					for(String i : UserMaps.keySet()){
						if(i.equals("location")){
							Map<String, Object> locMaps = (Map<String, Object>) UserMaps.get(i);
							for(String loc : locMaps.keySet()){
								Map<String, Object> locMap = (Map<String, Object>) locMaps.get(loc);
								DateFormat dF = new SimpleDateFormat("dd/mm/yyyy");
								String strDate = null;
								if(Month< 10){
									strDate = Day+"/0"+Month+"/"+Year;
								}else{
									strDate = Day+"/"+Month+"/"+Year;
								}
								Date date = new Date(Long.parseLong(locMap.get("date").toString()));
								String strdate = dF.format(date);
								if(strdate.equals(strDate)){
									LatLng latlng = new LatLng(Double.parseDouble(locMap.get("lat").toString()), Double.parseDouble(locMap.get("lng").toString()));
									marker = gMap.addMarker(new MarkerOptions().position(latlng));
									marker.setTitle(UserMaps.get("title").toString());
									marker.setSnippet("sales on Date: " + strdate);
									
									marker.showInfoWindow();
									markerList.add(marker);
									
									gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
										
										@Override
										public void onInfoWindowClick(final Marker marker) {
											// TODO Auto-generated method stub
											if(rl.getVisibility() == View.GONE){
												GoBtn.setVisibility(View.VISIBLE);
												getSaler(marker.getTitle());
												
												final double lat = marker.getPosition().latitude;
												final double lng = marker.getPosition().longitude;
												if(favList.size() > 0){
													for(int a=0; a<favList.size(); a++){
														if(favList.get(a).getFavID().equals(userKey)){
															setNotification(marker.getTitle());
															FavBtn.setBootstrapType("success");
														}
													}
												}
												
												ViewBtn.setOnClickListener(new OnClickListener(){

													@Override
													public void onClick(View v) {
														// TODO Auto-generated method stub
														Intent intent = new Intent(ViewSalesActivity.this, ProductList_User.class);
														intent.putExtra("userid", userKey);
														intent.putExtra("key", user.getId());
														startActivity(intent);
													}
													
												});
												
												FavBtn.setOnClickListener(new OnClickListener(){

													@Override
													public void onClick(View view) {
														// TODO Auto-generated method stub
														if(favList.size() > 0){
															for(int a=0; a<favList.size(); a++){
																if(favList.get(a).getFavID().equals(userKey)){
																	String url = "https://mmarketnyp.firebaseio.com/user/"+ userid+"/favourite/" + favList.get(a).getId();
																	updateFav = new Firebase(url);
																	updateFav.removeValue();
																	favList.remove(a);
																	FavBtn.setBootstrapType("primary");
																}else{
																	String url = "https://mmarketnyp.firebaseio.com/user/"+ userid;
																	updateFav = new Firebase(url);
																	Firebase FavRef = updateFav.child("favourite");
																	Date date = new Date();
																	
																		Long epoch = date.getTime();
																	
																	
																	Map<String, String> favMap = new HashMap<String, String>();
																	favMap.put("userfav", userKey);
																	favMap.put("date", epoch.toString());
																	FavRef.push().setValue(favMap);
																	FavBtn.setBootstrapType("success");
																}
															}
														}else{
															String url = "https://mmarketnyp.firebaseio.com/user/"+ userid;
															updateFav = new Firebase(url);
															Firebase FavRef = updateFav.child("favourite");
															Date date = new Date();
															SimpleDateFormat curFormater = new SimpleDateFormat("dd/M/yyyy"); 
															Map<String, String> favMap = new HashMap<String, String>();
															favMap.put("userfav", userKey);
															favMap.put("date", curFormater.format(date));
															FavRef.push().setValue(favMap);
															FavBtn.setBootstrapType("success");
														}
													}
										        	
										        });
												
												
												GoBtn.setOnClickListener(new OnClickListener(){

													@Override
													public void onClick(View view) {
														// TODO Auto-generated method stub
														Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+lat+","+lng+"&daddr="+mCurrentLocation.getLatitude()+","+ mCurrentLocation.getLongitude()));
														intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
														ViewSalesActivity.this.startActivityForResult(intent, 1);
													}
										        	
										        });
												rl.setVisibility(View.VISIBLE);
											}
										}
										
									});
								}
							}
						}
					}
				}
			}	

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
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
	}
	private void getFav(String userid, final ArrayList<FavItem> favList){
		String url = "https://mmarketnyp.firebaseio.com/user/"+ userid+"/favourite";
		Firebase ref = new Firebase(url);
		ref.addChildEventListener(new ChildEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildAdded(DataSnapshot data, String arg1) {
				// TODO Auto-generated method stub
				Map<String, Object> userFavMaps = (Map<String, Object>) data.getValue();
				FavItem item = new FavItem();
				item.setId(data.getKey());
				item.setFavID(userFavMaps.get("userfav").toString());
				item.setDate(userFavMaps.get("date").toString());
				favList.add(item);
			}

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
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
	}
	
	private void setNotification(String salename){
		notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifyBuilder = new NotificationCompat.Builder(getApplicationContext());
		
		notifyBuilder.setContentTitle("GeoMarket " + salename);
		notifyBuilder.setContentText("Your favourite sale is on don't miss them!!");
		notifyBuilder.setSmallIcon(R.drawable.ic_launcher);
		notifyBuilder.setTicker("FAVOURITE SALES DETECED!!!!");
		//PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(ViewSalesActivity.this, ViewSalesActivity.class), 0);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				notifyManager.notify(MY_NOTIFICATION_ID, notifyBuilder.build());
				
			}
			
		}).start();
	}
	
	public String getSharedPrefernces(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ViewSalesActivity.this);
		return pref.getString("userid", "null");
	}
	
	private void getSaler(String title){
		final String markerTitle = title;
		ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
		
		ref.addValueEventListener(new ValueEventListener(){

			@Override
			public void onCancelled(FirebaseError error) {
				// TODO Auto-generated method stub
				Toast.makeText(ViewSalesActivity.this, error.toString(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onDataChange(DataSnapshot snapshot) {
				// TODO Auto-generated method stub
				
				
				Map<String, Object> usersMaps = (Map<String, Object>) snapshot.getValue();
				
				
				
				for(String i : usersMaps.keySet()){
					
					Map<String, Object> userMap = (Map<String, Object>) usersMaps.get(i);
					
					if(userMap.get("role").toString().equals("sales")){
						if(userMap.get("title").toString().equals(markerTitle)){
								String imgurl = cloudinary.url().format("jpg").transformation(new Transformation().width(imgIV.getWidth()).crop("fit")).generate(i);
								
								GetImage getImg = new GetImage();
								getImg.imgurl = imgurl;
								getImg.execute();	
								
								ViewSalesActivity.user.setTitle(userMap.get("title").toString());
								ViewSalesActivity.user.setId(i);
								ViewSalesActivity.user.setName(userMap.get("name").toString());
								ViewSalesActivity.user.setEmail(userMap.get("email").toString());
								ViewSalesActivity.user.setContact(Integer.parseInt(userMap.get("contact").toString()));
						}
					}
				}
				ViewSalesActivity.setUser();
				
			}
			
		});
	}
	
	private List<Product> GetArrayListSharedPreferenced(){
		 SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		 Editor prefsEditor = appSharedPrefs.edit();
	      Gson gson = new Gson();
	      String json = appSharedPrefs.getString("MyProductArray", null);
	      Type type = new TypeToken<List<Product>>(){}.getType();
	      List<Product> productList = gson.fromJson(json, type);
	      return productList;
	}
	
}	
