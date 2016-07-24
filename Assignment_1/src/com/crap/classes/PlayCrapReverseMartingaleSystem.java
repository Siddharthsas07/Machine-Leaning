package com.crap.classes;

public class PlayCrapReverseMartingaleSystem extends AbstractPlayCraps {

	@Override
	public void playGame() {

//		displayGameStatisticsHeader();
		while( gameNo++ < 10 && balance != 0 ){
			boolean result = this.rollDice();
//			displayGameStatistics(result);
			if(result){
				addBalance();
				doubleWager();
			}else{
				deductBalance();
				resetWager();
			}
		}
//		displayRoundStatistics();
	}
	
	@Override
	public int getStrategy() {
		return 3;
	}
}
