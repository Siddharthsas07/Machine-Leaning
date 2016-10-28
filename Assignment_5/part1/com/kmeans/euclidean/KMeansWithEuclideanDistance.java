package com.kmeans.euclidean;

//import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class KMeansWithEuclideanDistance {

	private int numberOfCluster = -1; 
	private String inputFileName = null; 
	private String outputFileName = null;
	private List<Points> pointList = null;
	private List<Cluster> clusterList = null;
	
	private final int MAX_ITERATIONS = 25;
	
	public KMeansWithEuclideanDistance(int numberOfCluster, String inputFileName, 
			String outputFileName){
		
		this.numberOfCluster = numberOfCluster;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;	
	}

	public void findMeans(){
		pointList = readDataIntoList(inputFileName);
		clusterList = listClusters();
		findMeanIterativly();
		calucalteSSE();
	}
	
	
	private void findMeanIterativly(){
		int loopCount = 0;
		while(loopCount < MAX_ITERATIONS){
			List<Points> prevCentroids = copyCentroids();
			clearClusterPoints();
			assignPointsToCluster();
			calculateNewCentroids();
			List<Points> newCentroids = copyCentroids();
			if(!isCentroidsChanged(prevCentroids, newCentroids)){
				break;
			}
		}
		printClusters();
		//printValues();
	}
	
	private void printClusters(){
		try {
			System.setOut(new PrintStream(new File(outputFileName)));
			for(Cluster cluster : clusterList){
				System.out.print(cluster.getIndex() + " - ");
				//System.out.println("Cluster " + cluster.getCentroid().toString());
				for(Points points : cluster.getClusterPoints()){
					System.out.print(points.getId() + ", ");
					//System.out.println(points.toString());
				}
				System.out.println("");
			}
			
			/*for(Cluster cluster1 : clusterList){
				cluster1.printCluster();
			}*/
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
		
	private boolean isCentroidsChanged(List<Points> prevCentroids, List<Points> newCentroids){
		double dist = 0;
		for( int i = 0; i < newCentroids.size() ; i++ ){
			dist += calculateDistance(prevCentroids.get(i), newCentroids.get(i));
			if( dist > 0 )
				return true;
		}
		return false;
	}
	
	
	private void clearClusterPoints(){
		for(Cluster cluster : clusterList){
			cluster.clearClusterPoints();
		}
	}
	
	private void calculateNewCentroids(){
		for(Cluster cluster : clusterList){
			double sumOfX = 0;
			double sumOfY = 0;
			int pointsCnt = cluster.getClusterPoints().size();
			if(pointsCnt > 0 ){
				for(Points points : cluster.getClusterPoints()){
					sumOfX += points.getX();
					sumOfY += points.getY();
				}
				Points centroid = cluster.getCentroid();
				centroid.setX(sumOfX / pointsCnt);
				centroid.setY(sumOfY / pointsCnt);
			}
		}
	}
	
	private List<Points> copyCentroids(){
		List<Points> centroidList = new ArrayList<Points>();
		for(Cluster cluster : clusterList){
			
			Points centroid = cluster.getCentroid();
			Points copyCentroid = new Points(centroid.getX(), centroid.getY(), centroid.getId());
			centroidList.add(copyCentroid);
		}
		return centroidList;
	}
	
	private void assignPointsToCluster(){
		
		for(Points point : pointList){
			double minDistence = Double.MAX_VALUE;
			int centroidTobeAssigned = -1;
			int count = 0;
			for(Cluster cluster :  clusterList){
				double distance = calculateDistance(cluster.getCentroid(), point);
				if(distance < minDistence){
					minDistence = distance;
					centroidTobeAssigned = count;
				}
				count++;
			}
			point.setAssignedCluster(centroidTobeAssigned);
			Cluster cluster = clusterList.get(centroidTobeAssigned);
			cluster.addClusterPoints(point);
		}
	}
	
	private List<Cluster> listClusters(){
		int size = pointList.size();
		List<Cluster> clusterListTemp = new ArrayList<Cluster>();
		List<Integer> centroidIndexLst = new ArrayList<Integer>();
		for(int i = 0; i < numberOfCluster ; i++){
			boolean bNextCentroidSelected = false;
			while(!bNextCentroidSelected){
				Integer index = (int)(Math.random() * size) ;
//				Integer index = (int)( i * size / 6 );
				if(!centroidIndexLst.contains(index)){
					centroidIndexLst.add(index);
					bNextCentroidSelected = true;
					
					Points selectedPoint = pointList.get(index);
					Points centroid = new Points(selectedPoint.getX(), selectedPoint.getY(), selectedPoint.getId());
					Cluster cluster = new Cluster(centroid, i + 1 );
					clusterListTemp.add(cluster);
				}
			}
		}
		return clusterListTemp;
	}
	
	private List<Points> readDataIntoList(String fileName){
		final String STR_DELIM = "\\s";
		List<Points> pointList = new ArrayList<Points>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try{
			File file = new File(fileName);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			@SuppressWarnings("unused")
			String header = bufferedReader.readLine();
			String line = "";
			while(( line = bufferedReader.readLine() ) != null ){
				String[] dataArr = line.split(STR_DELIM);
				if(dataArr.length == 3){
					try{
						Points dataPoint = new Points(
								Double.parseDouble(dataArr[1]),
								Double.parseDouble(dataArr[2]),
								Integer.parseInt(dataArr[0]));
						pointList.add(dataPoint); 
					}catch (Exception e) {
					}
				}
			}
		}catch (Exception e) {
;			e.printStackTrace();
		}
		return pointList;
	}
	
	private double calculateDistance(Points centroid, Points genPoint){
		return Math.sqrt( Math.pow((centroid.getX() - genPoint.getX()), 2.0)  +  
				Math.pow((centroid.getY() - genPoint.getY()), 2.0) );
	}
	
	public void calucalteSSE(){
	    double sum = 0;
	    for(Cluster cluster : clusterList){
	         for(Points points : cluster.getClusterPoints()){
	        	 double temp = calculateDistance(cluster.getCentroid(), points);
	             sum += temp*temp;
	            }
	        }
	    System.out.println("SSE value for k = " + numberOfCluster + " is: " + sum);
	}
}

class Points{
	private double x;
	private double y;
	private int id;
	private int assignedCluster;
	
	public Points(double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAssignedCluster() {
		return assignedCluster;
	}
	public void setAssignedCluster(int assignedCluster) {
		this.assignedCluster = assignedCluster;
	}
	
	@Override
	public String toString() {
		return /*"Id : " + this.id +*/ "(" + this.x + ", " + this.y + ")";
	}
}

class Cluster{
	private List<Points> clusterPoints = new ArrayList<Points>();
	private Points centroid = null;
	private int index ;
	
	public Cluster(Points centroid , int index){
		this.centroid = centroid;
		this.index = index;
	}
	public List<Points> getClusterPoints() {
		return clusterPoints;
	}
	public void setClusterPoints(List<Points> clusterPoints) {
		this.clusterPoints = clusterPoints;
	}
	public void addClusterPoints(Points point) {
		clusterPoints.add(point);
	}
	public void clearClusterPoints() {
		clusterPoints.clear();
	}
	public Points getCentroid() {
		return centroid;
	}
	public void setCentroid(Points centroid) {
		this.centroid = centroid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	/*public void printCluster(){
		
		System.out.println(this.centroid.toString() + " - ");
		for(Points points : this.clusterPoints){
			System.out.print(points.toString() + ",");
		}
		System.out.println("");
	}*/
	
}