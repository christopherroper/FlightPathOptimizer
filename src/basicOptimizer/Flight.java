package basicOptimizer;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Flight {
    ArrayList<String> carriers = new ArrayList<>();
    Airport destinationAirport;
    double delay;
    double cancellationProbability;
    double time;
    double distance;
    double cost;
    public Flight(Airport destinationIn, ArrayList<String> carriers, double delay, double cancellation, double time, double distance, double cost) throws FileNotFoundException {
      destinationAirport = destinationIn;
      this.carriers.addAll(carriers);
      this.delay = delay;
      this.cancellationProbability = cancellation;
      this.time = time;
      this.distance = distance;
      this.cost = cost;
    }
  }
