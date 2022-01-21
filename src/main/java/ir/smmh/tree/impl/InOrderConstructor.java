package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.Tree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InOrderConstructor<DataType> implements Tree.Binary.OrderConstructor<DataType> {

    private final Sequential<DataType> preOrder, postOrder;
    private NodedBinaryTreeImpl<DataType> tree;
    private int size;
    private int preOrderIndex;

    public InOrderConstructor(Sequential<DataType> preOrder, Sequential<DataType> postOrder) {
        super();
        this.preOrder = preOrder;
        this.postOrder = postOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getFirstSource() {
        return preOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getSecondSource() {
        return postOrder;
    }

    @Override
    public final @NotNull Sequential<DataType> getTarget() {
        return getTree().traverseDataInOrder();
    }

    @Override
    public final @NotNull NodedBinaryTreeImpl<DataType> getTree() {
        if (tree == null) {
            tree = new NodedBinaryTreeImpl<>();
            size = preOrder.getSize();
            assert size == postOrder.getSize();
            preOrderIndex = 0;
            tree.setRootNode(makeNode(0, size - 1, null));
        }
        return tree;
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

