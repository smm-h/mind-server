package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import org.jetbrains.annotations.NotNull;

public class TraversedImpl<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> implements NodedTree.Traversed<DataType, NodeType, TreeType> {

    private final Sequential<NodeType> sequential;
    private final NodedTree.Traversal<DataType, NodeType, TreeType> type;
    private Sequential<DataType> data;

    public TraversedImpl(Sequential<NodeType> sequential, NodedTree.Traversal<DataType, NodeType, TreeType> type) {
        super();
        this.sequential = sequential;
        this.type = type;
    }

    @Override
    public final @NotNull Sequential<DataType> getData() {
        if (data == null) {
            data = sequential.applyOutOfPlace(NodeType::getData);
        }
        return data;
    }

    @Override
    public final @NotNull Sequential<NodeType> getNodes() {
        return sequential;
    }

    @Override
    public final @NotNull NodedTree.Traversal<DataType, NodeType, TreeType> getType() {
        return type;
    }

    @Override
    public String toString() {
        return sequential.toString();
    }
}
