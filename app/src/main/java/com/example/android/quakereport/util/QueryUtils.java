package com.example.android.quakereport.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.quakereport.model.EarthQuake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.android.quakereport.activities.EarthquakeActivity.LOG_TAG;

/**
 * Created by u1d090 on 10/13/2016.
 */

public class QueryUtils {

	/**
	 * Returns new URL object from the given string URL.
	 */
	private static URL createUrl(String stringUrl) {
		URL url = null;
		try {
			url = new URL(stringUrl);
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error with creating URL ", e);
		}
		return url;
	}

	/**
	 * Return a list of {@link EarthQuake} objects that has been built up from
	 * parsing a JSON response.
	 */
	public static ArrayList<EarthQuake> extractEarthquakes(String requestUrl) {

		// Create an empty ArrayList that we can start adding earthquakes to
		ArrayList<EarthQuake> earthquakes = new ArrayList<>();

		// If the JSON string is empty or null, then return early.
		if (TextUtils.isEmpty(requestUrl)) {
			return earthquakes;
		}
		// Return the list of earthquakes
		return fetchEarthQuakes(requestUrl);
	}

	protected static ArrayList<EarthQuake> fetchEarthQuakes(String requestUrl) {
		ArrayList<EarthQuake> earthquakes = new ArrayList<>();

		// Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
		// is formatted, a JSONException exception object will be thrown.
		// Catch the exception so the app doesn't crash, and print the error message to the logs.
		try {

			// Create URL object
			URL url = createUrl(requestUrl);

			// Perform HTTP request to the URL and receive a JSON response back
			String earthquakeJSON = null;
			try {
				earthquakeJSON = makeHttpRequest(url);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error closing input stream", e);
			}

			// TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
			// build up a list of Earthquake objects with the corresponding data.
			JSONObject root = new JSONObject(earthquakeJSON);
			JSONArray features = root.getJSONArray("features");
			Date dateObject;
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD,yyyy h:mm a", Locale.ENGLISH);

			for (int i = 0; i < features.length(); i++) {
				JSONObject earthquake = features.getJSONObject(i);
				JSONObject properties = earthquake.getJSONObject("properties");
				double mag = properties.getDouble("mag");
				String place = properties.getString("place");
				long time = properties.getLong("time");
				dateObject = new Date(time);
				String dateToDisplay = formatDate(dateObject);
				String timeToDisplay = formatTime(dateObject);
				earthquakes.add(new EarthQuake(mag, place, dateToDisplay, timeToDisplay));
			}

		} catch (JSONException e) {
			// If an error is thrown when executing any of the above statements in the "try" block,
			// catch the exception here, so the app doesn't crash. Print a log message
			// with the message from the exception.
			Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
		}
		return earthquakes;
	}

	/**
	 * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
	 */
	private static String formatDate(Date dateObject) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
		return dateFormat.format(dateObject);
	}

	/**
	 * Return the formatted date string (i.e. "4:30 PM") from a Date object.
	 */
	private static String formatTime(Date dateObject) {
		SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
		return timeFormat.format(dateObject);
	}

	/**
	 * Make an HTTP request to the given URL and return a String as the response.
	 */
	private static String makeHttpRequest(URL url) throws IOException {
		String jsonResponse = "";

		// If the URL is null, then return early.
		if (url == null) {
			return jsonResponse;
		}

		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setReadTimeout(10000 /* milliseconds */);
			urlConnection.setConnectTimeout(15000 /* milliseconds */);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// If the request was successful (response code 200),
			// then read the input stream and parse the response.
			if (urlConnection.getResponseCode() == 200) {
				inputStream = urlConnection.getInputStream();
				jsonResponse = readFromStream(inputStream);
			} else {
				Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
			}
		} catch (IOException e) {
			Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return jsonResponse;
	}

	/**
	 * Convert the {@link InputStream} into a String which contains the
	 * whole JSON response from the server.
	 */
	private static String readFromStream(InputStream inputStream) throws IOException {
		StringBuilder output = new StringBuilder();
		if (inputStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String line = reader.readLine();
			while (line != null) {
				output.append(line);
				line = reader.readLine();
			}
		}
		return output.toString();
	}

	/**
	 * Create a private constructor because no one should ever create a {@link QueryUtils} object.
	 * This class is only meant to hold static variables and methods, which can be accessed
	 * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
	 */
	private QueryUtils() {
	}

}
