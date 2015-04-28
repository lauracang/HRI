import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.auth.params.*;
import org.apache.http.client.params.*;
import org.apache.http.params.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;



import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
//import weka.core.AbstractInstance;
//import weka.core.DenseInstance;
import weka.core.Attribute;









import weka.core.converters.ConverterUtils.DataSource;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.Label;

public class MainGUI extends JFrame {
	private long[][] frameBufferB = new long[10][10]; // when ready
	private JPanel contentPane;
	private Thread mainThread;
	private WorkerRunnable worker;
	private boolean running = false;

	private static JPanel heatMap;
	private static JPanel[][] heatMapCells;
	private JLabel gestureLabelPtr;
	
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
				
		//Classifier loadedModel = WekaPredict.loadModel("randomForestnonenone.model");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI mainGUI = new MainGUI();
					mainGUI.setVisible(true);
					initHeatMap();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	
	/**
	 * Create the frame.
	 */
	public MainGUI() throws Exception {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1052, 648);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(711, 547, 113, 53);
		contentPane.add(btnStart);
		worker = new WorkerRunnable();
		
		
		
		// START the collection 
		btnStart.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
				
		    	mainThread = new Thread(worker);
		    	mainThread.start();
		    }
		});
		
		JPanel heatMapFrame = new JPanel();
		heatMapFrame.setBounds(26, 54, 600, 600);
		contentPane.add(heatMapFrame);
		heatMapFrame.setLayout(null);
		
		heatMap = heatMapFrame;
		
		
		
		// STOP the collection 
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(868, 547, 113, 53);
		contentPane.add(btnStop);
		
		JButton btnPurr = new JButton("PURR");
		btnPurr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*HttpClient httpClient = new DefaultHttpClient(); 
				try { 
					HttpPost request = new HttpPost("http://yoururl"); 
					StringEntity params = new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} "); 
					request.addHeader("content-type", "application/x-www-form-urlencoded"); 
					request.setEntity(params); HttpResponse response = httpClient.execute(request); 
					// handle response here... 
					}
				catch (Exception ex) { // handle exception here 
					
				} finally { 
					httpClient.getConnectionManager().shutdown(); 
				}
				}*/
				
				/*HttpClient httpclient = HttpClients.createDefault(); 
				HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/"); 
				// Request parameters and other properties. 
				List<NameValuePair> params = new ArrayList<NameValuePair>(2); 
				params.add(new BasicNameValuePair("param-1", "12345")); 
				params.add(new BasicNameValuePair("param-2", "Hello!")); 
				httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8")); 
				//Execute and get the response. 
				HttpResponse response = httpclient.execute(httppost); 
				HttpEntity entity = response.getEntity(); 
				if (entity != null) { 
					InputStream instream = entity.getContent(); 
					try { // do something useful 
					} 
					finally { 
						instream.close(); 
					} 
				}*/
					
				
			}
		});
		btnPurr.setBounds(804, 513, 89, 23);
		contentPane.add(btnPurr);
		
		JLabel lblSensorReaction = new JLabel("Sensor Reaction");
		lblSensorReaction.setBounds(251, 11, 135, 39);
		contentPane.add(lblSensorReaction);
		
		JLabel lblPrediction = new JLabel("PREDICTION");
		lblPrediction.setBounds(636, 99, 390, 59);
		contentPane.add(lblPrediction);
		Font labelFont = lblPrediction.getFont();
		String labelText = lblPrediction.getText();

		int stringWidth = lblPrediction.getFontMetrics(labelFont).stringWidth(labelText);
		int componentWidth = lblPrediction.getWidth();

		// Find out how much the font can grow in width.
		double widthRatio = (double)componentWidth / (double)stringWidth;

		int newFontSize = (int)(labelFont.getSize() * widthRatio);
		int componentHeight = lblPrediction.getHeight();

		// Pick a new font size so it will not be larger than the height of label.
		 int fontSizeToUse = Math.min(newFontSize, componentHeight);
		
		 System.out.println("fontSizeToUse = " + fontSizeToUse);
		// Set the label's font size to the newly determined size.
		lblPrediction.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		
		JLabel gestureLabel = new JLabel("");
		gestureLabel.setBounds(636, 169, 390, 59);
		contentPane.add(gestureLabel);
		gestureLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
		gestureLabelPtr = gestureLabel;
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent f) {
				MainGUI.this.worker.stop();
				//sensorSerial.startedCollection = false;
			}
		});
		
		
		
		
		
	/*	while (running == true) {
			long[][] currentFrame = sensorSerial.getFrame();
			Window currentWindow = new Window();
			currentWindow.addFrame(currentFrame);
			// send to Weka
			for (int i = 0; i < 2; i++) {
				System.out.println("wMax" + currentWindow.wMax());
			}
		}*/
		

	}

	public static void initHeatMap() {

		int width = heatMap.getBounds().width;
		int height = heatMap.getBounds().height;

		int numRows = 10;
		int numCols = 10;

		width = width - width % numCols;
		height = height - height % numRows;

		int cellWidth = width / numCols;
		int cellHeight = height / numRows;

		int cellSize = Math.min(cellWidth, cellHeight);

		//System.out.println("width: " + width + ", height: " + height
		//		+ ", cellWidth: " + cellWidth + ", cellHeight: " + cellHeight);

		heatMapCells = new JPanel[numRows][numCols];

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JPanel temp = new JPanel();
				temp.setBackground(Color.black);
				temp.setBounds(i * cellSize, j * cellSize, cellSize - 2,
						cellSize - 2);
				heatMap.add(temp);

				heatMapCells[i][j] = temp;
			}
		}
	}

	private Color getCellColor(long val) {
		//return new Color((int)(127 * (val / 1023.0 ) ),(int)( 127 * (val / 1023.0 ) ), (int)( 255* (val / 1023.0 ) ));
		return new Color(0,0,(int)( 255* (val / 1023.0 )));
		// int value = (int) val;
		// switch (value) {
		/*if ((0 <= val) && (val < 100)) {
			return new Color((int) (255 * val / 1023.0), 0, 0);
		} else if (100 <= val && val < 200) {
			return new Color((int) (127 * val / 1023.0), 0, 0);
		} else if (200 <= val && val < 300) {
			return new Color((int) (64 * val / 1023.0), 0, 0);
		} else if (300 <= val && val < 400) {
			return new Color((int) (64 * val / 1023.0),
					(int) (64 * val / 1023.0), 0);
		} else if (400 <= val && val < 500) {
			return new Color((int) (64 * val / 1023.0),
					(int) (127 * val / 1023.0), 0);
		} else if (500 <= val && val < 600) {
			return new Color((int) (64 * val / 1023.0),
					(int) (255 * val / 1023.0), 0);
		} else if (600 <= val && val < 700) {
			return new Color((int) (64 * val / 1023.0),
					(int) (255 * val / 1023.0), (int) (255 * val / 1023.0));
		} else if (700 <= val && val < 800) {
			return new Color((int) (64 * val / 1023.0),
					(int) (255 * val / 1023.0), (int) (127 * val / 1023.0));
		} else if (800 <= val && val < 900) {
			return new Color((int) (64 * val / 1023.0),
					(int) (255 * val / 1023.0), (int) (127 * val / 1023.0));
		} else if (900 <= val && val < 1024) {
			return new Color((int) (64 * val / 1023.0),
					(int) (255 * val / 1023.0), (int) (64 * val / 1023.0));
		} else {
			return new Color(0, 0, 0);
		}*/
	}

	class WorkerRunnable implements Runnable {
		private SensorReader sensorSerial;
		private boolean running = false;

		public WorkerRunnable() {
			sensorSerial = new SensorReader();
		}

		public void run() {
			running = true;
			sensorSerial.start();
			
			long[][] frameData;

			//System.out.println("WorkerRunnable Started");
			Window currentWindow = new Window();
			//System.out.println("collection has started: " + SensorReader.startedCollection + "\n");
			while (running && SensorReader.startedCollection == true) {
				try {
					SensorReader.SensorFrame frame = sensorSerial.getFrame();
				//	System.out.println("frame got");
					for (int row = 0; row < heatMapCells.length; row++) {
						for (int col = 0; col < heatMapCells.length; col++) {
							heatMapCells[row][col]
									.setBackground(getCellColor(frame.data[row][col]));
						}
					}

					//System.out.println(Arrays.deepToString(frame));
					
					

					
					// Classifier trainedModel = WekaPredict.loadModel("randomForestnonenone.model");
					// FastVector predictions = new FastVector();
					currentWindow.addFrame(frame);
					
					
					
					if (currentWindow.isReady()) {
				/*		Attribute wMax = new Attribute("wMax");
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
						 
						// Create an empty training set
						 Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
						 // Set class index
						 isTrainingSet.setClassIndex(3);*/
												 
						 // Make the last attribute be the class
						// instances.setClassIndex(instances.numAttributes() - 1);
						Instances currInstance = null;
						currInstance = currentWindow.getInstance();
						//Instance currInstance = new Instance(1.0, currInstance);
						String rootPath="C:/Users/Brian/Desktop/Laura/Projects/workspace/CuddleBot/"; 
						//QuestionInstanceClassifiy q = new QuestionInstanceClassifiy();
				      //  double result = q.classify(1.0d, 1, 1);
				       // System.out.println(result);
						//DataSource sourceFile = new DataSource(rootPath+"nonenone.arff");
						//Instances trainInstances = sourceFile.getDataSet();
						
						
						int predictInstance = 0;
						Classifier gestureModel;
						String[] classLabels = new String[7];
						classLabels[0]="notouch";
						classLabels[1]="constant";
						classLabels[2]="rub";
						classLabels[3]="pat";
						classLabels[4]="scratch";
						classLabels[5]="stroke";
						classLabels[6]="tickle";
						
						try {
							//Instances trainData = new Instances(readDataFile(rootPath + "nonenone.arff"));
							//int cIdx=trainData.numAttributes()-1;
							//trainData.setClassIndex(cIdx);
							//trainData.add(currInstance.instance(predictInstance));
							//gestureModel = (Classifier) weka.core.SerializationHelper.read(rootPath + "rFnonewotickle.model");
							gestureModel = (Classifier) weka.core.SerializationHelper.read(rootPath + "randomForestnonenone.model");

							Instances unlabeled = new Instances(currInstance);
							unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
							 Instances labeled = new Instances(unlabeled);
							 double predict = gestureModel.classifyInstance(unlabeled.instance(predictInstance));
				            //double result = gestureModel.classifyInstance(currInstance.instance(predictInstance));
							//String prediction=currInstance.classAttribute().value((int)result); 
							 System.out.println("predict val reads: " + predict);
							System.out.println("The predicted value of instance "+
							                    Integer.toString(predictInstance)+
							                    ": "+classLabels[(int)predict ]); 
							   //labeled.instance(i).setClassValue(predict);
							String labelPredDefault = "Collecting ...";
							String labelPred = classLabels[(int) predict];
							MainGUI.this.gestureLabelPtr.setText(labelPred);
						//	label.setBounds(701, 234, 500, 500);
			
							/*
							DatagramSocket dgSocket = new DatagramSocket();
							InetAddress ipAddr = InetAddress.getByName("10.10.10.175");
							byte[] bytes = labelPred.getBytes();
							DatagramPacket dgram = new DatagramPacket(bytes, bytes.length,ipAddr,1234);
							dgSocket.send(dgram);
							dgSocket.close();
							*/
							

							// Set the label's font size to the newly determined size.
							
						

				           // System.out.println("Result is: " + result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
								//which instance to predict class value
								//int s1=0;  

								//perform your prediction
								//double value=cls.classifyInstance(originalTrain.instance(s1));

								//get the name of the class value
								

						//System.out.println("instance got");
						//evaluateModel(trainedModel, currInstance);
						//currInstance.setDataset(data);
						//dataTrain.add(currInstance);
						

					}
					
					
					
					
				

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void stop() {
			running = false;
			sensorSerial.stop();
			SensorReader.startedCollection = false;
			Window.windowList.clear();
		}

	}
}
