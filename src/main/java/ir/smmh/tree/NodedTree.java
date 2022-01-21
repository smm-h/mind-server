package ir.smmh.tree;

import ir.smmh.nile.adj.Order;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.FIFO;
import ir.smmh.nile.adj.impl.LIFO;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanContain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static ir.smmh.util.FunctionalUtil.with;

/**
 * A {@code NodedTree} is a {@link Tree} that wraps a single {@link Node} called
 * its "root", to which it delegates most methods required by {@link Tree}.
 */
@SuppressWarnings("unused")
public interface NodedTree<DataType> extends Tree<DataType> {

    @NotNull CanContain<Node<DataType>> nodes();

    @Nullable Node<DataType> getRootNode();

    // TODO filter/prune, in-place/out-of-place, boolean leaf-only

    default <OtherDataType> @NotNull NodedTree<OtherDataType> applyOutOfPlace(Function<? super DataType, ? extends OtherDataType> toApply) {
        NodedTree<OtherDataType> otherTree = new NodedTreeImpl<>();
        Node<DataType> rootNode = getRootNode();
        if (getRootNode() != null) {
            applyOutOfPlace(otherTree, getRootNode(), null, toApply);
        }
        return otherTree;
    }

    default <OtherDataType> void applyOutOfPlace(NodedTree<OtherDataType> otherTree, Node<DataType> node, Node<OtherDataType> parent, Function<? super DataType, ? extends OtherDataType> toApply) {
        otherTree.new Node(toApply.apply(node.getData()), parent);
    }

    default @NotNull Traversed<DataType> traverse(@NotNull Traversal<DataType> type) {
        return with(getRootNode(), type::traverse, Traversed.empty(type));
    }

    default @NotNull Traversed<DataType> traverseLeafOnly() {
        return traverse(new Traversal.LeafOnly<>());
    }

    default @NotNull Sequential<Node<DataType>> getLeafNodes() {
        return traverseLeafOnly().getNodes();
    }

    @Override
    default @NotNull Sequential<DataType> getLeafData() {
        return traverseLeafOnly().getData();
    }

    @Override
    default @NotNull Sequential<DataType> getBreadthFirstData() {
        return traverseBreadthFirst().getData();
    }

    @Override
    default @NotNull Sequential<DataType> getDepthFirstData() {
        return traverseDepthFirst().getData();
    }

    default @NotNull Traversed<DataType> traverseBreadthFirst() {
        return traverse((Traversal.ByOrder<DataType>) FIFO::new);
    }

    default @NotNull Traversed<DataType> traverseDepthFirst() {
        return traverse((Traversal.ByOrderReverseChildren<DataType>) LIFO::new);
    }

    @Override
    default int getDegree() {
        return with(getRootNode(), Node::getDegree, 0);
    }

    @Override
    default int getHeight() {
        return with(getRootNode(), Node::getHeight, -1);
    }

    @Override
    default int getSize() {
        return with(getRootNode(), Node::getCount, 0);
    }

    @Override
    default int getLeafCount() {
        return with(getRootNode(), Node::getLeafCount, 0);
    }

    @Override
    default @NotNull Sequential<Tree<DataType>> getImmediateSubtrees() {
        return with(getRootNode(), Node::getImmediateSubtrees, Sequential.empty());
    }

    interface Mutable<DataType> extends NodedTree<DataType>, Tree.Mutable<DataType> {

        default void replaceData(Function<? super DataType, ? extends DataType> toReplace) {
            preMutate();
            for (NodedTree.Mutable.Node<DataType> node : traverseBreadthFirst().getNodes()) {
                node.replaceData(toReplace);
            }
            postMutate();
        }

        default void mutateData(Consumer<DataType> toApply) {
            preMutate();
            for (Node<DataType> node : traverseBreadthFirst().getNodes()) {
                node.mutateData(toApply);
            }
            postMutate();
        }

        @Override
        default void clear() {
            setRootNode(null);
        }

        void setRootNode(@Nullable Node<DataType> node);

        interface Node<DataType> extends NodedTree.Node<DataType> {
            @Override
            @NotNull Sequential.Mutable<Node<DataType>> getChildren();

            default void replaceData(Function<? super DataType, ? extends DataType> toReplace) {
                setData(toReplace.apply(getData()));
            }

