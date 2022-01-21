package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Chronometer;

import java.time.Clock;
import java.time.Instant;

/**
 * A {@link ClockChronometer} is a {@link Chronometer} that uses the
 * {@link Instant}s of any given {@link Clock}.
 */
public class ClockChronometer implements Chronometer {

    private final Clock clock;
    private Instant then;

    public ClockChronometer(Clock clock) {
        super();
        this.clock = clock;
    }

    @Override
    public final void reset() {
        then = clock.instant();
    }

    @Override
    public final double stop() {
        Instant now = clock.instant();
        long difference = then.toEpochMilli() - now.toEpochMilli();
        return difference / 10e6;
    }
}
