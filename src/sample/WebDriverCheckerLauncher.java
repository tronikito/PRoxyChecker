package sample;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayDeque;

public class WebDriverCheckerLauncher implements Runnable {

    private ArrayDeque<ProxyItem> proxyList;
    private Controller controller;
    private TimeWorked timeworked;
    private Label reportLabel;
    private Button btn;

    public WebDriverCheckerLauncher(ArrayDeque<ProxyItem> proxyList, Controller controller, Label reportLabel, Button delete ) {

        this.btn = delete;
        this.reportLabel = reportLabel;
        this.controller = controller;
        this.proxyList = proxyList;
    }

    @Override
    public void run() {

        System.out.println(proxyList.size() + ": testing proxys");

        int proxySize = proxyList.size();
        timeworked = new TimeWorked();
        timeworked.start();
        int proxyChecked = 0;

        /** report estimated time **/
        int estimatedTime = proxySize*6;
        int estimatedMin = (proxySize*6)/60;
        String estimated = "";
        if (estimatedTime > 60) {
            if (estimatedTime%60 < 10) estimated = estimatedMin + ":0" + estimatedTime%60;
            else estimated = estimatedMin + ":" + estimatedTime%60;
        } else {
            if (estimatedTime%60 < 10) estimated = "0" + estimatedTime + "s";
            else estimated =  estimatedTime + "s";
        }
        Platform.runLater(new Thread(new ReportLabel("Checking "+proxySize+" (estimated) " + estimated, reportLabel)));

        try {
            for (ProxyItem x : proxyList) {

                Thread WebDriverCheckerProxy = new Thread(new WebDriverCheckerProxy(proxyList.pop(), proxyList, controller));
                WebDriverCheckerProxy.start();
                WebDriverCheckerProxy.join();
                proxyChecked++;

                timeworked.end();
                Platform.runLater(new Thread(new ReportLabel("Checking "+proxyChecked+" of "+proxySize+" in: " + timeworked.resultTime() + " (estimated) " + estimated, reportLabel)));

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            timeworked.end();
            Platform.runLater(new Thread(new ReportLabel("Finish "+proxyChecked+" of "+proxySize+" in: " + timeworked.resultTime() + " (estimated) " + estimated, reportLabel)));

            Platform.runLater(new Thread(new EnableBtn(btn)));
        }


    }
}
