package com.example.prueba.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class UserProvider extends ContentProvider {

	private UserDatabase mDB;

	private static final String AUTHORITY = "com.example.prueba.data.UserProvider";
	public static final int USERS = 100;
	public static final int USERS_ID = 110;
	private static final String USERS_BASE_PATH = "users";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + USERS_BASE_PATH);
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/users";
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/users";
	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	private static final String TAG = UserDatabase.class.getSimpleName();
	static {
		sURIMatcher.addURI(AUTHORITY, USERS_BASE_PATH, USERS);
		sURIMatcher.addURI(AUTHORITY, USERS_BASE_PATH + "/#", USERS_ID);
	}

	@Override
	public boolean onCreate() {
		mDB = new UserDatabase(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(UserDatabase.TABLE);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case USERS_ID:
			queryBuilder.appendWhere(UserDatabase.C_ID + "="
					+ uri.getLastPathSegment());
			break;
		case USERS:
			// no filter
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
				projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		Log.e(TAG, "URI TYPE delete:" + String.valueOf(uriType));
		SQLiteDatabase sqlDB = mDB.getWritableDatabase();
		int rowsAffected = 0;
		switch (uriType) {
		case USERS:
			rowsAffected = sqlDB.delete(UserDatabase.TABLE, selection,
					selectionArgs);
			break;
		case USERS_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsAffected = sqlDB.delete(UserDatabase.TABLE,
						UserDatabase.C_ID + "=" + id, null);
			} else {
				rowsAffected = sqlDB
						.delete(UserDatabase.TABLE, selection + " and "
								+ UserDatabase.C_ID + "=" + id, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case USERS:
			return CONTENT_TYPE;
		case USERS_ID:
			return CONTENT_ITEM_TYPE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		if (uriType != USERS) {
			throw new IllegalArgumentException("Invalid URI for insert");
		}
		SQLiteDatabase sqlDB = mDB.getWritableDatabase();
		try {
			long newID = sqlDB.insertOrThrow(UserDatabase.TABLE, null, values);
			if (newID > 0) {
				Uri newUri = ContentUris.withAppendedId(uri, newID);
				getContext().getContentResolver().notifyChange(uri, null);
				return newUri;
			} else {
				throw new SQLException("Failed to insert row into " + uri);
			}
		} catch (SQLiteConstraintException e) {
			Log.i(TAG, "Ignoring constraint failure.");
		}
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = mDB.getWritableDatabase();

		int rowsAffected;

		switch (uriType) {
		case USERS_ID:
			String id = uri.getLastPathSegment();
			StringBuilder modSelection = new StringBuilder(UserDatabase.C_ID
					+ "=" + id);

			if (!TextUtils.isEmpty(selection)) {
				modSelection.append(" AND " + selection);
			}

			rowsAffected = sqlDB.update(UserDatabase.TABLE, values,
					modSelection.toString(), null);
			break;
		case USERS:
			rowsAffected = sqlDB.update(UserDatabase.TABLE, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsAffected;
	}
}
