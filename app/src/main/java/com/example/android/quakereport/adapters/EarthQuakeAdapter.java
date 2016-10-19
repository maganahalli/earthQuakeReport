package com.example.android.quakereport.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.model.EarthQuake;

/**
 * Created by u1d090 on 10/12/2016.
 */

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {

	private static final String LOG_TAG = EarthQuakeAdapter.class.getSimpleName();

	public EarthQuakeAdapter(Context mContext, List<EarthQuake> mItems) {
		super(mContext, 0, mItems);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Check if the existing view is being reused, otherwise inflate the view
		View listItemView = convertView;
		if (listItemView == null) {
			listItemView = LayoutInflater.from(getContext()).inflate(
					R.layout.list_item, parent, false);
		}

		// Get the {@link AndroidFlavor} object located at this position in the list
		EarthQuake currentQuake = getItem(position);

		// Find the TextView in the list_item.xml layout with the ID version_name
		TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude);
		magnitudeTextView.setText("" + currentQuake.getMagnitude());

		// Set the proper background color on the magnitude circle.
		 // Fetch the background from the TextView, which is a GradientDrawable.
		 GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();

		 // Get the appropriate background color based on the current earthquake magnitude
		 int magnitudeColor = getMagnitudeColor(currentQuake.getMagnitude());

		 // Set the color on the magnitude circle
		 magnitudeCircle.setColor(magnitudeColor);

		TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
		dateTextView.setText(currentQuake.getDate());

		TextView locationTextView = (TextView) listItemView.findViewById(R.id.primary_location);
		locationTextView.setText(currentQuake.getLocation());

		TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
		timeTextView.setText(currentQuake.getTime());

		// Find the ImageView in the list_item.xml layout with the ID list_item_icon
		//ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);
		// Get the image resource ID from the current AndroidFlavor object and
		// set the image to iconView
		//iconView.setImageResource(currentQuake.getImageResourceId());

		// Return the whole list item layout (containing 2 TextViews and an ImageView)
		// so that it can be shown in the ListView
		return listItemView;
	}

	private int getMagnitudeColor(double magnitude) {
    int magnitudeColorResourceId;
    int magnitudeFloor = (int) Math.floor(magnitude);
    switch (magnitudeFloor) {
        case 0:
        case 1:
            magnitudeColorResourceId = R.color.magnitude1;
            break;
        case 2:
            magnitudeColorResourceId = R.color.magnitude2;
            break;
        case 3:
            magnitudeColorResourceId = R.color.magnitude3;
            break;
        case 4:
            magnitudeColorResourceId = R.color.magnitude4;
            break;
        case 5:
            magnitudeColorResourceId = R.color.magnitude5;
            break;
        case 6:
            magnitudeColorResourceId = R.color.magnitude6;
            break;
        case 7:
            magnitudeColorResourceId = R.color.magnitude7;
            break;
        case 8:
            magnitudeColorResourceId = R.color.magnitude8;
            break;
        case 9:
            magnitudeColorResourceId = R.color.magnitude9;
            break;
        default:
            magnitudeColorResourceId = R.color.magnitude10plus;
            break;
    }
    return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
 }
}
