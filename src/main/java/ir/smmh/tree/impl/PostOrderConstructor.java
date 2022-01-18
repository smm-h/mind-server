package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PostOrderConstructor<DataType> implements Tree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> preOrder, inOrder;
    private NodedBinaryTreeImpl<DataType> tree;
    private int preOrderIndex;

    public PostOrderConstructor(Sequential<DataType> preOrder, Sequential<DataType> inOrder) {
        this.preOrder = preOrder;
        this.inOrder = inOrder;
    }

    @Override
    public @NotNull Sequential<DataType> getFirstSource() {
        return preOrder;
    }

    @Override
    public @NotNull Sequential<DataType> getSecondSource() {
        return inOrder;
    }

    @Override
    public @NotNull Sequential<DataType> getTarget() {
        return getTree().traverseDataPostOrder();
    }

    @Override
    public @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            int n = preOrder.getSize();
            assert n == inOrder.getSize();
            preOrderIndex = 0;
            tree.setRootNode(makeNode(0, n - 1, null));
        }
        return tree;
    }

    private NodedBinaryTreeImpl<DataType>.Node makeNode(int start, int end, @Nullable NodedBinaryTreeImpl<DataType>.Node parent) {

        if (start > end)
            return null;

        DataType data = preOrder.getAtIndex(preOrderIndex++);
        int m = inOrder.findFirst(data, start, end + 1); // TODO optimize this search with a lookup

        NodedBinaryTreeImpl<DataType>.Node node = tree.new Node(data, parent);

        node.setLeftChild(makeNode(start, m - 1, node));
        node.setRightChild(makeNode(m + 1, end, node));

        return node;
    }
}

