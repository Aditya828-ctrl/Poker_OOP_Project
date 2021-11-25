package apoker.com;

import java.util.Comparator;

public class SortByRank  implements Comparator<Card> {
	public int compare(Card a, Card b) {
		if(a.rank==b.rank) {
			if(a.suit>b.suit) return 1;
			else if(a.suit<b.suit) return -1;
			else return 0;
		}
		if(a.rank==14) return -1;
		if(b.rank==14) return 1;
		if(a.rank>b.rank) return 1;
		else return -1;
	}
}

