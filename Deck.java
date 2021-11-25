package apoker.com;

import java.util.*;

public class Deck {
	ArrayList<Card> deck;
	public Deck(){
		createDeck();
	}
	public Deck(Deck d) {
		this.deck=new ArrayList<>(d.deck);
	}
	public Deck(ArrayList<Card> d) {
		this.deck=d;
	}
	void shuffle() {
		String str=java.time.LocalTime.now().toString();
		str=str.substring(str.length()-5,str.length()-4);
		int r=Integer.parseInt(str);
		Collections.shuffle(this.deck, new Random(r));
	}
	private void createDeck() {
		deck=new ArrayList<>();
		for(int i=2;i<=14;i++) {
			for(int j=1;j<=4;j++) {
				deck.add(new Card(i,j));
			}
		}
	}
	
	public String toString() {
		String s="";
		for( Card c: deck) {
			s+=c.toString();
			s+="\n";
		}
		return s;
	}
}
