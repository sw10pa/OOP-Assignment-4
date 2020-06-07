package Bank;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Bank {

	private static final int ACCOUNTS = 20;
	private static final int CAPACITY = 100;
	private static final int BALANCE = 1000;
	private static final int DEFAULT_NUM_WORKERS = 1;
	private final Transaction nullTrans = new Transaction(-1, 0, 0);

	private CountDownLatch latch;
	private ArrayList<Account> accounts;
	private ArrayBlockingQueue<Transaction> transactions;

	public static void main(String[] args) throws InterruptedException {
		Bank bank = new Bank();

		if (args.length == 1) {
			bank.processFile(args[0], DEFAULT_NUM_WORKERS);
		} else if (args.length == 2) {
			bank.processFile(args[0], Integer.parseInt(args[1]));
		} else {
			System.out.println("Program Arguments: transaction-file [num-workers]");
		}
	}

	private void processFile(String file, int numWorkers) throws InterruptedException {
		transactions = new ArrayBlockingQueue<>(CAPACITY);
		latch = new CountDownLatch(numWorkers);
		accounts = new ArrayList<>();

		startWorkers(numWorkers);
		createAccounts();
		readFile(file);
		latch.await();

		printAccounts();
	}

	private void startWorkers(int numWorkers) {
		for (int i = 0; i < numWorkers; i++) {
			new Worker().start();
		}
	}

	private void createAccounts() {
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts.add(new Account(i, BALANCE));
		}
	}

	private void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StreamTokenizer tokenizer = new StreamTokenizer(reader);

			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) {
					transactions.add(nullTrans);
					break;
				}

				int from = (int) tokenizer.nval;

				tokenizer.nextToken();
				int to = (int) tokenizer.nval;
				
				tokenizer.nextToken();
				int amount = (int) tokenizer.nval;

				transactions.put(new Transaction(from, to, amount));
			}
		} catch (Exception ignored) { }
	}

	private void printAccounts() {
		for (int i = 0; i < ACCOUNTS; i++) {
			System.out.println(accounts.get(i).toString());
		}
	}

	private class Worker extends Thread {

		public void run() {
			while (true) {
				try {
					Transaction trans = transactions.take();
					if (trans == nullTrans) {
						transactions.add(nullTrans);
						latch.countDown();
						break;
					}

					int fromAccount = trans.getFromAccount();
					int toAccount = trans.getToAccount();
					int amount = trans.getAmount();

					accounts.get(fromAccount).withdrawMoney(amount);
					accounts.get(toAccount).depositMoney(amount);
				} catch (InterruptedException ignored) { }
			}
		}

	}

}
