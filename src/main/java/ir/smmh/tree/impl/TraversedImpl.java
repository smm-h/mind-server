package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import org.jetbrains.annotations.NotNull;

public class TraversedImpl<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> implements NodedTree.Traversed<DataType, NodeType, TreeType> {

    private final Sequential<NodeType> sequential;
    private Sequential<DataType> data;
    private final NodedTree.Traversal type;

    public TraversedImpl(Sequential<NodeType> sequential, NodedTree.Traversal type) {
        this.sequential = sequential;
        this.type = type;
    }

    @Override
    public @NotNull Sequential<DataType> getData() {
        if (data == null) {
            data = sequential.applyOutOfPlace(NodeType::getData);
        }
        return data;
    }

    @Override
    public @NotNull Sequential<NodeType> getNodes() {
        return sequential;
    }

    @Override
    public @NotNull NodedTree.Traversal getType() {
        return type;
    }
}
