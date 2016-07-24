import java.io.IOException;

import com.data.Entities;
import com.data.TreeObject;
import com.util.DesitionTreeUtils;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int LVal = Integer.parseInt(args[0]);
		int KVal = Integer.parseInt(args[1]);
		String fileName = args[2];
		String validationFileName = args[3];
		String testFileName = args[4];
		String toPrint = args[5];
		boolean bPrintTree = false;
		if(toPrint != null && toPrint.equalsIgnoreCase("yes")){
			bPrintTree = true;
		}
		
		Entities allEntities = DesitionTreeUtils.loadFile(fileName);
		
		Entities dataRows = DesitionTreeUtils.getSubEntities(allEntities, 0, 0, allEntities.getRowCount(), allEntities.getColCount() - 1);
		Entities resultRows = DesitionTreeUtils.getSubEntities(allEntities, 0, allEntities.getColCount() - 1,  allEntities.getRowCount(), 1);
		
		TreeGenerator treeGenerator = new TreeGenerator();
		TreeObject root = treeGenerator.buildTree(dataRows, resultRows);
		if(bPrintTree){
			try {
			treeGenerator.printTree(root);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		PrunedTreeBuilder prunedTreeBuilder = new PrunedTreeBuilder();	
		TreeObject prunedTree = prunedTreeBuilder.buildPrunedTree(root, LVal, KVal, validationFileName);
		Entities allTestEntities = DesitionTreeUtils.loadFile(testFileName);
		
		Entities testDataRows = DesitionTreeUtils.getSubEntities(allTestEntities, 0, 0, allTestEntities.getRowCount(), allTestEntities.getColCount() - 1);
		Entities testResultRows = DesitionTreeUtils.getSubEntities(allTestEntities, 0, allTestEntities.getColCount() - 1,  allTestEntities.getRowCount(), 1);
		
		TreeParser treeParser = new TreeParser();
		
		System.out.println("\n\nParsing with root tree :: ");
		treeParser.parseEntities(testDataRows, testResultRows, root);
		System.out.println("\nParsing with pruned tree :: ");
		treeParser.parseEntities(testDataRows, testResultRows, prunedTree);
		
	}

}
