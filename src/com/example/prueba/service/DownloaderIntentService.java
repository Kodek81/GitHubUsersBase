package com.example.prueba.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.prueba.R;
import com.example.prueba.UserListActivity;
import com.example.prueba.conexions.ConnectionClass;
import com.example.prueba.data.UserDatabase;
import com.example.prueba.data.UserProvider;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class DownloaderIntentService extends IntentService {
    private static final String SERVICE_NAME = "DownloadService";
    private static final String TAG = UserListActivity.class.getSimpleName();
    private static String url = "https://api.github.com/users";
    private static final int LIST_UPDATE_NOTIFICATION = 100;
    public static void requestDownload(Context context, Uri uri) {
        Intent intent = new Intent(context, DownloaderIntentService.class);
        intent.setData(uri);
        context.startService(intent);
    }

    public DownloaderIntentService() {
        super(SERVICE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        download();
    }

    private void download() {
        boolean succeeded = false;
        ConnectionClass sh = new ConnectionClass();
        // Making a request to url and getting response
        String where = "";
        String [] args = {};
        
        	String jsonStr = sh.getJson(url);
            if (jsonStr != null) {
            	JSONArray users;
            	try {
            		users = new JSONArray(jsonStr);
            		getContentResolver().delete(
                            UserProvider.CONTENT_URI,where,args
                            );
            		insertAll(users);   
            	} catch (JSONException e) {
            			Log.e(TAG, "error in converting to JSONArray");
            	}
         
            } else {
            		Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
       
    }
    private void sendNotification(boolean result)
    {      
    	Context context = DownloaderIntentService.this.getApplicationContext();
    	Notification updateComplete;
    	NotificationManager notificationManager = (NotificationManager) context
    			.getSystemService(NOTIFICATION_SERVICE);

    	final int sdkVersion = Build.VERSION.SDK_INT;

    	String contentTitle = context.getText(R.string.notification_title)
    			.toString();

    	String contentText;

    	if (!result) {
    		Log.w(TAG, " The update had errors");
    		contentText = context.getText(R.string.notification_info_fail)
    				.toString();
    	} else {
    		contentText = context.getText(
    				R.string.notification_info_success).toString();
    	}


    	updateComplete = new Notification();
    	updateComplete.icon = android.R.drawable.stat_notify_sync;
    	updateComplete.tickerText = context
    			.getText(R.string.notification_title);
    	updateComplete.when = System.currentTimeMillis();

    	Intent notificationIntent = new Intent(context,
    			UserListActivity.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
    			notificationIntent, 0);
    	notificationManager
    	.notify(LIST_UPDATE_NOTIFICATION, updateComplete);}
    
    private boolean insertAll(JSONArray users)
    {
    	int i =0;
    	
    	boolean result=false;
    	try
    	{
    		while (i < users.length())
    		{ 
    			ContentValues userData = new ContentValues();
    			JSONObject c = users.getJSONObject(i);
    			String id = (String.valueOf(i));
    			String name = c.getString(UserDatabase.C_LOGIN);
    			String url = c.getString(UserDatabase.C_URL);
    			String html_url = c.getString(UserDatabase.C_HTML_URL);
    			String repos_url = c.getString(UserDatabase.C_REPOS_URL);
    			String events_url= c.getString(UserDatabase.C_EVENTS_URL);            
		  	  
    			userData.put(UserDatabase.C_ID, id);
    			userData.put(UserDatabase.C_LOGIN, name);
		  	  	userData.put(UserDatabase.C_URL, url);
		  	  	userData.put(UserDatabase.C_HTML_URL, html_url);
		  	  	userData.put(UserDatabase.C_REPOS_URL, repos_url);
		  	  	Log.e (TAG,"kepa " + id);
		  	
		  	  	getContentResolver().insert(
                UserProvider.CONTENT_URI,
                userData);
		  	  	i++;
    	  } 
    		result=true;
    	}
    	catch (JSONException e) {
              Log.e(TAG, "Error during parsing", e);
    	  }
   
    return result;
    
    }
}