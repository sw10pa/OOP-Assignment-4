package WebLoader;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class WebWorker extends Thread {

    private final static String ERROR_STATUS = "err";
    private final static String INTERRUPTED_STATUS = "interrupted";

    private final WebFrame.MyUrl myUrl;
    private final WebFrame.Launcher launcher;

    public WebWorker(WebFrame.MyUrl myUrl, WebFrame.Launcher launcher) {
        this.myUrl = myUrl;
        this.launcher = launcher;
    }

    public void run() {
        launcher.startThread();
        launcher.updateBottomPanel();

        download();

        launcher.stopThread();
        launcher.updateTable();
        launcher.completeThread();
        launcher.updateBottomPanel();

        launcher.semaphore.release();
    }

    private void download() {
        InputStream input = null;
        try {
            long startTime = System.currentTimeMillis();

            URL url = new URL(myUrl.getUrl());
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();

            input = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            int len;
            char[] array = new char[1000];
            StringBuilder contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                contents.append(array, 0, len);
                Thread.sleep(100);
            }

            long endTime = System.currentTimeMillis();

            String status = new SimpleDateFormat("HH:mm:ss").format(new Date(startTime))
                    + "   " + (endTime - startTime) + "ms   "
                    + contents.length() + " bytes";

            myUrl.setStatus(status);

        } catch(IOException error) {
            myUrl.setStatus(ERROR_STATUS);
        } catch(InterruptedException interrupted) {
            myUrl.setStatus(INTERRUPTED_STATUS);
        } finally {
            try{
                if (input != null) input.close();
            }
            catch(IOException ignored) {}
        }
    }

}
