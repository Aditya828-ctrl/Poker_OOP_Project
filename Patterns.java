package apoker.com;

import java.util.ArrayList;
import java.util.Collections;

public class Patterns {
	Deck p;
	Deck t;
	ArrayList<Deck> al;
	public Patterns(Deck d) {
		ArrayList<Card> a1=new ArrayList<>();
		ArrayList<Card> a2=new ArrayList<>();
		for(int i=0;i<7;i++) {
			if(i<2) {
				Card c=d.deck.get(i);
				a1.add(c);
			}
			else {
				Card c=d.deck.get(i);
				a2.add(c);
			}
		}
		p=new Deck(a1);
		t=new Deck(a2);
		group();
	}
	private void group() {
		al=new ArrayList<>();
		for(int i=0;i<10;i++) {
			ArrayList<Card> a= new ArrayList<>();
			for(int j=0;j<2;j++) {
				a.add(p.deck.get(j));
			}
			al.add(new Deck(a));
		}
		al.get(0).deck.add(t.deck.get(0));al.get(0).deck.add(t.deck.get(1));al.get(0).deck.add(t.deck.get(2));
		al.get(1).deck.add(t.deck.get(0));al.get(1).deck.add(t.deck.get(1));al.get(1).deck.add(t.deck.get(3));
		al.get(2).deck.add(t.deck.get(0));al.get(2).deck.add(t.deck.get(1));al.get(2).deck.add(t.deck.get(4));
		al.get(3).deck.add(t.deck.get(0));al.get(3).deck.add(t.deck.get(2));al.get(3).deck.add(t.deck.get(3));
		al.get(4).deck.add(t.deck.get(0));al.get(4).deck.add(t.deck.get(2));al.get(4).deck.add(t.deck.get(4));
		al.get(5).deck.add(t.deck.get(0));al.get(5).deck.add(t.deck.get(3));al.get(5).deck.add(t.deck.get(4));
		al.get(6).deck.add(t.deck.get(1));al.get(6).deck.add(t.deck.get(2));al.get(6).deck.add(t.deck.get(3));
		al.get(7).deck.add(t.deck.get(1));al.get(7).deck.add(t.deck.get(2));al.get(7).deck.add(t.deck.get(4));
		al.get(8).deck.add(t.deck.get(1));al.get(8).deck.add(t.deck.get(3));al.get(8).deck.add(t.deck.get(4));
		al.get(9).deck.add(t.deck.get(2));al.get(9).deck.add(t.deck.get(3));al.get(9).deck.add(t.deck.get(4));
	}
	public Deck getPattern(int i) {
		return al.get(i);
	}
	public int isFlush() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkFlush(al.get(i));
			if(a) 	break;
		}
		return i+1;   //i=11---> Not a flush
	}
	private boolean checkFlush(Deck d) {
		Collections.sort(d.deck,new SortBySuit());
		return(d.deck.get(0).suit==d.deck.get(4).suit);
	}
	public int isStraight() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkStraight(al.get(i));
			if(a) {	
				System.out.println("l"+al.get(i).deck.toString());
				
				break;
				}
		}
		return i+1;   //i=11---> Not a straight
	}
	private boolean checkStraight(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		int rank=d.deck.get(0).rank;
		int testRank=rank+1;
		 if (rank == 14)
	      {
	         ArrayList<Card> al=d.deck;
	         boolean t1 = (al.get(1).rank == 2) && (al.get(2).rank == 3) &&
	                     (al.get(3).rank == 4) && (al.get(4).rank == 5) ;
	         boolean t2 = (al.get(1).rank == 10) && (al.get(2).rank == 11) &&
                     (al.get(3).rank == 12) && (al.get(4).rank == 13) ;
	         return t1||t2;
	      }
		for(int i=1;i<5;i++) {
			if(d.deck.get(i).rank==testRank) {
				testRank+=1;
				continue;
			}
			else return false;
		}
		return true;
	}
	public int isStraightFlush() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=(checkFlush(al.get(i)))&&(checkStraight(al.get(i)));
			if(a) break;
		}
		return i+1;   //i=11---> Not a straight flush
	}
	public int getHighCard() {
		int max=0;
		int i=0;
		for(;i<10;i++) {
			int a=checkHighCard(al.get(i));
			if(a>max) max=a;
		}
		return max;  //rank of High Card
	}
	private int checkHighCard(Deck d) {
		int max=0;
		for(int i=0;i<5;i++) {
			if(d.deck.get(i).rank>max) max=d.deck.get(i).rank;
		}
		return max;
	}
	public int isRoyalFlush() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkRoyalFlush(al.get(i));
			if(a) break;
		}
		return i+1;   //i=11---> Not a RoyalFlush
	}
	private boolean checkRoyalFlush(Deck d) {
		if(checkStraight(d)&&checkFlush(d)) {
			if(checkHighCard(d)==14) {
				return true;
			}
		}
		return false;
	}
	public int isFourOfAKind() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkFourOfAKind(al.get(i));
			if(a) break;
		}
		return i+1;   //i=11---> Not a Four of a Kind
	}
	private boolean checkFourOfAKind(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		for(int i=0;i<2;i++) {
			int rank=d.deck.get(i).rank;
			for(int j=0;j<=3;j++) {
				if(rank==d.deck.get(i+j).rank) {
					if(j==3) return true;
				}
				else break;
			}
		}
		return false;
	}
	public int isFullHouse() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkFullHouse(al.get(i));
			if(a) break;
				
		}
		return i+1;   //i=11---> Not a Full House
	}
	private boolean checkFullHouse(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		boolean a1= ((d.deck.get(0).rank==d.deck.get(1).rank)&&(d.deck.get(1).rank==d.deck.get(2).rank))&&
									(d.deck.get(3).rank==d.deck.get(4).rank);
		boolean a2= ((d.deck.get(2).rank==d.deck.get(3).rank)&&(d.deck.get(3).rank==d.deck.get(4).rank))&&
				(d.deck.get(0).rank==d.deck.get(1).rank);
		return a1||a2;
	}
	public int isThreeOfAKind() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkThreeOfAKind(al.get(i));
			if(a) break;
		}
		return i+1;   //i=11---> Not three of a kind
	}
	private boolean checkThreeOfAKind(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		boolean a1= ((d.deck.get(0).rank==d.deck.get(1).rank)&&(d.deck.get(1).rank==d.deck.get(2).rank));
		boolean a2= ((d.deck.get(1).rank==d.deck.get(2).rank)&&(d.deck.get(2).rank==d.deck.get(3).rank));
		boolean a3= ((d.deck.get(2).rank==d.deck.get(3).rank)&&(d.deck.get(3).rank==d.deck.get(4).rank));
		return(a1||a2||a3);
	}
	public int isTwoPair() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkTwoPair(al.get(i));
			if(a) break;
		}
		return i+1;   //i=11---> Not three of a kind
	}
	private boolean checkTwoPair(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		boolean a1= ((d.deck.get(0).rank==d.deck.get(1).rank)&&(d.deck.get(2).rank==d.deck.get(3).rank));
		boolean a2= ((d.deck.get(0).rank==d.deck.get(1).rank)&&(d.deck.get(3).rank==d.deck.get(4).rank));
		boolean a3= ((d.deck.get(1).rank==d.deck.get(2).rank)&&(d.deck.get(3).rank==d.deck.get(4).rank));
		return(a1||a2||a3);
	}
	public int isOnePair() {
		boolean a=false;
		int i=0;
		for(;i<10;i++) {
			a=checkOnePair(al.get(i));
			if(a) break;
		}
		return i+1;   //i=11---> Not three of a kind
	}
	private boolean checkOnePair(Deck d) {
		Collections.sort(d.deck,new SortByRank());
		boolean a1= ((d.deck.get(0).rank==d.deck.get(1).rank));
		boolean a2= ((d.deck.get(1).rank==d.deck.get(2).rank));
		boolean a3= ((d.deck.get(2).rank==d.deck.get(3).rank));
		boolean a4= ((d.deck.get(3).rank==d.deck.get(4).rank));
		return(a1||a2||a3||a4);
	}
}
