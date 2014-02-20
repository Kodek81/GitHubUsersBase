package com.example.prueba;

import android.app.Activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.prueba.data.*;

public class UserListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private OnUserSelectedListener userSelectedListener;
    private static final int USER_LIST_LOADER = 0x01;

    private SimpleCursorAdapter adapter;
    private static final String TAG = UserListActivity.class.getSimpleName();
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String projection[] = { UserDatabase.C_HTML_URL };
        Cursor userCursor = getActivity().getContentResolver().query(
                Uri.withAppendedPath(UserProvider.CONTENT_URI,
                        String.valueOf(id)), projection, null, null, null);
        if (userCursor.moveToFirst()) {
            String userUrl = userCursor.getString(0);
            Log.e(TAG,userUrl);
            userSelectedListener.onUserSelected(userUrl);
        }
        userCursor.close();
        l.setItemChecked(position, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] uiBindFrom = { UserDatabase.C_LOGIN };
        int[] uiBindTo = { R.id.loginName};

        getLoaderManager().initLoader(USER_LIST_LOADER, null, this);

        adapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.listrow_user,
                null, uiBindFrom, uiBindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(adapter);
        setHasOptionsMenu(true);
    }

    public interface OnUserSelectedListener {
        public void onUserSelected(String tutUrl);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            userSelectedListener = (OnUserSelectedListener)  activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTutSelectedListener");
        }
    }

    // options menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       /* inflater.inflate(R.menu.options_menu, menu);

        // refresh menu item
        Intent refreshIntent = new Intent(
                getActivity().getApplicationContext(),
                TutListDownloaderService.class);
        refreshIntent
                .setData(Uri
                        .parse("http://feeds.feedburner.com/mobile-tuts-summary?format=xml"));

        MenuItem refresh = menu.findItem(R.id.refresh_option_item);
        refresh.setIntent(refreshIntent);

        // pref menu item
        Intent prefsIntent = new Intent(getActivity().getApplicationContext(),
                TutListPreferencesActivity.class);

        MenuItem preferences = menu.findItem(R.id.settings_option_item);
        preferences.setIntent(prefsIntent);
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
       /* case R.id.refresh_option_item:
            getActivity().startService(item.getIntent());
            break;
        case R.id.settings_option_item:
            getActivity().startActivity(item.getIntent());
            break;
           */
        }
        return true;
    }

    // LoaderManager.LoaderCallbacks<Cursor> methods

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { UserDatabase.C_ID, UserDatabase.C_LOGIN };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                UserProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}

