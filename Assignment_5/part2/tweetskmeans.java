import com.kmeans.jaccard.KMeansWithJaccardDistance;

public class tweetskmeans {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length != 4){
			System.out.println("Please enter 4 arguments : <numberOfClusters> <initialSeedsFile> <TweetsDataFile> <outputFile>");
		}else{
			try{
				int numberOfCluster = Integer.parseInt(args[0]);
				KMeansWithJaccardDistance kMeans = new KMeansWithJaccardDistance(
						numberOfCluster, args[2], args[1], args[3]);
				
				kMeans.findMeans();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
