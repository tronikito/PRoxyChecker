package mainApplication.interfaceControllers;

import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SetLabel implements Runnable {

    private String report;
    private TextFlow reportLabel;
    private ScrollPane scrollPane;

    public SetLabel(String report, TextFlow reportLabel, ScrollPane scrollPane) {
        this.reportLabel = reportLabel;
        this.report = report;
        this.scrollPane = scrollPane;
    }

    @Override
    public void run() {

        Text t1 = new Text(report);
        reportLabel.getChildren().add(t1);
        reportLabel.getChildren().add(new Text(System.lineSeparator()));

    }
}
