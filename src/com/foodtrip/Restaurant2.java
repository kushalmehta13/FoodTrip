package com.foodtrip;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
import org.json.JSONObject;
import org.json.JSONArray;

//This class calls the zomato API
public class Restaurant2 {
	List<double[]> points;
	//private static String StartPoint;
	//private static String EndPoint;
	//private List<double[]> StopPoints;
	//private List<double[]> geoPoints;
	//private double[] LatLon;
	private String APIKEY = "8f84030f75ed4362a28aa642da3e4a62";
	private static final String baseURL = "https://developers.zomato.com/api/v2.1/geocode";
//	Client client;
//	static WebResource webResource;
//	ClientResponse response;
	JSONObject result;
	JSONArray rests;
	String response;
	private int total;
	private int max;
	public Restaurant2(double lat, double lon) {
		//points = p;
//		client = Client.create();
		String reqURL = baseURL+"?lat="+lat+"&lon="+lon;
		System.out.println(reqURL);
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("user-key", APIKEY);
			conn.setRequestProperty("Accept", "application/json");

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
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
//		webResource = client.resource(baseURL+"?lat="+lat+"&lon="+lon);
//		response = webResource.accept(MediaType.APPLICATION_JSON).header("user-key", APIKEY).get(ClientResponse.class);
//		if (response.getStatus() != 200) {
//			throw new RuntimeException("Failed: HTTP error code: "+response.getStatus());
//		}
		result = new JSONObject(response.substring(4));
		System.out.println(result);
		rests = result.getJSONArray("nearby_restaurants");
		total = rests.length();
		max = 5;
		if(max>total) {
			max = total;
		}
	}
	public int getMax()
	{
		return max;
	}
	
	public List<String> getRestaurantNames() {
		List<String> restNames = new ArrayList<String>();
		for(int i=0;i<max;++i) {
			restNames.add(rests.getJSONObject(i).getJSONObject("restaurant").getString("name"));
		}
		return restNames;
	}
	
	public List<String> getRestaurantLoc() {
		List<String> restLoc = new ArrayList<String>();
		for(int i=0;i<max;++i) {
			restLoc.add(rests.getJSONObject(i).getJSONObject("restaurant").getJSONObject("location").getString("locality"));
		}
		return restLoc;
	}
	
	public List<Double> getRestaurantLat() {
		List<Double> restLat = new ArrayList<Double>();
		for(int i=0;i<max;++i) {
			restLat.add(Double.parseDouble(rests.getJSONObject(i).getJSONObject("restaurant").getJSONObject("location").getString("latitude")));
		}
		return restLat;
	}
	public List<Double> getRestaurantLon() {
		List<Double> restLon = new ArrayList<Double>();
		System.out.println(Double.parseDouble(rests.getJSONObject(0).getJSONObject("restaurant").getJSONObject("location").getString("longitude")));
		for(int i=0;i<max;++i) {
			restLon.add(Double.parseDouble(rests.getJSONObject(i).getJSONObject("restaurant").getJSONObject("location").getString("longitude")));
		}
		return restLon;
	}
	public List<String> getRestaurantCuisines() {
		List<String> restCuisine = new ArrayList<String>();
		for(int i=0;i<max;++i) {

			restCuisine.add(rests.getJSONObject(i).getJSONObject("restaurant").getString("cuisines"));
		}
		return restCuisine;
	}
}
