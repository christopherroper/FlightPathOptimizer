package basicOptimizer;

import java.util.ArrayList;

public class Airport {
    String originationAirportName;
    ArrayList <Flight> neighboringFlights;
    double costSoFar;
    Airport previous;
    boolean visited;

    public Airport(String airportName) {
      originationAirportName = airportName;
      neighboringFlights = new ArrayList<>();
      visited = false;
      costSoFar=Integer.MAX_VALUE;
      previous = null;
    }
  }
