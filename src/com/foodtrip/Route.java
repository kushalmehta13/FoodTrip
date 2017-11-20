package com.foodtrip;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;

//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import org.json.JSONObject;
import org.json.JSONArray;

public class Route {
	private static String StartPoint;
	private static String EndPoint;
	private List<double[]> StopPoints;
	private List<double[]> geoPoints;
	private double[] LatLon;
	private String APIKEY = "AIzaSyCfiL7_1ad6PF9HUwOHs3KE8ZaWbO5RqzU";
	private static final String baseURL = "https://maps.googleapis.com/maps/api/directions/json";
	private String reqURL = null;
	private String response;
//	Client client;
//	static WebResource webResource;
	public Route(String origin,String dest) {
		StopPoints = new ArrayList<double[]>();
		LatLon = new double[2];
		geoPoints = new ArrayList<double[]>();
		StartPoint = origin;
		EndPoint = dest;
//		client = Client.create();
//		webResource = client.resource(baseURL+"?origin="+origin+"&destination="+dest+"&key="+APIKEY);
		reqURL = baseURL+"?origin="+origin+"&destination="+dest+"&key="+APIKEY;
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestProperty("Accept","application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				response += output;
			}
			conn.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Function to get lat and long of points on the route at regular time intervals. Default is every 1 hour.
	//Returns a list of lat,long along the route.
	public List<double[]> getPointsInIntervalOf(int time) {
//		JSONObject result = new JSONObject(getRoute(StartPoint,EndPoint));
		System.out.println(response.substring(4));
		JSONObject result = new JSONObject(response.substring(4));
		JSONArray steps = result.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
		int duration = 0;
		//Time in seconds because the JSON returned by google maps api has time in seconds
		int maxDuration = time * 60 * 60;
		int i;
		//logic for calculating stop points at regular intervals
		for(i=0;i<steps.length();i++) {
			duration += steps.getJSONObject(i).getJSONObject("duration").getInt("value");
			if(duration > maxDuration) {
				LatLon = calculateStopPoint(i,steps);
				StopPoints.add(LatLon);
				duration = duration - maxDuration;
			}
		}
		return StopPoints; 
	}
	
	// JSON grunt work to get the lat and long.
	private double[] calculateStopPoint(int i,JSONArray steps) {
		double lat = steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat");
		double lon = steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng");
		double temp[] = {lat,lon};
		return temp;
	}
	
	//JAX-RS call to the google api
//	private String getRoute(String o,String d) {
//	ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
//	if (response.getStatus() != 200) {
//		throw new RuntimeException("Failed: HTTP error code: "+response.getStatus());
//	}
//	return response.getEntity(String.class); 
//	}
}

