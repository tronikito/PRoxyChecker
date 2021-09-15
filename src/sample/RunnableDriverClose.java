package sample;

import org.openqa.selenium.WebDriver;

public class RunnableDriverClose implements Runnable {

    private final WebDriver driver;
    private final ProxyItem proxy;
    public RunnableDriverClose(WebDriver driver, ProxyItem proxy) {
        this.driver = driver;
        this.proxy = proxy;
    }
    @Override
    public void run() {


    }
}
