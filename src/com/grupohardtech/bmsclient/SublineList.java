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

@SuppressLint("CutPasteId")
public class SublineList extends ListActivity {

	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
	private SystemUiHider mSystemUiHider;

	String division_code = null;
	String division_name = null;
	String line_code = null;
	String line_name = null;

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
			line_code = extras.getString("line_code");
			line_name = extras.getString("line_name");
		}

		super.onCreate(icicle);
		overridePendingTransition(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);
		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters.add(new BasicNameValuePair("line_code", line_code));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/subline/index.html", parameters);

			JSONArray sublineItems = object.getJSONArray("sublineItems");

			ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

			for (int i = 0; i < sublineItems.length(); i++) {
				JSONObject subline = sublineItems.getJSONObject(i);

				String subline_code = subline.getString("subline_code");
				String subline_name = subline.getString("subline_name");
				String subline_merchandise_count = subline
						.getString("subline_merchandise_count");

				HashMap<String, String> item = new HashMap<String, String>();
				item.put("subline_code", subline_code);
				item.put("subline_name", subline_name);
				item.put("subline_merchandise_count", "Productos: "
						+ subline_merchandise_count);
				arrayList.add(item);
			}

			SimpleAdapter sa = new SimpleAdapter(this, arrayList,
					R.layout.subline_item, new String[] { "subline_code",
							"subline_name", "subline_merchandise_count" },
					new int[] { R.id.subline_code, R.id.subline_name,
							R.id.subline_merchandise_count });

			setListAdapter(sa);

			setContentView(R.layout.list);

			findViewById(R.id.list_back).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(getApplicationContext(),
									LineList.class);
							i.putExtra("division_code", division_code);
							i.putExtra("division_name", division_name);
							startActivity(i);
						}
					});

			TextView fullscreen_content = (TextView) findViewById(R.id.list_fullscreen_content);
			fullscreen_content.setText("Sublíneas en " + line_name);

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

		Intent i = new Intent(getApplicationContext(), MerchandiseList.class);
		i.putExtra("division_code", division_code);
		i.putExtra("division_name", division_name);
		i.putExtra("line_code", line_code);
		i.putExtra("line_name", line_name);
		i.putExtra("subline_code", item.get("subline_code"));
		i.putExtra("subline_name", item.get("subline_name"));
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		return;
	}
}