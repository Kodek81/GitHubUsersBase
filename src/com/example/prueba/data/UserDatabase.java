
package com.example.prueba.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class UserDatabase extends SQLiteOpenHelper {
	private static final String TAG = UserDatabase.class.getSimpleName();
    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "GitHubDB";
    public static final String TABLE = "users";
    
    public static final String C_ID = BaseColumns._ID; // Special for id
    public static final String C_LOGIN = "login" ;
    public static final String C_URL =  "url";
    public static final String C_HTML_URL = "html_url";
    public static final String C_REPOS_URL = "repos_url";
    public static final String C_EVENTS_URL = "events_url";
    
   
    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.e("sql","oncreate DB");
        String sql = String.format(
            "create table %s (%s int primary key, %s TEXT, %s TEXT, %s TEXT, %s TEXT,%s TEXT)",
            TABLE, C_ID,C_LOGIN, C_URL, C_HTML_URL, C_REPOS_URL, C_EVENTS_URL);
         Log.d(TAG, "onCreate sql: " + sql);
         try {
      	   db.execSQL(sql);
      	   Log.e(TAG,"oncreate DB created");
         }
         catch (SQLException e){
      	   Log.e(TAG,"oncreate DB error");
         }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      // Typically you do ALTER TABLE... here
      Log.d(TAG, "onUpgrade sql");
      try {
    	  db.execSQL("drop table if exists " + TABLE);
    	  this.onCreate(db);
    	  Log.e(TAG,"onUpgrade DB finished");
      }
      catch (SQLException e){
    	  Log.e(TAG,"onUpgrade DB error");
      }}

 
}
