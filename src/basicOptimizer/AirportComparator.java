package basicOptimizer;

import java.util.Comparator;

	  public class AirportComparator implements Comparator<Airport>{

	    @Override
	    public int compare(Airport o1, Airport o2) {
	      return (int) (o1.costSoFar - o2.costSoFar);
	    }
	  }
