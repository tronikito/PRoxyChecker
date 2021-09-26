package mainApplication;

public abstract class Resources {

    private static int width = 500;
    private static int height = 720;
    private static String lastLoadFilePath;
    private static String lastSaveFilePath;
    private static String colorFail = "#bf5258";
    private static String colorWork = "#5fc553";
    private static Thread threadTestController;

    public static Thread getThreadTestController() {
        return threadTestController;
    }

    public static void setThreadTestController(Thread threadTestController) {
        Resources.threadTestController = threadTestController;
    }

    public static int getWidth() {
        return width;
    }

    public static void setWidth(int width) {
        Resources.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public static void setHeight(int height) {
        Resources.height = height;
    }

    public static String getLastLoadFilePath() {
        return lastLoadFilePath;
    }

    public static String getLastSaveFilePath() {
        return lastSaveFilePath;
    }

    public static void setLastLoadFilePath(String lastLoadFilePath) {
        Resources.lastLoadFilePath = lastLoadFilePath;
    }

    public static void setLastSaveFilePath(String lastSaveFilePath) {
        Resources.lastSaveFilePath = lastSaveFilePath;
    }

    public static String getColorFail() {
        return colorFail;
    }

    public static void setColorFail(String colorFail) {
        Resources.colorFail = colorFail;
    }

    public static String getColorWork() {
        return colorWork;
    }

    public static void setColorWork(String colorWork) {
        Resources.colorWork = colorWork;
    }
}