package ir.smmh.util.jile;

public abstract class Service extends Thread {

    public final long pace;

    protected Service() {
        this(100L);
    }

    protected Service(long milliseconds) {
        super();
        pace = milliseconds;
    }

    @Override
    public final void run() {
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