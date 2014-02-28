package com.grupohardtech.bmsclient;

import com.grupohardtech.bmsclient.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class Index extends Activity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		setContentView(R.layout.index);



		final View contentView = findViewById(R.id.index_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();

		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(getApplicationContext(),
						Search.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
		return;
	}
}
