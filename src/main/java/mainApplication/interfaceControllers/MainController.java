package mainApplication.interfaceControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainApplication.CheckerLauncher;
import mainApplication.Main;
import mainApplication.ProxyItem;
import mainApplication.Resources;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class MainController {

    public MainController(Stage stage) {
        this.stage = stage;
    }

    protected Stage stage;
    @FXML
    protected TableView listProxyTable;
    @FXML
    protected TableColumn proxyIp;
    @FXML
    protected TableColumn proxyPort;
    @FXML
    protected TableColumn proxyWorking;
    @FXML
    protected Button btnLoadFile;
    @FXML
    protected Button btnCheck;
    @FXML
    protected Button btnDelete;
    @FXML
    protected Button btnSave;
    @FXML
    protected ScrollPane scrollPane;
    @FXML
    protected Button btnClear;
    @FXML
    protected TextFlow reportLabel;


    private static ArrayDeque<ProxyItem> proxyItemList;
    private static ObservableList<ProxyItem> proxyObservableList;

    private String colorFail = Resources.getColorFail();
    private String colorWork = Resources.getColorWork();

    private Thread checkingProxyWorking;

    private ArrayList<Button> listButton;

    public void initialize() {

        listButton = new ArrayList<Button>();
        listButton.add(btnDelete);
        listButton.add(btnSave);
        listButton.add(btnCheck);
        listButton.add(btnLoadFile);

        reportLabel.heightProperty().addListener((observable, oldValue, newValue) ->
                scrollPane.vvalueProperty().set(newValue.doubleValue()));

        proxyIp.setCellValueFactory(new PropertyValueFactory<ProxyItem, String>("ip"));
        proxyPort.setCellValueFactory(new PropertyValueFactory<ProxyItem, Integer>("port"));
        proxyWorking.setCellValueFactory(new PropertyValueFactory<ProxyItem, String>("working"));

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (stage.getWidth() < 500) stage.setWidth(500);
            else reloadWindow(stage);
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (stage.getHeight() < 720) stage.setHeight(720);
            else reloadWindow(stage);
        });

        btnLoadFile.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {

                FileChooser fileChooser = new FileChooser();
                String path = null;

                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                try {
                    File fileDir = new File(Resources.getLastLoadFilePath());
                    if (fileDir.isFile()) {
                        fileDir = new File(fileDir.getParent());
                    }
                    fileChooser.setInitialDirectory(fileDir);

                } catch (IllegalArgumentException | NullPointerException e) {
                    System.out.println("Not default folder found in config file");
                } finally {
                    try {
                        path = fileChooser.showOpenDialog(stage).getPath();
                        try {
                            updateList(refreshTableFromTxt(path));
                            btnCheck.setDisable(false);
                        } catch (IOException e) {
                            btnCheck.setDisable(true);
                            e.printStackTrace();
                        }
                        Main.modifyProps("lastLoadFilePath", path);
                        Resources.setLastLoadFilePath(path);
                    } catch (NullPointerException e) {
                    }
                }
            }
        });

        btnCheck.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                try {
                    checkProxyWorking();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                deleteFailedProxy();
            }
        });

        btnSave.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {

                FileChooser fileChooser = new FileChooser();
                String path = null;

                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));

                try {
                    File fileDir = new File(Resources.getLastSaveFilePath());
                    if (fileDir.isFile()) {
                        fileDir = new File(fileDir.getParent());
                    }
                    fileChooser.setInitialDirectory(fileDir);


                } catch (NullPointerException e) {
                } finally {
                    try {
                        path = fileChooser.showSaveDialog(stage).getPath();

                        FileOutputStream out = null;

                        try {
                            out = new FileOutputStream(new File(path));

                            for (ProxyItem x : proxyItemList) {
                                String proxy = x.getIp() + ":" + x.getPort() + System.lineSeparator();
                                byte b[] = proxy.getBytes();
                                out.write(b);
                            }
                            out.close();

                        } catch (FileNotFoundException e) {
                            System.out.println("No config.properties file");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            Main.modifyProps("lastSaveFilePath", path);
                            Resources.setLastSaveFilePath(path);

                            reportLabel.getChildren().add(new Text("Saved Proxy List in: " + path));
                            reportLabel.getChildren().add(new Text(System.lineSeparator()));
                        }
                    } catch (NullPointerException e) {
                    }
                }
            }
        });

        btnClear.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                clearLog();
            }
        });
    }

    public void reloadWindow(Stage stage) {

        listProxyTable.prefHeightProperty().bind(stage.heightProperty());
        listProxyTable.setMaxWidth(stage.getWidth() - 35);

        double proportion = (listProxyTable.getMaxWidth()) / 3;
        double proxyIpSize = proportion * 2;
        double proxyPortSize = proportion / 2;
        double proxyWorkingSize = proportion / 2;

        proxyIp.setPrefWidth(proxyIpSize - 17);
        proxyPort.setPrefWidth(proxyPortSize);
        proxyWorking.setPrefWidth(proxyWorkingSize);

        listProxyTable.setMaxWidth(proxyIpSize + proxyPortSize + proxyWorkingSize);
    }

    private ArrayDeque<ProxyItem> refreshTableFromTxt(String path) throws IOException {

        File file = new File(path);
        ArrayDeque<ProxyItem> listProxy = new ArrayDeque<ProxyItem>();

        String proxy;
        FileReader f = new FileReader(file);
        BufferedReader b = new BufferedReader(f);
        while ((proxy = b.readLine()) != null) {
            String[] parts = proxy.split(":");
            String ip = parts[0];
            try {
                int port = Integer.parseInt(parts[1]);
                ProxyItem newP = new ProxyItem(ip, port, false);
                listProxy.add(newP);
            } catch (Exception e) {
                System.out.println(ip + ":ERROR PORT");
            }
        }
        b.close();

        System.out.println(listProxy.size() + " Total Proxy's in List");
        Text t1 = new Text(listProxy.size() + " Total Proxy's in List");
        reportLabel.getChildren().add(t1);
        reportLabel.getChildren().add(new Text(System.lineSeparator()));
        scrollPane.setVvalue(1.0D);

        return listProxy;
    }

    public void updateList(ArrayDeque<ProxyItem> itemList) {

        proxyItemList = itemList;
        proxyObservableList = FXCollections.observableArrayList(proxyItemList);
        listProxyTable.setItems(null);
        listProxyTable.setItems(proxyObservableList);

        listProxyTable.setRowFactory(tv -> new TableRow<ProxyItem>() {
            @Override
            protected void updateItem(ProxyItem item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {

                    if (!item.getWorking()) {
                        setStyle("-fx-background-color: " + colorFail + ";");
                    }
                    if (item.getWorking()) {
                        setStyle("-fx-background-color: " + colorWork + ";");
                    }
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void checkProxyWorking() throws IOException, InterruptedException {

        if (checkingProxyWorking != null) {
            if (checkingProxyWorking.isAlive()) {
                checkingProxyWorking.interrupt();
            }
        }

        checkingProxyWorking = new Thread(new CheckerLauncher(proxyItemList, this, reportLabel, listButton));
        Resources.setThreadTestController(checkingProxyWorking);
        checkingProxyWorking.start();

    }

    private void deleteFailedProxy() {
        proxyItemList.removeIf(x -> !x.getWorking());
        updateList(proxyItemList);
        btnSave.setDisable(false);
    }

    private void clearLog() {
        reportLabel.getChildren().clear();
    }
}