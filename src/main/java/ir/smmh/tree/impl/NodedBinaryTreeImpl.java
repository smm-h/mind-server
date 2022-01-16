package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.BinarySequentialImpl;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.NodedTree;
import ir.smmh.util.ArrayUtil;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class NodedBinaryTreeImpl<DataType> implements NodedTree.Binary.Mutable<DataType, NodedBinaryTreeImpl<DataType>.Node, NodedBinaryTreeImpl<DataType>> {
    private final Listeners<FunctionalUtil.OnEventListener> onPreMutateListeners = new ListenersImpl<>();
    private final Listeners<FunctionalUtil.OnEventListener> onPostMutateListeners = new ListenersImpl<>();
    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> onCleanListeners = new ListenersImpl<>();
    private @Nullable Node root;
    private final CanContain<Node> nodeContainer = new CanContain<>() {
        @Override
        public boolean contains(Node node) {
            return root != null && NodedBinaryTreeImpl.this.contains(root, node);
        }

        @Override
        public boolean isEmpty() {
            return NodedBinaryTreeImpl.this.isEmpty();
        }

        @Override
        public int getSize() {
            return NodedBinaryTreeImpl.this.getSize();
        }
    };

    public static <DataType> NodedBinaryTreeImpl<DataType> fromInOrderAndPostOrder(Sequential<DataType> inOrder, Sequential<DataType> postOrder) {
        NodedBinaryTreeImpl<DataType> tree = new NodedBinaryTreeImpl<>();
        int n = inOrder.getSize();
        tree.setRootNode(tree.fromInOrderAndPostOrder(inOrder.toArray(), postOrder.toArray(), 0, n - 1, 0, n - 1, null));
        return tree;
    }

    public static <DataType> NodedBinaryTreeImpl<DataType> fromInOrderAndPostOrder(DataType[] inOrder, DataType[] postOrder) {
        NodedBinaryTreeImpl<DataType> tree = new NodedBinaryTreeImpl<>();
        int n = inOrder.length;
        tree.setRootNode(tree.fromInOrderAndPostOrder(inOrder, postOrder, 0, n - 1, 0, n - 1, null));
        return tree;
    }

    @Override
    public String toString() {
        return with(root, Node::nodeToString, "{empty}");
    }

    private Node fromInOrderAndPostOrder(DataType[] inOrder, DataType[] postOrder, int inOrderFirst, int inOrderLast, int postOrderFirst, int postOrderLast, @Nullable Node parent) {

        // base case
        if (inOrderFirst > inOrderLast)
            return null;

        Node node = new Node(postOrder[postOrderLast], parent);

        // if this node has children
        if (inOrderFirst != inOrderLast) {

            // find the index in in-order traversal
            int index = ArrayUtil.getIndexOf(inOrder, node.getData(), inOrderFirst, inOrderLast + 1);

            // and use it to construct both subtrees
            node.setLeftChild(fromInOrderAndPostOrder(inOrder, postOrder,
                    inOrderFirst,
                    index - 1,
                    postOrderFirst,
                    postOrderFirst - inOrderFirst + index - 1,
                    node));

            node.setRightChild(fromInOrderAndPostOrder(inOrder, postOrder,
                    index + 1,
                    inOrderLast,
                    postOrderLast - inOrderLast + index,
                    postOrderLast - 1,
                    node));
        }

        return node;
    }

    @Override
    public boolean contains(DataType data) {
        return root != null && contains(root, data);
    }

    private boolean contains(Node root, DataType data) {
        if (root.getData().equals(data)) return true;
        for (NodedBinaryTreeImpl<DataType>.Node child : root.getChildren()) {
            if (contains(child, data)) return true;
        }
        return false;
    }

    private boolean contains(Node root, Node node) {
        if (root.equals(node)) return true;
        for (NodedBinaryTreeImpl<DataType>.Node child : root.getChildren()) {
            if (contains(child, node)) return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public @NotNull CanContain<Node> nodes() {
        return nodeContainer;
    }

    @Override
    public @Nullable Node getRootNode() {
        return root;
    }

    @Override
    public void setRootNode(@Nullable Node node) {
        this.root = node;
    }

    @Override
    public DataType getRootData() {
        return root == null ? null : root.getData();
    }

    @Override
    public void setRootData(DataType data) {
        setRootNode(new Node(data, null));
    }

    @Override
    public NodedBinaryTreeImpl<DataType> specificThis() {
        return this;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void setDirty(boolean dirty) {

    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners() {
        return onPreMutateListeners;
    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners() {
        return onPostMutateListeners;
    }

    @Override
    public @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners() {
        return onCleanListeners;
    }

    @Override
    public @NotNull String serialize() {
        return null;
    }

    class Node implements NodedTree.Binary.Mutable.Node<DataType, Node, NodedBinaryTreeImpl<DataType>> {

        private Node leftChild, rightChild;
        private DataType data;

        private @Nullable Node parent;


        Node(DataType data, @Nullable Node parent) {
            this.data = data;
            this.parent = parent;
        }

        private String nodeToString() {
            Node l = getLeftChild();
            Node r = getRightChild();
            String s = getData().toString();
            if (l != null || r != null)
                s += ":(" +
                        (l == null ? "-" : l.nodeToString()) + ", " +
                        (r == null ? "-" : r.nodeToString()) + ")";
            return s;
        }

        @Override
        public @NotNull NodedBinaryTreeImpl<DataType> asTree() {
            NodedBinaryTreeImpl<DataType> subtree = new NodedBinaryTreeImpl<>();
            subtree.setRootNode(this);
            // handle mutation and/or viewing
            return subtree;
        }

        @Override
        public @NotNull Sequential<Node> getChildren() {
            return new BinarySequentialImpl<>(leftChild, rightChild);
        }

        @Override
        public int getIndexInParent() {
            if (parent != null) {
                if (this == parent.leftChild)
                    return 0;
                if (this == parent.rightChild)
                    return 1;
            }
            return -1;
        }

        @Override
        public @NotNull DataType getData() {
            return data;
        }

        @Override
        public void setData(DataType data) {
            this.data = data;
        }

        @Override
        public @Nullable Node getParent() {
            return parent;
        }

        @Override
        public void setParent(Node parent) {
            this.parent = parent;
        }

        @Override
        public @NotNull NodedBinaryTreeImpl<DataType> getTree() {
            return NodedBinaryTreeImpl.this;
        }

        @Override
        public Node specificThis() {
            return this;
        }

        @Override
        public @Nullable Node getLeftChild() {
            return leftChild;
        }

        @Override
        public void setLeftChild(@Nullable Node leftChild) {
            this.leftChild = leftChild;
        }

        @Override
        public @Nullable Node getRightChild() {
            return rightChild;
        }

        @Override
        public void setRightChild(@Nullable Node rightChild) {
            this.rightChild = rightChild;
        }
    }
}
