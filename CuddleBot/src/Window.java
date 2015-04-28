import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.AbstractSequentialList;
import java.util.LinkedList;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

//import SensorReader.SensorFrame;
import weka.classifiers.Classifier;
import weka.core.Utils;

public class Window {
	public static final int WINDOW_SIZE = 54;
	static LinkedList<long[][]> windowList = new LinkedList<long[][]>();
	LinkedList<Double> listOfFrameSums = new LinkedList<Double>();
	LinkedList<Double> listOfCentRow = new LinkedList<Double>();
	LinkedList<Double> listOfCentCol = new LinkedList<Double>();
	private int lastFrameNum = -1;
	//LinkedList<Double> features = new LinkedList<Double>();
	public double wMax() {
		//System.out.println("wMax = " + getMax(listOfFrameSums) + "\n");
		return getMax(listOfFrameSums);
	}
	public double wMin() {
		//System.out.println("wMin = " + getMin(listOfFrameSums) + "\n");
		return getMin(listOfFrameSums);
	}
	public double wVar () {
	//	System.out.println("wVar = " + getVariance(listOfFrameSums) + "\n");
		return getVariance(listOfFrameSums);
	}
	public double wTVar() {
		//System.out.println("wTVar = " + getTotVar(listOfFrameSums) + "\n");
		return getTotVar(listOfFrameSums);
	}
	public double wMean() {
		//System.out.println("wMean = " + getMean(listOfFrameSums) + "\n");
		return getMean(listOfFrameSums);
	}
	public double wMed() {
		//System.out.println("wMed = " + getMedian(listOfFrameSums) + "\n");
		return getMedian(listOfFrameSums);
	}
	public double wAUC() {
		//System.out.println("wAUC = " + getWindowSum(listOfFrameSums) + "\n");
		return getWindowSum(listOfFrameSums);
	}
	public double rMax() {
		//System.out.println("rMax = " + getMax(listOfCentRow) + "\n");
		return getMax(listOfCentRow);
	}
	public double rMin() {
		//System.out.println("rMin = " + getMin(listOfCentRow) + "\n");
		return getMin(listOfCentRow);
	}
	public double rVar() {
		//System.out.println("rVar = " + getVariance(listOfCentRow) + "\n");
		return getVariance(listOfCentRow);
	}
	public double rTVar() {
		//System.out.println("rTVar = " + getTotVar(listOfCentRow) + "\n");
		return getTotVar(listOfCentRow);
	}
	public double rMean() {
		//System.out.println("rMean = " + getMean(listOfCentRow) + "\n");
		return getMean(listOfCentRow);
	}
	public double rMed() {
		//System.out.println("rMed = " + getMedian(listOfCentRow) + "\n");
		return getMedian(listOfCentRow);
	}
	public double rAUC() {
		//System.out.println("rAUC = " + getWindowSum(listOfCentRow) + "\n");
		return getWindowSum(listOfCentRow);
	}
	public double cMax() {
		//System.out.println("cMax = " + getMax(listOfCentCol) + "\n");
		return getMax(listOfCentCol);
	}
	public double cMin() {
		//System.out.println("cMin = " + getMin(listOfCentCol) + "\n");
		return getMin(listOfCentCol);
	}
	public double cVar() {
		//System.out.println("cVar = " + getVariance(listOfCentCol) + "\n");
		return getVariance(listOfCentCol);
	}
	public double cTVar() {
		//System.out.println("cTVar = " + getTotVar(listOfCentCol) + "\n");
		return getTotVar(listOfCentCol);
	}
	public double cMean() {
		//System.out.println("cMean = " + getMean(listOfCentCol) + "\n");
		return getMean(listOfCentCol);
	}
	public double cMed() {
		//System.out.println("cMed = " + getMedian(listOfCentCol) + "\n");
		return getMedian(listOfCentCol);
	}
	public double cAUC() {
		//System.out.println("cAUC = " + getWindowSum(listOfCentCol) + "\n");
		return getWindowSum(listOfCentCol);
	}
		
	public boolean isReady() {
		System.out.println("There are things in this here window, " + windowList.size() + " of them.");
		return (windowList.size() == WINDOW_SIZE);
	}
	
