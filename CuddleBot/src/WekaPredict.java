import java.util.ArrayList;
import java.util.Arrays;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.FastVector;
//import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

public class WekaPredict {

    public static void main(String[] args) {
    	double[] values = new double[21];
    	Arrays.fill(values, 0);
        WekaPredict q = new WekaPredict();
        double result = q.classify(values);
        System.out.println(result);
    }

    private Instance currInstance;

    public double classify(double[] values)  {

        // Create attributes to be used with classifiers
        // Test the model
        double result = -1;
        try {

            FastVector attributeList = new FastVector();

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
            //Attribute longitude = new Attribute("longitude");
            //Attribute carbonmonoxide = new Attribute("co");

            ArrayList<String> classVal = new ArrayList<String>();
            classVal.add("notouch");
            classVal.add("constant");
            classVal.add("rub");
            classVal.add("pat");
            classVal.add("scratch");
            classVal.add("stroke");
            classVal.add("tickle");


            attributeList.addElement(wMax);
            attributeList.addElement(wMin);
            attributeList.addElement(wMean);
            attributeList.addElement(wMed);
            attributeList.addElement(wVar);
            attributeList.addElement(wTVar);
            attributeList.addElement(wAUC);
            attributeList.addElement(rMax);
            attributeList.addElement(rMin);
            attributeList.addElement(rMean);
            attributeList.addElement(rMed);
            attributeList.addElement(rVar);
            attributeList.addElement(rTVar);
            attributeList.addElement(rAUC);
            attributeList.addElement(cMax);
            attributeList.addElement(cMin);
            attributeList.addElement(cMean);
            attributeList.addElement(cMed);
            attributeList.addElement(cVar);
            attributeList.addElement(cTVar);
            attributeList.addElement(cAUC);
           // attributeList.addElement(new Attribute("@@class@@",classVal));
            attributeList.addElement(new Attribute("@@class@@"));

            Instances data = new Instances("TestInstances",attributeList,0);


            // Create instances for each pollutant with attribute values latitude,
            // longitude and pollutant itself
            Instance currInstance = new Instance(data.numAttributes());
            data.add(currInstance);

            // Set instance's values for the attributes "latitude", "longitude", and
            // "pollutant concentration"
            currInstance.setValue(wMax, values[0]);
            currInstance.setValue(wMin, values[1]);
            currInstance.setValue(wMean, values[2]);
            currInstance.setValue(wMed, values[3]);
            currInstance.setValue(wVar, values[4]);
            currInstance.setValue(wTVar, values[5]);
            currInstance.setValue(wAUC, values[6]);
            currInstance.setValue(rMax, values[7]);
            currInstance.setValue(rMin, values[8]);
            currInstance.setValue(rMean, values[9]);
            currInstance.setValue(rMed, values[10]);
            currInstance.setValue(rVar, values[11]);
            currInstance.setValue(rTVar, values[12]);
            currInstance.setValue(rAUC, values[13]);
            currInstance.setValue(cMax, values[14]);
            currInstance.setValue(cMin, values[15]);
            currInstance.setValue(cMean, values[16]);
            currInstance.setValue(cMed, values[17]);
            currInstance.setValue(cVar, values[18]);
            currInstance.setValue(cTVar, values[19]);
            currInstance.setValue(cAUC, values[20]);
            
            // inst_co.setMissing(cluster);

            // load classifier from file
			String rootPath="C:/Users/Brian/Desktop/Laura/Projects/workspace/CuddleBot/"; 

            Classifier gestureModel = (Classifier) weka.core.SerializationHelper.read(rootPath + "randomForestnonenone.model");
            //Classifier rFModel = (Classifier) weka.core.SerializationHelper.read("/CO_J48Model.model");

            result = gestureModel.classifyInstance(currInstance);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}