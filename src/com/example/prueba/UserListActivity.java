package com.example.prueba;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.prueba.service.DownloaderIntentService;

public class UserListActivity extends SherlockFragmentActivity implements
		UserListFragment.OnUserSelectedListener {

	private boolean mTwoPane;

	ProgressBar progressBar;
	static int myprogress = 0;

	private static final String TAG = UserListActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentlist);
		Context context = this.getApplicationContext();
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

		}

		Intent downloader = new Intent(context, DownloaderIntentService.class);
		context.startService(downloader);

	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onUserSelected(String link) {
		Log.e(TAG, "onItemSelected");
		// Here we detect if there's dual fragment
		if (mTwoPane) {

			Bundle arguments = new Bundle();
			arguments.putString(ItemDetailFragment.URL_LOGIN, link);
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
		} else {

			Intent viewIntent = new Intent("android.intent.action.VIEW",
					Uri.parse(link));
			startActivity(viewIntent);
		}

	}

}
