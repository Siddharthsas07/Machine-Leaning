package com.crap.classes;

import java.util.Random;
import com.crap.interfaces.IPlayCraps;


public abstract class AbstractPlayCraps implements IPlayCraps{

	private static final int INITIAL_BALANCE = 1000;
	private static final int DEFAULT_WAGER = 100;

	protected int wager = DEFAULT_WAGER;
	protected int balance = INITIAL_BALANCE;
	protected int gameNo = 0 ;
	
	public void resetValues(){
		
		wager = DEFAULT_WAGER;
		balance = INITIAL_BALANCE;
		gameNo = 0 ;
	}
	
	public boolean rollDice() {
		
		int rollOfdiceSum = generateRandomDiceValues();
//		System.out.println("rollOfdiceSum :: " + rollOfdiceSum);
		
		if(rollOfdiceSum == 7 || rollOfdiceSum == 11 ){
			return true;
		}
		
		if( rollOfdiceSum == 2 || rollOfdiceSum == 3 || rollOfdiceSum == 12 ){
			return false;
		}
		
		int rollAgain  = -1;
		while(( rollAgain = generateRandomDiceValues()) != 7 ){
//			System.out.println("rollAgain :: " + rollAgain );
			if(rollAgain == rollOfdiceSum)
				return true;
		}
		return false;
	}

	private int generateRandomDiceValues(){
		
		int dice1 = new Random().nextInt(6) + 1;
		int dice2 = new Random().nextInt(6) + 1;
		
		return dice1 + dice2;
	}
	
	protected void resetWager() {
		this.wager = DEFAULT_WAGER;		
	}

	protected void doubleWager() {

		if(this.wager * 2 < this.balance){
			this.wager = this.wager * 2;
		}else{
			this.wager = this.balance;
		}
	}

	protected void addBalance() {
		this.balance = this.balance + this.wager;
	}

	protected void deductBalance() {
		this.balance = this.balance - this.wager;
		if(this.balance < 0 )
			this.balance = 0;
	}

	@Override
	public int getStrategy() {
		return -1;
	}

	protected void displayGameStatistics(boolean bWinStatus){
		System.out.println( (gameNo) + "," + balance +"," + wager +"," + 
					(bWinStatus ? "Win" : "Loss"));
	}

	protected void displayGameStatisticsHeader(){
		System.out.println("Game #,Balance,Wager,Outcome");
	}
	
	@Override
	public void displayRoundStatistics(){
//		displayRoundStatisticsHeader();
		System.out.println(this.getStrategy() + "," + (gameNo - 1) + "," + balance );
	}
	
	@Override
	public void displayRoundStatisticsHeader(){
		System.out.println("Strategy, Number of games, Ending Balance ");
	}

	
	public String displayRoundStatistics2(){
//		displayRoundStatisticsHeader();
		return this.getStrategy() + "," + (gameNo - 1) + "," + balance ;
	}
	
	
	public String displayRoundStatisticsHeader2(){
		return "Strategy, Number of games, Ending Balance ";
	}
}
