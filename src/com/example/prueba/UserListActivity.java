package com.example.prueba;

import com.example.prueba.service.DownloaderService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class UserListActivity extends FragmentActivity implements UserListFragment.OnUserSelectedListener 
		 {

	private boolean mTwoPane;
   
    ProgressBar progressBar;
    static int myprogress=0;

    private static final String TAG = UserListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_item_list);
		Context context = this.getApplicationContext();
		//GetUsers userAsyntask= new GetUsers(this);
	    //userAsyntask.execute();
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		
		}
		Intent downloader = new Intent(context, DownloaderService.class);
	    context.startService(downloader);
		
		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onUserSelected(String link) 
	{
		Log.e(TAG,"onItemSelected");
		// Here we detect if there's dual fragment
        if (mTwoPane) {
          	
			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.URL_LOGIN, link);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
								.replace(R.id.item_detail_container, fragment).commit();
        }
		else {
			
			Intent i = new Intent(this, WebViewActivity.class);
			i.putExtra("link", link);
			startActivity(i); 
		}
		
	}

	
}
