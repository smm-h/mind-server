package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.NodedTree;
import ir.smmh.util.FunctionalUtil;
import ir.smmh.util.Listeners;
import ir.smmh.util.ListenersImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static ir.smmh.util.FunctionalUtil.with;

@ParametersAreNonnullByDefault
public class NodedTreeImpl<DataType> implements NodedTree.Mutable<DataType, NodedTreeImpl<DataType>.Node, NodedTreeImpl<DataType>> {

    private final Listeners<FunctionalUtil.OnEventListener> onPreMutateListeners = new ListenersImpl<>();
    private final Listeners<FunctionalUtil.OnEventListener> onPostMutateListeners = new ListenersImpl<>();
    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> onCleanListeners = new ListenersImpl<>();
    private @Nullable Node root;
    private final CanContain<Node> nodeContainer = new CanContain<>() {
        @Override
        public boolean contains(Node node) {
            return root != null && NodedTreeImpl.this.contains(root, node);
        }

        @Override
        public boolean isEmpty() {
            return NodedTreeImpl.this.isEmpty();
        }
    };

    @Override
    public boolean contains(DataType data) {
        return root != null && contains(root, data);
    }

    private boolean contains(Node root, DataType data) {
        if (root.getData().equals(data)) return true;
        for (NodedTreeImpl<DataType>.Node child : root.getChildren()) {
            if (contains(child, data)) return true;
        }
        return false;
    }

    private boolean contains(Node root, Node node) {
        if (root.equals(node)) return true;
        for (NodedTreeImpl<DataType>.Node child : root.getChildren()) {
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
    public void setRootNode(Node node) {
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
    public NodedTreeImpl<DataType> specificThis() {
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

    class Node implements NodedTree.Mutable.Node<DataType, Node, NodedTreeImpl<DataType>> {
        private final Sequential.Mutable<Node> children = new SequentialImpl<>();
        private DataType data;
        @Nullable
        private Node parent;

        Node(DataType data, @Nullable Node parent) {
            this.data = data;
            this.parent = parent;
        }

        @Override
        public @NotNull NodedTreeImpl<DataType> asTree() {
            NodedTreeImpl<DataType> subtree = new NodedTreeImpl<>();
            subtree.setRootNode(this);
            // handle mutation and/or viewing
            return subtree;
        }

        @Override
        public @NotNull Sequential.Mutable<Node> getChildren() {
            return children;
        }

        @Override
        public int getIndexInParent() {
            return with(parent, p -> p.children.findFirst(this), -1);
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
        public @NotNull NodedTreeImpl<DataType> getTree() {
            return NodedTreeImpl.this;
        }

        @Override
        public Node specificThis() {
            return this;
        }
    }
}
