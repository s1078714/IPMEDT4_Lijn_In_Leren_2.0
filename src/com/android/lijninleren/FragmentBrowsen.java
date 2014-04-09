package com.android.lijninleren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentBrowsen extends ListFragment {

	public static ListFragment newInstance(Context context) {
		FragmentBrowsen f = new FragmentBrowsen();

		return f;
	}

	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();

	// url to get all products list
	private static String url_all_products = MainActivity.url + "www/android_connect/get_all_products.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";

	// products JSONArray
	JSONArray products = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_browsen, null);




		return root;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Loading products in Background Thread
		new LoadAllItems().execute();

		// Get listview
		ListView lv = getListView();

		// on seleting single product
		// launching Edit Product Screen
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting values from selected ListItem
				String pid = ((TextView) view.findViewById(R.id.pid)).getText()
						.toString();
			}
		});
	}

	/**
	 * Background Async Task to Load all product by making HTTP Request
	 * */
	class LoadAllItems extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog( getActivity() );
			pDialog.setMessage("Gegevens laden. Een moment geduld...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			try {
				
				// getting JSON string from URL
				JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
				// Check your log cat for JSON reponse
				Log.d("Alle gegevens: ", json.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// products found
						// Getting Array of Products
						products = json.getJSONArray(TAG_PRODUCTS);

						// looping through All Products
						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);

							// Storing each json item in variable
							String id = c.getString(TAG_PID);
							String name = c.getString(TAG_NAME);

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_PID, id);
							map.put(TAG_NAME, name);

							// adding HashList to ArrayList
							itemsList.add(map);
						}
					} else {
						// no products found

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			catch (Exception e)
			{
				CharSequence message = "Kan gegevens niet downloaden!";
				Toast.makeText(getActivity().getBaseContext(), message, Toast.LENGTH_LONG).show();
			}



			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					ListAdapter adapter = new SimpleAdapter(
							getActivity(), itemsList,
							R.layout.list_item, new String[] { TAG_PID,
								TAG_NAME},
								new int[] { R.id.pid, R.id.name });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}

	}
}