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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LineList extends ListActivity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	String division_code = null;
	String division_name = null;

	@SuppressLint("CutPasteId")
	@Override
	public void onCreate(Bundle icicle) {

		final View contentView = findViewById(R.id.list_fullscreen_content);

		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);

		mSystemUiHider.hide();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			division_code = extras.getString("division_code");
			division_name = extras.getString("division_name");
		}

		super.onCreate(icicle);

		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("division_code",
					division_code));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/line/index.html", parameters);

			JSONArray lineItems = object.getJSONArray("lineItems");

			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < lineItems.length(); i++) {
				JSONObject line = lineItems.getJSONObject(i);

				String line_code = line.getString("line_code");
				String line_name = line.getString("line_name");
				String line_subline_count = line
						.getString("line_subline_count");

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("line_code", line_code);
				item.put("line_name", line_name);
				item.put("line_subline_count", "Sublíneas: "
						+ line_subline_count);
				arrayList.add(item);
			}

			SimpleAdapter sa = new SimpleAdapter(this, arrayList,
					R.layout.line_item, new String[] { "line_code",
							"line_name", "line_subline_count" }, new int[] {
							R.id.line_item_code, R.id.line_item_name,
							R.id.line_item_subline_count });

			setListAdapter(sa);
			setContentView(R.layout.list);

			findViewById(R.id.list_back).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplicationContext(),
									DivisionList.class);
							startActivity(intent);
						}
					});

			TextView fullscreen_content = (TextView) findViewById(R.id.list_fullscreen_content);
			fullscreen_content.setText("Líneas en " + division_name);

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onListItemClick(ListView parent, View v, int position, long id) {

		@SuppressWarnings("unchecked")
		HashMap<String, String> item = (HashMap<String, String>) parent
				.getItemAtPosition(position);

		Intent i = new Intent(this, SublineList.class);
		i.putExtra("division_code", division_code);
		i.putExtra("division_name", division_code);
		i.putExtra("line_code", item.get("line_code"));
		i.putExtra("line_name", item.get("line_name"));
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		return;
	}
}