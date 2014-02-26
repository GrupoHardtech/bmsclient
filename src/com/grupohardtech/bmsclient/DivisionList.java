package com.grupohardtech.bmsclient;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import classes.USPost;

import com.grupohardtech.bmsclient.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DivisionList extends ListActivity {

	String appname;

	@Override
	public void onCreate(Bundle icicle) {

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			appname = extras.getString("appname");
		}

		super.onCreate(icicle);
		setTitle("Divisiones | " + appname);

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
					new int[] { R.id.division_code, R.id.division_name,
							R.id.division_line_count });

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

		Intent i = new Intent(this, LineList.class);
		i.putExtra("division_code", item.get("division_code"));
		i.putExtra("division_name", item.get("division_name"));
		startActivity(i);
	}

//	@Override
//	public void onBackPressed() {
//
//	}
}