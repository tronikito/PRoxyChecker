package mainApplication;

import java.util.concurrent.TimeUnit;

public class TimeWorked {
    private long timeStart;
    private long timeEnd;

    public void start() {
        this.timeStart = System.nanoTime();
    }

    public void end() {
        this.timeEnd = System.nanoTime();
    }

    public String resultTime() {

        String result = "";
        long resultTime = timeEnd - timeStart;
        long convert = TimeUnit.SECONDS.convert(resultTime, TimeUnit.NANOSECONDS);

        if (convert < 10) result = "0" + convert + "s";
        else if (convert > 10 && convert < 60) result = convert + "s";
        else if (convert > 60) {

            long convertS = TimeUnit.SECONDS.convert(resultTime, TimeUnit.NANOSECONDS)%60;
            convert = TimeUnit.MINUTES.convert(resultTime, TimeUnit.NANOSECONDS);

            if (convertS > 10) result = convert +":"+convertS ;
            else result = convert +":0"+convertS ;
        }
        return result;
    }
}
