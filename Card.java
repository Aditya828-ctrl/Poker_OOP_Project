package apoker.com;

//import java.util.*;

/*
 * suits-
 * CLUB		=1
 * DIAMOND	=2
 * HEART	=3
 * SPADES	=4
 */

/*
 * ranks-
 * A-14,K-13,Q-12,J-11
 * 
 */
public class Card implements Comparable<Card> {
	public int rank;
	public int suit;
	public String name;
	public boolean faceUp;
	
	Card(int rank,int suit){
		this.rank=rank;
		this.suit=suit;
		faceUp=false;
		this.setName();
	}
	public int compareTo(Card c) {
		if(this.rank==14)return -1;
		if(c.rank==14) return 1;
		if(this.rank>c.rank) return 1;
		else if(this.rank==c.rank) return 0;
		else return -1;
	}
	private void setName() {
		if(rank>10) {
			switch(rank) {
			case 11: name="J";break;
			case 12: name="Q";break;
			case 13: name="K";break;
			case 14: name="A";break;
			}
		}
		else {
			name= ""+rank;
		}
	}
	public void flipFace() {
		if(faceUp) faceUp=false;
		else faceUp=true;
	}
	public String getSuitName() {
		String s="";
		switch(suit) {
		case 1:s="Clubs";break;
		case 2:s="Diamonds";break;
		case 3:s="Hearts";break;
		case 4:s="Spades";break;
		}
		return s;
	}
	public String toString() {
		String s="";
		if(faceUp) {
			s=""+this.name+" of "+this.getSuitName();
		}
		else {
			s="*";
		}
		return s;
	}
	
	
}
