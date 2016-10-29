import com.kmeans.euclidean.KMeansWithEuclideanDistance;

public class kmeans {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length != 3){
			System.out.println("Please enter 3 arguments : <numberOfClusters> <input-file-name> <output-file-name>");
		}else{
			try{
				int numberOfCluster = Integer.parseInt(args[0]);
				KMeansWithEuclideanDistance kMeans = new KMeansWithEuclideanDistance(
						numberOfCluster, args[1], args[2]);
				
				kMeans.findMeans();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
