import java.util.Arrays;
import java.util.LinkedList;


public class FrameStatistics{
	// given a window of frame vals, return stats
	LinkedList<Double> data = new LinkedList<Double>();
    double size;    

    public FrameStatistics(LinkedList<Double> data) {
        this.data = data;
        size = data.size();
    }   

    double getWindowSum()  {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum;
    }
    
    double getMean() {
    	return getWindowSum()/size;
    }

    double getVariance() {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }

    double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double median() {
       double[] b = new double[data.size()];
       System.arraycopy(data, 0, b, 0, b.length);
       Arrays.sort(b);

       if (data.size() % 2 == 0) 
       {
          return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
       } 
       else 
       {
          return b[b.length / 2];
       }
    }
    
    public double getMax() {
    	double max = 0;
    	for (int i = 1; i <= size; i++) {
    	    if ( data.get(i) > max) 
    	      max = data.get(i);
    	}
    	return max;
    }
    
    public double getMin() {
    	double min = 1023;
    	for (int i = 1; i<=size; i++) {
    		if (data.get(i) < min)
    			min = data.get(i);
    		else if (data.get(i) < 0)
    			min = 0;
    	}
    	return min;
    } 
   
}