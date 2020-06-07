package WebLoader;

import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class WebFrame extends JFrame {

    private static final String APPLICATION_NAME = "WebLoader";

    private static final String URL_COLUMN_NAME = "url";
    private static final String STATUS_COLUMN_NAME = "status";

    private static final String SINGLE_THREAD_FETCH_BUTTON_NAME = "Single Thread Fetch";
    private static final String CONCURRENT_FETCH_BUTTON_NAME = "Concurrent Fetch";
    private static final String STOP_BUTTON_NAME = "Stop";

    private static final String RUNNING_LABEL_TEXT = "Running:";
    private static final String COMPLETED_LABEL_TEXT = "Completed:";
    private static final String ELAPSED_LABEL_TEXT = "Elapsed:";

    private static final String DEFAULT_NUM_THREADS = "4";

    private final Container panel;
    private DefaultTableModel model;

    private JButton singleThreadFetchButton;
    private JButton concurrentFetchButton;
    private JTextField numThreadsField;
    private JLabel runningLabel;
    private JLabel completedLabel;
    private JLabel elapsedLabel;
    private JProgressBar progressBar;
    private JButton stopButton;

    private final ArrayList<MyUrl> urls = new ArrayList<>();
    private Launcher launcher;

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            WebFrame frame = new WebFrame(args[0]);
            frame.setVisible(true);
        } else {
            System.out.println("Program Arguments: urls-file-name");
        }
    }

    public WebFrame(String urlsFileName) throws IOException {
        super(APPLICATION_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = this.getContentPane();
        setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        createTable();

        readUrls(urlsFileName);
        addUrlsToTable();

        createBottomPanel();
        addActionListeners();

        pack();
    }

    private void createTable() {
        model = new DefaultTableModel(new String[] {URL_COLUMN_NAME, STATUS_COLUMN_NAME}, 0);
        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane);
    }

    private void readUrls(String urlsFileName) throws IOException {
        FileInputStream fstream = new FileInputStream(urlsFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fstream));

        while (true) {
            String url = reader.readLine();
            if (url == null) break;
            urls.add(new MyUrl(url, ""));
        }

        reader.close();
        fstream.close();
    }

    private void addUrlsToTable() {
        for (MyUrl url : urls) {
            model.addRow(new String[] {url.getUrl(), url.getStatus()});
        }
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        singleThreadFetchButton = new JButton(SINGLE_THREAD_FETCH_BUTTON_NAME);
        bottomPanel.add(singleThreadFetchButton);

        concurrentFetchButton = new JButton(CONCURRENT_FETCH_BUTTON_NAME);
        bottomPanel.add(concurrentFetchButton);

        numThreadsField = new JTextField();
        numThreadsField.setMaximumSize(new Dimension(200, 25));
        numThreadsField.setText(DEFAULT_NUM_THREADS);
        bottomPanel.add(numThreadsField);

        runningLabel = new JLabel(RUNNING_LABEL_TEXT + "0");
        bottomPanel.add(runningLabel);

        completedLabel = new JLabel(COMPLETED_LABEL_TEXT + "0");
        bottomPanel.add(completedLabel);

        elapsedLabel = new JLabel(ELAPSED_LABEL_TEXT);
        bottomPanel.add(elapsedLabel);

        progressBar = new JProgressBar(0, urls.size());
        bottomPanel.add(progressBar);

        stopButton = new JButton(STOP_BUTTON_NAME);
        stopButton.setEnabled(false);
        bottomPanel.add(stopButton);

        panel.add(bottomPanel);
    }

    private void addActionListeners() {
        singleThreadFetchButton.addActionListener(e -> {
            setButtonsEnabled(false);
            launcher = new Launcher(1);
            launcher.start();
        });

        concurrentFetchButton.addActionListener(e -> {
            setButtonsEnabled(false);
            launcher = new Launcher(Integer.parseInt(numThreadsField.getText()));
            launcher.start();
        });

        stopButton.addActionListener(e -> {
            setButtonsEnabled(true);
            launcher.interrupt();
        });
    }

    private void setButtonsEnabled(boolean bool) {
        singleThreadFetchButton.setEnabled(bool);
        concurrentFetchButton.setEnabled(bool);
        stopButton.setEnabled(!bool);
    }

    public static class MyUrl {

        private final String url;
        private String status;

        public MyUrl(String url, String status) {
            this.url = url;
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class Launcher extends Thread {

        public Semaphore semaphore;

        private final ArrayList<WebWorker> workers = new ArrayList<>();

        private final AtomicInteger runningThreads = new AtomicInteger(0);
        private final AtomicInteger completedThreads = new AtomicInteger(0);

        private Thread timeUpdater;
        private long startTime;

        public Launcher(int numThreads) {
            semaphore = new Semaphore(numThreads);
        }

        public void run() {
            startTimeUpdater();
            startThread();

            for (MyUrl url : urls) {
                try {
                    semaphore.acquire();
                    WebWorker worker = new WebWorker(url, this);
                    workers.add(worker);
                    worker.start();
                } catch (InterruptedException e) {
                    killWorkers();
                    break;
                }
            }

            stopThread();
            stopTimeUpdater();

            for (WebWorker worker : workers) {
                try {
                    worker.join();
                } catch (InterruptedException ignored) { }
            }

            setButtonsEnabled(true);
        }

        private void startTimeUpdater() {
            startTime = System.currentTimeMillis();
            timeUpdater = new Thread(() -> {
                while(runningThreads.get() > 0) {
                    try {
                        sleep(30);
                    } catch (InterruptedException e) {
                        break;
                    }
                    SwingUtilities.invokeLater(() -> elapsedLabel.setText(ELAPSED_LABEL_TEXT + (System.currentTimeMillis() - startTime)));
                }

            });
            timeUpdater.start();
        }

        private void stopTimeUpdater() {
            timeUpdater.interrupt();
        }

        public void startThread() {
            runningThreads.incrementAndGet();
        }

        public void stopThread() {
            runningThreads.decrementAndGet();
        }

        public void completeThread() {
            completedThreads.incrementAndGet();
        }

        private void killWorkers() {
            for (WebWorker worker : workers) {
                worker.interrupt();
                updateTable();
                updateBottomPanel();
            }
        }

        public void updateTable() {
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < urls.size(); i++) {
                    model.setValueAt(urls.get(i).getStatus(), i, 1);
                }
            });
        }

        public void updateBottomPanel() {
            SwingUtilities.invokeLater(() -> {
                runningLabel.setText(RUNNING_LABEL_TEXT + runningThreads.get());
                completedLabel.setText(COMPLETED_LABEL_TEXT + completedThreads.get());
                progressBar.setValue(completedThreads.get());
            });
        }

    }

}