            default void mutateData(Consumer<DataType> toApply) {
                toApply.accept(getData());
            }
        }
    }

    interface Node<DataType> {

        default boolean isLeaf() {
            return getChildren().isEmpty();
        }

        default @NotNull Sequential<Node<DataType>> getSiblings() {
            return Sequential.View.allButOne(with(getParent(), Node::getChildren, Sequential.empty()), getIndexInParent());
        }

        @NotNull Tree<DataType> asTree();

        default @NotNull Sequential<Tree<DataType>> getImmediateSubtrees() {
            Sequential.Mutable.VariableSize<Tree<DataType>> subtrees = new SequentialImpl<>();
            for (Node<DataType> child : getChildren()) {
                subtrees.append(child.asTree());
            }
            return subtrees;
        }

        @NotNull Sequential<Node<DataType>> getChildren();

        int getIndexInParent();

        @Nullable
        DataType getData();

        void setData(DataType data);

        default boolean hasData() {
            return getData() != null;
        }

        @Nullable
        Node<DataType> getParent();

        void setParent(Node<DataType> parent);

        @NotNull
        Tree<DataType> getTree();

        default int getDegree() {
            if (getChildren().isEmpty()) {
                return 0;
            } else {
                int degree = getChildren().getSize();
                for (Node<DataType> child : getChildren()) {
                    degree = Math.max(degree, with(child, Node::getDegree, 0));
                }
                return degree;
            }
        }

        default int getHeight() {
            if (getChildren().isEmpty()) {
                return 0;
            } else {
                int height = 0;
                for (Node<DataType> child : getChildren()) {
                    height = Math.max(height, with(child, Node::getHeight, 0));
                }
                return height + 1;
            }
        }

        default int getCount() {
            if (getChildren().isEmpty()) {
                return 1;
            } else {
                int count = 0;
                for (Node<DataType> child : getChildren()) {
                    count += with(child, Node::getCount, 0);
                }
                return count;
            }
        }

        default int getLeafCount() {
            if (getChildren().isEmpty()) {
                return 1;
            } else {
                int leafCount = 0;
                for (Node<DataType> child : getChildren()) {
                    leafCount += with(child, Node::getLeafCount, 0);
                }
                return leafCount;
            }
        }
    }

    interface Binary<DataType> extends NodedTree<DataType>, Tree.Binary<DataType> {
        @Override
        default int getDegree() {
            return Tree.Binary.super.getDegree();
        }

        default @NotNull Traversed<DataType> traversePreOrder() {
            return traverse(new Traversal.Binary.PreOrder<>());
        }

        default @NotNull Traversed<DataType> traverseInOrder() {
            return traverse(new Traversal.Binary.InOrder<>());
        }

        default @NotNull Traversed<DataType> traversePostOrder() {
            return traverse(new Traversal.Binary.PostOrder<>());
        }

        @Override
        default @NotNull Sequential<DataType> traverseDataPreOrder() {
            return traversePreOrder().getData();
        }

        @Override
        default @NotNull Sequential<DataType> traverseDataInOrder() {
            return traverseInOrder().getData();
        }

        @Override
        default @NotNull Sequential<DataType> traverseDataPostOrder() {
            return traversePostOrder().getData();
        }

        interface Mutable<DataType> extends NodedTree.Binary<DataType>, NodedTree.Mutable<DataType>, Tree.Binary.Mutable<DataType> {

            interface Node<DataType> extends NodedTree.Binary.Node<DataType>, NodedTree.Mutable.Node<DataType> {
                void setLeftChild(@Nullable Node<DataType> leftChild);

                void setRightChild(@Nullable Node<DataType> rightChild);
            }
        }

        interface Node<DataType> extends NodedTree.Node<DataType> {
            @Nullable Node<DataType> getLeftChild();

            @Nullable Node<DataType> getRightChild();
        }
    }

    interface Traversal<DataType> {
        @NotNull Traversed<DataType> traverse(@NotNull Node<DataType> root);

        /**
         * TODO DOC
         * Conditional implies testing occurs before adding
         * Filter implies testing occurs after adding
         */
        interface Conditional<DataType> extends Traversal<DataType> {

            @Override
            default @NotNull Traversed<DataType> traverse(@NotNull Node<DataType> root) {
                Sequential.Mutable.VariableSize<Node<DataType>> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                fillNodes(root, seq, getCondition());
                return Traversed.of(seq, this);
            }

