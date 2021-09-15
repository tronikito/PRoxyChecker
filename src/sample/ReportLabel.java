package sample;

import javafx.scene.control.Label;

public class ReportLabel implements Runnable{

    private String report;
    private Label reportLabel;

    public ReportLabel(String report, Label reportLabel) {
        this.reportLabel = reportLabel;
        this.report = report;
    }

    @Override
    public void run() {
        reportLabel.setText(report);
    }
}
