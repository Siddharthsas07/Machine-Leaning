package com.crap.classes;

public class PlayCrapMartingaleSystem extends AbstractPlayCraps {

	@Override
	public void playGame() {

//		displayGameStatisticsHeader();
		while( gameNo++ < 10 && balance != 0 ){
			boolean result = this.rollDice();
//			displayGameStatistics(result);
			if(result){
				addBalance();
				resetWager();
			}else{
				deductBalance();
				doubleWager();
			}
		}
//		displayRoundStatistics();
	}
	
	@Override
	public int getStrategy() {
		return 2;
	}

}
