//package ir.smmh.tree.impl;
//
//import ir.smmh.nile.adj.Sequential;
//import ir.smmh.tree.NodedTree;
//import org.jetbrains.annotations.NotNull;
//
//public class TraversedImpl<DataType> implements NodedTree.Traversed<DataType> {
//
//    private final Sequential<Node<DataType>> sequential;
//    private final NodedTree.Traversal<DataType> type;
//    private Sequential<DataType> data;
//
//    public TraversedImpl(Sequential<Node<DataType>> sequential, NodedTree.Traversal<DataType> type) {
//        super();
//        this.sequential = sequential;
//        this.type = type;
//    }
//
//    @Override
//    public final @NotNull Sequential<DataType> getData() {
//        if (data == null) {
//            data = sequential.applyOutOfPlace(Node::getData);
//        }
//        return data;
//    }
//
//    @Override
//    public final @NotNull Sequential<Node<DataType>> getNodes() {
//        return sequential;
//    }
//
//    @Override
//    public final @NotNull NodedTree.Traversal<DataType> getType() {
//        return type;
//    }
//
//    @Override
//    public String toString() {
//        return sequential.toString();
//    }
//}
