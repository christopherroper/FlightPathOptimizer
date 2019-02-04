package basicOptimizer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;

public class Flight {
	HashSet<String> carriers = new HashSet<>();
	Airport destinationAirport;
	ArrayList<Double> delayData = new ArrayList<>();
	ArrayList<Double> cancellationData = new ArrayList<>();
	ArrayList<Double> timeData = new ArrayList<>();
	ArrayList<Double> distanceData = new ArrayList<>();
	ArrayList<Double> costData = new ArrayList<>();
	double delay;
	double cancellationProbability;
	double time;
	double distance;
	double cost;

	public Flight(Airport destination) throws FileNotFoundException {
		destinationAirport = destination;
	}

	public double updateAverage(ArrayList<Double> dataSet) {
		double sum = 0;
		for(Double data : dataSet) {
			sum += data;
		}
		return sum / dataSet.size();
	}
}
