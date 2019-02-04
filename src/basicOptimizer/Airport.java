package basicOptimizer;

import java.util.HashMap;

public class Airport {
	String airportName;
	HashMap<String, Flight> neighboringFlights;
	double costSoFar;
	Airport previous;
	boolean visited;

	public Airport(String name) {
		airportName = name;
		neighboringFlights = new HashMap<>();
		visited = false;
		costSoFar=Integer.MAX_VALUE;
		previous = null;
	}
}
