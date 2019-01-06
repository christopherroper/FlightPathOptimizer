package basicOptimizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * <p>
 * An example of how a user will use your best flight API.
 * </p>
 */
public class FindBestPathTester {

  public static void main(String[] args) throws FileNotFoundException {
    NetworkGraph airportGraph = null;

    try {
      airportGraph = new NetworkGraph("flights.csv");
    } catch (Exception e) {
      e.printStackTrace();
    }

//    BestPath shortestDistancePath2 = airportGraph.getBestPath("LCP", "REC", FlightCriteria.TIME);
//    System.out.println(shortestDistancePath2.toString());
//    BestPath shortestDistancePath3 = airportGraph.getBestPath("LCP", "REC", FlightCriteria.DELAY);
//    System.out.println(shortestDistancePath3.toString());
//    BestPath shortestDistancePath4 = airportGraph.getBestPath("LCP", "REC", FlightCriteria.COST);
//    System.out.println(shortestDistancePath4.toString());
//    BestPath shortestDistancePath5 = airportGraph.getBestPath("LCP", "REC", FlightCriteria.CANCELED);
//    System.out.println(shortestDistancePath5.toString());
    
    
    // Returns the shortest distance path of flights from MOB to ACV
    // Solution: a path of ['MOB', 'DFW', 'SFO', 'ACV'] and distance of 2253
     BestPath shortestDistancePath = airportGraph.getBestPath("MOB", "ACV", FlightCriteria.DISTANCE);
    System.out.println(shortestDistancePath.toString());
    //System.out.println("hello Casey");

    // Returns the shortest distance path of flights from SFO to DWF when flying
    // with DL
    // Solution: a path of ['SFO', 'SLC', 'DFW'] and distance of 1588
    BestPath shortestDistancePath2 = airportGraph.getBestPath("SFO", "DFW", FlightCriteria.DISTANCE, "DL");
    System.out.println(shortestDistancePath2.toString());

    // Returns the shortest flight time path from MOB to SLC
    // Solution: a path of ['MOB', 'DFW', 'SLC'] and time of ~269.25
    BestPath shortestTimePath = airportGraph.getBestPath("MOB", "SLC", FlightCriteria.TIME);
    System.out.println(shortestTimePath.toString());

    // Returns the fiscally cheapest path of flights from LAS to LAX
    // Solution: a path of ['LAS', 'LAX'] and cost of ~138.39
    BestPath cheapestPath = airportGraph.getBestPath("LAS", "LAX", FlightCriteria.COST);
    System.out.println(cheapestPath.toString());
  }

}
