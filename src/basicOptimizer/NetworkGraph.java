package basicOptimizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Scanner;

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
	HashMap<String, Airport> allAirports;
	String fileName;

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
	 * @throws FileNotFoundException 
	 */

	public BestPath getBestPath(String origin, String destination, FlightCriteria criteria) throws FileNotFoundException {
		importAirportData();
		BestPath best = new BestPath();
		best.path = new ArrayList<>();
		Airport originAirport = allAirports.get(origin);
		Airport destinationAirport = allAirports.get(destination);

		Comparator<Airport> comparator = new AirportComparator();
		PriorityQueue<Airport> airportEdgeQueue = new PriorityQueue<>(100,comparator);

		originAirport.costSoFar = 0;
		airportEdgeQueue.add(originAirport);

		while(!airportEdgeQueue.isEmpty()) {

			Airport toBeRemoved = airportEdgeQueue.remove();
			toBeRemoved.visited = true;
			//loop through all edges of queue min
			for(Entry<String, Flight> flight : toBeRemoved.neighboringFlights.entrySet()) {
				Flight currentFlight = flight.getValue();

				if(!currentFlight.destinationAirport.visited) {
					//    		  System.out.println("visiting " + flight.destinationAirport.airportName);
					double edgeCost = 0;

					switch (criteria) {
					case COST: edgeCost = currentFlight.cost;
					break;
					case DELAY: edgeCost = currentFlight.delay;
					break;
					case DISTANCE: edgeCost = currentFlight.distance;
					break;
					case CANCELED: edgeCost = currentFlight.cancellationProbability;
					break;
					case TIME: edgeCost = currentFlight.time;
					break;
					}

					if(currentFlight.destinationAirport.costSoFar > toBeRemoved.costSoFar + edgeCost) {
						currentFlight.destinationAirport.costSoFar = toBeRemoved.costSoFar + edgeCost;
						currentFlight.destinationAirport.previous = toBeRemoved;
					}

					//queue doesn't already contain flight
					if(!airportEdgeQueue.contains(currentFlight.destinationAirport)) {
						airportEdgeQueue.add(currentFlight.destinationAirport); 
					}
				} 
			}
		}

		best.pathLength = destinationAirport.costSoFar;

		while(destinationAirport != null) {
			best.path.add(destinationAirport.airportName);
			destinationAirport = destinationAirport.previous;
		}
		
		return best;
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
	 * @throws FileNotFoundException 
	 */
	public BestPath getBestPath(String origin, String destination, FlightCriteria criteria, String airliner) throws FileNotFoundException {
		importAirportData();
		BestPath best = new BestPath();
		best.path = new ArrayList<>();
		Airport originAirport = allAirports.get(origin);
		Airport destinationAirport = allAirports.get(destination);

		Comparator<Airport> comparator = new AirportComparator();
		PriorityQueue<Airport> airportEdgeQueue = new PriorityQueue<>(100,comparator);

		originAirport.costSoFar = 0;
		airportEdgeQueue.add(originAirport);

		while(!airportEdgeQueue.isEmpty()) {

			Airport toBeRemoved = airportEdgeQueue.remove();
			toBeRemoved.visited = true;
			//loop through all edges of queue min
			for(Entry<String, Flight> flight : toBeRemoved.neighboringFlights.entrySet()) {
				Flight currentFlight = flight.getValue();

				if(!currentFlight.destinationAirport.visited && currentFlight.carriers.contains(airliner)) {
					//    		  System.out.println("visiting " + flight.destinationAirport.airportName);
					double edgeCost = 0;

					switch (criteria) {
					case COST: edgeCost = currentFlight.cost;
					break;
					case DELAY: edgeCost = currentFlight.delay;
					break;
					case DISTANCE: edgeCost = currentFlight.distance;
					break;
					case CANCELED: edgeCost = currentFlight.cancellationProbability;
					break;
					case TIME: edgeCost = currentFlight.time;
					break;
					}

					if(currentFlight.destinationAirport.costSoFar > toBeRemoved.costSoFar + edgeCost) {
						currentFlight.destinationAirport.costSoFar = toBeRemoved.costSoFar + edgeCost;
						currentFlight.destinationAirport.previous = toBeRemoved;
					}

					//queue doesn't already contain flight
					if(!airportEdgeQueue.contains(currentFlight.destinationAirport)) {
						airportEdgeQueue.add(currentFlight.destinationAirport); 
					}
				} 
			}
		}

		best.pathLength = destinationAirport.costSoFar;

		while(destinationAirport != null) {
			best.path.add(destinationAirport.airportName);
			destinationAirport = destinationAirport.previous;
		}     
		return best;
	}

	public NetworkGraph (String fileIn) throws FileNotFoundException {
		fileName = fileIn;
	}

	public void importAirportData() throws FileNotFoundException {
		allAirports = new HashMap<>();
		Scanner file = new Scanner(new File(fileName));
		file.nextLine();  //imports header

		while(file.hasNextLine()) {
			String[] airportData = file.nextLine().split(",");
			String originationAirportString = airportData[0];
			String destinationAirportString = airportData[1];
			String carrierString = airportData[2];
			double delay = Double.parseDouble(airportData[3]);
			double canceled = Double.parseDouble(airportData[4]);
			double time = Double.parseDouble(airportData[5]);
			double distance = Double.parseDouble(airportData[6]);
			double cost = Double.parseDouble(airportData[7]);

			Airport currentAirport;
			if(!allAirports.containsKey(originationAirportString)) {
				currentAirport = new Airport(originationAirportString);
				allAirports.put(originationAirportString, currentAirport);
			} else {
				currentAirport = allAirports.get(originationAirportString);
			}

			Airport destinationAirport;
			if(!allAirports.containsKey(destinationAirportString)) {
				destinationAirport = new Airport(destinationAirportString);
				allAirports.put(destinationAirportString, destinationAirport);
			} else {
				destinationAirport = allAirports.get(destinationAirportString);
			}

			Flight currentFlight;
			if(!currentAirport.neighboringFlights.containsKey(destinationAirportString)) {
				currentFlight = new Flight(destinationAirport);
				currentAirport.neighboringFlights.put(destinationAirportString, currentFlight);
			} else {
				currentFlight = currentAirport.neighboringFlights.get(destinationAirportString);
			}

			currentFlight.carriers.add(carrierString);
			currentFlight.delayData.add(delay);
			currentFlight.cancellationData.add(canceled);
			currentFlight.timeData.add(time);
			currentFlight.distanceData.add(distance);
			currentFlight.costData.add(cost);

			currentFlight.delay = currentFlight.updateAverage(currentFlight.delayData);
			currentFlight.cancellationProbability = currentFlight.updateAverage(currentFlight.cancellationData);
			currentFlight.time = currentFlight.updateAverage(currentFlight.timeData);
			currentFlight.distance = currentFlight.updateAverage(currentFlight.distanceData);
			currentFlight.cost = currentFlight.updateAverage(currentFlight.costData);
		}

		file.close();
	}
}
