package com.foodtrip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
@Path("/")
public class service {
	private ServiceHandler s;
	public service() {
		s = new ServiceHandler();
	}
	//handles Favorable Stop Points
	//route/{origin}/{destination}/{time}
	@GET
	@Path("{origin}/{destination}/{time}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStops(@PathParam("origin") String origin,@PathParam("destination") String dest,@PathParam("time") int time) {
		return s.getFavStopPoints(origin,dest,time);
	}
	@GET
	@Path("{origin}/{destination}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStopsDef(@PathParam("origin") String origin,@PathParam("destination") String dest) {
		return s.getFavStopPoints(origin,dest,2);
	}
	@GET
	@Path("{origin}/{destination}/{time}/cuisine/{cuisine}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStopsCuisine(@PathParam("origin") String origin,@PathParam("destination") String dest,@PathParam("time")int time,@PathParam("cuisine")String cuisine) {
		return s.getFavCuisine(origin,dest,time,cuisine);
	}
	@GET
	@Path("{origin}/{destination}/cuisine/{cuisine}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStopsCuisineDef(@PathParam("origin") String origin,@PathParam("destination") String dest,@PathParam("cuisine")String cuisine) {
		return s.getFavCuisine(origin,dest,2,cuisine);
	}
	@GET
	@Path("{origin}/{destination}/{time}/restaurant/{rest}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStopsRest(@PathParam("origin") String origin,@PathParam("destination") String dest,@PathParam("time")int time,@PathParam("rest")String rest) {
		return s.getFavRest(origin,dest,time,rest);
	}
	@GET
	@Path("{origin}/{destination}/restaurant/{rest}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getFavStopsRestDef(@PathParam("origin") String origin,@PathParam("destination") String dest,@PathParam("rest")String rest) {
		return s.getFavRest(origin,dest,2,rest);
	}
	
}
