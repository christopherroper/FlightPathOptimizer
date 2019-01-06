package basicOptimizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

/**
 * <p>
 * This class represents a graph of flights and airports along with specific
 * data about those flights. It is recommended to create an airport class and a
 * flight class to represent nodes and edges respectively. There are other ways
 * to accomplish this and you are free to explore those.
 * </p>
 * 
 * <p>
 * Testing will be done with different criteria for the "best" path so be sure
 * to keep all information from the given file. Also, before implementing this
 * class (or any other) draw a diagram of how each class will work in relation
 * to the others. Creating such a diagram will help avoid headache and confusion
 * later.
 * </p>
 * 
 * <p>
 * Be aware also that you are free to add any member variables or methods needed
 * to completed the task at hand
 * </p>
 */
public class NetworkGraph {
  ArrayList<Airport> allAirports;

  /**
   * <p>
   * Constructs a NetworkGraph object and populates it with the information
   * contained in the given file. See the sample files or a randomly generated one
   * for the proper file format.
   * </p>
   * 
   * <p>
   * You will notice that in the given files there are duplicate flights with some
   * differing information. That information should be averaged and stored
   * properly. For example some times flights are canceled and that is represented
   * with a 1 if it is and a 0 if it is not. When several of the same flights are
   * reported totals should be added up and then reported as an average or a
   * probability (value between 0-1 inclusive).
   * </p>
   * 
   * @param flightInfoPath
   *          - The path to the file containing flight data. This should be a
   *          *.csv(comma separated value) file
   * 
   * @throws FileNotFoundException
   *           The only exception that can be thrown in this assignment and only
   *           if a file is not found.
   */

  /**
   * This method returns a BestPath object containing information about the best
   * way to fly to the destination from the origin. "Best" is defined by the
   * FlightCriteria parameter <code>enum</code>. This method should throw no
   * exceptions and simply return a BestPath object with information dictating the
   * result. If a destination or origin is not contained in this instance of
   * NetworkGraph simply return a BestPath object with no path (not a
   * <code>null</code> path). If origin or destination are <code>null</code>, also
   * return a BestPath object with no path.
   * 
   * @param origin
   *          - The starting location to find a path from. This should be a 3
   *          character long string denoting an airport.
   * 
   * @param destination
   *          - The destination location from the starting airport. Again, this
   *          should be a 3 character long string denoting an airport.
   * 
   * @param criteria
   *          - This enum dictates the definition of "best". Based on this value a
   *          path should be generated and return.
   * 
   * @return - An object containing path information including origin,
   *         destination, and everything in between.
   */

  public BestPath getBestPath(String origin, String destination, FlightCriteria criteria) {
    BestPath best = new BestPath();
    best.path = new ArrayList<>();
    Airport originAirport = null;
    Airport destinationAirport = null;

    for(Airport airport : allAirports) {
      if(airport.originationAirportName.equals(origin)) {
        originAirport = airport;
      }
      if(airport.originationAirportName.equals(destination)) {
        destinationAirport = airport;
      }
      airport.visited=false;
    }

    Comparator<Airport> comparator = new AirportComparator();
    PriorityQueue<Airport> airportEdgeQueue = new PriorityQueue(100,comparator);

    originAirport.costSoFar = 0;
    airportEdgeQueue.add(originAirport);

    while(!airportEdgeQueue.isEmpty()) {
      //identify minimum cost, tag as airport
      Airport toBeRemoved=airportEdgeQueue.peek();

      if(toBeRemoved.equals(destinationAirport)) {
        best.path.add(destinationAirport.originationAirportName);
        best.pathLength=toBeRemoved.costSoFar;
        while((!toBeRemoved.equals(originAirport)) && toBeRemoved.previous != null) {
          best.path.add(toBeRemoved.previous.originationAirportName);
          toBeRemoved = toBeRemoved.previous;
        }     
        return best;    
      }

      airportEdgeQueue.remove();
      toBeRemoved.visited = true;
      //loop through all edges of queue min
      for(Flight flight : toBeRemoved.neighboringFlights) {
        if(flight.destinationAirport.visited == false) {
          //queue doesn't already contain node
          if(!airportEdgeQueue.contains(flight.destinationAirport)) {
            switch (criteria) {
            case COST: flight.destinationAirport.costSoFar=flight.cost;
            break;
            case DELAY: flight.destinationAirport.costSoFar=flight.delay;
            break;
            case DISTANCE: flight.destinationAirport.costSoFar=flight.distance;
            break;
            case CANCELED: flight.destinationAirport.costSoFar=flight.cancellationProbability;
            break;
            case TIME: flight.destinationAirport.costSoFar=flight.time;
            break;
            }
            flight.destinationAirport.costSoFar+=toBeRemoved.costSoFar;
            flight.destinationAirport.previous = toBeRemoved;
            airportEdgeQueue.add(flight.destinationAirport); 
          }
          //already in the queue
          else {
            double edgeCost = 0;
            switch (criteria) {
            case COST: edgeCost=flight.cost;
            break;
            case DELAY: edgeCost=flight.delay;
            break;
            case DISTANCE: edgeCost=flight.distance;
            break;
            case CANCELED: edgeCost=flight.cancellationProbability;
            break;
            case TIME: edgeCost=flight.time;
            break;
            }
            // compare current item cost with new cost
            if(flight.destinationAirport.costSoFar > toBeRemoved.costSoFar + edgeCost) {
              //if new cost is less, update current cost and previous node
              flight.destinationAirport.costSoFar = toBeRemoved.costSoFar + edgeCost;
              airportEdgeQueue.remove(flight.destinationAirport);
              airportEdgeQueue.add(flight.destinationAirport);
              flight.destinationAirport.previous = toBeRemoved;
            }
          }
        } 
      }
    }
    return null;
  }

