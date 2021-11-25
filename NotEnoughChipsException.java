package apoker.com;

public class NotEnoughChipsException extends Exception {
	private static final long serialVersionUID = 1L;

	public NotEnoughChipsException() {
		super("You do not have enough chips. Please re-choose.");
	}
}
