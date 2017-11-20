package com.foodtrip;

public class favStopPoints {
	double lat;
	double lon;
	String name;
	String cuisine;
	String locality;
	public favStopPoints(double lat, double lon, String name, String cuisine,String locality) {
		// TODO Auto-generated constructor stub
		this.lat = lat;
		this.lon = lon;
		this.name = name;
		this.cuisine = cuisine;
		this.locality = locality;
	}
	public String toString() {
	    return "[" + locality + lat + " " + lon +
		       " " + name +" "+ cuisine+ "]";
	}

}
