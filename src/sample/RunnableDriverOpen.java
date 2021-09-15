package sample;

import org.openqa.selenium.WebDriver;

public class RunnableDriverOpen implements Runnable {

    private final WebDriver driver;
    private final String baseUrl;
    public RunnableDriverOpen(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
    }
    @Override
    public void run() {

        try {

        } catch (Exception e) {}

    }

}
