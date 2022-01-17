package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import org.jetbrains.annotations.NotNull;

public class TraversedNodesImpl<NodeType> implements NodedTree.TraversedNodes<NodeType> {

    private final Sequential<NodeType> nodes;
    private final NodedTree.NodeTraversal type;

    public TraversedNodesImpl(Sequential<NodeType> nodes, NodedTree.NodeTraversal type) {
        this.nodes = nodes;
        this.type = type;
    }

    @Override
    public @NotNull Sequential<NodeType> getData() {
        return nodes;
    }

    @Override
    public @NotNull NodedTree.NodeTraversal getType() {
        return type;
    }
}
