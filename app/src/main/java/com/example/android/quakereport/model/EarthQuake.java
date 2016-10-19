package com.example.android.quakereport.model;

import java.io.Serializable;

/**
 * Created by u1d090 on 10/12/2016.
 */

public class EarthQuake implements Serializable {

	private String date = "";
	// Drawable resource ID
	private String location = "";
	private double magnitude;
	private String time = "";

	public EarthQuake(double magnitude, String location, String date, String time) {
		this.date = date;
		this.location = location;
		this.magnitude = magnitude;
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public String getLocation() {
		return location;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public String getTime() {
		return time;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setMagnitude(double magnitude) {
		this.magnitude = magnitude;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
