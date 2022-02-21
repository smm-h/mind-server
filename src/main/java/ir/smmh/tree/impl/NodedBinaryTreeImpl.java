package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.DoubleSequence;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.NodedTree;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import ir.smmh.util.Mutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class NodedBinaryTreeImpl<DataType> implements NodedTree.Binary.Mutable<DataType,
        NodedBinaryTreeImpl<DataType>.Node, NodedBinaryTreeImpl<DataType>>, Mutable.WithListeners {
    private final Listeners<FunctionalUtil.OnEventListener> onPreMutateListeners = ListenersImpl.blank();
    private final Listeners<FunctionalUtil.OnEventListener> onPostMutateListeners = ListenersImpl.blank();
    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> onCleanListeners = ListenersImpl.blank();
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

    @Override
    public final String toString() {
        return with(root, Node::nodeToString, "{empty}");
    }

    @Override
    public final boolean contains(DataType data) {
        return root != null && contains(root, data);
    }

    private boolean contains(Node root, DataType data) {
        if (Objects.equals(root.getData(), data)) return true;
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
    public final boolean isEmpty() {
        return root == null;
    }

    @Override
    public final @NotNull CanContain<Node> nodes() {
        return nodeContainer;
    }

    @Override
    public final @Nullable Node getRootNode() {
        return root;
    }

    @Override
    public final void setRootNode(@Nullable Node node) {
        root = node;
    }

    @Override
    public final DataType getRootData() {
        return root == null ? null : root.getData();
    }

    @Override
    public final void setRootData(DataType data) {
        setRootNode(new Node(data, null));
    }

    @Override
    public final NodedBinaryTreeImpl<DataType> specificThis() {
        return this;
    }

    @Override
    public final boolean isDirty() {
        return false;
    }

    @Override
    public void setDirty(boolean dirty) {

    }

    @Override
    public final @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPreMutateListeners() {
        return onPreMutateListeners;
    }

    @Override
    public final @NotNull Listeners<FunctionalUtil.OnEventListener> getOnPostMutateListeners() {
        return onPostMutateListeners;
    }

    @Override
    public final @NotNull Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> getOnCleanListeners() {
        return onCleanListeners;
    }

    @Override
    public final @NotNull String serialize() {
        return null; // TODO serialize binary tree
    }

    public class Node implements NodedTree.Binary.Mutable.Node<DataType, Node, NodedBinaryTreeImpl<DataType>> {

        private Node leftChild, rightChild;
        private @Nullable DataType data;

        private @Nullable Node parent;

        public Node(@Nullable DataType data, @Nullable Node parent) {
            super();
            this.data = data;
            this.parent = parent;
        }

        @Override
        public final String toString() {
            return "<" + data + ">[" + leftChild + "|" + rightChild + "]";
        }

        private String nodeToString() {
            Node l = getLeftChild();
            Node r = getRightChild();
            String s = with(getData(), Object::toString, "~");
            if (l != null || r != null)
                s += ":(" +
                        (l == null ? "-" : l.nodeToString()) + ", " +
                        (r == null ? "-" : r.nodeToString()) + ")";
            return s;
        }

        @Override
        public final @NotNull NodedBinaryTreeImpl<DataType> asTree() {
            NodedBinaryTreeImpl<DataType> subtree = new NodedBinaryTreeImpl<>();
            subtree.setRootNode(this);
            // handle mutation and/or viewing
            return subtree;
        }

        @Override
        public final @NotNull Sequential.Mutable<Node> getChildren() {
            return new DoubleSequence<>(leftChild, rightChild);
        }

        @Override
        public final int getIndexInParent() {
            if (parent != null) {
                if (this == parent.leftChild)
                    return 0;
                if (this == parent.rightChild)
                    return 1;
            }
            return -1;
        }

        @Override
        public final @Nullable DataType getData() {
            return data;
        }

        @Override
        public final void setData(DataType data) {
            this.data = data;
        }

        @Override
        public final @Nullable Node getParent() {
            return parent;
        }

        @Override
        public final void setParent(Node parent) {
            this.parent = parent;
        }

        @Override
        public final @NotNull NodedBinaryTreeImpl<DataType> getTree() {
            return NodedBinaryTreeImpl.this;
        }

        @Override
        public final Node specificThis() {
            return this;
        }

        @Override
        public final @Nullable Node getLeftChild() {
            return leftChild;
        }

        @Override
        public final void setLeftChild(@Nullable Node leftChild) {
            this.leftChild = leftChild;
        }

        @Override
        public final @Nullable Node getRightChild() {
            return rightChild;
        }

        @Override
        public final void setRightChild(@Nullable Node rightChild) {
            this.rightChild = rightChild;
        }
    }
}
