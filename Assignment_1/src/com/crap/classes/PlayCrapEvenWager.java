package com.crap.classes;

public class PlayCrapEvenWager extends AbstractPlayCraps {

	@Override
	public void playGame() {

//		displayGameStatisticsHeader();
		while( gameNo++ < 10 && balance != 0 ){
			boolean result = this.rollDice();
//			displayGameStatistics(result);
			if(result){
				addBalance();
			}else{
				deductBalance();
			}
		}
//		displayRoundStatistics();
	}
	
	@Override
	public int getStrategy() {
		return 1;
	}

}
