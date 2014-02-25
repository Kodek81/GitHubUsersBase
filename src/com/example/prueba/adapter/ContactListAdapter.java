package com.example.prueba.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prueba.R;
import com.example.prueba.data.UserDatabase;

public class ContactListAdapter extends CursorAdapter {

	private boolean inSearchMode = false;

	private static final String TAG = ContactListAdapter.class.getSimpleName();

	private ContactsSectionIndexer indexer = null;
	private final LayoutInflater inflater;

	public ContactListAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mContext = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void SetIndexer(Cursor c) {
		setIndexer(new ContactsSectionIndexer(c));

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		try {

			int loginIndex = cursor.getColumnIndexOrThrow(UserDatabase.C_LOGIN);
			String loginValue = cursor.getString(loginIndex);
			TextView nameView = (TextView) view.findViewById(R.id.LoginName);
			nameView.setText(loginValue);

			if (cursor.getCount() > 0) {

				showSectionViewIfFirstItem((LinearLayout) view, loginValue,
						cursor.getPosition());
			}

		} catch (IllegalArgumentException e) {

			Log.e(TAG, "error while retrieving data from bindView");
		}

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(R.layout.example_contact_item, parent, false);
	}

	public boolean isInSearchMode() {
		return inSearchMode;
	}

	public void setInSearchMode(boolean inSearchMode) {
		this.inSearchMode = inSearchMode;
	}

	public ContactsSectionIndexer getIndexer() {
		return indexer;
	}

	public void setIndexer(ContactsSectionIndexer indexer) {
		this.indexer = indexer;
	}

	// get the section textview from row view
	// the section view will only be shown for the first item
	public TextView getSectionTextView(View rowView) {
		TextView sectionTextView = (TextView) rowView
				.findViewById(R.id.sectionTextView);
		return sectionTextView;
	}

	public void showSectionViewIfFirstItem(View rowView, String name,
			int position) {
		TextView sectionTextView = getSectionTextView(rowView);

		// if in search mode then dun show the section header
		if (inSearchMode) {
			sectionTextView.setVisibility(View.GONE);
		} else {
			// if first item then show the header

			if (!(indexer == null)) {
				if (indexer.isFirstItemInSection(position)) {

					String sectionTitle = indexer.getSectionTitle(name);

					sectionTextView.setText(sectionTitle);

					sectionTextView.setVisibility(View.VISIBLE);

				} else {
					sectionTextView.setVisibility(View.GONE);

				}
			}
		}
	}

}
