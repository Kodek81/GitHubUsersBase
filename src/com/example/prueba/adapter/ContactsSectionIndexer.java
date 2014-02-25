package com.example.prueba.adapter;

import java.util.Arrays;

import android.database.Cursor;
import android.util.Log;
import android.widget.SectionIndexer;

import com.example.prueba.UserListActivity;
import com.example.prueba.data.UserDatabase;

public class ContactsSectionIndexer implements SectionIndexer {

	private static String OTHER = "#";
	private static String[] mSections = { OTHER, "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	private static int OTHER_INDEX = 0; // index of other in the mSections array
	private int[] mPositions;
	private int mCount; // this is the count for total number of contacts
	private static final String TAG = UserListActivity.class.getSimpleName();

	public ContactsSectionIndexer(Cursor contacts) {
		mCount = contacts.getCount();
		initPositions(contacts);
		for (int i = 0; i < mSections.length; i++) {
			// Log.i("ContactSectionIndexer", "Letter: " + mSections[i]
			// + ", pos: " + mPositions[i]);
		}

	}

	public String getSectionTitle(String indexableItem) {
		int sectionIndex = getSectionIndex(indexableItem);
		return mSections[sectionIndex];
	}

	// return which section this item belong to
	public int getSectionIndex(String indexableItem) {
		if (indexableItem == null) {
			return OTHER_INDEX;
		}

		indexableItem = indexableItem.trim();
		String firstLetter = OTHER;

		if (indexableItem.length() == 0) {
			return OTHER_INDEX;
		} else {
			// get the first letter
			firstLetter = String.valueOf(indexableItem.charAt(0)).toUpperCase();
		}

		int sectionCount = mSections.length;
		for (int i = 0; i < sectionCount; i++) {
			if (mSections[i].equals(firstLetter)) {
				Log.e(TAG, indexableItem + "is equal to = " + mSections[i]);

				return i;
			}
		}
		Log.e(TAG, "RETURNING 0" + indexableItem);
		return OTHER_INDEX;

	}

	// initialize the position index
	public void initPositions(Cursor cursor) {

		int sectionCount = mSections.length;
		int itemIndex = 0;
		mPositions = new int[sectionCount];
		// Log.e(TAG,"COUNT:" + sectionCount);
		Arrays.fill(mPositions, -1); // initialize everything to -1
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			// do what you need with the cursor here
			int loginIndex = cursor.getColumnIndex(UserDatabase.C_LOGIN);
			String indexableItem = cursor.getString(loginIndex);
			// Log.e(TAG, "indexable item: " + indexableItem);
			int sectionIndex = getSectionIndex(indexableItem);

			if (mPositions[sectionIndex] == -1) // if not set before, then do
												// this, otherwise just ignore
				mPositions[sectionIndex] = itemIndex;

			itemIndex++;

		}

		int lastPos = -1;

		for (int i = 0; i < sectionCount; i++) {
			if (mPositions[i] == -1)
				mPositions[i] = lastPos;

			lastPos = mPositions[i];

		}

	}

	@Override
	public int getPositionForSection(int section) {
		if (section < 0 || section >= mSections.length) {
			return -1;
		}

		return mPositions[section];
	}

	@Override
	public int getSectionForPosition(int position) {
		if (position < 0 || position >= mCount) {
			return -1;
		}

		int index = Arrays.binarySearch(mPositions, position);
		return index >= 0 ? index : -index - 2;
	}

	@Override
	public Object[] getSections() {
		return mSections;
	}

	public boolean isFirstItemInSection(int position) {

		int section = Arrays.binarySearch(mPositions, position);
		return (section > -1);

	}

}