  /**
   * <p>
   * This overloaded method should do the same as the one above only when looking
   * for paths skip the ones that don't match the given airliner.
   * </p>
   * 
   * @param origin
   *          - The starting location to find a path from. This should be a 3
   *          character long string denoting an airport.
   * 
   * @param destination
   *          - The destination location from the starting airport. Again, this
   *          should be a 3 character long string denoting an airport.
   * 
   * @param criteria
   *          - This enum dictates the definition of "best". Based on this value a
   *          path should be generated and return.
   * 
   * @param airliner
   *          - a string dictating the airliner the user wants to use exclusively.
   *          Meaning no flights from other airliners will be considered.
   * 
   * @return - An object containing path information including origin,
   *         destination, and everything in between.
   */
  public BestPath getBestPath(String origin, String destination, FlightCriteria criteria, String airliner) {
    BestPath best = new BestPath();
    best.path = new ArrayList<>();
    Airport originAirport = null;
    Airport destinationAirport = null;
    for(Airport airport : allAirports) {
      if(airport.originationAirportName.equals(origin)) {
        originAirport = airport;
      }
      if(airport.originationAirportName.equals(destination)) {
        destinationAirport = airport;
      }
      airport.visited=false;
    }
    Comparator<Airport> comparator = new AirportComparator();
    PriorityQueue<Airport> airportEdgeQueue = new PriorityQueue(100,comparator);

    originAirport.costSoFar = 0;
    airportEdgeQueue.add(originAirport);

    while(!airportEdgeQueue.isEmpty()) {
      Airport toBeRemoved=airportEdgeQueue.peek();

      if(toBeRemoved.equals(destinationAirport)) {
        best.path.add(destinationAirport.originationAirportName);
        best.pathLength=toBeRemoved.costSoFar;
        while((!toBeRemoved.equals(originAirport)) && toBeRemoved.previous != null) {
          best.path.add(toBeRemoved.previous.originationAirportName);
          toBeRemoved = toBeRemoved.previous;
        }     
        return best;    
      }

      airportEdgeQueue.remove();
      toBeRemoved.visited = true;
      for(Flight flight : toBeRemoved.neighboringFlights) {
        if(flight.destinationAirport.visited == false && flight.carriers.contains(airliner)) {
          // not in queue
          if(!airportEdgeQueue.contains(flight.destinationAirport)) {
            switch (criteria) {
            case COST: flight.destinationAirport.costSoFar=flight.cost;
            break;
            case DELAY: flight.destinationAirport.costSoFar=flight.delay;
            break;
            case DISTANCE: flight.destinationAirport.costSoFar=flight.distance;
            break;
            case CANCELED: flight.destinationAirport.costSoFar=flight.cancellationProbability;
            break;
            case TIME: flight.destinationAirport.costSoFar=flight.time;
            break;
            }
            flight.destinationAirport.costSoFar+=toBeRemoved.costSoFar;
            flight.destinationAirport.previous = toBeRemoved;
            airportEdgeQueue.add(flight.destinationAirport); 
          }
          //already in the queue
          else {
            double edgeCost = 0;
            switch (criteria) {
            case COST: edgeCost=flight.cost;
            break;
            case DELAY: edgeCost=flight.delay;
            break;
            case DISTANCE: edgeCost=flight.distance;
            break;
            case CANCELED: edgeCost=flight.cancellationProbability;
            break;
            case TIME: edgeCost=flight.time;
            break;
            }
            // compare current item cost with new cost
            if(flight.destinationAirport.costSoFar > toBeRemoved.costSoFar + edgeCost) {
              //if new cost is less, update current cost and previous node
              flight.destinationAirport.costSoFar = toBeRemoved.costSoFar + edgeCost;
              airportEdgeQueue.remove(flight.destinationAirport);
              airportEdgeQueue.add(flight.destinationAirport);
              flight.destinationAirport.previous = toBeRemoved;
            }
          }
        } 
      }
    }
    return null;
  }

