import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.data.Columns;
import com.data.Entities;
import com.data.TreeObject;
import com.util.DesitionTreeUtils;


public class TreeGenerator {

	public TreeObject buildTree (Entities dataRows, Entities resultRows) {
		
		List<Columns> columnLst =  createAllAttributes(dataRows);
		return iterateCoulmsRecur(dataRows, resultRows, columnLst);
		
	}

	
	private TreeObject iterateCoulmsRecur (Entities dataRows, Entities resultRows, List<Columns> columnLst) {
		TreeObject root = new TreeObject();
		if (allSameNodes(resultRows) || columnLst.isEmpty()) {
			/*if(columnLst.isEmpty()){
				return null;
			}*/
			int l = -1;
			try{
			l = resultRows.getRowColumnVal(0, 0);
			}catch (Exception e) {
				l = 0;
			}
			root.setValue(l);
		}else {
			
			if(resultRows.getRowCount() == 0 ){
				return null;
			}
			List<int[]> finalResultCountArrLst  = new ArrayList<int[]>();
			Columns bestColumn = searchSplitColumn(dataRows, resultRows, columnLst, finalResultCountArrLst);
			root.setColumns(bestColumn);
			root.setResultCounts(finalResultCountArrLst.get(0));
			
			for (int value = 0; value < 2 ; value++) {
				Entities[] subEntitiesArray = DesitionTreeUtils.createSubEntityForGivenValue(bestColumn.getIndex(),
						value, dataRows, resultRows);
				
				columnLst.remove(bestColumn);
				TreeObject treeObject = iterateCoulmsRecur(subEntitiesArray[0], subEntitiesArray[1], columnLst);
				if(treeObject != null)
					root.addSubTreeList(treeObject);
				columnLst.add(bestColumn);
			}
		}
		return root;
	}
	
	private Columns searchSplitColumn(Entities dataRows, Entities resultRows, List<Columns> columnLst, 
			List<int[]> finalResultCountArrLst) {
		double maxGain = -1;
		Columns returnColumn = null;
		for (Columns column : columnLst) {
			int colIndex = column.getIndex();
			List<int[]> resultCountArrLst  = new ArrayList<int[]>();
			double gain = DesitionTreeUtils.getTotalGain(colIndex, dataRows, resultRows, resultCountArrLst);
			if (gain > maxGain) {
				maxGain = gain;
				returnColumn = column;
				finalResultCountArrLst.clear();
				finalResultCountArrLst.add(resultCountArrLst.get(0));
			}
		}
		return returnColumn;
	}
	
	public void printTree(TreeObject root) throws IOException {
		System.setOut(new PrintStream(new File("output.txt")));
		System.out.println("Printing Tree :: " );
		printTreeSubMethod(root,0);
	}
	
	private void printTreeSubMethod(TreeObject root,int depth) {
		String myLabel = "";
		if (root.getSubTreeList().isEmpty()) {
			myLabel = "" + root.getValue();
			System.out.print(myLabel);
		}
		
		if (!root.getSubTreeList().isEmpty()) {
			List<TreeObject> keyArray = root.getSubTreeList();
			for (int y = 0; y < keyArray.size(); y++ ) {
				
				System.out.println();
				for(int x = 0; x < depth ; x++){
					System.out.print(" | ");
				}
				myLabel = root.getColumns().getName() + "-" + root.getColumns().getIndex() + " =  ";
				System.out.print(myLabel);
				
				System.out.print( y + " : ");
				
				printTreeSubMethod(keyArray.get(y), depth + 1);
			}
		}
	}
	
	private boolean allSameNodes(Entities resultRows){
		
		if(resultRows != null && resultRows.getRowLst() != null && resultRows.getRowLst().size() > 0 ){
			int temp = resultRows.getRowLst().get(0)[0];
			for(int[] i : resultRows.getRowLst()){
				if( temp != i[0]){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	
	private List<Columns> createAllAttributes (Entities entities) {
		List<Columns> columnLst = new LinkedList<Columns>();
		for (int i = 0; i < entities.getColCount(); i++) {
			Columns column = new Columns(i, entities.getColumnName(i));
			columnLst.add(column);
		}
		return columnLst;
	}
	
}