	public void addFrame(SensorReader.SensorFrame frame) {
		
		if( frame.count <= lastFrameNum  )
			return;
		
		lastFrameNum = frame.count;
		
		//System.out.println("adding a Frame to Window");
		if (windowList.size() >= WINDOW_SIZE) {
			windowList.remove();
			//System.out.println("removed from window list");
			listOfFrameSums.remove();
			//System.out.println("removed from framesums list");
			listOfCentRow.remove();
			//System.out.println("removed from centX list");
			listOfCentCol.remove();
			//System.out.println("removed from centY list");
		}
		windowList.add(frame.data);
		//System.out.println("added to window list");
		listOfFrameSums.add(frameSum(frame.data));
		//System.out.println("added to framesum list");
		listOfCentRow.add(centRow(frame.data));
		//System.out.println("added to centX list");
		listOfCentCol.add(centCol(frame.data));
		//System.out.println("added to centY list");
	}
	
	public double centRow(long[][] aFrame) {
		double framedSum = frameSum(aFrame);
		double rowCentroid = 0.0;
		for (int i = 0; i < 10; i++) {
			double rowTotal = 0;
			for (int j = 0; j < 10; j++) {
				rowTotal = rowTotal + aFrame[i][j];
			}
			rowCentroid = i*rowTotal;
		}
		return rowCentroid/framedSum;
	}
	
	public double centCol(long[][] aFrame) {
		double framedSum = frameSum(aFrame);
		double colCentroid = 0.0;
		for (int i = 0; i < 10; i++) {
			double colTotal = 0;
			for (int j = 0; j < 10; j++) {
				colTotal = colTotal + aFrame[j][i];
			}
			colCentroid = i*colTotal;
		}
		return colCentroid/framedSum;
	}
	
