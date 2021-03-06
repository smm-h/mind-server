package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Chronometer;

/**
 * A {@link MChronometer} is a {@link Chronometer} that uses
 * {@link System#currentTimeMillis()}. Another chronometer is the
 * {@link NChronometer} that is just as precise, if not more so. That being
 * said, this one is cross-platform and more portable.
 */
public class MChronometer implements Chronometer {

    private double m;

    @Override
    public final void reset() {
        m = System.currentTimeMillis();
    }

    @Override
    public final double stop() {
        return System.currentTimeMillis() - m;
    }
}
