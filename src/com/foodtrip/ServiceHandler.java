package com.foodtrip;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.google.gson.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public String getFavRestList() {
		List<UserFav> favList = new ArrayList<UserFav>();
		UserFav u;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/favoriteroutes","root","");
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from fav");
			while(rs.next()) {
				u = new UserFav();
				u.setID(rs.getInt("ID"));
				u.setRest(rs.getString("Restaurant"));
				u.setOrigin(rs.getString("Origin"));
				u.setDest(rs.getString("Destination"));
				u.setLoc(rs.getString("Location"));
				favList.add(u);
			}
			conn.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		String favJSON = gson.toJson(favList);
		return favJSON;
	}
	
	public void addToFavRestList(String data) {
		String[] keyVals = data.split("&");
		String origin = "";
		String destination = "";
		String location = "";
		String restaurant = "";
		for(int i=0;i<keyVals.length;++i) {
			if(keyVals[i].contains("origin")) {origin = keyVals[i].split("=")[1];}
			if(keyVals[i].contains("destination")) {destination = keyVals[i].split("=")[1];}
			if(keyVals[i].contains("location")) {location = keyVals[i].split("=")[1];}
			if(keyVals[i].contains("restaurant")) {restaurant = keyVals[i].split("=")[1];}
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/favoriteroutes","root","");
			String query = "INSERT INTO `fav` (`ID`, `Restaurant`, `Origin`, `Destination`, `Location`) VALUES (NULL, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, restaurant);
			preparedStmt.setString(2, origin);
			preparedStmt.setString(3, destination);
			preparedStmt.setString(4, location);
			preparedStmt.execute();
			conn.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public String updateFavRest(String data) {
		String[] keyVals = data.split("&");
		int id = 0;
		String location = null;
		String restaurant = null;
		for(int i=0;i<keyVals.length;++i) {
			if(keyVals[i].contains("id")) {id = Integer.parseInt(keyVals[i].split("=")[1]);}
			if(keyVals[i].contains("location")) {location = keyVals[i].split("=")[1];}
			if(keyVals[i].contains("restaurant")) {restaurant = keyVals[i].split("=")[1];}
		}
		if(location != null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/favoriteroutes","root","");
				Statement stmt = conn.createStatement();
				String sql = "UPDATE `fav` SET `Location` = '"+ location +"' WHERE `fav`.`ID` = "+id;
				stmt.executeUpdate(sql);
				conn.close();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/favoriteroutes","root","");
				Statement stmt = conn.createStatement();
				String sql = "UPDATE `fav` SET `Restaurant` = '"+ restaurant +"' WHERE `fav`.`ID` = "+id;
				stmt.executeUpdate(sql);
				conn.close();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return "Updated successfully";
	}

	public String deleteFavRest(int id2) {
		int id = id2;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/favoriteroutes","root","");
			Statement stmt = conn.createStatement();
			String sql = "DELETE FROM `fav` WHERE `fav`.`ID` = "+id;
			stmt.executeUpdate(sql);
			conn.close();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Deleted entry";
	}

//	public void addFavRestToList(String data, int count) {
//		System.out.println("Adding");
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
//		u.setID(count);
//		u.setOrigin(origin);
//		u.setDest(destination);
//		u.setLoc(location);
//		u.setRest(restaurant);
//		
//		List<UserFav> tempList = null;
//		File f = new File("list_of_favs.ser");
//		if(f.exists() && !f.isDirectory()) {
//			System.out.println("Here");
//		     try
//		        {   
//		            // Reading the object from a file
//		            FileInputStream file = new FileInputStream("list_of_favs.ser");
//		            ObjectInputStream in = new ObjectInputStream(file);
//		             
//		            // Method for deserialization of object
//		            tempList = (List<UserFav>)in.readObject();
//		             
//		            in.close();
//		            file.close();
//		            System.out.println(tempList.toString());
//		            tempList.add(u);
//		            FileOutputStream file2 = new FileOutputStream("list_of_favs.ser");
//		            ObjectOutputStream out = new ObjectOutputStream(file2);
//		           
//		            // Method for serialization of object
//		            out.writeObject(tempList);
//		             
//		            out.close();
//		            file2.close();
//		        }
//		         
//		        catch(IOException ex)
//		        {
//		        	ex.printStackTrace();
//		        }
//		         
//		        catch(ClassNotFoundException ex)
//		        {
//		            System.out.println("ClassNotFoundException is caught");
//		        }
//		     
//		}
//		
//		else {
//			favList.add(u);
//		 try
//	        {   
//			 	
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
//	            ex.printStackTrace();
//	        }
//		}
//		 System.out.println("added to id : "+u.getID());
//		 //System.out.println(favList);
//	
//	}
//	
//	public String getFavRestList() {
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
//	            ex.printStackTrace();
//	        }
//	         
//	        catch(ClassNotFoundException ex)
//	        {
//	            System.out.println("ClassNotFoundException is caught");
//	        }
//		
//		Gson gson = new Gson();
//		String restJSON = gson.toJson(list);
//		System.out.println(restJSON);
//		return restJSON;
//	}
//	
//	public void updateFavRestList(String data) {
//		String[] keyVals = data.split("&");
//		int id = -1;
//		String location = "";
//		String restaurant = "";
//		for(int i=0;i<keyVals.length;++i) {
//			if(keyVals[i].contains("ID")) {id = Integer.parseInt(keyVals[i].split("=")[1]);}
//			if(keyVals[i].contains("location")) {location = keyVals[i].split("=")[1];}
//			if(keyVals[i].contains("restaurant")) {restaurant = keyVals[i].split("=")[1];}
//		}
//		Iterator it = favList.iterator();
//		while(it.hasNext()) {
//			UserFav u = (UserFav)it.next();
//			if(u.getID()==id) {
//				u.setLoc(location);
//				u.setRest(restaurant);
//				try{   
//		            //Saving of object in a file
//		            FileOutputStream file = new FileOutputStream("list_of_favs.ser");
//		            ObjectOutputStream out = new ObjectOutputStream(file);
//		             
//		            // Method for serialization of object
//		            out.writeObject(favList);
//		            out.close();
//		            file.close();
//			    }
//		        catch(IOException ex)
//		        {
//		            System.out.println("IOException is caught");
//		        }
//				System.out.println("updated id : "+id);
//				break;
//			}
//		}
//	}
//	
//	public void removeFavRestList(int id) {
//		Iterator it = favList.iterator();
//		while(it.hasNext()) {
//			UserFav u = (UserFav)it.next();
//			if(u.getID()==id) {
//				it.remove();
//				break;
//			}
//		}
//		System.out.println("removed the id : "+id);
//	}
}