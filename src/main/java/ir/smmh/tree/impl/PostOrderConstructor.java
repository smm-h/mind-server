package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import ir.smmh.tree.Tree.TraversedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.smmh.tree.NodedTree.DataTraversal.Binary.IN_ORDER;
import static ir.smmh.tree.NodedTree.DataTraversal.Binary.PRE_ORDER;

public class PostOrderConstructor<DataType> implements NodedTree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> preOrder, inOrder;
    private NodedBinaryTreeImpl<DataType> tree;
    private int preOrderIndex;

    public PostOrderConstructor(Sequential<DataType> preOrder, Sequential<DataType> inOrder) {
        this.preOrder = preOrder;
        this.inOrder = inOrder;
    }

    @Override
    public @NotNull TraversedData<DataType> getFirstSource() {
        return TraversedData.of(preOrder, PRE_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getSecondSource() {
        return TraversedData.of(inOrder, IN_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getTarget() {
        makeTree();
        System.out.println(tree);
        return tree.traverseDataPostOrder();
    }

    @Override
    public @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        makeTree();
        return tree;
    }

    private void makeTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            int n = preOrder.getSize();
            assert n == inOrder.getSize();
            preOrderIndex = 0;
            tree.setRootNode(makeNode(0, n - 1, null));
        }
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

