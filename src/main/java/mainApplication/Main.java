package mainApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mainApplication.interfaceControllers.MainController;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

public class Main extends Application {

    //--module-path "D:\Proyectos\botStormgain\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadProp();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../frames/mainFrame" + ".fxml"));

        MainController controller = new MainController(primaryStage);
        fxmlLoader.setController(controller);

        Parent mainFrame = fxmlLoader.load();
        Scene scene = new Scene(mainFrame, Resources.getWidth(), Resources.getHeight());
        primaryStage.setResizable(true);
        primaryStage.setTitle("Proxy Checker 1.0");
        primaryStage.setScene(scene);
        controller.reloadWindow(primaryStage);
        primaryStage.show();

    }

    public void stop() {

        System.out.println("Closing app");

        if (Resources.getThreadTestController() != null) {
            Resources.getThreadTestController().interrupt();
        }

    }

    public static void modifyProps(String property, String value) {

        Properties props = new Properties();

        FileInputStream in;
        FileOutputStream out = null;

        try {

            in = new FileInputStream(new File("config.properties"));
            props.load(in);
            in.close();

            out = new FileOutputStream(new File("config.properties"));
            props.setProperty(property, value);
            props.store(out, null);
            out.close();

        } catch (FileNotFoundException e) {
            System.out.println("No config.properties file");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadProp() {

        Properties prop = new Properties();
        FileInputStream propertiesStream = null;

        try {

            propertiesStream = new FileInputStream("config.properties");
            prop.load(propertiesStream);

            Field fld[] = Resources.class.getDeclaredFields();

            for (Map.Entry<Object, Object> e : prop.entrySet()) {

                for (Field x : fld) {
                    if (x.getName().equals(e.getKey().toString())) {
                        x.setAccessible(true);
                        if (x.getType().getName().equals("int")) {
                            try {
                                if (!e.getValue().toString().equals("")) {
                                    x.set(null, Integer.parseInt(e.getValue().toString()));
                                    System.out.println("Variable Name is : " + e.getKey().toString() + " Value : " + e.getValue().toString());
                                }
                            } catch (IllegalAccessException y) {
                            }
                        } else if (x.getType().getName().equals("java.lang.String")) {
                            try {
                                if (!e.getValue().toString().equals("")) {
                                    x.set(null, e.getValue().toString());
                                    System.out.println("Variable Name is : " + e.getKey().toString() + " Value : " + e.getValue().toString());
                                }
                            } catch (IllegalAccessException y) {
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No config.properties file");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                propertiesStream.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }
}

