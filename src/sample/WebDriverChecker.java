package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

class WebDriverCheckerProxy implements Runnable {

    private ProxyItem x;
    private Controller controller;
    private ArrayDeque<ProxyItem> proxyList;

    public WebDriverCheckerProxy(ProxyItem x, ArrayDeque<ProxyItem> proxyList, Controller controller) {
        this.controller = controller;
        this.x = x;
        this.proxyList = proxyList;
    }


    @Override
    public void run() {

        System.out.println("Start checking: " + x.getIp() + ":" + x.getPort());

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--proxy-server=http://" + x.getIp() + ":" + x.getPort());
        //options.addArguments("start-maximized");
        options.addArguments("--incognito");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);


        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");

        WebDriver driver = new ChromeDriver(options);

        String baseUrl = "https://www.google.com/";

        System.setProperty("webdriver.chrome.silentOutput", "true");
        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);

        try {
            driver.get(baseUrl);
        } catch (Exception e) {
        } finally {

            List<WebElement> listOfElements = driver.findElements(By.xpath("//body"));

            try {
                if (listOfElements.get(0).getAttribute("jsmodel") != null) {
                    System.out.println(x.getIp() + ":" + x.getPort() + ": working true");
                    ProxyItem newProxy = new ProxyItem(x.getIp(), x.getPort(), true);
                    proxyList.remove(x);
                    proxyList.add(newProxy);
                } else {
                    System.out.println(x.getIp() + ":" + x.getPort() + " working false");
                    ProxyItem newProxy = new ProxyItem(x.getIp(), x.getPort(), false);
                    proxyList.remove(x);
                    proxyList.add(newProxy);
                }
            } catch (Exception e) {
                //No Body
            }
        }

        driver.close();
        controller.updateList(proxyList);

    }
}

