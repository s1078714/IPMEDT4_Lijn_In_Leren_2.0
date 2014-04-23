package com.android.lijninleren;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	static String url = "http://lijninleren.hostoi.com:80/www/android_connect/";
	
	CheckBox checkBoxFav;

	private String[] data;
	private String[] fragments;
	private int pos;

	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	@Override
    public void onBackPressed()
    {
        super.onBackPressed();
        FragmentBrowsen.onBackPressed();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerTitle = getTitle();
		data = getResources().getStringArray(R.array.menu_array);
		fragments = getResources().getStringArray(R.array.fragment_array);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActionBar().getThemedContext(), android.R.layout.simple_list_item_1, data);

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		final ListView navList = (ListView) findViewById(R.id.drawer);
		navList.setAdapter(adapter);

		navList.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){
				mTitle = data[pos];
				if ( pos == 4 )
				{
					finish();
				}
				getActionBar().setTitle(mTitle);
				FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
				tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragments[pos]));
				tx.commit();
				drawer.closeDrawer(navList);
			}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		/**
		 * Deactiveert icon in ActionBar
		 */
		getActionBar().setDisplayShowHomeEnabled(false);

		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  	/* host Activity */
				drawer,         		/* DrawerLayout object */
				R.drawable.ic_drawer,  	/* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  	/* "open drawer" description for accessibility */
				R.string.drawer_close  	/* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				System.out.println("Drawer is gesloten2222! mTitle = " + mTitle );
				System.out.println("Drawer is gesloten!");
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				System.out.println("mDrawerTitle = " + mDrawerTitle );
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		drawer.setDrawerListener(mDrawerToggle);

		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.main,Fragment.instantiate(MainActivity.this, fragments[2]));
		tx.commit();

		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...
		System.out.println( "getTitle na setTitle(data[pos]); = " + data[pos] );

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
