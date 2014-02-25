package com.example.prueba.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class ExampleContactListView extends ContactListView {

	public ExampleContactListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void createScroller() {

		mScroller = new IndexScroller(getContext(), this);
		mScroller.setAutoHide(autoHide);
		mScroller.setShowIndexContainer(true);
		mScroller.setIndexPaintColor(Color.WHITE);

		if (autoHide)
			mScroller.hide();
		else
			mScroller.show();

	}
}
