package com.grupohardtech.bmsclient;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.USPost;

import com.grupohardtech.bmsclient.R;
import com.grupohardtech.bmsclient.util.SystemUiHider;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DivisionList extends ListActivity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	@SuppressLint("CutPasteId")
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		View contentView = findViewById(R.id.list_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();

		try {
			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/division/index.html", null);

			JSONArray divisionItems = object.getJSONArray("divisionItems");

			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < divisionItems.length(); i++) {
				JSONObject division = divisionItems.getJSONObject(i);

				String division_code = division.getString("division_code");
				String division_name = division.getString("division_name");
				String division_line_count = division
						.getString("division_line_count");

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("division_code", division_code);
				item.put("division_name", division_name);
				item.put("division_line_count", "Líneas: "
						+ division_line_count);
				arrayList.add(item);
			}

			SimpleAdapter sa = new SimpleAdapter(this, arrayList,
					R.layout.division_item, new String[] { "division_code",
							"division_name", "division_line_count" },
					new int[] { R.id.division_item_code,
							R.id.division_item_name,
							R.id.division_item_line_count });

			setListAdapter(sa);

			setContentView(R.layout.list);

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

		TextView fullscreen_content = (TextView) findViewById(R.id.list_fullscreen_content);
		fullscreen_content.setText(R.string.app_name);

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

		Intent i = new Intent(this, LineList.class);
		i.putExtra("division_code", item.get("division_code"));
		i.putExtra("division_name", item.get("division_name"));
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		return;
	}
}