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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MerchandiseView extends Activity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	String division_code = null;
	String division_name = null;
	String line_code = null;
	String line_name = null;
	String subline_code = null;
	String subline_name = null;
	String mark_code = null;
	String mark_name = null;
	String rownumber = null;
	String previous = null;
	String next = null;

	@Override
	public void onCreate(Bundle icicle) {

		final View contentView = findViewById(R.id.list_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			subline_code = extras.getString("subline_code");
			rownumber = extras.getString("rownumber");
		}

		super.onCreate(icicle);

		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("rownumber", rownumber));
			parameters
					.add(new BasicNameValuePair("subline_code", subline_code));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/merchandise/view.html", parameters);

			String image_url = object.getString("image");
			String merchandise_name = object.getString("name");
			String merchandise_model = object.getString("model");
			division_code = object.getString("division_code");
			division_name = object.getString("division_name");
			line_code = object.getString("line_code");
			line_name = object.getString("line_name");
			subline_code = object.getString("subline_code");
			subline_name = object.getString("subline_name");
			mark_code = object.getString("mark_code");
			mark_name = object.getString("mark_name");

			previous = object.getString("previous");
			next = object.getString("next");
			// String merchandise_division_code =
			// object.getString("merchandise_division_code");

			setTitle(merchandise_name);
			setContentView(R.layout.merchandise_view);

			// ImageLoader imageLoader = ImageLoader.getInstance();
			//
			// imageLoader.displayImage(image_url,
			// (ImageAware) findViewById(R.id.merchandise_view_image));

			TextView merchandise_view_name = (TextView) findViewById(R.id.merchandise_view_name);
			merchandise_view_name.setText(merchandise_name);

			TextView merchandise_view_model = (TextView) findViewById(R.id.merchandise_view_model);
			merchandise_view_model.setText("Modelo: " + merchandise_model);

			TextView merchandise_view_division = (TextView) findViewById(R.id.merchandise_view_division);
			merchandise_view_division.setText("División: " + division_name);
			merchandise_view_division.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					Intent i = new Intent(getApplicationContext(),
							LineList.class);
					i.putExtra("division_code", division_code);
					i.putExtra("division_name", division_name);
					startActivity(i);
				}
			});

			Button merchandise_view_previous = (Button) findViewById(R.id.merchandise_view_previous);
			merchandise_view_previous.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							MerchandiseView.class);
					i.putExtra("rownumber", previous);
					i.putExtra("subline_code", subline_code);
					startActivity(i);
				}
			});

			Button merchandise_view_next = (Button) findViewById(R.id.merchandise_view_next);
			merchandise_view_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							MerchandiseView.class);
					i.putExtra("rownumber", next);
					i.putExtra("subline_code", subline_code);
					startActivity(i);
				}
			});

			Button merchandise_view_back = (Button) findViewById(R.id.merchandise_view_back);
			merchandise_view_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(),
							SublineList.class);
					i.putExtra("division_code", division_code);
					i.putExtra("division_name", division_code);
					i.putExtra("line_code", line_code);
					i.putExtra("line_name", line_name);
					startActivity(i);
				}
			});
			// Button merchandise_view_division = (Button)
			// findViewById(R.id.merchandise_view_division);
			// merchandise_view_division.setText("Modelo: " +
			// merchandise_model);

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onBackPressed() {
		return;
	}
}