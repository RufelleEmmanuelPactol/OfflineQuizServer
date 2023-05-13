package onBoard.network.networkUtils;

import onBoard.network.exceptions.ConcurrencyTimeoutException;

import java.sql.Time;
import java.util.concurrent.TimeoutException;

public class NetworkTimer extends Thread{
    Watch observe;
    RequestToken comparingTo;
    boolean type;
    public double delay = 10.0;
    public void timeThisUntil (Watch observe, Object intendedValue) {
        type = true;
        this.observe = observe;
        this.start();
    }

    public void timeThisWhile(Watch observe, Object runWhile){
        this.observe = observe;
        type = false;
        this.start();
    }


    @Override
    public void run () {
        var atStart = System.nanoTime();
        if (type) {
            while (observe.watch!=comparingTo) {
                if (delay < nanoToSeconds(System.nanoTime()-atStart)) throw new ConcurrencyTimeoutException();
            }
        } else {
            while (observe.watch==comparingTo) {
                if (delay < nanoToSeconds(System.nanoTime()-atStart)) throw new ConcurrencyTimeoutException();
            }
        }
        System.out.println("Timer exited without a hitch!");
    }

    private double nanoToSeconds (double elapsedTime) {
        return (double) elapsedTime / 1000000000.0;
    }
}
