package com.foodtrip;

import java.util.List;

public class Test {
	public static void main(String[] args) {
		Route r = new Route("Bangalore","Chennai");
		//Time in hours
		int time = 1;
		List<double[]> points = r.getPointsInIntervalOf(time);
		for (double[] x : points) {
			System.out.println(x[0]);
			System.out.println(x[1]);
		}
	}
}
