package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.data.Entities;
import com.data.TreeObject;

public class DesitionTreeUtils {
	
	
	private static int objectNumber = 0;
	
	public static Entities getSubEntities( Entities entities, int rowIndex,	int colIndex,
			int rowCnt, int colCnt){
		
		List<int[]> tempRowLst = new ArrayList< int[] >();
		for(int j = 0; j < rowCnt; j++) {
			int[] rowData = entities.getRow(rowIndex + j);
			int[] subRowData = new int[colCnt];
			for(int i = 0; i < colCnt; i++)
				subRowData[i] = rowData[colIndex + i];
			tempRowLst.add(subRowData);
		}
		List<String> tempNameLst = new ArrayList<String>();
		for(int i = 0; i < colCnt; i++) {
			tempNameLst.add(entities.getColumnNames().get(colIndex + i));
		}
		
		Entities subEntitie = new Entities(tempRowLst, tempNameLst);
		return subEntitie;
	}

	public static Entities[] createSubEntityForGivenValue(int colIndex, int value, Entities dataRows, 
			Entities resultRows){
		
		List<Integer> indexLst = new ArrayList<Integer>();
		Entities dataSubRows = getRowsForSingleVal(dataRows, colIndex, dataRows.getRowCount() , dataRows.getColCount(), value, indexLst);
		Entities resultSubRows = getRowsForSingleVal(resultRows, 0, resultRows.getRowCount() , resultRows.getColCount(), -1, indexLst);
		Entities[] subRowsArray = new Entities[2];
		subRowsArray[0] = dataSubRows;
		subRowsArray[1] = resultSubRows;
		return subRowsArray;
		
	}
	
	public static Entities getRowsForSingleVal( Entities entities, int columnIndex, int rowCnt, int colCnt,
			int value, List<Integer> indexLst ){
		
		List<int []> tempRowDataLst = new ArrayList<int[]>();
		if (value == -1) {
			for (int i = 0; i < entities.getRowCount(); i++) {
				if (indexLst.contains(i)) {
					int[] rowSrc = entities.getRow(i);
					int[] rowDest = new int[colCnt];
					for(int j = 0; j < colCnt; j++)
						rowDest[j] = rowSrc[j];
					tempRowDataLst.add(rowDest);
				}
			}
		}
		else {
			for(int j = 0; j < entities.getRowCount(); j++) {
				if (entities.getRowColumnVal(j, columnIndex) == value) {
					int[] rowSrc = entities.getRow(j);
					int[] rowDest = new int[colCnt];
					for(int i = 0; i < colCnt; i++)
						rowDest[i] = rowSrc[i];
					tempRowDataLst.add(rowDest);
					indexLst.add(j);
				}
			}
		}
		List<String> tmpColumnNamesLst = new ArrayList<String>();
		for(int i = 0; i < colCnt; i++) {
			tmpColumnNamesLst.add(entities.getColumnName(i));
		}
		
		Entities subEntities = new Entities(tempRowDataLst, tmpColumnNamesLst);
		
		return subEntities;
	}



	public static double getTotalGain (int colIndex, Entities dataRows, Entities resultRows,
			List<int[]> resultCountArrLst) {
		
		ArrayList<int[]> dataCountMainArr = getDataCounts(colIndex, dataRows, resultRows);
		int[] resultCountArr = getResultCountArray(resultRows);
		resultCountArrLst.add(resultCountArr);
		int resultUniqueValCount = 2;
		int dataUniqueValCount = 2;
		double totalResultCount = 0;
		for (int i = 0; i < 2; i++) {
			totalResultCount += resultCountArr[i];
		}
		double[] totalDataCountForSingleType = new double[dataUniqueValCount];
		for (int k = 0; k < dataUniqueValCount; k++) {
			for (int l = 0; l < resultUniqueValCount; l++) {
				totalDataCountForSingleType[k] += dataCountMainArr.get(k)[l];
			}
		}
		double entropyVal = 0;
		for (int h = 0; h < dataUniqueValCount; h++) {
			entropyVal += (-1) * (totalDataCountForSingleType[h] / totalResultCount) * getEntropy(dataCountMainArr.get(h));
		}
		double gain = getEntropy(resultCountArr);
		gain += entropyVal;
		return gain;
	}
	
