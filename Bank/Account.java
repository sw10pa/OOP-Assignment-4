package Bank;

public class Account {

	private final int id;
	private int balance;
	private int transactions;

	public Account(int id, int balance) {
		this.id = id;
		this.balance = balance;
		this.transactions = 0;
	}

	public synchronized void depositMoney(int amount) {
		balance += amount;
		transactions++;
	}

	public synchronized void withdrawMoney(int amount) {
		balance -= amount;
		transactions++;
	}

	public String toString() {
		return ("acct:" + id + " bal:" + balance + " trans:" + transactions);
	}

}
