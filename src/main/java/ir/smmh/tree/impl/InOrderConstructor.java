package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.NodedTree;
import ir.smmh.tree.Tree.TraversedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ir.smmh.tree.NodedTree.DataTraversal.Binary.POST_ORDER;
import static ir.smmh.tree.NodedTree.DataTraversal.Binary.PRE_ORDER;

public class InOrderConstructor<DataType> implements NodedTree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> preOrder, postOrder;
    private NodedBinaryTreeImpl<DataType> tree;
    private int size;
    private int preOrderIndex;

    public InOrderConstructor(Sequential<DataType> preOrder, Sequential<DataType> postOrder) {
        this.preOrder = preOrder;
        this.postOrder = postOrder;
    }

    @Override
    public @NotNull TraversedData<DataType> getFirstSource() {
        return TraversedData.of(preOrder, PRE_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getSecondSource() {
        return TraversedData.of(postOrder, POST_ORDER);
    }

    @Override
    public @NotNull TraversedData<DataType> getTarget() {
        makeTree();
        return tree.traverseDataInOrder();
    }

    @Override
    public @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        makeTree();
        return tree;
    }

    private void makeTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            size = preOrder.getSize();
            assert size == postOrder.getSize();
            preOrderIndex = 0;
            tree.setRootNode(makeNode(0, size - 1, null));
        }
    }

    private NodedBinaryTreeImpl<DataType>.Node makeNode(int start, int end, @Nullable NodedBinaryTreeImpl<DataType>.Node parent) {
        if (preOrderIndex < size && start <= end) {
            NodedBinaryTreeImpl<DataType>.Node node = tree.new Node(preOrder.getAtIndex(preOrderIndex++), parent);
            if (preOrderIndex < size && start != end) {
                int m;
                for (m = start; m <= end; m++) {
                    if (postOrder.getAtIndex(m) == preOrder.getAtIndex(preOrderIndex))
                        break;
                }
                if (m <= end) {
                    node.setLeftChild(makeNode(start, m, node));
                    node.setRightChild(makeNode(m + 1, end - 1, node));
                }
            }
            return node;
        } else {
            return null;
        }
    }
}

