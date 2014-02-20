/*
 * Copyright (c) 2011, Lauren Darcey and Shane Conder
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of 
 *   conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice, this list 
 *   of conditions and the following disclaimer in the documentation and/or other 
 *   materials provided with the distribution.
 *   
 * * Neither the name of the <ORGANIZATION> nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific prior 
 *   written permission.
 *   
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR 
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF 
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * <ORGANIZATION> = Mamlambo
 */
package com.example.prueba.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.github.ConnectionClass;
import com.example.prueba.UserListActivity;
import com.example.prueba.UserListFragment;
import com.example.prueba.R;
import com.example.prueba.data.*;

import android.support.v4.app.NotificationCompat;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;


public class DownloaderService extends Service {

    private static final String DEBUG_TAG = "TutListDownloaderService";
    private DownloaderTask userDownloader;
  
    private static final int LIST_UPDATE_NOTIFICATION = 100;
    private static String url = "https://api.github.com/users";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        URL gitHubPath;
        try {
        	gitHubPath = new URL(url);
            userDownloader = new DownloaderTask();
            userDownloader.execute(gitHubPath);
        } catch (MalformedURLException e) {
            Log.e(DEBUG_TAG, "Bad URL", e);
        }

        return Service.START_FLAG_REDELIVERY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

   
    private class DownloaderTask extends AsyncTask<URL, Void, Boolean> {

        private static final String TAG = "TutListDownloaderService";

        @Override
        protected Boolean doInBackground(URL... params) {
            boolean succeeded = false;
            ConnectionClass sh = new ConnectionClass();
            // Making a request to url and getting response
	 
            String jsonStr = sh.getJson(url);
            
            if (jsonStr != null) {
            	JSONArray users;
            try {
            	users = new JSONArray(jsonStr);
            	insertAll(users);   
            } catch (JSONException e) {
            	Log.e(TAG, "error in converting to JSONArray");
            }                           
     
    } else {
        Log.e("ServiceHandler", "Couldn't get any data from the url");
    }
            return succeeded;
        }
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
   		  	  	Log.e (TAG,id + url);
   		  	
   		  	  	getContentResolver().insert(
                    UserProvider.CONTENT_URI,
                    userData);
   		  	  	i++;
        	  } 
        		result=true;
        	}
        	catch (JSONException e) {
                  Log.e(DEBUG_TAG, "Error during parsing", e);
        	  }
       
        return result;
        
        }
    

        @Override
        protected void onPostExecute(Boolean result) {
        Context context = DownloaderService.this
                    .getApplicationContext();


       
        NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(NOTIFICATION_SERVICE);
        
        String contentTitle = context.getText(R.string.notification_title)
                .toString();
        String contentText;
        if (!result) {
            Log.w(DEBUG_TAG, " The update had errors");
            contentText = context.getText(R.string.notification_info_fail)
                .toString();
        } else {
            contentText = context.getText(
                R.string.notification_info_success).toString();
        }
        
       /* NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this);
        */
        /*NotificationCompat updateComplete = new NotificationCompat.Builder(context)
        .setContentTitle(contentTitle).setContentText(contentText).setSmallIcon(android.R.drawable.stat_notify_sync);
        
       
        Intent notificationIntent = new Intent(context,
        		ItemListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
            notificationIntent, 0);

        
        updateComplete.setLatestEventInfo(context, contentTitle,
            contentText, contentIntent);

        notificationManager
            .notify(LIST_UPDATE_NOTIFICATION, updateComplete);
          */      
        }
    }

}
