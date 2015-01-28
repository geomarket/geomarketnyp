package com.geomarketv3.geofencing;

import java.util.ArrayList;


import java.util.List;



import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.geomarketv3.R;
import com.example.geomarketv3_uilogic.ViewSalesActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;


/**
 * This class receives geofence transition events from Location Services, in the
 * form of an Intent containing the transition type and geofence id(s) that triggered
 * the event.
 */
public class ReceiveTransitionsIntentService extends IntentService {
	private List<String> prefList;
	private String[] array;
	private String arrayName = "locArray";
    /**
     * Sets an identifier for this class' background thread
     */
    public ReceiveTransitionsIntentService() {
        super("ReceiveTransitionsIntentService");
    }

    /**
     * Handles incoming intents
     * @param intent The Intent sent by Location Services. This Intent is provided
     * to Location Services (inside a PendingIntent) when you call addGeofences()
     */
    @Override
    protected void onHandleIntent(Intent intent) {
    	int count =0;
    	prefList = new ArrayList<String>();
        // Create a local broadcast Intent
        Intent broadcastIntent = new Intent();

        // Give it the category for all intents sent by the Intent Service
        broadcastIntent.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

        // First check for errors
        if (LocationClient.hasError(intent)) {

            // Get the error code
            int errorCode = LocationClient.getErrorCode(intent);

            // Get the error message
            String errorMessage = LocationServiceErrorMessages.getErrorString(this, errorCode);

            // Log the error
            Log.e(GeofenceUtils.APPTAG,
                    getString(R.string.geofence_transition_error_detail, errorMessage)
            );

            // Set the action and error message for the broadcast intent
            broadcastIntent.setAction(GeofenceUtils.ACTION_GEOFENCE_ERROR)
                           .putExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS, errorMessage);

            // Broadcast the error *locally* to other components in this app
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

        // If there's no error, get the transition type and create a notification
        } else {

            // Get the type of transition (entry or exit)
            int transition = LocationClient.getGeofenceTransition(intent);
            if(transition == Geofence.GEOFENCE_TRANSITION_ENTER){
            	System.out.println("tranisition " + transition);
            }
            // Test that a valid transition was reported
            if (
                    (transition == Geofence.GEOFENCE_TRANSITION_ENTER)
                    ||
                    (transition == Geofence.GEOFENCE_TRANSITION_EXIT)
               ) {

                // Post a notification
                List<Geofence> geofences = LocationClient.getTriggeringGeofences(intent);
                System.out.println("geofences size " + geofences.size());
                String[] geofenceIds = new String[geofences.size()];
                int currentNoOfGeofences = 0;
                String eol = System.getProperty("line.separator");  
                String contentText = "";
                count = geofences.size();
                array = new String[count];
                
                for (int index = 0; index < geofences.size() ; index++) {
                    geofenceIds[index] = geofences.get(index).getRequestId();
                    currentNoOfGeofences += 1;          
                    contentText += geofences.get(index).getRequestId() + eol;
                }
                
                String ids = TextUtils.join(GeofenceUtils.GEOFENCE_ID_DELIMITER,geofenceIds);
                String transitionType = getTransitionString(transition);
                
                if(transitionType == getString(R.string.geofence_transition_entered)){
                	
                	 String locID = ids.substring(0,ids.indexOf(" "));
                	 
                		 savePreferences("geoID", locID);
                		 System.out.println(count);
                		 for(int i=0; i< count; i++){
                			 
                			 array[i] = locID;
                		 }
                		 saveArray(array, arrayName, this);
                }else if(transitionType == getString(R.string.geofence_transition_exited)){
                	Toast.makeText(getApplicationContext(), "exited", Toast.LENGTH_LONG).show();
                	Log.d("geofencing", "exited");
                	SharedPreferences preferences = getSharedPreferences("result", 0);
                	String value = preferences.getString("geoID", null);
                	if(value == null){
                		Toast.makeText(getApplicationContext(), "No SharedPreferences found!", Toast.LENGTH_LONG).show();
                	}else{
                		Toast.makeText(getApplicationContext(), "SharedPreferences found!", Toast.LENGTH_LONG).show();
                		clearPrefernces("geoID");
                	}
                }
                
                String contentTitle = currentNoOfGeofences + intent.getExtras().getString("contentTitle");
                int GeoFenceID = intent.getExtras().getInt("GeoFenceID");
                //String contentText = intent.getExtras().getString("contentText");
                
                sendNotification(transitionType, ids, contentTitle,geofenceIds, GeoFenceID);

                // Log the transition type and a message
                Log.d(GeofenceUtils.APPTAG,
                        getString(
                                R.string.geofence_transition_notification_title,
                                transitionType,
                                ids));
                Log.d(GeofenceUtils.APPTAG,
                        getString(R.string.geofence_transition_notification_text));

            // An invalid transition was reported
            } else {
                // Always log as an error
                Log.e(GeofenceUtils.APPTAG,
                        getString(R.string.geofence_transition_invalid_type, transition));
            }
        }
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the main Activity.
     * @param transitionType The type of transition that occurred.
     *
     */
    private void sendNotification(String transitionType, String ids, String contentTitle, String[] contentText, int GeoFenceID) {
    	NotificationCompat.Builder  mBuilder = 
    		      new NotificationCompat.Builder(this);	
    			
    		
    	
    		if(GeoFenceID == 0){
    			
    			mBuilder.setContentTitle("GeoMarket");
    		      mBuilder.setContentText("There is a SALES nearby you don't miss it!!!");
    		      mBuilder.setTicker("NEARBY SALES DETECED!!!!");
    		      mBuilder.setSmallIcon(R.drawable.ic_launcher);

    		      /* Increase notification number every time a new notification arrives */


    		      /* Add Big View Specific Configuration */
    		      NotificationCompat.InboxStyle inboxStyle =
    		             new NotificationCompat.InboxStyle();
    		      
    		      // Sets a title for the Inbox style big view
    		      inboxStyle.setBigContentTitle("GeoMarket Offer!!");
    		      // Moves events into the big view
    		      for (int i=0; i < contentText.length; i++) {

    		         inboxStyle.addLine(contentText[i]);
    		      }
    		      mBuilder.setStyle(inboxStyle);
    		       
    		      
    		      /* Creates an explicit intent for an Activity in your app */
    		     Intent resultIntent = new Intent(this, ViewSalesActivity.class);

    		      TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    		      stackBuilder.addParentStack(ViewSalesActivity.class);

    		      /* Adds the Intent that starts the Activity to the top of the stack */
    		      stackBuilder.addNextIntent(resultIntent);
    		      PendingIntent resultPendingIntent =
    		         stackBuilder.getPendingIntent(
    		            0,
    		            PendingIntent.FLAG_UPDATE_CURRENT
    		         );

    		      mBuilder.setContentIntent(resultPendingIntent);

    		      NotificationManager mNotificationManager =
    		      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    		      /* notificationID allows you to update the notification later on. */
    		      mNotificationManager.notify(0, mBuilder.build());
    		}      
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
       
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.geofence_transition_entered);

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.geofence_transition_exited);

            default:
                return getString(R.string.geofence_transition_unknown);
        }
    }
    
    
    private void savePreferences(String key, String value){
    	System.out.println("test pref" + key +" "+ value);
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	Editor edit = sp.edit();
    	edit.putString(key, value);
    	edit.commit();
    }
    
   
    private boolean saveArray(String[] array, String arrayName, Context mContext){
    	SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putInt(arrayName + "_size", array.length);
    	for(int i=0; i< array.length; i++){
    		editor.putString(arrayName + "_" + i, array[i]);
    	}
    	return editor.commit();
    }
    private void clearPrefernces(String key){
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	SharedPreferences.Editor editor = sp.edit();
    	editor.remove(key).commit();
    }
}
