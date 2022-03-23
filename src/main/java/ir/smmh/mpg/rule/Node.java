package ir.smmh.mpg.rule;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Node {
    @Position int getPosition();

    @Nullable Node getParent();

    default int getCost() {
        int cost = 0;
        Node node = this;
        do {
            cost++;
            node = node.getParent();
        } while (node != null);
        return cost;
    }

    default @NotNull Sequential<@Position Integer> getSteps() {
        Sequential.Mutable.VariableSize<@Position Integer> steps = new SequentialImpl<>();
        Node node = this;
        do {
            steps.append(node.getPosition());
            node = node.getParent();
        } while (node != null);
        return Sequential.View.reversed(steps);
    }
}
