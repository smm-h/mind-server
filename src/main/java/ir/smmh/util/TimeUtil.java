package ir.smmh.util;

import java.util.Date;

public interface TimeUtil {
    /**
     * @return Current time in Unix time
     */
    static long now() {
        return System.currentTimeMillis() / 1000L;
    }

    static String toString(long time) {
        return new Date(time * 1000).toString();
    }
}
