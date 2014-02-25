package com.example.prueba;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.prueba.adapter.ContactListAdapter;
import com.example.prueba.data.UserDatabase;
import com.example.prueba.data.UserProvider;
import com.example.prueba.widget.ExampleContactListView;

public class UserListFragment extends SherlockFragment implements TextWatcher,
		LoaderManager.LoaderCallbacks<Cursor> {
	private OnUserSelectedListener userSelectedListener;
	private static final int USER_LIST_LOADER = 0x01;
	private ExampleContactListView listview;
	private ContactListAdapter adapter;
	private static final String TAG = UserListActivity.class.getSimpleName();

	private EditText searchBox;
	private String searchString;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.loginlistview, container, false);

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public interface OnUserSelectedListener {
		public void onUserSelected(String tutUrl);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(USER_LIST_LOADER, null, this);

		adapter = new ContactListAdapter(getActivity().getApplicationContext(),
				null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		listview = (ExampleContactListView) getView().findViewById(
				R.id.listview);
		listview.setFastScrollEnabled(true);
		listview.setAdapter(adapter);

		adapter.setFilterQueryProvider(new FilterQueryProvider() {

			public Cursor runQuery(CharSequence constraint) {
				Log.d(TAG, "runQuery constraint:" + constraint);
				String[] projection = { UserDatabase.C_ID, UserDatabase.C_LOGIN };
				String selection = UserDatabase.C_LOGIN + " LIKE '"
						+ constraint.toString() + "%'";

				Cursor cur = getSherlockActivity().getContentResolver().query(
						UserProvider.CONTENT_URI, projection, selection, null,
						"lower(+ " + UserDatabase.C_LOGIN + ") ASC");
				return cur;
			}

		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				Log.e(TAG, "onItemClick");
				String projection[] = { UserDatabase.C_HTML_URL };
				Cursor userCursor = getActivity().getContentResolver().query(
						Uri.withAppendedPath(UserProvider.CONTENT_URI,
								String.valueOf(id)), projection, null, null,
						null);
				if (userCursor.moveToFirst()) {
					String userUrl = userCursor.getString(0);
					Log.e(TAG, userUrl);
					userSelectedListener.onUserSelected(userUrl);
				}
				userCursor.close();

			}
		});
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		searchBox = (EditText) getSherlockActivity().findViewById(
				R.id.input_search_query);
		searchBox.addTextChangedListener(this);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			userSelectedListener = (OnUserSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnTutSelectedListener");
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { UserDatabase.C_ID, UserDatabase.C_LOGIN };

		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				UserProvider.CONTENT_URI, projection, null, null, "lower(+ "
						+ UserDatabase.C_LOGIN + ") ASC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		String[] projection = { UserDatabase.C_ID, UserDatabase.C_LOGIN };

		adapter.swapCursor(cursor);
		adapter.SetIndexer(cursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
		adapter.SetIndexer(null);
	}

	@Override
	public void afterTextChanged(Editable s) {
		adapter.getFilter().filter(s);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}

}
