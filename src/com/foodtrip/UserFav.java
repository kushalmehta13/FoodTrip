package com.foodtrip;

import java.io.Serializable;

public class UserFav implements Serializable {
	private	int ID;
	private String origin;
	private String dest;
	private String restaurant;
	private String location;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDest() {
		return dest;
	}
	public void setDest(String dest) {
		this.dest = dest;
	}
	public String getRest() {
		return restaurant;
	}
	public void setRest(String rest) {
		this.restaurant = rest;
	}
	public String getLoc() {
		return location;
	}
	public void setLoc(String loc) {
		this.location = loc;
	}
	public String toString() {
	    return "{"+ID +" " + origin + dest + " " + restaurant +
		       " " + location +"}";
	}
}
