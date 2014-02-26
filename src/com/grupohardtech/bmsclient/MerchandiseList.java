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

public class MerchandiseList extends ListActivity {

	String subline_code = null;
	String subline_name = null;

	@Override
	public void onCreate(Bundle icicle) {

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			subline_code = extras.getString("subline_code");
			subline_name = extras.getString("subline_name");
		}

		super.onCreate(icicle);
		setTitle("Productos en " + subline_name);

		try {

			ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			parameters
					.add(new BasicNameValuePair("subline_code", subline_code));

			JSONObject object = USPost.connect(getString(R.string.domain)
					+ "/merchandise/index.html", parameters);

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
					R.layout.merchandise_item, new String[] {
							"rownumber", "merchandise_code",
							"merchandise_name", "merchandise_model",
							"merchandise_mark_name", "merchandise_price" },
					new int[] { R.id.merchandise_rownumber,
							R.id.merchandise_code, R.id.merchandise_name,
							R.id.merchandise_model, R.id.merchandise_mark_name,
							R.id.merchandise_price });

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

		Intent i = new Intent(this, MerchandiseView.class);
		i.putExtra("rownumber", item.get("rownumber"));
		i.putExtra("subline_code", subline_code);
		startActivity(i);
	}
}