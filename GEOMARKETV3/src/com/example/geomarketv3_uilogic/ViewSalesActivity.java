package com.example.geomarketv3_uilogic;

import java.text.DateFormat;
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
import com.example.geomarketv3_asynctask.GetImageProduct;
import com.example.geomarketv3_asynctask.GetProductList;
import com.example.geomarketv3_asynctask.GetSaleMemberDetail;
import com.example.geomarketv3_fragment.SalesMemberFragment;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
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
    public BootstrapButton GoBtn, FavBtn;
    private static TextView nameTV, titleTV, emailTV, contactTV;
    private ProductAdapter adapter;
    private ArrayList<Product> productList;
    private Cloudinary cloudinary;
    private ListView list;
    private ArrayList<String> favList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_sales);
		mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		fl = (FrameLayout) findViewById(R.id.saleMemberFragment);
		rl = (RelativeLayout) findViewById(R.id.rlView);
		rl.setVisibility(View.GONE);
		userid = getIntent().getStringExtra("userid");
		user = new User();
		favList = new ArrayList<String>();
		getFav(userid, favList);
		productList = new ArrayList<Product>();
		adapter = new ProductAdapter(this, productList);
		list = (ListView) findViewById(R.id.productlistview);
		list.setAdapter(adapter);
		cloudinary = new Cloudinary(Cloudinary.asMap(
				"cloud_name","dfm9692pu",
				"api_key", "443893967666533",
				"api_secret", "uYlUVpAZK405EHc6CsrHF64VVlg"));
		imgIV= (ImageView) findViewById(R.id.imgIV);
		nameTV = (TextView) findViewById(R.id.nameTV);
		titleTV = (TextView) findViewById(R.id.titleTV);
		emailTV = (TextView) findViewById(R.id.emailTV);
		contactTV = (TextView) findViewById(R.id.contactTV);

		GoBtn = (BootstrapButton) findViewById(R.id.GoBtn);
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
					
					 ref = new Firebase("https://mmarketnyp.firebaseio.com/user");
				       ref.addChildEventListener(new ChildEventListener(){

						@Override
						public void onCancelled(FirebaseError arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onChildAdded(DataSnapshot data, String arg1) {
							// TODO Auto-generated method stub
							GetSalesLoc(data);
							GetSalesProduct(data);
						}

						@Override
						public void onChildChanged(DataSnapshot data, String arg1) {
							// TODO Auto-generated method stub
							GetSalesLoc(data);
							GetSalesProduct(data);
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
				       
				       list.setOnTouchListener(new OnTouchListener(){

							@Override
							public boolean onTouch(View v, MotionEvent arg1) {
								// TODO Auto-generated method stub
								v.getParent().requestDisallowInterceptTouchEvent(true);
								return false;
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
		}else{
			Toast.makeText(getApplicationContext(), "You wan to quit? not yet done ahahaha", Toast.LENGTH_SHORT).show();
		}
		
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
		contactTV.setText(Integer.toString(user.getContact()));
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}
	private void GetSalesProduct(DataSnapshot data){
		Map<String, Object> saleUserMaps = (Map<String, Object>) data.getValue();
		if(saleUserMaps.get("role").equals("sales")){
			Map<String, Object> saleProductMaps = (Map<String, Object>) saleUserMaps.get("product");
			if(saleProductMaps != null){
				for(String i: saleProductMaps.keySet()){
					
					Map<String, Object> product = (Map<String, Object>) saleProductMaps.get(i);
					if(product.get("status").toString().equals("active")){
						
						Product item = new Product();
						item.setId(i);
						item.setDetail(product.get("detail").toString());
						item.setName(product.get("name").toString());
						item.setPrice(Double.parseDouble(product.get("price").toString()));
						item.setStatus(product.get("status").toString());
						String url = cloudinary.url().format("jpg").transformation(new Transformation().width(300).crop("fit")).generate(i);
						item.setImgURL(url);
						GetImageProduct getImage = new GetImageProduct(ViewSalesActivity.this,url, item, adapter);
						getImage.execute();	
						productList.add(item);
					}
				}
			}
		}
	}
	private void GetSalesLoc(DataSnapshot data){
		final String userKey = data.getKey();
		Map<String, Object> saleUserMaps = (Map<String, Object>) data.getValue();
		if(saleUserMaps.get("role").equals("sales")){
			
			Map<String, Object> saleLocMaps = (Map<String, Object>) saleUserMaps.get("location");
			if(saleLocMaps != null){
				Date date = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
				
				for(String i: saleLocMaps.keySet()){
						
						Map<String, Object> saleLocMap = (Map<String, Object>) saleLocMaps.get(i);
						
						if(dateFormat.format(date).toString().equals(saleLocMap.get("date").toString())){
							LatLng latlng = new LatLng(Double.parseDouble(saleLocMap.get("lat").toString()), Double.parseDouble(saleLocMap.get("lng").toString()));
							
							marker = gMap.addMarker(new MarkerOptions().position(latlng));
							marker.setTitle(saleUserMaps.get("title").toString());
							marker.setSnippet("sales on Date: " + saleLocMap.get("date").toString());
							marker.showInfoWindow();
							
							
							gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){
	
								@Override
								public void onInfoWindowClick(final Marker marker) {
									// TODO Auto-generated method stub
									if(rl.getVisibility() == View.GONE){
										GetSaleMemberDetail getSaleMem = new GetSaleMemberDetail(ViewSalesActivity.this, marker.getTitle());
										getSaleMem.execute();
										final double lat = marker.getPosition().latitude;
										final double lng = marker.getPosition().longitude;
										if(favList.size() > 0){
											for(int a=0; a<favList.size(); a++){
												if(favList.get(a).equals(userKey)){
													FavBtn.setBootstrapType("success");
												}
											}
										}
										
										FavBtn.setOnClickListener(new OnClickListener(){

											@Override
											public void onClick(View view) {
												// TODO Auto-generated method stub
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
								        	
								        });
										
										
										GoBtn.setOnClickListener(new OnClickListener(){

											@Override
											public void onClick(View view) {
												// TODO Auto-generated method stub
												Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+lat+","+lng+"&daddr=1.379468,103.848559"));
												
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
	
	private void getFav(String userid, final ArrayList<String> favList){
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
				favList.add(userFavMaps.get("userfav").toString());
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
	
}