            @NotNull Predicate<Node<DataType>> getCondition();

            default void fillNodes(Node<DataType> node, CanAppendTo<? super Node<DataType>> canAppendTo, Predicate<? super Node<DataType>> condition) {
                if (node == null) return;
                if (condition.test(node)) {
                    canAppendTo.add(node);
                }
                for (Node<DataType> child : node.getChildren()) {
                    fillNodes(child, canAppendTo, condition);
                }
            }
        }

        interface Binary<DataType> extends Traversal<DataType> {
            @Override
            default @NotNull Traversed<DataType> traverse(@NotNull Node<DataType> root) {
                Sequential.Mutable.VariableSize<Node<DataType>> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                assert root.getDegree() <= 2;
                fillData(root, seq);
                return Traversed.of(seq, this);
            }

            void fillData(Node<DataType> node, CanAppendTo<? super Node<DataType>> canAppendTo);

            class PreOrder<DataType> implements Binary<DataType> {
                @Override
                public final void fillData(Node<DataType> node, CanAppendTo<? super Node<DataType>> canAppendTo) {
                    if (node == null) return;
                    canAppendTo.add(node);
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                }
            }

            class InOrder<DataType> implements Binary<DataType> {
                @Override
                public final void fillData(Node<DataType> node, CanAppendTo<? super Node<DataType>> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    canAppendTo.add(node);
                    fillData(node.getRightChild(), canAppendTo);
                }
            }

            class PostOrder<DataType> implements Binary<DataType> {
                @Override
                public final void fillData(Node<DataType> node, CanAppendTo<? super Node<DataType>> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                    canAppendTo.add(node);
                }
            }
        }

        @FunctionalInterface
        interface ByOrder<DataType> extends Traversal<DataType> {

            @Override
            default @NotNull Traversed<DataType> traverse(@NotNull Node<DataType> root) {
                Sequential.Mutable.VariableSize<Node<DataType>> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                Order<Node<DataType>> order = makeOrder(root.getCount());
                order.enter(root);
                while (true) {
                    Node<DataType> node = order.poll();
                    if (node == null) break;
                    seq.append(node);
                    for (Node<DataType> child : node.getChildren()) {
                        order.enter(child);
                    }
                }
                return Traversed.of(seq, this);
            }

            Order<Node<DataType>> makeOrder(int capacity);
        }

        @FunctionalInterface
        interface ByOrderReverseChildren<DataType> extends Traversal<DataType> {

            @Override
            default @NotNull Traversed<DataType> traverse(@NotNull Node<DataType> root) {
                Sequential.Mutable.VariableSize<Node<DataType>> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                Order<Node<DataType>> order = makeOrder(root.getCount());
                order.enter(root);
                while (true) {
                    Node<DataType> node = order.poll();
                    if (node == null) break;
                    seq.append(node);
                    for (Node<DataType> child : node.getChildren().inReverse()) {
                        order.enter(child);
                    }
                }
                return Traversed.of(seq, this);
            }

            Order<Node<DataType>> makeOrder(int capacity);
        }

        class LeafOnly<DataType> implements Conditional<DataType> {
            @Override
            public final @NotNull Predicate<Node<DataType>> getCondition() {
                return Node::isLeaf;
            }
        }

        class NonLeafOnly<DataType> implements Conditional<DataType> {
            @Override
            public final @NotNull Predicate<Node<DataType>> getCondition() {
                return Predicate.not(Node::isLeaf);
            }
        }

        class HasDataOnly<DataType> implements Conditional<DataType> {
            @Override
            public final @NotNull Predicate<Node<DataType>> getCondition() {
                return Node::hasData;
            }
        }
    }

    interface Traversed<DataType> {
        static <DataType> Traversed<DataType> empty(Traversal<DataType> type) {
            //noinspection Convert2Diamond
            return new TraversedImpl<DataType>(Sequential.empty(), type);
        }

        static <DataType> Traversed<DataType> of(Sequential<Node<DataType>> sequential, Traversal<DataType> type) {
            return new TraversedImpl<>(sequential, type);
        }

        @NotNull Sequential<Node<DataType>> getNodes();

        @NotNull Sequential<DataType> getData();

        @NotNull NodedTree.Traversal<DataType> getType();
    }
}