package mainApplication.interfaceControllers;

import javafx.scene.control.Label;

public class SetLabel implements Runnable{

    private String report;
    private Label reportLabel;

    public SetLabel(String report, Label reportLabel) {
        this.reportLabel = reportLabel;
        this.report = report;
    }

    @Override
    public void run() {
        reportLabel.setText(report);
    }
}
