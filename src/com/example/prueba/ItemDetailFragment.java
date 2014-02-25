package com.example.prueba;

import android.os.Bundle;
import com.actionbarsherlock.app.SherlockFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;



/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends SherlockFragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	private String currentURL;
	private static final String TAG = UserListActivity.class.getSimpleName();
	public static final String URL_LOGIN= "url_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(URL_LOGIN)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			currentURL = getArguments().getString(
					URL_LOGIN);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.web_layout,
				container, false);

		// Show the dummy content as text in a TextView.
		WebView wv = (WebView) rootView.findViewById(R.id.webPage);
		//wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new SwAWebClient());
		wv.loadUrl(currentURL);

		return rootView;
	
	
	}
	private class SwAWebClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
		
	}
	
}
