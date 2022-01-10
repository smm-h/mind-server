package ir.smmh.jile.common;

public abstract class Service extends Thread {

    public final long pace;

    public Service() {
        this(100L);
    }

    public Service(long milliseconds) {
        pace = milliseconds;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(Math.max(5L, pace));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!serve());
    }

    /**
     * return true to stop the service loop, and false to continue
     */

    // TODO reverse method
    public abstract boolean serve();
}