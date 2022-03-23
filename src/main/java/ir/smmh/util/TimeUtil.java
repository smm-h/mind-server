package ir.smmh.util;

public interface TimeUtil {
    /**
     * @return Current time in Unix time
     */
    static long now() {
        return System.currentTimeMillis() / 1000L;
    }
}
