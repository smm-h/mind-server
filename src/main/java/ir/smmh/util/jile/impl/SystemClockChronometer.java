package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Chronometer;

import java.time.Instant;

/**
 * A {@link SystemClockChronometer} is a {@link ClockChronometer} that uses the
 * {@link Instant}s of the system clock. For a more general approach see
 * {@link ClockChronometer} that uses the instants of any given clock.
 */
public class SystemClockChronometer implements Chronometer {

    private Instant then;

    @Override
    public final void reset() {
        then = Instant.now();
    }

    @Override
    public final double stop() {
        Instant now = Instant.now();
        long difference = then.toEpochMilli() - now.toEpochMilli();
        return difference / 10e6;
    }
}
