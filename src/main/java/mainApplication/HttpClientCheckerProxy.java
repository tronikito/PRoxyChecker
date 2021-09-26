package mainApplication;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class HttpClientCheckerProxy implements Runnable {

    private ProxyItem x;
    private TextFlow reportLabel;
    private ArrayDeque<ProxyItem> proxyList;

    public HttpClientCheckerProxy(ProxyItem x, ArrayDeque<ProxyItem> proxyList, TextFlow reportLabel) {
        this.reportLabel = reportLabel;
        this.x = x;
        this.proxyList = proxyList;
    }

    @Override
    public void run() {

        System.out.println("Start checking: " + x.getIp() + ":" + x.getPort());

        HttpClient clientProxy = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .proxy(ProxySelector.of(new InetSocketAddress(x.getIp(), x.getPort())))
                .build();

        String header = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://www.google.com"))
                .setHeader("User-Agent", header)
                .GET() // GET is default
                .build();

        String result = null;

        try {
            CompletableFuture<HttpResponse<String>> response = clientProxy.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString());

            result = response.thenApply(HttpResponse::statusCode).get(20, TimeUnit.SECONDS) + "";

        } catch (ExecutionException | TimeoutException | InterruptedException e) {
             result = "404";
        } finally {

            if (!result.equals("404")) reportProxy(true, result);
            else reportProxy(false, result);

        }
    }

    private void reportProxy(boolean status, String result) {

        final Runnable r = new Runnable() {
            public void run() {
                reportLabel.getChildren().add(new Text(x.getIp() + ":" + x.getPort() + "...StatusCode: " + result + System.lineSeparator()));
            }
        };

        System.out.println(x.getIp() + ":" + x.getPort() + " | StatusCode: " + result);
        Platform.runLater(new Thread(r));
        ProxyItem newProxy = new ProxyItem(x.getIp(), x.getPort(), status);
        proxyList.add(newProxy);

    }


}