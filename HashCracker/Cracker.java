package HashCracker;

import java.util.*;
import java.security.*;
import java.util.concurrent.*;

public class Cracker {

	private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private static final int DEFAULT_NUM_WORKERS = 1;

	private ArrayList<String> crackedPasswords;
	private CountDownLatch latch;
	private String hashToCrack;
	private int length;

	public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {
		Cracker cracker = new Cracker();

		if (args.length == 1) {
			cracker.generationMode(args[0]);
		} else if (args.length == 2) {
			cracker.crackingMode(args[0], Integer.parseInt(args[1]), DEFAULT_NUM_WORKERS);
		} else if (args.length == 3) {
			cracker.crackingMode(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		} else {
			System.out.println("Program Arguments:\n" +
					"\tGeneration Mode: password\n" +"" +
					"\tCracking Mode: hash-value length [num-workers]\n");
		}
	}

	private void generationMode(String password) throws NoSuchAlgorithmException {
		System.out.println(hashValueOf(password));
	}

	private static String hashValueOf(String s) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(s.getBytes());
		return hexToString(md.digest());
	}

	private static String hexToString(byte[] bytes) {
		StringBuilder buff = new StringBuilder();
		for (int aByte : bytes) {
			int val = aByte;
			val = val & 0xff;
			if (val < 16) buff.append('0');
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	private void crackingMode(String hashToCrack, int length, int numWorkers) throws InterruptedException {
		latch = new CountDownLatch(numWorkers);
		crackedPasswords = new ArrayList<>();
		this.hashToCrack = hashToCrack;
		this.length = length;

		startWorkers(numWorkers);
		latch.await();

		printCrackedPasswords();
	}

	private void startWorkers(int numWorkers) {
		int segmentLength = CHARS.length / numWorkers;

		for (int i = 0; i < numWorkers; i++) {
			char[] segment;
			if (i != numWorkers - 1) {
				segment = Arrays.copyOfRange(CHARS, i * segmentLength, (i + 1) * segmentLength);
			} else {
				segment = Arrays.copyOfRange(CHARS, i * segmentLength, CHARS.length);
			}

			Worker worker = new Worker(segment);
			worker.start();
		}
	}

	private void printCrackedPasswords() {
		for (String crackedPassword : crackedPasswords) {
			System.out.println(crackedPassword);
		}
		System.out.println("all done");
	}

	private class Worker extends Thread {

		private final char[] segment;

		public Worker(char[] segment) {
			this.segment = segment;
		}

		public void run() {
			for (char aChar : segment) {
				try {
					hashGenerator(Character.toString(aChar));
				} catch (NoSuchAlgorithmException ignored) { }
			}
			latch.countDown();
		}

		private void hashGenerator(String curr) throws NoSuchAlgorithmException {
			if (curr.length() == length) {
				if (hashValueOf(curr).equals(hashToCrack)) {
					crackedPasswords.add(curr);
				}
			} else {
				for (char aChar : CHARS) {
					hashGenerator(curr + aChar);
				}
			}
		}

	}

}
