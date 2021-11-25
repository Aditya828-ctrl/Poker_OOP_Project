package apoker.com;

import java.io.*;
//import java.util.*;

public class Player {
	public String name;
	private int currentChips;
	public int chipsSpent=0; //It stores the chips that the player has spent in the current round.
	public Deck hand;
	public boolean isPlaying;
	Player(String name, int chips, Deck hand){
		this.name=name;
		this.currentChips=chips;
		this.hand=hand;
		this.isPlaying=true;
	}
	public void updateChips(int c) throws NotEnoughChipsException{
		if((currentChips<c)) throw new NotEnoughChipsException();
		else {
			currentChips-=c;
			chipsSpent+=c;
		}
	}
	public void addChips() {
		System.out.println(this.name+", you current have "+this.currentChips+" in your account.");
		System.out.println("How much more chips to add?");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		int a=0;
		try {
			a=Integer.parseInt(br.readLine());
		}
		catch (Exception e) {
			System.out.println("ERROR");
		}
		this.currentChips+=a;
	}
	public int getCurrentChips() {
		return this.currentChips;
	}
	public void setCurrentChips(int a) {
		this.currentChips=a;
	}
	public void showCards() {
		System.out.println(name+"'s cards:");
//		for(Card c:hand.deck) {
//			System.out.println(c);
//		}
		System.out.println(hand.deck.toString());
	}
	public void call(int betAmount) throws NotEnoughChipsException {
		updateChips(Math.abs(chipsSpent-betAmount));
	}
	public int raise(int betAmount) throws NotEnoughChipsException { //returns the amount by which the player raised
		System.out.println("Enter Raise amount:");
		System.out.println("Note that the amount you enter is over and above the bet amount ("+(betAmount-chipsSpent)+")");
		BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
		int r=0;
		try {
			r=Integer.parseInt(br.readLine());
		}
		catch(Exception e) {
			System.out.println("ERROR");
		}
		
		updateChips(betAmount+r-chipsSpent);
		return r;
	}
	public void check() {
		return;
	}
	public void fold() {
		isPlaying=false;
	}
}
