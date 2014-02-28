package com.grupohardtech.bmsclient;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import classes.USPost;

import com.grupohardtech.bmsclient.R;
import com.grupohardtech.bmsclient.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends ListActivity {

	String term = null;

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	@SuppressLint("CutPasteId")
	@Override
	public void onCreate(Bundle icicle) {
		final View contentView = findViewById(R.id.list_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			term = extras.getString("term");
		}

		super.onCreate(icicle);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		setContentView(R.layout.search);

		final EditText search_term_field = (EditText) findViewById(R.id.search_term);
		search_term_field.setText(term);
		search_term_field.requestFocus();
		search_term_field.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					term = search_term_field.getText().toString();
					search();
					return true;
				}
				return false;
			}
		});

		search();

		findViewById(R.id.search_button).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						term = search_term_field.getText().toString();
						search();
					}
				});

		TextView fullscreen_content = (TextView) findViewById(R.id.list_fullscreen_content);
		fullscreen_content.setText("Búsqueda de productos");

		findViewById(R.id.list_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Index.class);
				startActivity(i);
			}
		});

	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>) parent
				.getItemAtPosition(position);

		Intent i = new Intent(this, MerchandiseView.class);
		i.putExtra("rownumber", item.get("rownumber"));
		i.putExtra("term", term);
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		return;
	}

	public void search() {
		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("term", term));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/merchandise/search.html", parameters);

			int search_count = object.getInt("search_count");

			JSONArray merchandiseItems = object
					.getJSONArray("merchandiseItems");

			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < merchandiseItems.length(); i++) {
				JSONObject subline = merchandiseItems.getJSONObject(i);

				String merchandise_code = subline.getString("merchandise_code");
				String merchandise_name = subline.getString("merchandise_name");
				String merchandise_model = subline
						.getString("merchandise_model");
				String merchandise_mark_name = subline
						.getString("merchandise_mark_name");
				String merchandise_price = subline
						.getString("merchandise_price");

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("rownumber", String.valueOf(i));
				item.put("merchandise_code", merchandise_code);
				item.put("merchandise_name", merchandise_name);
				item.put("merchandise_model", "Modelo: " + merchandise_model);
				item.put("merchandise_mark_name", "Marca: "
						+ merchandise_mark_name);
				item.put("merchandise_price", merchandise_price);

				arrayList.add(item);
			}

			SimpleAdapter sa = new SimpleAdapter(this, arrayList,
					R.layout.merchandise_item, new String[] { "rownumber",
							"merchandise_code", "merchandise_name",
							"merchandise_model", "merchandise_mark_name",
							"merchandise_price" }, new int[] {
							R.id.merchandise_item_rownumber,
							R.id.merchandise_item_code,
							R.id.merchandise_item_name,
							R.id.merchandise_item_model,
							R.id.merchandise_item_mark_name,
							R.id.merchandise_item_price });

			setListAdapter(sa);

			TextView fullscreen_content = (TextView) findViewById(R.id.search_counter);
			fullscreen_content.setText(String.valueOf(search_count)
					+ " resultados");

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}
}