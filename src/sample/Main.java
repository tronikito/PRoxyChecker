package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private int width = 500;
    private int height = 720;
    private static Thread threadTestController;

    public static void setThreadTestController(Thread threadTestController) {
        Main.threadTestController = threadTestController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sample" + ".fxml"));

        Controller controller = new Controller(primaryStage);
        fxmlLoader.setController(controller);

        Parent mainFrame = fxmlLoader.load();
        Scene scene = new Scene(mainFrame, width, height);
        primaryStage.setResizable(true);
        primaryStage.setTitle("Proxy Checker 1.0");
        primaryStage.setScene(scene);
        controller.reloadWindow(primaryStage);
        primaryStage.show();

    }

    public void stop() {
        System.out.println("Closing app");

        if (threadTestController != null) {
            threadTestController.interrupt();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }
}

