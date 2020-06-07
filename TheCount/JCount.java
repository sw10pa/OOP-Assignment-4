package TheCount;

import java.awt.*;
import javax.swing.*;

public class JCount extends JPanel {

	private static final int NUM_WORKERS = 4;
	private static final int SLEEP_MSECS = 100;
	private static final int UPDATE_INTERVAL = 10000;

	private static final int MIN_VALUE = 1;
	private static final int MAX_VALUE = 100000000;

	private final JTextField countToField;
	private final JLabel currentValueLabel;
	private final JButton startButton;
	private final JButton stopButton;

	private Worker worker;
	private int currentValue;
	private int countToValue;

	static public void main(String[] args)  {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		JFrame frame = createFrame();
		frame.setVisible(true);
	}

	private static JFrame createFrame() {
		JFrame frame = new JFrame("The Count");
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		for (int i = 0; i < NUM_WORKERS; i++) {
			JCount counterSection = new JCount();
			frame.add(counterSection);
			frame.add(Box.createRigidArea(new Dimension(0, 40)));
		}

		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		return frame;
	}

	public JCount() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		countToField = new JTextField(Integer.toString(MAX_VALUE));
		add(countToField);

		currentValueLabel = new JLabel(Integer.toString(MIN_VALUE));
		add(currentValueLabel);

		startButton = new JButton("Start");
		add(startButton);

		stopButton = new JButton("Stop");
		add(stopButton);

		addActionListeners();
	}

	private void addActionListeners() {
		startButton.addActionListener(e -> startWorker());
		stopButton.addActionListener(e -> stopWorker());
	}

	private void startWorker() {
		if (worker != null && !worker.isInterrupted()) {
			stopWorker();
		}

		countToValue = Integer.parseInt(countToField.getText());

		worker = new Worker();
		worker.start();
	}

	private void stopWorker() {
		if (worker != null && !worker.isInterrupted()) {
			worker.interrupt();
		}
	}

	private class Worker extends Thread {

		public void run() {
			for (currentValue = MIN_VALUE; currentValue < countToValue; currentValue++) {
				if (currentValue % UPDATE_INTERVAL == 0) {
					try {
						sleep(SLEEP_MSECS);
						updateCurrentValue();
					} catch (InterruptedException e) {
						break;
					}
				}
			}
			updateCurrentValue();
		}

		private void updateCurrentValue() {
			SwingUtilities.invokeLater(() -> currentValueLabel.setText(Integer.toString(currentValue)));
		}

	}

}