  ArrayList<String> uniqueAirportNames;
  public NetworkGraph (String fileName) throws FileNotFoundException {
    uniqueAirportNames = new ArrayList<>();
    LinkedList<String> origins = new LinkedList<>();
    allAirports = new ArrayList<>();
    ArrayList<String> origin = new ArrayList<>();
    ArrayList<String> destination = new ArrayList<>();
    ArrayList<String> carrier = new ArrayList<>();
    ArrayList<Double> delay = new ArrayList<>();
    ArrayList<Double> canceled = new ArrayList<>();
    ArrayList<Double> time = new ArrayList<>();
    ArrayList<Double> distance = new ArrayList<>();
    ArrayList<Double> cost = new ArrayList<>();
    Scanner file = new Scanner(new File(fileName));

    file.nextLine();  //imports header
    while(file.hasNextLine()) {
      String nextLine = file.nextLine();
      String[]converted = nextLine.split(",");

      if(!uniqueAirportNames.contains(converted[0])) {
        uniqueAirportNames.add(converted[0]);
        Airport newAirport = new Airport(converted[0]);
        allAirports.add(newAirport);
      }

      origin.add(converted[0]);
      destination.add(converted[1]);
      carrier.add(converted[2]);
      delay.add(Double.parseDouble(converted[3]));
      canceled.add(Double.parseDouble(converted[4]));
      time.add(Double.parseDouble(converted[5]));
      distance.add(Double.parseDouble(converted[6]));
      cost.add(Double.parseDouble(converted[7]));  
    }
    file.close();

    for(Airport airportName : allAirports) {

      ArrayList<String> tempDestinations = new ArrayList<>();
      ArrayList<String> airportDestinations = new ArrayList<>();
      ArrayList<String> tempCarriers = new ArrayList<>();
      ArrayList<Double> tempDelays = new ArrayList<>();
      ArrayList<Double> tempCancellations = new ArrayList<>();
      ArrayList<Double> tempTimes = new ArrayList<>();
      ArrayList<Double> tempDistances = new ArrayList<>();
      ArrayList<Double> tempCosts = new ArrayList<>();
      ArrayList<Airport> uniqueDestinationList = new ArrayList<>();

      for(int i = 0; i < origin.size(); i++) {
        if(origin.get(i).equals(airportName.originationAirportName)) {
          if(!tempDestinations.contains(destination.get(i))) {
            tempDestinations.add(destination.get(i));
          }
          airportDestinations.add(destination.get(i));
          tempCarriers.add(carrier.get(i));
          tempDelays.add(delay.get(i));
          tempCancellations.add(canceled.get(i));
          tempTimes.add(time.get(i));
          tempDistances.add(distance.get(i));
          tempCosts.add(cost.get(i));
        }
      }
      for(String destinationAirport : tempDestinations) {

        for(Airport airport : allAirports) {
          if (airport.originationAirportName.equals(destinationAirport)){
            ArrayList<String> destinationCarriers = new ArrayList<>();
            int counter = 0;
            double delayAverage=0;
            double cancellationAverage=0;
            double timeAverage=0;
            double distanceAverage=0;
            double costAverage=0;
            for(int i = 0; i < airportDestinations.size(); i++) {
              if(airportDestinations.get(i).equals(destinationAirport)) {
                counter++;
                if(!destinationCarriers.contains(tempCarriers.get(i))) {
                  destinationCarriers.add(tempCarriers.get(i));
                }
                delayAverage += tempDelays.get(i);
                cancellationAverage += tempCancellations.get(i);
                timeAverage += tempTimes.get(i);
                distanceAverage += tempDistances.get(i);
                costAverage += tempCosts.get(i);
              }
            }

            delayAverage /= counter;
            cancellationAverage /= counter;
            timeAverage /= counter;
            distanceAverage /= counter;
            costAverage /= counter;
            if(!uniqueDestinationList.contains(airport)) {
              uniqueDestinationList.add(airport);
              Flight newFlight = new Flight(airport,destinationCarriers,delayAverage,cancellationAverage,timeAverage,distanceAverage,costAverage);
              airportName.neighboringFlights.add(newFlight);
            }
          }
        }
      }
    }
  }
}
