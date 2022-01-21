package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Chronometer;

/**
 * A {@link NChronometer} is a {@link Chronometer} that uses
 * {@link System#nanoTime()}. Another chronometer is the {@link MChronometer}
 * that is often much less precise.
 */
public class NChronometer implements Chronometer {

    private double n;

    @Override
    public final void reset() {
        n = System.nanoTime();
    }

    @Override
    public final double stop() {
        return (System.nanoTime() - n) / 10e6;
    }
}
