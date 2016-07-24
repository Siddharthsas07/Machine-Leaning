import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.data.Entities;
import com.data.TreeObject;


public class TreeParser {

	public double parseEntities(Entities testDataRows, Entities testResults, TreeObject root){

		List<Integer> accuracyList = new ArrayList<Integer>();
		accuracyList.add(0);
		accuracyList.add(0);
		List<int[]> dataRowLst = testDataRows.getRowLst();
		for(int rowIndex = 0 ; rowIndex <  dataRowLst.size(); rowIndex++ ){
			parseTree(dataRowLst.get(rowIndex), testResults, root,accuracyList , rowIndex);
		}
		
		System.out.println("Correct : " + accuracyList.get(0) );
		System.out.println("Wrong : " + accuracyList.get(1) );
		System.out.println("Total : " + ( accuracyList.get(0) + accuracyList.get(1)) );
		System.out.println("% Accu : " + ( accuracyList.get(0) * 100.0 / ( accuracyList.get(0) + accuracyList.get(1))) );
		
		return  (accuracyList.get(0) * 100.0 / ( accuracyList.get(0) + accuracyList.get(1)));
	}
	
	public double parseEntitiesWithoutPrint(Entities testDataRows, Entities testResults, TreeObject root){

		List<Integer> accuracyList = new ArrayList<Integer>();
		accuracyList.add(0);
		accuracyList.add(0);
		List<int[]> dataRowLst = testDataRows.getRowLst();
		for(int rowIndex = 0 ; rowIndex <  dataRowLst.size(); rowIndex++ ){
			parseTree(dataRowLst.get(rowIndex), testResults, root,accuracyList , rowIndex);
		}
		return  (accuracyList.get(0) * 100.0 / ( accuracyList.get(0) + accuracyList.get(1)));
	}
	
	private void parseTree( int[] dataArray, Entities testResults, TreeObject root, List<Integer> accuracyList , int rowIndex ){
		
		if( root.getSubTreeList().size() > 0 ){
			int columnIndex = root.getColumns().getIndex();
			try{
				parseTree(dataArray, testResults, root.getSubTreeList().get(dataArray[columnIndex]), accuracyList , rowIndex);
			}catch (Exception e) {
				accuracyList.set(1, accuracyList.get(1) + 1 );
			}
		}else{
			if(testResults.getRow(rowIndex)[0] == root.getValue() ){
				accuracyList.set(0, accuracyList.get(0) + 1 );
			}else{
				accuracyList.set(1, accuracyList.get(1) + 1 );
			}
		}
	}
	
}
