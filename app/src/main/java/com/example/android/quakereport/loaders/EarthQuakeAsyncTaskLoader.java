package com.example.android.quakereport.loaders;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.quakereport.model.EarthQuake;
import com.example.android.quakereport.util.QueryUtils;

/**
 * Created by u1d090 on 10/13/2016.
 */

public class EarthQuakeAsyncTaskLoader extends AsyncTaskLoader<List<EarthQuake>> {

	String queryUrl = "";

	public EarthQuakeAsyncTaskLoader(Context context, String queryUrl) {
		super(context);
		this.queryUrl = queryUrl;
	}

	@Override
	public List<EarthQuake> loadInBackground() {
		if (queryUrl == null || "".equals(queryUrl)) {
			return new ArrayList<>();
		}
		return QueryUtils.extractEarthquakes(queryUrl);
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

}
