package com.grupohardtech.bmsclient;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import classes.USPost;

import com.grupohardtech.bmsclient.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SublineList extends ListActivity {

	String line_code = null;
	String line_name = null;

	@Override
	public void onCreate(Bundle icicle) {

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			line_code = extras.getString("line_code");
			line_name = extras.getString("line_name");
		}

		super.onCreate(icicle);
		setTitle("Sublíneas en " + line_name);

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

		Intent i = new Intent(this, MerchandiseList.class);
		i.putExtra("subline_code", item.get("subline_code"));
		i.putExtra("subline_name", item.get("subline_name"));
		startActivity(i);
	}
}