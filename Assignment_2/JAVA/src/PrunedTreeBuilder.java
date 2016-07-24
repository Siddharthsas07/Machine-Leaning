import java.util.ArrayList;

import com.data.Entities;
import com.data.TreeObject;
import com.util.DesitionTreeUtils;

public class PrunedTreeBuilder {

	public TreeObject buildPrunedTree(TreeObject root, int L, int K, String validationFileName){

		Entities allTestEntities = DesitionTreeUtils.loadFile(validationFileName);
		Entities testDataRows = DesitionTreeUtils.getSubEntities(allTestEntities, 0, 0, allTestEntities.getRowCount(), allTestEntities.getColCount() - 1);
		Entities testResultRows = DesitionTreeUtils.getSubEntities(allTestEntities, 0, allTestEntities.getColCount() - 1,  allTestEntities.getRowCount(), 1);

		double bestAccuracy = -1;
		TreeObject dTreeBest = root;
		bestAccuracy = testAcurracy(dTreeBest, testDataRows, testResultRows);

		for(int l = 1;  l  < L ;  l++ ){

			TreeObject dTreeTemp = DesitionTreeUtils.copyTree(root);
			int M = (int)(Math.random()  * K)  + 1;

			for(int m = 0; m < M  ; m++){
				int totalNodes = DesitionTreeUtils.generateTreeNodeNumbers(dTreeTemp);
				int nodeNumber = (int)(Math.random()  * totalNodes) ;

				removeSubTree(dTreeTemp, nodeNumber);
			}

			double tempAccuracy = testAcurracy(dTreeTemp, testDataRows, testResultRows);

			if(tempAccuracy > bestAccuracy){
				bestAccuracy = tempAccuracy;
				dTreeBest = dTreeTemp;
			}
		}
		return dTreeBest;
	}

	private double testAcurracy(TreeObject root, Entities testDataRows, Entities testResultRows){

		TreeParser treeParser = new TreeParser();
		double accuracyPercentage = treeParser.parseEntitiesWithoutPrint(testDataRows, testResultRows, root);

		return accuracyPercentage;

	}


	private boolean removeSubTree(TreeObject dTreeTemp, int nodeNumber){

		if(dTreeTemp != null){
			if(dTreeTemp.getSubTreeList().size() > 0 ){
				if( dTreeTemp.getObjectNumber() == nodeNumber){
					removeSubNodes(dTreeTemp);
					return true;
				}
				for(int i = 0; i < dTreeTemp.getSubTreeList().size() ; i++ ){
					boolean bReturn  = false;
					TreeObject rootSubObj = dTreeTemp.getSubTreeList().get(i);
					bReturn = removeSubTree(rootSubObj, nodeNumber);
					if(bReturn){
						return true;
					}
				}
			}
			return false;
		}else{
			return false;
		}
	}

	private void removeSubNodes(TreeObject dTreeTemp){

		dTreeTemp.setSubTreeList(new ArrayList<TreeObject>());
		int[] resultCounts = dTreeTemp.getResultCounts();
		int tempVal = -1;
		for(int i = 0; i < resultCounts.length ; i++ ){
			if(resultCounts[i] > tempVal ){
				tempVal = resultCounts[i];
				dTreeTemp.setValue(i);
			}
		}
	}

}
