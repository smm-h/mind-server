package ir.smmh.mpg.rule.impl;

import ir.smmh.mpg.rule.Node;
import ir.smmh.mpg.rule.Position;
import ir.smmh.mpg.rule.World;
import ir.smmh.nile.adj.Order;
import ir.smmh.nile.adj.Sequential;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSearch implements World.Search {
    private final World world;
    private final @NotNull Set<@Position Integer> goals = new HashSet<>();
    private final Order<Node> fringe;
    private final @Position int start;

    private AbstractSearch(World world, Order<Node> fringe, @Position int start) {
        this.world = world;
        this.fringe = fringe;
        this.start = start;
    }

    public AbstractSearch(World world, Order<Node> fringe, @Position int start, @Position int goal) {
        this(world, fringe, start);
        this.goals.add(goal);
    }

    public AbstractSearch(World world, Order<Node> fringe, @Position int start, Iterable<@Position Integer> goals) {
        this(world, fringe, start);
        for (@Position int goal : goals)
            this.goals.add(goal);
    }

    @Override
    public @NotNull World getWorld() {
        return world;
    }

    @Override

    public @Position int getStart() {
        return start;
    }

    @Override
    public boolean isGoal(@Position int p) {
        return goals.contains(p);
    }

    @Override
    public @Nullable Sequential<@Position Integer> search() {
        fringe.clear();
        final Set<@Position Integer> explored = new HashSet<>();
        @Position Node pointer = new NodeImpl(start, null);
        while (true) {
            int pp = pointer.getPosition();
            if (isGoal(pp)) {
                return pointer.getSteps();
            } else {
                if (!explored.contains(pp)) {
                    explored.add(pp);
                    for (@Position int p : getWorld().nextMoves(pp)) {
                        if (!explored.contains(p)) {
                            fringe.enter(new NodeImpl(p, pointer));
                        }
                    }
                }
                Node poll = fringe.poll();
                if (poll == null) {
                    return null;
                } else {
                    pointer = poll;
                }
            }
        }
    }

    private static class NodeImpl implements Node {
        private final @Position int position;
        private final Node parent;

        private NodeImpl(int position, @Nullable Node parent) {
            this.position = position;
            this.parent = parent;
        }

        @Override
        public @Position int getPosition() {
            return position;
        }

        @Override
        public @Nullable Node getParent() {
            return parent;
        }
    }
}
