package apoker.com;

import java.io.*;

public class Run {
	
	public static int input() {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		int n=4;
		while(true) {
			try {
				System.out.println("Enter the no. of players:");
				n=Integer.parseInt(br.readLine());
				if((n>4)||(n<2)) {
					System.out.println("Sorry, no. of players should be in the range 2-4 (inclusive)");
					System.out.println("Try Again");
				}
				else break;
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
		return n;  
	}
	
	public static void getPlayers(int n, Table t) {
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		try {
			for(int i=0;i<n;i++) {
				System.out.println("Enter the name of the Player"+(i+1));
				String name=br.readLine();
				System.out.println("Enter the chips Player"+(i+1)+" holds");
				int chips=Integer.parseInt(br.readLine());
				if(t.login(name, chips)) continue;
				else {
					System.out.println("This name already exists. Please re-enter details using a different name.");
					i--;
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) throws IOException {
		int n=input();
		System.out.println("Enter the bet amount: ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int a=Integer.parseInt(br.readLine());
		int count=2;
		Table t2=new Table(a,1);
		getPlayers(n,t2);
		boolean pG=t2.play();
		for(;;count++) {
			if(pG) {
				Table t1=t2;
				t2=new Table(a,count);
				for(int i=0;i<n;i++) {
					t2.login(t1.players.get(i).name, t1.players.get(i).getCurrentChips());
				}
				pG=t2.play();
			}
			else {
				System.out.println("GoodBye");
				break;
			}
		}	
	}
}
