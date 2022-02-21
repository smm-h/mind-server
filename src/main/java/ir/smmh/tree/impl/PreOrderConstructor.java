package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PreOrderConstructor<DataType> implements Tree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> inOrder, postOrder;
    private NodedBinaryTreeImpl<DataType> tree;

    public PreOrderConstructor(Sequential<DataType> inOrder, Sequential<DataType> postOrder) {
        super();
        this.inOrder = inOrder;
        this.postOrder = postOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getFirstSource() {
        return inOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getSecondSource() {
        return postOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getTarget() {
        return getTree().traverseDataPreOrder();
    }

    @Override
    public final @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            int n = inOrder.getSize();
            assert n == postOrder.getSize();
            tree.setRootNode(makeNode(0, n - 1, 0, n - 1, null));
        }
        return tree;
    }

    private NodedBinaryTreeImpl<DataType>.Node makeNode(int inOrderFirst, int inOrderLast, int postOrderFirst, int postOrderLast, @Nullable NodedBinaryTreeImpl<DataType>.Node parent) {

        // base case
        if (inOrderFirst > inOrderLast)
            return null;

        DataType data = postOrder.getAtIndex(postOrderLast);

        NodedBinaryTreeImpl<DataType>.Node node = tree.new Node(data, parent);

        // if this node has children
        if (inOrderFirst != inOrderLast) {

            // find the index in in-order traversal
            int index = inOrder.findFirst(data, inOrderFirst, inOrderLast + 1);

            // and use it to construct both subtrees
            node.setLeftChild(makeNode(
                    inOrderFirst,
                    index - 1,
                    postOrderFirst,
                    postOrderFirst - inOrderFirst + index - 1,
                    node));

            node.setRightChild(makeNode(
                    index + 1,
                    inOrderLast,
                    postOrderLast - inOrderLast + index,
                    postOrderLast - 1,
                    node));
        }

        return node;
    }
}
