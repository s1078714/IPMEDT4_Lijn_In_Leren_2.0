package com.android.lijninleren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AllItemsFragment extends ListFragment 
{
	// Progress Dialog
	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> itemsList;

	// url to get all products list
	private static String url_all_items = "http://192.168.2.5:80/android_connect/get_all_products.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ITEMS = "items";
	private static final String TAG_IID = "id";
	private static final String TAG_NAME = "name";
	

	// products JSONArray
	JSONArray items = null;

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		return inflater.inflate( R.id.listfragment, container, false );
	}
	
	@Override
	public void onStart() {
		super.onStart();


		// Hashmap for ListView
		itemsList = new ArrayList<HashMap<String, String>>();

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
				String iid = ((TextView) view.findViewById(R.id.iid)).getText()
						.toString();

				//					// Starting new intent
				//					Intent in = new Intent(getApplicationContext(),
				////							EditProductActivity.class);
				//					// sending iid to next activity
				//					in.putExtra(TAG_IID, iid);
				//					
				//					// starting new activity and expecting some response back
				//					startActivityForResult(in, 100);
			}
		});

	}



	/**
	 * Background Async Task to Load all items by making HTTP Request
	 * */
	class LoadAllItems extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait...", true);
//			pDialog = new ProgressDialog(AllItemsActivity.this);
			pDialog.setMessage("Items laden. Een ogenblik geduld...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All items from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_items, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					items = json.getJSONArray(TAG_ITEMS);

					// looping through All Products
					for (int i = 0; i < items.length(); i++) {
						JSONObject c = items.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_IID);
						String name = c.getString(TAG_NAME);

						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();

						// adding each child node to HashMap key => value
						map.put(TAG_IID, id);
						map.put(TAG_NAME, name);

						// adding HashList to ArrayList
						itemsList.add(map);
					}
				} else {
					// no products found
					// Launch Add New product Activity
					//						Intent i = new Intent(getApplicationContext(),
					//								NewProductActivity.class);
					//						// Closing all previous activities
					//						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//						startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
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
							R.layout.drawer_list_item, new String[] { TAG_IID,
									TAG_NAME},
									new int[] { R.id.iid, R.id.name });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}
	}
}

