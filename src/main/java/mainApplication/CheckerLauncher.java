package mainApplication;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import mainApplication.interfaceControllers.MainController;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class CheckerLauncher implements Runnable {

    private ArrayDeque<ProxyItem> proxyList;
    private MainController controller;
    private TextFlow reportLabel;
    private Thread HttpClientCheckerProxy;
    private ArrayList<Button> listButton;

    public CheckerLauncher(ArrayDeque<ProxyItem> proxyList, MainController controller, TextFlow reportLabel, ArrayList<Button> listButton) {

        this.listButton = listButton;
        this.reportLabel = reportLabel;
        this.controller = controller;
        this.proxyList = proxyList;
    }

    @Override
    public void run() {

        for (Button x : listButton) {
            x.setDisable(true);
        }

        sendTextToController(System.lineSeparator() + "Testing " + proxyList.size() + " proxy" + System.lineSeparator());

        int proxySize = proxyList.size();
        TimeWorked timeworked = new TimeWorked();
        timeworked.start();
        int proxyChecked = 0;

        try {
            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (ProxyItem x : proxyList) {

                HttpClientCheckerProxy = new Thread(new HttpClientCheckerProxy(proxyList.pop(), proxyList, reportLabel));
                HttpClientCheckerProxy.start();

                listThreads.add(HttpClientCheckerProxy);
                proxyChecked++;

            }
            for (Thread x : listThreads) {
                x.join();
            }
        } catch (InterruptedException e) {

        } finally {

            controller.updateList(proxyList);

            timeworked.end();
            sendTextToController("Finish " + proxyChecked + " of " + proxySize + " in: " + timeworked.resultTime());

            for (Button x : listButton) {
                enableBtn(x);
            }
        }
    }

    private void sendTextToController(String report) {

        final Runnable r = new Runnable() {
            public void run() {
                reportLabel.getChildren().add(new Text(report + System.lineSeparator()));
            }
        };

        Platform.runLater(new Thread(r));

    }

    private void enableBtn(Button btn) {

        final Runnable r = new Runnable() {
            public void run() {
                btn.setDisable(false);
            }
        };

        Platform.runLater(new Thread(r));
    }
}
