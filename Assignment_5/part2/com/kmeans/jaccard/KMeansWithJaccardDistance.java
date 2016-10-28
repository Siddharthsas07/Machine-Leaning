package com.kmeans.jaccard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class KMeansWithJaccardDistance {

	private int numberOfCluster = 25; 
	private String inputFileName = null; 
	private String outputFileName = null;
	private String inputClusterIdFileName = null;
	private List<Text> textList = null;
	private List<Cluster> clusterList = null;
	
	private final int MAX_ITERATIONS = 25;
	
	public KMeansWithJaccardDistance(int numberOfCluster, String inputFileName, 
			String inputClusterIdFileName, String outputFileName){
		
		this.numberOfCluster = numberOfCluster;
		this.inputFileName = inputFileName;
		this.inputClusterIdFileName = inputClusterIdFileName;
		this.outputFileName = outputFileName;
		
	}

	public void findMeans(){
		
		textList = readDataIntoList();
		
		clusterList = listClusters();
		
		findMeanIterativly();
		
		calucalteSSE();
	}
	
	
	private void findMeanIterativly(){
		
		int loopCount = 0;
		
		while(loopCount < MAX_ITERATIONS){
			
			List<String> prevCentroids = copyCentroids();
			
			clearClusterPoints();
			assignPointsToCluster();
			
			calculateNewCentroids();
			
			List<String> newCentroids = copyCentroids();
			
			if(!isCentroidsChanged(prevCentroids, newCentroids)){
				break;
			}
		}
		
		printClusters();
	}
	
	private void printClusters(){
		
		try {
			System.setOut(new PrintStream(new File(outputFileName)));
			
			for(Cluster cluster : clusterList){
				
				System.out.print(cluster.getIndex() + " - ");
				for(Text points : cluster.getClusterPoints()){
					System.out.print(points.getId() + ", ");
				}
				System.out.println("");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isCentroidsChanged(List<String> prevCentroids, List<String> newCentroids){

		boolean bCentroidsChangedFlag = false;
		for( int i = 0; i < newCentroids.size() ; i++ ){
			if( ! prevCentroids.get(i).equals(newCentroids.get(i)) ){
				bCentroidsChangedFlag = true;
				break;
			}
		}
		return bCentroidsChangedFlag;
	}
	
	
	private void clearClusterPoints(){
		for(Cluster cluster : clusterList){
			cluster.clearClusterPoints();
		}
	}
	
	private void calculateNewCentroids(){
		
		for(Cluster cluster : clusterList){
			
			int pointsCnt = cluster.getClusterPoints().size(); 
			if(pointsCnt > 0 ){
				double minDistance = Double.MAX_VALUE;
				Text centroidTempRef = null;
				
				for(Text outerPoint : cluster.getClusterPoints()){
					double distance = 0;
					for(Text innerPoint : cluster.getClusterPoints()){
						if(!outerPoint.getId().equals(innerPoint.getId())){
							distance += calculateDistance(outerPoint, innerPoint);
						}
					}
					if(distance < minDistance){
						minDistance = distance;
						centroidTempRef = outerPoint;
					}
				}
				cluster.setCentroid(centroidTempRef);
			}
		}
	}
	
	private List<String> copyCentroids(){
		
		List<String> centroidIdList = new ArrayList<String>();
		for(Cluster cluster : clusterList){
			
			Text centroid = cluster.getCentroid();
			centroidIdList.add(centroid.getId());
		}
		return centroidIdList ;
	}
	
	private void assignPointsToCluster(){
		
		for(Text point : textList){
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
			clusterList.get(centroidTobeAssigned).addClusterPoints(point);
			
		}
	}
	
	private List<Cluster> listClusters(){
		
		List<Cluster> clusterListOfFile = new ArrayList<Cluster>();
		List<Cluster> clusterList = new ArrayList<Cluster>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try{
			File file = new File(inputClusterIdFileName);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			
			String line = "";
			int index = 0;
//			for (int i = 0; i<numberOfCluster; i++){
			while(( line = bufferedReader.readLine() ) != null ){
//				line = bufferedReader.readLine();	
				if(line != null && line.length() > 0 && line.endsWith(",")){
					line = line.replace(",","");
				}
				int count = 0;
				for(Text text : textList){
					if(text.getId().equals(line)){
						break;
					}
					count++;
				}
				Text centroid = textList.get(count);
				Cluster cluster = new Cluster(centroid, ++index);
				clusterListOfFile.add(cluster);
			}
//			}
			int clusterPointSizeInFile = clusterListOfFile.size();
			List<Integer> centroidIndexLst = new ArrayList<Integer>();
			for (int i = 0; i<numberOfCluster; i++){
				boolean bNextCentroidSelected = false;
				while(!bNextCentroidSelected){
					Integer indexOfCluster = (int)(Math.random() * clusterPointSizeInFile);
					if(!centroidIndexLst.contains(indexOfCluster)){
						centroidIndexLst.add(indexOfCluster);
						bNextCentroidSelected = true;
						clusterList.add(clusterListOfFile.get(indexOfCluster));
					}
				}	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
			
			if(fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
				}
			}
		}
		return clusterList;
	}
	
	private List<Text> readDataIntoList(){
		
		List<Text> textList = new ArrayList<Text>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try{
			File file = new File(inputFileName);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			
			String line = "";
			while(( line = bufferedReader.readLine() ) != null ){
				
				Text text = splitDataAndGetPoint(line);
				if(text != null){
					textList.add(text);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
			
			if(fileReader != null){
				try {
					fileReader.close();
				} catch (IOException e) {
				}
			}
		}
		
		return textList;
	}
	
	private Text splitDataAndGetPoint(String line){
		
		String text = "";
		String id = "";
		
		int strIndex = line.indexOf("\"text\": \"");
		int endIndex = line.indexOf("\",",strIndex);
		
		if(strIndex + 9 < endIndex){
			text = line.substring(strIndex + 9,endIndex).trim();
		}else{
			return null;
		}
		
        while(text.contains("RT @")){
        	text = text.replace(text.substring(text.indexOf("RT @"), text.indexOf(":",text.indexOf("RT @"))+2), "");
        }
  
//        while(text.contains("@")){
//        	text = text.replace(text.substring(text.indexOf("@"), text.indexOf(":",text.indexOf("@"))+2), "");
//        }
        
        text.replaceAll("[^a-zA-Z0-9 ]", "");
        
		strIndex = line.indexOf("\"id\":");
		endIndex = line.indexOf(",",strIndex);
		if(strIndex + 9 < endIndex){
			id =  line.substring(strIndex + 6,endIndex).trim();
		}else{
			return null;
		}
		
		List<String> textList = null;
		String[] textArr = text.split("\\s");
		if( textArr != null && textArr.length > 0 ){
			textList = Arrays.asList(textArr);
		}else{
			return null;
		}
		
		return new Text(id, textList);
	}
	
	private double calculateDistance(Text centroid, Text genPoint){
		
		
		final Set<String> allTokens = new HashSet<String>();
        allTokens.addAll(centroid.getWordList());
        
        final int termsInString1 = allTokens.size();
        final Set<String> secondStringTokens = new HashSet<String>();
        secondStringTokens.addAll(genPoint.getWordList());
        
        final int termsInString2 = secondStringTokens.size();

        //now combine the sets
        allTokens.addAll(secondStringTokens);
        
        final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();

        //return Jaccard Distance
        return 1 - (float) (commonTerms) / (float) (allTokens.size());
		
		
	
		
		/*
		double intersectionOfAandB = 0;
		
		for(String word : genPoint.getWordList()){
			for(String word2 : centroid.getWordList()){
//			if(centroidWords.contains(word))
				if(word2 != null && word2.equals(word))
						intersectionOfAandB++;
			}
		}
		
		double unionOfAandB = centroidWords.size() + genPoint.getWordList().size() - intersectionOfAandB;
		
		return ( 1 - (intersectionOfAandB / unionOfAandB) );*/
	}
	
	public void calucalteSSE(){
	    double sum = 0;
	    for(Cluster cluster : clusterList){
	         for(Text points : cluster.getClusterPoints()){
	             double temp = calculateDistance(cluster.getCentroid(), points);
	             sum += temp*temp;
	            }
	        }
	    System.out.println("SSE value for k = " + numberOfCluster + " is: " + sum);
	}

	
}

class Text{

	private String id;
	private List<String> wordList = null;
	private int assignedCluster;
	
	public Text(String id, List<String> wordList) {

		this.id = id;
		this.wordList = wordList;
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getWordList() {
		return wordList;
	}

	public void setWordList(List<String> wordList) {
		this.wordList = wordList;
	}

	public int getAssignedCluster() {
		return assignedCluster;
	}

	public void setAssignedCluster(int assignedCluster) {
		this.assignedCluster = assignedCluster;
	}
}


class Cluster{
	
	private List<Text> clusterPoints = new ArrayList<Text>();
	private Text centroid = null;
	private int index ;
	
	public Cluster(Text centroid , int index){
		
		this.centroid = centroid;
		this.index = index;
		
	}

	public List<Text> getClusterPoints() {
		return clusterPoints;
	}

	public void setClusterPoints(List<Text> clusterPoints) {
		this.clusterPoints = clusterPoints;
	}

	public void addClusterPoints(Text point) {
		clusterPoints.add(point);
	}

	public void clearClusterPoints() {
		clusterPoints.clear();
	}
	
	public Text getCentroid() {
		return centroid;
	}

	public void setCentroid(Text centroid) {
		this.centroid = centroid;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
}