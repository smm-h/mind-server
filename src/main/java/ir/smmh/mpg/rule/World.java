package ir.smmh.mpg.rule;

import ir.smmh.mpg.lobby.Player;
import ir.smmh.mpg.rule.impl.AbstractSearch;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.ArrayQueue;
import ir.smmh.nile.adj.impl.ArrayStack;
import ir.smmh.nile.adj.impl.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public interface World {
    long getTime();

    int getWidth();

    int getHeight();

    default int getSize() {
        return getWidth() * getHeight();
    }

    void occupy(@Position int p);

    default @Position int positionOf(int x, int y) {
        return x + y * getWidth();
    }

    default int xOf(@Position int p) {
        return p % getWidth();
    }

    default int yOf(@Position int p) {
        return p / getWidth();
    }

    default boolean isInBounds(@Position int p) {
        int px = xOf(p);
        int py = yOf(p);
        return px >= 0 && py >= 0 && px < getWidth() && py < getHeight();
    }

    default void occupyLineHorizontal(int x0, int x1, int y) {
        for (int x = x0; x <= x1; x++) {
            occupy(positionOf(x, y));
        }
    }

    default void occupyLineVertical(int y0, int y1, int x) {
        for (int y = y0; y <= y1; y++) {
            occupy(positionOf(x, y));
        }
    }

    @NotNull Thread getThread();

    @NotNull JSONObject getState(Sequential<@Position Integer> positions);

    @NotNull String getId();

    @NotNull Iterable<Player> getPlayers();

    void playerJoin(Player player);

    void playerLeft(Player player);

    boolean isPlayerIn(Player player);

    boolean isUnoccupied(@Position int p);

    @Nullable Building getBuildingAt(@Position int p);

    default double distance(@Position int from, @Position int to) {
        int dx = xOf(from) - xOf(to);
        int dy = yOf(from) - yOf(to);
        return Math.sqrt(dx * dx + dy * dy);
    }

    default @Nullable Search getPath(@Position int from, @Position int to) {
        return new AStarSearch(this, from, to);
    }

//    double getElevation(@Position int p);

//    void dig(@Position int p, double depth) throws GameException;

    default @NotNull Iterable<@Position Integer> nextMoves(@Position int origin) {
        int x = xOf(origin);
        int y = yOf(origin);
        List<@Position Integer> list = new ArrayList<>(4);
        @Position int p;

        if (x > 0)
            if (isUnoccupied(p = positionOf(x - 1, y))) list.add(p);

        if (y > 0)
            if (isUnoccupied(p = positionOf(x, y - 1))) list.add(p);

        if (x < getWidth())
            if (isUnoccupied(p = positionOf(x + 1, y))) list.add(p);

        if (y < getHeight())
            if (isUnoccupied(p = positionOf(x, y + 1))) list.add(p);

        return list;
    }

    interface Building {
        @NotNull String getPatternId();

        int getCorner();

        int getWidth();

        int getHeight();

        int getLevel();

        @Nullable World.Command getCommand();

        interface Pattern {

            @NotNull JSONObject serialize();

            @NotNull String getUniqueId();

            int getMinWidth();

            int getMaxWidth();

            int getMinHeight();

            int getMaxHeight();

            int getMinLevel();

            int getMaxLevel();
        }
    }

    interface Search {
        @NotNull World getWorld();

        @Position int getStart();

        boolean isGoal(@Position int p);

        @Nullable Sequential<@Position Integer> search();
    }

    interface Command {

        void execute(Context context, Adder adder);

        interface Context {
            @NotNull World getWorld();

            @Nullable <T> T getAttribute(String name);

            <T> void setAttribute(String name, T value);
        }

        @FunctionalInterface
        interface Adder {
            void add(Command command);
        }

        class Due implements Command {

            private final long due;
            private final Command command;

            public Due(long due, Command command) {
                this.due = due;
                this.command = command;
            }

            @Override
            public void execute(Context context, Adder adder) {
                if (context.getWorld().getTime() >= due) {
                    command.execute(context, adder);
                } else {
                    adder.add(this);
                }
            }
        }
    }

    class BreadthFirstSearch extends AbstractSearch {
        public BreadthFirstSearch(World world, @Position int start, @Position int goal) {
            super(world, new ArrayQueue<>(world.getSize()), start, goal);
        }
    }

    class DepthFirstSearch extends AbstractSearch {
        public DepthFirstSearch(World world, @Position int start, @Position int goal) {
            super(world, new ArrayStack<>(world.getSize()), start, goal);
        }
    }

    class AStarSearch extends AbstractSearch {
        public AStarSearch(World world, @Position int start, @Position int goal) {
            super(world, new Priority<>((Node n) -> n.getCost() + world.distance(n.getPosition(), goal)), start, goal);
        }
    }
}