	public double frameSum(long[][] aFrame) {
		double sumInFrame = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				sumInFrame = sumInFrame + (double) aFrame[i][j];
			}
		}
		return sumInFrame;
	}
	
	 double getWindowSum(LinkedList<Double> data)  {
	        double sum = 0.0;
	        for(double a : data)
	            sum += a;
	        return sum;
	    }
	    
	    double getMean(LinkedList<Double> data) {
	    	return getWindowSum(data)/data.size();
	    }

	    double getVariance(LinkedList<Double> data) {
	        double mean = getMean(data);
	        double temp = 0;
	        for(double a :data)
	            temp += (mean-a)*(mean-a);
	        return temp/data.size();
	    }

	    double getTotVar(LinkedList<Double> data) {
	    	
	        double temp = 0;
	        for(int i = 1; i < data.size(); i++) {
	        	double diff = data.get(i) - data.get(i-1);
	            temp += Math.abs(diff);
	        }
	        return temp;
	    }

	    public double getMedian(LinkedList<Double> data) {
	       double[] b = new double[data.size()];
	       //System.out.println(data.size() + " = " + b.length);
	       //System.arraycopy(data, 0, b, 0, data.size());
	       for (int i = 0; i < data.size(); i++) {
	    	   b[i] = data.get(i);
	       }
	       Arrays.sort(b);

	       if (data.size() % 2 == 0) 
	       {
	          return (b[(data.size() / 2) - 1] + b[data.size() / 2]) / 2.0;
	       } 
	       else 
	       {
	          return b[data.size() / 2];
	       }
	    }
	    
	    public double getMax(LinkedList<Double> data) {
	    	double max = 0;
	    	for (int i = 0; i < data.size(); i++) {
	    	    if ( data.get(i) > max) 
	    	      max = data.get(i);
	    	}
	    	return max;
	    }
	    
	    public double getMin(LinkedList<Double> data) {
	    	double min = 1023;
	    	for (int i = 0; i < data.size(); i++) {
	    		if (data.get(i) < min)
	    			min = data.get(i);
	    		else if (data.get(i) < 0)
	    			min = 0;
	    	}
	    	return min;
	    } 
	    
	    /*BufferedReader datafile = readDataFile("nonenone.arff");
		 
		Instances data;
		try {
			data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} */
	    
	   public static BufferedReader readDataFile(String filename) {
			BufferedReader inputReader = null;
	 
			try {
				inputReader = new BufferedReader(new FileReader(filename));
			} catch (FileNotFoundException ex) {
				System.err.println("File not found: " + filename);
			}
	 
			return inputReader;
		}
	   
   	  public Instances getInstance() {
	   //public Instance getInstance() {
   		   		//System.out.println("trying to get instance");
	    	     FastVector      attributes;
	    	    // FastVector      attsRel;
	    	     //FastVector      attVals;
	    	     //FastVector      attValsRel;
	    	     
	    	     Instances       data = null;
	    	//     System.out.println("printing data: " + data);
	    	    // Instances       dataRel;
	    	     double[]        vals;
	    	   //  double[]        valsRel;
	    	    // int             i;
	    	 
	    	     // 1. set up attributes
	    	     /*attributes = new FastVector();
	    	     // - numeric
	    	     attributes.addElement(new Attribute("wMax"));
	    	     attributes.addElement(new Attribute("wMin"));
	    	     attributes.addElement(new Attribute("wMean")); 
	    	     attributes.addElement(new Attribute("wMed"));
	    	     attributes.addElement(new Attribute("wVar"));
	    	     attributes.addElement(new Attribute("wTVar"));
	    	     attributes.addElement(new Attribute("wAUC"));
	    	     attributes.addElement(new Attribute("rMax"));
	    	     attributes.addElement(new Attribute("rMin"));
	    	     attributes.addElement(new Attribute("rMean"));
	    	     attributes.addElement(new Attribute("rMed"));
	    	     attributes.addElement(new Attribute("rVar"));
	    	     attributes.addElement(new Attribute("rTVar"));
	    	     attributes.addElement(new Attribute("rAUC"));
	    	     attributes.addElement(new Attribute("cMax"));
	    	     attributes.addElement(new Attribute("cMin"));
	    	     attributes.addElement(new Attribute("cMean"));
	    	     attributes.addElement(new Attribute("cMed"));
	    	     attributes.addElement(new Attribute("cVar"));
	    	     attributes.addElement(new Attribute("cTVar"));
	    	     attributes.addElement(new Attribute("cAUC"));*/
	    	    // System.out.println("Printing attributes: " + attributes);
	    	     Attribute wMax = new Attribute("wMax");
					Attribute wMin = new Attribute("wMin");
					Attribute wMean = new Attribute("wMean");
					Attribute wMed = new Attribute("wMed");
					Attribute wVar = new Attribute("wVar");
					Attribute wTVar = new Attribute("wTVar");
					Attribute wAUC = new Attribute("wAUC");
					Attribute rMax = new Attribute("rMax");
					Attribute rMin = new Attribute("rMin");
					Attribute rMean = new Attribute("rMean");
					Attribute rMed = new Attribute("rMed");
					Attribute rVar = new Attribute("rVar");
					Attribute rTVar = new Attribute("rTVar");
					Attribute rAUC = new Attribute("rAUC");
					Attribute cMax = new Attribute("cMax");
					Attribute cMin = new Attribute("cMin");
					Attribute cMean = new Attribute("cMean");
					Attribute cMed = new Attribute("cMed");
					Attribute cVar = new Attribute("cVar");
					Attribute cTVar = new Attribute("cTVar");
					Attribute cAUC = new Attribute("cAUC");
					
					
					FastVector fvClassVal = new FastVector(7);
					fvClassVal.addElement("notouch");
					fvClassVal.addElement("constant");
					fvClassVal.addElement("rub");
					fvClassVal.addElement("pat");
					fvClassVal.addElement("scratch");
					fvClassVal.addElement("stroke");
					fvClassVal.addElement("tickle");

					 Attribute ClassAttribute = new Attribute("theClass", fvClassVal);
					//System.out.println("window is ready ie all 108 frames filled");
					 
					 FastVector fvWekaAttributes = new FastVector(21);
					 fvWekaAttributes.addElement(wMax);
					 fvWekaAttributes.addElement(wMin);
					 fvWekaAttributes.addElement(wMean);
					 fvWekaAttributes.addElement(wMed);
					 fvWekaAttributes.addElement(wVar);
					 fvWekaAttributes.addElement(wTVar);
					 fvWekaAttributes.addElement(wAUC);
					 fvWekaAttributes.addElement(rMax);
					 fvWekaAttributes.addElement(rMin);
					 fvWekaAttributes.addElement(rMean);
					 fvWekaAttributes.addElement(rMed);
					 fvWekaAttributes.addElement(rVar);
					 fvWekaAttributes.addElement(rTVar);
					 fvWekaAttributes.addElement(rAUC);
					 fvWekaAttributes.addElement(cMax);
					 fvWekaAttributes.addElement(cMin);
					 fvWekaAttributes.addElement(cMean);
					 fvWekaAttributes.addElement(cMed);
					 fvWekaAttributes.addElement(cVar);
					 fvWekaAttributes.addElement(cTVar);
					 fvWekaAttributes.addElement(cAUC);
					 fvWekaAttributes.addElement(ClassAttribute);
	    	     // 2. create Instances object
	    	     data = new Instances("WindowVals", fvWekaAttributes, 0);
	    	    // System.out.println("printing data: " + data);
	    	 
	    	     // 3. fill with data
	    	     // first instance
	    	     vals = new double[data.numAttributes()];
	    	     // - numeric
	    	     vals[0] = wMax();
	    	     
	    	     // - nominal
	    	     vals[1] = wMin();
	    	     // - string
	    	     vals[2] = wMean();
	    	     vals[3] = wMed();
	    	     vals[4] = wVar();
	    	     vals[5] = wTVar();
	    	     vals[6] = wAUC();
	    	     vals[7] = rMax();
	    	     vals[8] = rMin();
	    	     vals[9] = rMean();
	    	     vals[10] = rMed();
	    	     vals[11] = rVar();
	    	     vals[12] = rTVar();
	    	     vals[13] = rAUC();
	    	     vals[14] = cMax();
	    	     vals[15] = cMin();
	    	     vals[16] = cMean();
	    	     vals[17] = cMed();
	    	     vals[18] = cVar();
	    	     vals[19] = cTVar();
	    	     vals[20] = cAUC();
	    	     //System.out.println("Calculating vals: " + vals);
	    	    
	    	     // - relational
	    	    /* dataRel = new Instances(data.attribute(21).relation(), 0);
	    	     // -- first instance
	    	     valsRel = new double[2];
	    	     valsRel[0] = Math.PI + 1;
	    	     valsRel[1] = attValsRel.indexOf("val5.3");
	    	     dataRel.add(new Instance(1.0, valsRel));
	    	     // -- second instance
	    	     valsRel = new double[2];
	    	     valsRel[0] = Math.PI + 2;
	    	     valsRel[1] = attValsRel.indexOf("val5.2");
	    	     dataRel.add(new Instance(1.0, valsRel));
	    	     vals[4] = data.attribute(4).addRelation(dataRel);
	    	     // add*/
	    	     
	    	     Instance currInstance = new Instance(1.0, vals);
	    	     
	    	     data.add(currInstance);
	    	     return data;
	    	     //System.out.println("This is an instance " + currInstance);
	    	     //return currInstance;
	    	     // second instance
	    	    /* vals = new double[data.numAttributes()];  // important: needs NEW array!
	    	     // - numeric
	    	     vals[0] = Math.E;
	    	     // - nominal
	    	     vals[1] = attVals.indexOf("val1");
	    	     // - string
	    	     vals[2] = data.attribute(2).addStringValue("And another one!");
	    	     // - date
	    	     vals[3] = data.attribute(3).parseDate("2000-12-01");
	    	     // - relational
	    	     dataRel = new Instances(data.attribute(4).relation(), 0);
	    	     // -- first instance
	    	     valsRel = new double[2];
	    	     valsRel[0] = Math.E + 1;
	    	     valsRel[1] = attValsRel.indexOf("val5.4");
	    	     dataRel.add(new Instance(1.0, valsRel));
	    	     // -- second instance
	    	     valsRel = new double[2];
	    	     valsRel[0] = Math.E + 2;
	    	     valsRel[1] = attValsRel.indexOf("val5.1");
	    	     dataRel.add(new Instance(1.0, valsRel));*/
	    	  //  vals[4] = data.attribute(4).addRelation(dataRel);
	    	     // add
	    	  // data.add(new Instance(1.0, vals));
	    	    // */
	    	    // System.out.println("This is an instance: "+ data);
	    	   // return data;
   	   }
	   
	
}