	public static TreeObject copyTree(TreeObject rootObj){
		
		if(rootObj  != null){
			TreeObject  newObj = new TreeObject(rootObj.getValue(), new ArrayList<TreeObject>(),
					rootObj.getColumns(), rootObj.getObjectNumber());
			if(rootObj.getSubTreeList().size() > 0 ){
				for(int i = 0; i < rootObj.getSubTreeList().size() ; i++ ){
					TreeObject rootSubObj = rootObj.getSubTreeList().get(i);
					TreeObject subObj = copyTree(rootSubObj);
					newObj.getSubTreeList().add(subObj);
					newObj.setResultCounts(rootObj.getResultCounts());
				}
			}
			
			return newObj;
		}else{
			return null;
		}
	}
	
	
	public static int generateTreeNodeNumbers(TreeObject rootObj){
		
		resetNodeNumber();
		generateTreeNodeNumbersIterativaly(rootObj);
		return objectNumber;
	}
	
	private static void resetNodeNumber(){
		objectNumber = 0;
	}
	
	private static void generateTreeNodeNumbersIterativaly(TreeObject rootObj){
		
		if(rootObj != null){
			if(rootObj.getSubTreeList().size() > 0 ){
				rootObj.setObjectNumber(objectNumber++);
				for(int i = 0; i < rootObj.getSubTreeList().size() ; i++ ){
					TreeObject rootSubObj = rootObj.getSubTreeList().get(i);
					generateTreeNodeNumbersIterativaly(rootSubObj);
				}
			}
		}
	}
	
	private static double getEntropy (int[] dataArray) {
		int dataUniqueValCount = 2;
		double totalDataCnt = 0;
		int counterForZeroSizeAttribute = 0;
		for (int i = 0; i < dataUniqueValCount; i ++) {
			if (dataArray[i] == 0)
				counterForZeroSizeAttribute++;
			totalDataCnt += dataArray[i];
		}
		if (totalDataCnt == 0)
			return 0;
		if (counterForZeroSizeAttribute == dataUniqueValCount - 1)
			return 0;
		
		double entropyValForSingleAttribute = 0;
		for (int i = 0; i < dataUniqueValCount; i++) {
			if (dataArray[i] != 0)
				entropyValForSingleAttribute += (-1) * (dataArray[i]/totalDataCnt) * (Math.log10(dataArray[i]/totalDataCnt) / Math.log10(2)); 
		}
		return entropyValForSingleAttribute;
	}
	
	private static int[] getResultCountArray(Entities resultRows) {
		int value = 2;
		int[] resultDataArray = new int[value];
		for (int i = 0; i < value; i++) {
			resultDataArray[i] = 0;
			for (int j = 0; j < resultRows.getRowCount(); j++) {
				if ( i == resultRows.getRowColumnVal(j, 0))
					resultDataArray[i]++;
			}
		}
		return resultDataArray;
	}
	
	
	private static ArrayList<int[]> getDataCounts (int colIndex, Entities dataRows, Entities resultRows) {
		int resultUniqueValCnt = 2;
		int dataUniqueValCnt = 2; 
		ArrayList<int[]> dataCountMainArr = new ArrayList<int[]>();
		for (int i = 0; i < dataUniqueValCnt; i++) {
			int[] dataCountSubArr = new int[resultUniqueValCnt];
			for (int j = 0; j < dataRows.getRowCount(); j++) {
				int resultVal = (int)resultRows.getRowColumnVal(j, 0);
				if ( i == dataRows.getRowColumnVal(j, colIndex) )
					dataCountSubArr[resultVal]++;
			}
			dataCountMainArr.add(dataCountSubArr);
		}
		return dataCountMainArr;
	}

	public static Entities loadFile(String  fileName){
		
		final String STR_DELIM = ",";
		List<int[]> rowLst = new ArrayList<int[]>();
		
		// R+ead File
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		Entities entities = new Entities();
		try{
			File file = new File(fileName);
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);
			
			String header = bufferedReader.readLine();
			String[] headerArrray = header.split(STR_DELIM);
			if(headerArrray != null && headerArrray.length > 0){
				List<String> headerList = Arrays.asList(headerArrray);
				entities.setColumnNames(headerList);
				int length = headerList.size();
				
				String line = "";
				while(( line = bufferedReader.readLine()) != null ){
					StringTokenizer stringTokenizer = new StringTokenizer(line, STR_DELIM);
					int count = 0;
					int[] rowData = new int[length];
					while(stringTokenizer.hasMoreTokens()){
						String data = stringTokenizer.nextToken();
						try{
							rowData[count++] = Integer.parseInt(data);
						}catch (Exception e) {
							rowData[count++] = 0;
						}
					}
					rowLst.add(rowData);
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bufferedReader != null ){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fileReader != null ){
				try {
					fileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		entities.setRowLst(rowLst);
		return entities;
	}
	
	
	
}
