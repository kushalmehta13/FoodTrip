package com.foodtrip;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		Route r = new Route("Bangalore","Chennai");
		//Time in hours
//		int time = 1;
//		List<double[]> points = r.getPointsInIntervalOf(time);
//		for (double[] x : points) {
//			System.out.println(x[0]);
//			System.out.println(x[1]);
		Restaurant2 r1 = new Restaurant2(12.8930750,77.5795280);
//		System.out.println(r1.getRestaurantCuisines());
		ServiceHandler s = new ServiceHandler();
		System.out.println(s.getFavCuisine("Jaipur","Delhi",1,"American"));
		}
	}
