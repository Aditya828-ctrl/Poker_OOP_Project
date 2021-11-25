package apoker.com;

import java.util.*;

public class SortBySuit implements Comparator<Card> {
	public int compare(Card a, Card b) {
		if(a.suit>b.suit) return 1;
		else if(a.suit<b.suit) return -1;
		else {
			if(a.rank==14) return -1;
			if(b.rank==14) return 1;
			if(a.rank>b.rank) return 1;
			else if(a.rank<b.rank) return -1;
			else return 0;
		}
	}
}
