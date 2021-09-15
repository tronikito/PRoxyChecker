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

        //System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
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


/***
 BrowserVersion[] webClientList = BrowserVersion.ALL_SUPPORTED_BROWSERS;

 for (int x = 0; x < webClientList.length; x++) {


 WebClient webClient = new WebClient(webClientList[x]);


 //webClient.getOptions().setJavaScriptEnabled(false);
 webClient.getOptions().setThrowExceptionOnScriptError(true);
 webClient.getCookieManager().setCookiesEnabled(false);
 //webClient.getOptions().setJavaScriptEnabled(true);

 // Set some example credentials
 //creds.addCredentials("tronikito1@gmail.com", "pepito54321");

 // And now add the provider to the webClient instance
 //webClient.setCredentialsProvider(creds);
 //webClient.getOptions().setCssEnabled(false);
 webClient.getOptions().setUseInsecureSSL(true);

 webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
 webClient.getOptions().setThrowExceptionOnScriptError(false);


 WebClient webClient = new WebClient();

 HtmlPage page = webClient.getPage("https://app.stormgain.com/crypto-miner/");
 webClient.waitForBackgroundJavaScript(35000);

 System.out.println(webClientList[x].getApplicationCodeName() + "ASDLASDKASDKASDJKASD");
 System.out.println(page.getWebResponse().getContentAsString());

 webClient.close();
 //HtmlInput login = page.getHtmlElementById("email");
 //HtmlInput searchBox = page.getElementByName("login");
 //searchBox.setValueAttribute("tronikito1@gmail.com");


 System.out.println(page.getPage().getBody().getChildElementCount());

 for (DomElement x : page.getPage().getBody().getChildElements()) {

 if (x.hasAttributes()) {

 System.out.println("Attributos de " + x.getNodeName());
 for (Map.Entry<String, DomAttr> entry : x.getAttributesMap().entrySet()) {
 System.out.println(entry.getKey() + "/" + entry.getValue());
 }
 }
 }
 }
 **/

/**

 System.out.println("Start");


 //File file = new File("extension_1_2_0_0.crx"); // zip files are also accepted
 //File file2 = new File("extension_6_4_0_0.crx"); // zip files are also accepted

 ChromeOptions options = new ChromeOptions();
 //options.addExtensions(file);
 //options.addExtensions(file2);

 String proxy = "12313123";

 options.addArguments("--proxy-server" + proxy);
 options.addArguments("start-maximized");
 options.addArguments("--incognito");
 DesiredCapabilities capabilities = DesiredCapabilities.chrome();
 capabilities.setCapability(ChromeOptions.CAPABILITY, options);

 System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");

 WebDriver driver = new ChromeDriver(options);

 String baseUrl = "https://app.stormgain.com/crypto-miner/#modal_login";

 driver.get(baseUrl);


 /** webdriver fail
 *
 WebElement elementLogin = driver.findElement(By.name("login"));
 elementLogin.sendKeys("tronikito1@gmail.com");
 WebElement elementPass = driver.findElement(By.name("password"));
 elementPass.sendKeys("pepito54321");

 List<WebElement> listOfElements = driver.findElements(By.xpath("//input"));

 for (WebElement x : listOfElements) {
 if (x.getAttribute("type").equals("submit")) {
 x.click();
 }
 }

 boolean found = false;
 do {
 Thread.sleep(5000);
 List<WebElement> listIframe = driver.findElements(By.xpath("//iframe"));

 try {
 for (WebElement x : listIframe) {
 x.click();
 }
 } catch (Exception e) {
 }

 Thread.sleep(5000);
 listIframe = driver.findElements(By.xpath("//iframe"));

 for (WebElement x : listIframe) {
 try {
 driver.switchTo().frame(x);
 } catch (Exception e) {
 }

 List<WebElement> listDiv = driver.findElements(By.className("no-selection"));
 if (listDiv.size() > 0) {
 found = true;
 System.out.println("CaptchaSpawnTrue");
 break;
 }
 }
 } while (!found);

 System.out.println("end");
 //driver.close();
 }*/