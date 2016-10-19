/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport.activities;

import java.util.List;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.adapters.EarthQuakeAdapter;
import com.example.android.quakereport.loaders.EarthQuakeAsyncTaskLoader;
import com.example.android.quakereport.model.EarthQuake;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

	/**
	 * Constant value for the earthquake loader ID. We can choose any integer.
	 * This really only comes into play if you're using multiple loaders.
	 */
	private static final int EARTHQUAKE_LOADER_ID = 1;
	public static final String LOG_TAG = EarthquakeActivity.class.getName();
	/** URL for earthquake data from the USGS dataset */
	private static final String USGS_REQUEST_URL =
			"http://earthquake.usgs.gov/fdsnws/event/1/query";
	private EarthQuakeAdapter earthQuakeAdapter;

	protected boolean isNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager)
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.earthquake_activity);
		getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		Log.i(LOG_TAG, "Loader OnCreate");

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String minMagnitude = sharedPrefs.getString(
				getString(R.string.settings_min_magnitude_key),
				getString(R.string.settings_min_magnitude_default));

		String limit = sharedPrefs.getString(
				getString(R.string.settings_limit_label_key),
				getString(R.string.settings_limit_label_default));

		String orderBy = sharedPrefs.getString(
				getString(R.string.settings_order_by_key),
				getString(R.string.settings_order_by_default)
		);

		Uri baseUri = Uri.parse(USGS_REQUEST_URL);
		Uri.Builder uriBuilder = baseUri.buildUpon();

		uriBuilder.appendQueryParameter("format", "geojson");
		uriBuilder.appendQueryParameter("limit", limit);
		uriBuilder.appendQueryParameter("minmag", minMagnitude);
		uriBuilder.appendQueryParameter("orderby", orderBy);
		return new EarthQuakeAsyncTaskLoader(this, uriBuilder.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
		Log.i(LOG_TAG, "Loader Finished");

		// Hide loading indicator because the data has been loaded
		View loadingIndicator = findViewById(R.id.loading_indicator);
		loadingIndicator.setVisibility(View.GONE);
		// Find a reference to the {@link ListView} in the layout
		ListView earthquakeListView = (ListView) findViewById(R.id.list);
		TextView emptyView = (TextView) findViewById(R.id.empty_view);
		emptyView.setText(isNetworkAvailable() ? "No EarthQuakes Found" : "No Internet Connection");
		// If there is no result, do nothing.
		if (data.isEmpty()) {
			return;
		}
		emptyView.setVisibility(View.GONE);
		if (earthQuakeAdapter != null) {
			earthQuakeAdapter.clear();
		}

		// Create a new {@link ArrayAdapter} of earthquakes
		EarthQuakeAdapter earthQuakeAdapter = new EarthQuakeAdapter(getApplicationContext(), data);
		// Get a reference to the ListView, and attach the adapter to the listView.
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(earthQuakeAdapter);
		// Set the adapter on the {@link ListView}
		// so the list can be populated in the user interface
		earthquakeListView.setAdapter(earthQuakeAdapter);
	}

	@Override
	public void onLoaderReset(Loader loader) {
		Log.i(LOG_TAG, "Loader Reset");
		if (earthQuakeAdapter != null) {
			earthQuakeAdapter.clear();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent settingsIntent = new Intent(this, SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
