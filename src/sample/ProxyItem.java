package sample;

public class ProxyItem {
    public String ip;
    public int port;
    public Boolean working;

    public ProxyItem(String ip, int port, Boolean working) {
        this.ip = ip;
        this.port = port;
        this.working = working;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getWorking() {
        return working;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }
}

