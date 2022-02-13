package ir.smmh.apps.rule.impl;

import ir.smmh.apps.rule.Player;
import ir.smmh.apps.rule.World;
import ir.smmh.nile.adj.Order;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.LinkedQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.*;

public class WorldImpl implements World {

    private final Thread thread = new WorldThread();
    private final String id;
    private final Set<Player> players = new HashSet<>();
    private final int width, height;
    private final Sequential.Mutable<Boolean> unoccupiedPositions;
    private final Sequential.Mutable<Building> buildingsOfPositions;
    private final Order<Command> commands = new LinkedQueue<>();
    private long time = 0;
    private long timeSpeed = 10;
    private final Command SLOW_DOWN_TIME = new Command() {
        @Override
        public void execute(Context context, Adder adder) {
            timeSpeed /= 2;
            if (timeSpeed > 1) adder.add(SLOW_DOWN_TIME);
        }
    };
    private boolean running = true;

    public WorldImpl(String id, int width, int height, @Nullable Iterable<Building> buildings) {
        int size = width * height;
        if (size > 0) {
            this.id = id;
            this.width = width;
            this.height = height;
            boolean[] array = new boolean[size];
            Arrays.fill(array, true);
            this.unoccupiedPositions = Sequential.Mutable.of(array);
            this.buildingsOfPositions = Sequential.Mutable.of(new Building[size]);
            if (buildings != null) {
                for (Building b : buildings) {
                    Command c = b.getCommand();
                    if (c != null)
                        this.commands.enter(c);
                    @Position int corner = b.getCorner();
                    int x1 = xOf(corner);
                    int y1 = yOf(corner);
                    int x2 = x1 + b.getWidth();
                    int y2 = y1 + b.getHeight();
                    for (int x = x1; x < x2; x++) {
                        for (int y = y1; y < y2; y++) {
                            @Position int p = positionOf(x, y);
                            this.buildingsOfPositions.setAtIndex(p, b);
                            occupy(p); // TODO
                        }
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("size cannot be less than 1");
        }
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void occupy(int p) {
        unoccupiedPositions.setAtIndex(p, false);
    }

    @Override
    public @NotNull Thread getThread() {
        return thread;
    }

    @Override
    public @NotNull JSONObject getState(Sequential<@Position Integer> positions) {
        JSONObject state = new JSONObject();
        for (@Position int p : positions) {
            // TODO
        }
        return state;
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull Iterable<Player> getPlayers() {
        return players;
    }

    @Override
    public void playerJoin(Player player) {
        players.add(player);
    }

    @Override
    public void playerLeft(Player player) {
        players.remove(player);
    }

    @Override
    public boolean isPlayerIn(Player player) {
        return players.contains(player);
    }

    @Override
    public boolean isUnoccupied(int p) {
        return unoccupiedPositions.getAtIndex(p);
    }

    @Override
    public @Nullable Building getBuildingAt(int p) {
        return buildingsOfPositions.getAtIndex(p);
    }

    public void stop() {
        this.running = false;
    }

    private class ContextImpl implements Command.Context {

        private final Map<String, Object> attributes = new HashMap<>();

        @Override
        public @NotNull World getWorld() {
            return WorldImpl.this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> @Nullable T getAttribute(String name) {
            try {
                return (T) attributes.get(name);
            } catch (ClassCastException e) {
                System.err.println("Mistyped attributed: " + name);
                return null;
            }
        }

        @Override
        public <T> void setAttribute(String name, T value) {
            attributes.put(name, value);
        }
    }

    private class WorldThread extends Thread {
        @Override
        public void run() {
            Command.Context context = new ContextImpl();
            Command.Adder adder = commands::enter;
            while (running) {
                time++;
                //noinspection ConstantConditions
                commands.enter(null);
                while (true) {
                    Command command = commands.poll();
                    if (command == null) break;
                    else {
                        try {
                            command.execute(context, adder);
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
                if (timeSpeed < 1000) {
                    try {
                        Thread.sleep(1000 / timeSpeed);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}
