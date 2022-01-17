package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import ir.smmh.tree.Tree.TraversedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.smmh.tree.NodedTree.DataTraversal.Binary.IN_ORDER;
import static ir.smmh.tree.NodedTree.DataTraversal.Binary.POST_ORDER;

public class PreOrderConstructor<DataType> implements NodedTree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> inOrder, postOrder;
    private NodedBinaryTreeImpl<DataType> tree;

    public PreOrderConstructor(Sequential<DataType> inOrder, Sequential<DataType> postOrder) {
        this.inOrder = inOrder;
        this.postOrder = postOrder;
    }


    @Override
    public @NotNull TraversedData<DataType> getFirstSource() {
        return TraversedData.of(inOrder, IN_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getSecondSource() {
        return TraversedData.of(postOrder, POST_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getTarget() {
        makeTree();
        return tree.traverseDataPreOrder();
    }

    @Override
    public @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        makeTree();
        return tree;
    }

    private void makeTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            int n = inOrder.getSize();
            assert n == postOrder.getSize();
            tree.setRootNode(makeNode(0, n - 1, 0, n - 1, null));
        }
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
