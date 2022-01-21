package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
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

@ParametersAreNonnullByDefault
public class NodedTreeImpl<DataType> implements NodedTree.Mutable<DataType, NodedTreeImpl<DataType>.Node,
        NodedTreeImpl<DataType>>, Mutable.WithListeners {
    private final Listeners<FunctionalUtil.OnEventListener> onPreMutateListeners = ListenersImpl.blank();
    private final Listeners<FunctionalUtil.OnEventListener> onPostMutateListeners = ListenersImpl.blank();
    private final Listeners<FunctionalUtil.OnEventListenerWithException<CleaningException>> onCleanListeners = ListenersImpl.blank();
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

        @Override
        public int getSize() {
            return NodedTreeImpl.this.getSize();
        }
    };

    @Override
    public final boolean contains(DataType data) {
        return root != null && contains(root, data);
    }

    private boolean contains(Node root, @Nullable DataType data) {
        if (Objects.equals(root.getData(), data)) return true;
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
    public final NodedTreeImpl<DataType> specificThis() {
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
        return null; // TODO serialize tree
    }

    class Node implements NodedTree.Mutable.Node<DataType, Node, NodedTreeImpl<DataType>> {
        private final Sequential.Mutable<Node> children = new SequentialImpl<>();
        private @Nullable DataType data;
        private @Nullable Node parent;

        Node(DataType data, @Nullable Node parent) {
            super();
            this.data = data;
            this.parent = parent;
        }

        final Node makeNode(DataType data) {
            return new Node(data, this);
        }

        @Override
        public final @NotNull NodedTreeImpl<DataType> asTree() {
            NodedTreeImpl<DataType> subtree = new NodedTreeImpl<>();
            subtree.setRootNode(this);
            // handle mutation and/or viewing
            return subtree;
        }

        @Override
        public final @NotNull Sequential.Mutable<Node> getChildren() {
            return children;
        }

        @Override
        public final int getIndexInParent() {
            return with(parent, p -> p.children.findFirst(this), -1);
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
        public final @NotNull NodedTreeImpl<DataType> getTree() {
            return NodedTreeImpl.this;
        }

        @Override
        public final Node specificThis() {
            return this;
        }
    }
}
