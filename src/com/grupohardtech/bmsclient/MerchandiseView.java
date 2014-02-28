package com.grupohardtech.bmsclient;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import classes.USPost;

import com.grupohardtech.bmsclient.R;
import com.grupohardtech.bmsclient.util.SystemUiHider;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MerchandiseView extends Activity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private float MIN_DISTANCE = 200;
	private SystemUiHider mSystemUiHider;

	String term = null;
	String rownumber = null;
	String previous = null;
	String next = null;

	float initialX = 0;
	float initialY = 0;
	float finalX = 0;
	float finalY = 0;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle icicle) {

		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		MIN_DISTANCE = getWindowManager().getDefaultDisplay().getWidth() / 2;

		final View contentView = findViewById(R.id.list_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			term = extras.getString("term");
			rownumber = extras.getString("rownumber");
		}

		super.onCreate(icicle);

		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("term", term));
			parameters.add(new BasicNameValuePair("rownumber", rownumber));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/merchandise/view.html", parameters);

			String image_url = object.getString("image");
			String merchandise_name = object.getString("name");
			String merchandise_model = object.getString("model");
			String division_code = object.getString("division_code");
			String division_name = object.getString("division_name");
			String line_code = object.getString("line_code");
			String line_name = object.getString("line_name");
			String subline_code = object.getString("subline_code");
			String subline_name = object.getString("subline_name");
			String mark_code = object.getString("mark_code");
			String mark_name = object.getString("mark_name");

			previous = object.getString("previous");
			next = object.getString("next");
			// String merchandise_division_code =
			// object.getString("merchandise_division_code");

			setTitle(merchandise_name);
			setContentView(R.layout.merchandise_view);

			// ImageLoaderConfiguration config = new
			// ImageLoaderConfiguration.Builder(
			// getApplicationContext()).build();
			// ImageLoader imageLoader = ImageLoader.getInstance();
			//
			// imageLoader.init(config);
			//
			// imageLoader.displayImage(image_url,
			// (ImageAware) findViewById(R.id.merchandise_view_image));
			//
			// imageLoader.displayImage(image_url,
			// (ImageAware) findViewById(R.id.merchandise_view_image));

			TextView merchandise_view_name = (TextView) findViewById(R.id.merchandise_view_name);
			merchandise_view_name.setText(merchandise_name);

			TextView merchandise_view_model = (TextView) findViewById(R.id.merchandise_view_model);
			merchandise_view_model.setText(merchandise_model);

			TextView merchandise_view_division = (TextView) findViewById(R.id.merchandise_view_division);
			merchandise_view_division.setText(division_name);

			TextView merchandise_view_line = (TextView) findViewById(R.id.merchandise_view_line);
			merchandise_view_line.setText(line_name);

			TextView merchandise_view_subline = (TextView) findViewById(R.id.merchandise_view_subline);
			merchandise_view_subline.setText(subline_name);

			TextView merchandise_view_mark = (TextView) findViewById(R.id.merchandise_view_mark);
			merchandise_view_mark.setText(mark_name);

			Button merchandise_view_back = (Button) findViewById(R.id.merchandise_view_back);
			merchandise_view_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), Search.class);
					i.putExtra("term", term);
					startActivity(i);
				}
			});

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			initialX = event.getX();
			initialY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:

			break;

		case MotionEvent.ACTION_UP:

			finalX = event.getX();
			finalY = event.getY();

			float deltaX = finalX - initialX;

			if (deltaX < -MIN_DISTANCE) {
				Intent i = new Intent(getApplicationContext(),
						MerchandiseView.class);
				i.putExtra("term", term);
				i.putExtra("rownumber", previous);
				startActivity(i);
			}

			if (deltaX > MIN_DISTANCE) {
				Intent i = new Intent(getApplicationContext(),
						MerchandiseView.class);
				i.putExtra("term", term);
				i.putExtra("rownumber", next);
				startActivity(i);
			}

			break;
		}

		return true;

	}
}