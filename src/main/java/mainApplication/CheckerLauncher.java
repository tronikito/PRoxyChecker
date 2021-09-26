package mainApplication;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import mainApplication.interfaceControllers.EnableBtn;
import mainApplication.interfaceControllers.MainController;
import mainApplication.interfaceControllers.SetLabel;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class CheckerLauncher implements Runnable {

    private ArrayDeque<ProxyItem> proxyList;
    private MainController controller;
    private TextFlow reportLabel;
    private Button btn;
    private Thread WebDriverCheckerProxy;
    private ScrollPane scrollPane;

    public CheckerLauncher(ArrayDeque<ProxyItem> proxyList, MainController controller, TextFlow reportLabel, Button delete, ScrollPane scrollPane) {

        this.btn = delete;
        this.reportLabel = reportLabel;
        this.controller = controller;
        this.proxyList = proxyList;
        this.scrollPane = scrollPane;
    }

    @Override
    public void run() {

        System.out.println(proxyList.size() + ": testing proxys");

        int proxySize = proxyList.size();
        TimeWorked timeworked = new TimeWorked();
        timeworked.start();
        int proxyChecked = 0;

        /** report estimated time **/ // Used when WebDriver because load time estimated is 6;
        /*
        int estimatedTime = proxySize * 6;
        int estimatedMin = (proxySize * 6) / 60;
        String estimated = "";
        if (estimatedTime > 60) {
            if (estimatedTime % 60 < 10) estimated = estimatedMin + ":0" + estimatedTime % 60;
            else estimated = estimatedMin + ":" + estimatedTime % 60;
        } else {
            if (estimatedTime % 60 < 10) estimated = "0" + estimatedTime + "s";
            else estimated = estimatedTime + "s";
        }
        sendTextToController("Checking " + proxySize + " (estimated) " + estimated);
        */

        try {
            ArrayList<Thread> listThreads = new ArrayList<Thread>();
            for (ProxyItem x : proxyList) {

                WebDriverCheckerProxy = new Thread(new HttpClientCheckerProxy(proxyList.pop(), proxyList, reportLabel, scrollPane));
                WebDriverCheckerProxy.start();
                /** used when thread launched 1 by 1, with WebDriver**/
                //WebDriverCheckerProxy.join();

                listThreads.add(WebDriverCheckerProxy);
                proxyChecked++;

                //timeworked.end();
                //sendTextToController("Checking " + proxyChecked + " of " + proxySize + " in: " + timeworked.resultTime());
            }
            for (Thread x : listThreads) {
                x.join();
            }
        } catch (InterruptedException e) {

            /** used when thread launched 1 by 1, with WebDriver**/
            //System.out.println("Launcher test Thread trying to stop before some threads end");
            //System.out.println("Thread will stop when test Threads end");

        } finally {

            controller.updateList(proxyList);

            timeworked.end();
            sendTextToController("Finish " + proxyChecked + " of " + proxySize + " in: " + timeworked.resultTime());

            enableBtn(btn);

        }


    }

    private void sendTextToController(String report) {
        Platform.runLater(new Thread(new SetLabel(report, reportLabel, scrollPane)));
    }

    private void enableBtn(Button btn) {
        Platform.runLater(new Thread(new EnableBtn(btn)));
    }
}
