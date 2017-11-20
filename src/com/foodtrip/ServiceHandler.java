package com.foodtrip;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.gson.*;
//This class calls and combines methods of route and restaurant
//It is acts like a translator of the service
public class ServiceHandler {
	private List<UserFav> favList = new ArrayList<UserFav>();
	int uCount = 0;
	private Restaurant2 rest;
	private Route route;
	public String getFavStopPoints(String origin, String dest, int time)  {
		route = new Route(origin,dest);
		List<double[]> points = route.getPointsInIntervalOf(time);
		String FSPJSON = getRestJson(points);
		return FSPJSON;
	}
	
	private String getRestJson(List<double[]> points) {
		String json = "";
//		int counter = 0;
		int max;
    	for(double[] x: points) {
    		rest = new Restaurant2(x[0],x[1]);
       		max = rest.getMax();
       		favStopPoints[]  fList = new favStopPoints[max];
       		System.out.println(max);
    		for(int i=0;i<max;++i) {
    			fList[i] = new favStopPoints(rest.getRestaurantLat().get(i),rest.getRestaurantLon().get(i),rest.getRestaurantNames().get(i),rest.getRestaurantCuisines().get(i),rest.getRestaurantLoc().get(i));
    		}
    	json += new Gson().toJson(fList) + ",";
	}
    	String newJson = "";
    	newJson += "{";
    	int x = 0;
    	for(int i=0;i<json.substring(4).length();++i) {
    		if(json.charAt(i) == '[') {
    			newJson += "\"stop"+x+++"\":[";
    		}
    		else {
    			newJson += json.charAt(i);
    		}
    	}
    	newJson += "\"}]}";
    	return newJson;
	}

	public String getFavCuisine(String origin, String dest, int time, String cuisine) {
		route = new Route(origin,dest);
		List<double[]> points = route.getPointsInIntervalOf(time);
		String FSPJSON = getRestJson(points);
		JSONObject newJSON = new JSONObject();
		JSONObject json = new JSONObject(FSPJSON);
		Iterator<?> keys = json.keys();
		while(keys.hasNext()) {
			String key = (String)keys.next();
			JSONArray stops = json.getJSONArray(key);
			JSONArray newRests = new JSONArray();
			for(int i=0;i<stops.length();++i) {
				if(stops.getJSONObject(i).getString("cuisine").toLowerCase().contains(cuisine.toLowerCase())) {
					newRests.put(stops.getJSONObject(i));
				}
			}
			if(newRests.length()!=0)
				newJSON.put(key, newRests);
		}
		return  newJSON.toString();
	}

	public String getFavRest(String origin, String dest, int time, String rest) {
		 route = new Route(origin,dest);
		List<double[]> points = route.getPointsInIntervalOf(time);
		String FSPJSON = getRestJson(points);
		JSONObject newJSON = new JSONObject();
		JSONObject json = new JSONObject(FSPJSON);
		Iterator<?> keys = json.keys();
		while(keys.hasNext()) {
			String key = (String)keys.next();
			JSONArray stops = json.getJSONArray(key);
			JSONArray newRests = new JSONArray();
			for(int i=0;i<stops.length();++i) {
				if(stops.getJSONObject(i).getString("name").toLowerCase().contains(rest.toLowerCase())) {
					newRests.put(stops.getJSONObject(i));
				}
			}
			if(newRests.length()!=0)
				newJSON.put(key, newRests);
		}
		return  newJSON.toString();
	}


//	public void addFavRest(String data) {
//		String[] keyVals = data.split("&");
//		String origin = "";
//		String destination = "";
//		String location = "";
//		String restaurant = "";
//		for(int i=0;i<keyVals.length;++i) {
//			if(keyVals[i].contains("origin")) {origin = keyVals[i].split("=")[1];}
//			if(keyVals[i].contains("destination")) {destination = keyVals[i].split("=")[1];}
//			if(keyVals[i].contains("location")) {location = keyVals[i].split("=")[1];}
//			if(keyVals[i].contains("restaurant")) {restaurant = keyVals[i].split("=")[1];}
//		}
//		UserFav u = new UserFav();
//		u.setID(uCount++);
//		u.setOrigin(origin);
//		u.setDest(destination);
//		u.setLoc(location);
//		u.setRest(restaurant);
//		favList.add(u);
//		
//		 try
//	        {   
//	            //Saving of object in a file
//	            FileOutputStream file = new FileOutputStream("list_of_favs.ser");
//	            ObjectOutputStream out = new ObjectOutputStream(file);
//	             
//	            // Method for serialization of object
//	            out.writeObject(favList);
//	             
//	            out.close();
//	            file.close();
//	 
//	        }
//	         
//	        catch(IOException ex)
//	        {
//	            System.out.println("IOException is caught");
//	        }
//		
//	}
//	public String getFavRests() {
//		List<UserFav> list = null;
//	     try
//	        {   
//	            // Reading the object from a file
//	            FileInputStream file = new FileInputStream("list_of_favs.ser");
//	            ObjectInputStream in = new ObjectInputStream(file);
//	             
//	            // Method for deserialization of object
//	            list = (List<UserFav>)in.readObject();
//	             
//	            in.close();
//	            file.close();
//	        }
//	         
//	        catch(IOException ex)
//	        {
//	            System.out.println("IOException is caught");
//	        }
//	         
//	        catch(ClassNotFoundException ex)
//	        {
//	            System.out.println("ClassNotFoundException is caught");
//	        }
//		
//		Gson gson = new Gson();
//		String restJSON = gson.toJson(list);
//		return restJSON;
//	}
}
