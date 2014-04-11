package com.android.lijninleren;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	private static String data;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	// Database
	private CategoryHelper databaseHelper;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> itemsList = new ArrayList<HashMap<String, String>>();

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "lijn_in_leren_groep_bouw_tabel";
	private static final String TAG_PID = "id";
	private static final String TAG_LEERLIJN = "leerlijn";
	private static final String TAG_VAK = "vak";
	private static final String TAG_ONDERDEEL = "onderdeel";
	private static final String TAG_KERNDOEL = "kerndoel";
	private static final String TAG_GROEP = "groep";
	private static final String TAG_INFORMATIE_KIND = "informatie_kind";
	private static final String TAG_INFORMATIE_LERAAR = "informatie_leraar";

	// url to get all products list
	private static String url_all_products = MainActivity.url + "www/android_connect/get_all_items.php?table=" + TAG_PRODUCTS;

	// products JSONArray
	JSONArray products = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_browsen, null);


		databaseHelper=new CategoryHelper( getActivity().getApplicationContext() );

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
				Log.d("GetText()", "GESTART!");
				// Get user defined values
				String Select  = "vak";
				String GroupBy = Select;
				String Where1  = "leerlijn";
				String Where2  = "Taal onderwijs"; // pass.getText().toString()
				String Where3  = ""; // pass.getText().toString()

				// Create data variable for sent values to server  

				data = URLEncoder.encode("select", "UTF-8") 
						+ "=" + URLEncoder.encode(Select, "UTF-8"); 

				data += "&" + URLEncoder.encode("groupby", "UTF-8") + "="
						+ URLEncoder.encode(GroupBy, "UTF-8"); 

				data += "&" + URLEncoder.encode("where1", "UTF-8") 
						+ "=" + URLEncoder.encode(Where1, "UTF-8");

				data += "&" + URLEncoder.encode("where2", "UTF-8") 
						+ "=" + URLEncoder.encode(Where2, "UTF-8");

				data += "&" + URLEncoder.encode("where3", "UTF-8") 
						+ "=" + URLEncoder.encode(Where3, "UTF-8");

				String text = "";
				StringBuilder sb;
				BufferedReader reader=null;
				Log.d("Data: ", data);
				Log.d("url_all_products = ", url_all_products);

				// Send data 
				try
				{ 

					// Defined URL  where to send data
					URL url = new URL(url_all_products + "&" + data);
					Log.d("URL = ", url.toString() );

					// Send POST data request

					URLConnection conn = url.openConnection(); 
					conn.setDoOutput(true); 
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 
					wr.write( data ); 
					wr.flush(); 

					// Get the server response 

					reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					sb = new StringBuilder();
					String line = null;

					// Read Server Response
					while((line = reader.readLine()) != null)
					{
						// Append server response in string
						sb.append(line + "\n");
					}


					text = sb.toString();
					Log.d("PHP response = ", text);
				}
				catch(Exception ex)
				{

				}
				finally
				{
					try
					{

						reader.close();
					}

					catch(Exception ex) {}
				}
				// getting JSON string from URL
				JSONObject json = new JSONObject(text); /*jParser.makeHttpRequest(url_all_products, "GET", params);*/
				// Check your log cat for JSON reponse
				Log.d("Alle gegevens: ", json.toString());
				Log.d("params = ", params.toString());

				try {
					// Checking for SUCCESS TAG
					int success = json.getInt(TAG_SUCCESS);
					Log.d("Try", "gestart");
					if (success == 1) {



						// products found
						// Getting Array of Products
						products = json.getJSONArray(TAG_PRODUCTS);

						// looping through All Products
						for (int i = 0; i < products.length(); i++) {
							JSONObject c = products.getJSONObject(i);

							Log.d("For-loop", "Gestart!");
							// Storing each json item in variable
							String id = c.getString(TAG_PID);
							String leerlijn = c.getString(TAG_LEERLIJN);
							String vak = c.getString(TAG_VAK);
							String onderdeel = c.getString(TAG_ONDERDEEL);
							String kerndoel = c.getString(TAG_KERNDOEL);
							String groep = c.getString(TAG_GROEP);
							String informatie_kind = c.getString(TAG_INFORMATIE_KIND);
							String informatie_leraar = c.getString(TAG_INFORMATIE_LERAAR);

							Log.d("For-loop", "getStrings zijn uitgevoerd! Loop i = " + i);

							databaseHelper.saveCategoryRecord(id,leerlijn,vak,onderdeel,kerndoel,groep,informatie_kind,informatie_leraar);

							Log.d("For-loop", "databaseHelper.saveCategoryRecord() uitgevoerd!");

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							// adding each child node to HashMap key => value
							map.put(TAG_PID, id);
							map.put(TAG_LEERLIJN, leerlijn);
							map.put(TAG_VAK, vak);
							map.put(TAG_ONDERDEEL, onderdeel);
							map.put(TAG_KERNDOEL, kerndoel);
							map.put(TAG_GROEP, groep);
							map.put(TAG_INFORMATIE_KIND, informatie_kind);
							map.put(TAG_INFORMATIE_LERAAR, informatie_leraar);

							Log.d("For-loop", "Gelukt! Vóór itemsList.add(map)");
							// adding HashList to ArrayList
							itemsList.add(map);
							Log.d("For-loop", "Gelukt! Na itemsList.add(map)");
						}
						Log.d("Try", "beeindigd!");
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
							/* AANPASSEN VOOR LISTVIEW */							R.layout.list_item, new String[] { TAG_PID, TAG_LEERLIJN, TAG_VAK, TAG_ONDERDEEL, TAG_KERNDOEL, TAG_GROEP, TAG_INFORMATIE_KIND, TAG_INFORMATIE_LERAAR},
							new int[] { R.id.pid, R.id.leerlijn/*, R.id.vak, R.id.onderdeel, R.id.kerndoel, R.id.groep, R.id.informatie_kind, R.id.informatie_leraar */ /* MOETEN ER HIER NOG MEER ID'S?? */ });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}
	}


	//Create GetText Metod
	public  void  GetText()  throws  UnsupportedEncodingException
	{
		

		// Show response on activity
		//       content.setText( text  );

	}
}