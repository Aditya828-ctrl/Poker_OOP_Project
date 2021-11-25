package apoker.com;

import java.io.*;
import java.util.*;

public class Table {
	private int count=0;
	public int currentChips=0;
	public Deck currentDeck;
	public Deck deck;
	public Deck communityCards;
	public int bigBlind;
	public int smallBlind;
	public boolean bb=false;
	public boolean sb=false;
	private int betAmount;
	public List<Player> players;
	public Table(int betAmount,int count){
		this.count=count;
		players=new ArrayList<Player>();
		currentDeck=new Deck();
		currentDeck.shuffle();
		deck=new Deck(currentDeck);
		this.setBlind();
		this.betAmount=betAmount;
	}
	private void setBlind() {
		smallBlind=count-1;
		bigBlind=count;
	}
	public int getNoOfPlayers() {
		return players.size();
	}
	public boolean play() {
		initial();
		makeCommCards();
		preFlop();
		flop();
		turn();
		river();
		showTime();
		evaluateResult();
		if(playAgain()) return true;
		return false;
	}
	public boolean login(String name, int chips) {
		if(players.size()<=4) {
			if(!authorize(name)) return false;
			ArrayList<Card> hand=new ArrayList<>();
			for(int i=0;i<2;i++) {
				Card c=deck.deck.get(0);
				deck.deck.remove(0);
				hand.add(c);
			}
			Player p=new Player(name,chips,new Deck(hand));
			players.add(p);
			return true;
		}
		else return false;
	}
	public boolean authorize(String name) {
		for(Player p:players) {
			if(p.name.compareTo(name)==0) return false;
		}
		return true;
	}
	private void initial() {
		for(Player p:players) {
			for(Card c: p.hand.deck) {
				c.faceUp=true;
			}
		}
	}
	private void makeCommCards() {
		ArrayList<Card> cl=new ArrayList<>();
		for(int i=0;i<5;i++) {
			Card c=deck.deck.get(0);
			deck.deck.remove(0);
			cl.add(c);
		}
		communityCards=new Deck(cl);
	}
	private void resetChipsSpent() {
		for(Player p:players) {
			if(!(p.isPlaying)) continue;
			else {
				p.chipsSpent=0;
			}
		}
	}
	private void flipCommunityCards(int l,int r) {
		for(int i=l;i<=r;i++) {
			communityCards.deck.get(i).flipFace();
		}
	}
	private void showCommunityCards() {
		System.out.println("Community cards:");
		System.out.println(communityCards.deck.toString());
	}
	private boolean isRoundOver() {
		if(!(bb&&sb)) return false;
		for(Player p:players) {
			if(!(p.isPlaying)) continue;
			if(betAmount!=p.chipsSpent) return false;
		}
		return true;
	}
	private void preFlop() {
		System.out.println("-----Pre Flop Round-----");
		if(smallBlind==players.size()) smallBlind=0;
		if(bigBlind==players.size()) bigBlind=0;
		playSmallBlind();
		playBigBlind();
		int i=smallBlind+2;
		for(;;i++) {
			if(isRoundOver()) break;
			if(i>=players.size()) i=0;
			Player p=players.get(i);
			if(!(p.isPlaying)) continue;
			if(i==smallBlind) sb=true;
			if(i==bigBlind) bb=true;
			preFlopOptions(i);
		}
		System.out.println("Pre flop round over....");
	}
	private void playSmallBlind() {
		Player p=players.get(smallBlind);
		System.out.println(p.name+" is Small Blind");
		System.out.println(p.name+"'s round---");
		try {
			p.updateChips(betAmount/2);
			this.currentChips+=(betAmount/2);
			System.out.println("Next--");
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("The play can not start because small blind does not have required chips.");
			System.out.println(p.name+" you need to add chips to start the game.");
			p.addChips();
			playSmallBlind();
		}
	}
	private void playBigBlind() {
		Player p=players.get(bigBlind);
		System.out.println(p.name+" is Big Blind");
		System.out.println(p.name+"'s round---");
		try {
			p.updateChips(betAmount);
			this.currentChips+=betAmount;
			System.out.println("Next--");
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("The play can not start because big blind does not have required chips.");
			System.out.println(p.name+" you need to add chips to start the game.");
			p.addChips();
			playBigBlind();
		}
	}
	private void preFlopOptions(int i) {
		Player p=players.get(i);
		System.out.println(p.name+"'s round---");
		p.showCards();
		System.out.println("Currently, you have "+p.getCurrentChips()+" chips");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose:\n1. Call ( "+(Math.abs(p.chipsSpent-betAmount))+" )\n2. Raise ( >"+(Math.abs(p.chipsSpent-betAmount))+" )\n3. Fold ( 0 )");
		System.out.println("4. Add Chips");
		int op=0;
		try {
			op=Integer.parseInt(br.readLine());
			switch(op) {
			case 1 : p.call(betAmount); this.currentChips+=betAmount; break;
			case 2 : int r=p.raise(betAmount);
					 betAmount+=r; this.currentChips+=betAmount;
					 break;
			case 3 : p.fold(); break;
			case 4 : p.addChips(); preFlopOptions(i); break;
			default: System.out.println("Invalid option chosen.\n Try Again");
					 preFlopOptions(i);
					 break;
			}
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("Try Again");
			preFlopOptions(i);
		}
	}
	private void flop() {
		System.out.println("-----Flop Round-----");
		resetChipsSpent();
		flipCommunityCards(0,2);
		showCommunityCards();
		betAmount=0;
		System.out.println("Table currently has: "+this.currentChips+" chips.");
		playSmallBlind2();
		int i=bigBlind;
		boolean decide=false;
		int count=1;
		for(;;i++) {
			count++;
			if(i>=players.size()) i=0;
			Player p=players.get(i);
			if(!(p.isPlaying)) continue;
			preFlopOptions(i);
			if(count==players.size()) decide=true;
			if(decide&&isRoundOver()) break;
		}
		System.out.println("Flop round over....");
	}
	private void playSmallBlind2() {
		Player p=players.get(smallBlind);
		if(!(p.isPlaying)) return;
		System.out.println(p.name+"'s round---");
		p.showCards();
		System.out.println("Currently, you have "+p.getCurrentChips()+" chips");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Choose:\n1. Check ( 0 )\n2. Bet ( >0 )\n3. Fold ( 0 )");
		System.out.println("4. Add Chips");
		int op=0;
		try {
			op=Integer.parseInt(br.readLine());
			switch(op) {
			case 1 : p.check(); break;
			case 2 : int r=p.raise(betAmount);
					 betAmount+=r; this.currentChips+=betAmount;
					 break;
			case 3 : p.fold(); break;
			case 4 : p.addChips(); playSmallBlind(); break;
			default: System.out.println("Invalid option chosen.\n Try Again");
					 playSmallBlind2();
					 break;
			}
		}
		catch(Exception e){
			System.out.println(e);
			System.out.println("Try Again");
			playSmallBlind2();
		}
	}
	private void turn() {
		System.out.println("-----Turn Round-----");
		resetChipsSpent();
		flipCommunityCards(3,3);
		showCommunityCards();
		betAmount=0;
		System.out.println("Table currently has: "+this.currentChips+" chips.");
		playSmallBlind2();
		int i=bigBlind;
		boolean decide=false;
		int count=1;
		for(;;i++) {
			count++;
			if(i>=players.size()) i=0;
			Player p=players.get(i);
			if(!(p.isPlaying)) continue;
			preFlopOptions(i);
			if(count==players.size()) decide=true;
			if(decide&&isRoundOver()) break;
		}
		System.out.println("Turn round over....");
	}
	private void river() {
		System.out.println("-----River Round-----");
		resetChipsSpent();
		flipCommunityCards(4,4);
		showCommunityCards();
		betAmount=0;
		System.out.println("Table currently has: "+this.currentChips+" chips.");
		playSmallBlind2();
		int i=bigBlind;
		boolean decide=false;
		int count=1;
		for(;;i++) {
			count++;
			if(i>=players.size()) i=0;
			Player p=players.get(i);
			if(!(p.isPlaying)) continue;
			preFlopOptions(i);
			if(count==players.size()) decide=true;
			if(decide&&isRoundOver()) break;
		}
		System.out.println("River round over....");
		System.out.println("Showdown time....");
	}
	private void showTime() {
		showCommunityCards();
		for(Player p:players) {
			if(p.isPlaying) {
				p.showCards();
			}
		}
		System.out.println("Table currently has: "+this.currentChips+" chips.");
	}
	private void evaluateResult() {
		System.out.println("Evaluating results");
		Patterns[] pr=new Patterns[players.size()];
		for(int i=0;i<players.size();i++) {
			ArrayList<Card> a=new ArrayList<>();
			a.add(players.get(i).hand.deck.get(0));
			a.add(players.get(i).hand.deck.get(1));
			for(int j=0;j<5;j++) {
				a.add(communityCards.deck.get(j));
			}
			Patterns p=new Patterns(new Deck(a));
			pr[i]=p;
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isRoyalFlush();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Royal Flush)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isStraightFlush();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Straight Flush)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isFourOfAKind();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Four Of a Kind)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isFullHouse();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Full House)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isFlush();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Flush)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isStraight();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Straight)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isThreeOfAKind();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Three Of a Kind)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isTwoPair();
				if(a<11) {
					System.out.println(p.name+" has won.\n(Two Pair)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].isOnePair();
				if(a<11) {
					System.out.println(p.name+" has won.\n(One Pair)");
					System.out.println(pr[i].getPattern(a-1).deck.toString());
					System.out.println("Congratulations you get "+this.currentChips);
					p.setCurrentChips(p.getCurrentChips()+this.currentChips);
					return;
				}
			}
		}
		int[] high=new int[players.size()];
		for(int i=0;i<players.size();i++) {
			Player p=players.get(i);
			if(p.isPlaying) {
				int a=pr[i].getHighCard();
				high[i]=a;
			}
			else high[i]=0;
		}
		int max=0; int maxDex=0;
		for(int i=0;i<players.size();i++) {
			if(high[i]>max) {
				max=high[i];
				maxDex=i;
			}
		}
		Player p=players.get(maxDex);
		System.out.println(p.name+" has won.\n(High Card)");
		System.out.println("Congratulations you get "+this.currentChips);
		p.setCurrentChips(p.getCurrentChips()+this.currentChips);
		return;
	}
	private boolean playAgain() {
		System.out.println("Do you want to play again?\n1-Yes\n2-No");
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		int a=0;
		try {
		a=Integer.parseInt(br.readLine());
		}
		catch(Exception e) {
			System.out.println(e);
		}
		if(a==1) return true;
		else return false;
		
	}
}
