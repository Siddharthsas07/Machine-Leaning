import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.crap.classes.PlayCrapEvenWager;
import com.crap.classes.PlayCrapMartingaleSystem;
import com.crap.classes.PlayCrapReverseMartingaleSystem;
import com.crap.interfaces.IPlayCraps;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File file = new File("output.txt");
			FileWriter fw = new FileWriter(file);
			for (int i = 1; i <= 5; i++) {

				IPlayCraps evenWager = new PlayCrapEvenWager();
				evenWager.playGame();

				IPlayCraps martingaleSystem = new PlayCrapMartingaleSystem();
				martingaleSystem.playGame();

				IPlayCraps reverseMartingaleSystem = new PlayCrapReverseMartingaleSystem();
				reverseMartingaleSystem.playGame();

				fw.write("\nRound " + i + ":\n");
				fw.write(evenWager.displayRoundStatisticsHeader2()+":\n");
				fw.write(evenWager.displayRoundStatistics2()+":\n");
				fw.write(martingaleSystem.displayRoundStatistics2()+":\n");
				fw.write(reverseMartingaleSystem.displayRoundStatistics2()+":\n");
			}
			fw.close();
		} catch (IOException e) {
		}
	}

}
