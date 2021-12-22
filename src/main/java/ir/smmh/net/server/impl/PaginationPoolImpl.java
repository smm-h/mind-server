package ir.smmh.net.server.impl;

import ir.smmh.net.server.Pagination;
import ir.smmh.net.server.PaginationPool;
import ir.smmh.util.Logger;
import ir.smmh.util.RandomUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PaginationPoolImpl implements PaginationPool {

    private final Map<String, Pagination> paginationPool = new HashMap<>();
    private final Map<String, Instant> paginationLastUsed = new HashMap<>();
    private final Duration timeOutDuration = Duration.ofMinutes(5);
    private final long idleWait = TimeUnit.SECONDS.toMillis(30);

    {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(idleWait);
                    final Instant now = Instant.now();
                    for (final String paginationId : paginationLastUsed.keySet()) {
                        final Instant lastUsed = paginationLastUsed.get(paginationId);
                        if (Duration.between(lastUsed, now).abs().compareTo(timeOutDuration) > 0) {
                            paginationLastUsed.remove(paginationId);
                            paginationPool.remove(paginationId);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // TODO make pagination non-generic

    @Override
    public @Nullable Pagination find(@NotNull String paginationId) {
        if (paginationPool.containsKey(paginationId)) {
            paginationLastUsed.put(paginationId, Instant.now());
            return paginationPool.get(paginationId);
        } else {
            return null;
        }
    }

    @Override
    public @NotNull String add(@NotNull Pagination pagination) {
        final String paginationId = RandomUtil.generateRandomHex(32);
        paginationPool.put(paginationId, pagination);
        paginationLastUsed.put(paginationId, Instant.now());
        return paginationId;
    }

    private final PrintStream log = Logger.getLog("pagination-pool");

    @Override
    public @NotNull PrintStream getLog() {
        return log;
    }
}
