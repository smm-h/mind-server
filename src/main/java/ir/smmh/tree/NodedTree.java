package ir.smmh.tree;

import ir.smmh.nile.adj.Order;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.FIFO;
import ir.smmh.nile.adj.impl.LIFO;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.impl.TraversedImpl;
import ir.smmh.util.FunctionalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Predicate;

import static ir.smmh.util.FunctionalUtil.with;

/**
 * A {@code NodedTree} is a {@link SpecificTree} and a wrapper over a single {@link Node} called
 * its "root", to which it delegates most methods required by {@link Tree}.
 */
@SuppressWarnings("unused")
public interface NodedTree<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends SpecificTree<DataType, TreeType> {

    @NotNull CanContain<NodeType> nodes();

    @Nullable NodeType getRootNode();

    default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull Traversal<DataType, NodeType, TreeType> type) {
        return with(getRootNode(), type::traverse, Traversed.empty(type));
    }

    default @NotNull Traversed<DataType, NodeType, TreeType> traverseLeafOnly() {
        return traverse(new Traversal.LeafOnly<>());
    }


    default @NotNull Sequential<NodeType> getLeafNodes() {
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

    default @NotNull Traversed<DataType, NodeType, TreeType> traverseBreadthFirst() {
        return traverse((Traversal.ByOrder<DataType, NodeType, TreeType>) FIFO::new);
    }

    default @NotNull Traversed<DataType, NodeType, TreeType> traverseDepthFirst() {
        return traverse((Traversal.ByOrderReverseChildren<DataType, NodeType, TreeType>) LIFO::new);
    }

    @Override
    default int getDegree() {
        return with(getRootNode(), NodeType::getDegree, 0);
    }

    @Override
    default int getHeight() {
        return with(getRootNode(), NodeType::getHeight, -1);
    }

    @Override
    default int getSize() {
        return with(getRootNode(), NodeType::getCount, 0);
    }

    @Override
    default int getLeafCount() {
        return with(getRootNode(), NodeType::getLeafCount, 0);
    }

    @Override
    default @NotNull Sequential<TreeType> getImmediateSubtrees() {
        return with(getRootNode(), Node::getImmediateSubtrees, Sequential.empty());
    }

    interface Mutable<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends Mutable<DataType, NodeType, TreeType>> extends NodedTree<DataType, NodeType, TreeType>, SpecificTree.Mutable<DataType, TreeType> {

        @Override
        default void clear() {
            setRootNode(null);
        }

        void setRootNode(@Nullable NodeType node);

        interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Mutable<DataType, NodeType, TreeType>> extends NodedTree.Node<DataType, NodeType, TreeType> {
            @Override
            @NotNull Sequential.Mutable<NodeType> getChildren();
        }
    }

    interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends FunctionalUtil.RecursivelySpecific<NodeType> {

        default boolean isLeaf() {
            return getChildren().isEmpty();
        }

        default @NotNull Sequential<NodeType> getSiblings() {
            return Sequential.View.allButOne(with(getParent(), Node::getChildren, Sequential.empty()), getIndexInParent());
        }

        @NotNull TreeType asTree();

        default @NotNull Sequential<TreeType> getImmediateSubtrees() {
            Sequential.Mutable.VariableSize<TreeType> subtrees = new SequentialImpl<>();
            for (NodeType child : getChildren()) {
                subtrees.append(child.asTree());
            }
            return subtrees;
        }

        @NotNull Sequential<NodeType> getChildren();

        int getIndexInParent();

        @Nullable
        DataType getData();

        void setData(DataType data);

        default boolean hasData() {
            return getData() != null;
        }

        @Nullable
        NodeType getParent();

        void setParent(NodeType parent);

        @NotNull
        TreeType getTree();

        default int getDegree() {
            if (getChildren().isEmpty()) {
                return 0;
            } else {
                int degree = getChildren().getSize();
                for (NodeType child : getChildren()) {
                    degree = Math.max(degree, with(child, NodeType::getDegree, 0));
                }
                return degree;
            }
        }

        default int getHeight() {
            if (getChildren().isEmpty()) {
                return 0;
            } else {
                int height = 0;
                for (NodeType child : getChildren()) {
                    height = Math.max(height, with(child, NodeType::getHeight, 0));
                }
                return height + 1;
            }
        }

        default int getCount() {
            if (getChildren().isEmpty()) {
                return 1;
            } else {
                int count = 0;
                for (NodeType child : getChildren()) {
                    count += with(child, NodeType::getCount, 0);
                }
                return count;
            }
        }

        default int getLeafCount() {
            if (getChildren().isEmpty()) {
                return 1;
            } else {
                int leafCount = 0;
                for (NodeType child : getChildren()) {
                    leafCount += with(child, NodeType::getLeafCount, 0);
                }
                return leafCount;
            }
        }
    }

    interface Binary<DataType, NodeType extends Binary.Node<DataType, NodeType, TreeType>, TreeType extends Binary<DataType, NodeType, TreeType>> extends NodedTree<DataType, NodeType, TreeType>, SpecificTree.Binary<DataType, TreeType> {
        @Override
        default int getDegree() {
            return SpecificTree.Binary.super.getDegree();
        }

        default @NotNull Traversed<DataType, NodeType, TreeType> traversePreOrder() {
            return traverse(new Traversal.Binary.PreOrder<>());
        }

        default @NotNull Traversed<DataType, NodeType, TreeType> traverseInOrder() {
            return traverse(new Traversal.Binary.InOrder<>());
        }

        default @NotNull Traversed<DataType, NodeType, TreeType> traversePostOrder() {
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

        interface Mutable<DataType, NodeType extends Mutable.Node<DataType, NodeType, TreeType>, TreeType extends Mutable<DataType, NodeType, TreeType>> extends NodedTree.Binary<DataType, NodeType, TreeType>, NodedTree.Mutable<DataType, NodeType, TreeType>, SpecificTree.Binary.Mutable<DataType, TreeType> {

            interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary.Mutable<DataType, NodeType, TreeType>> extends NodedTree.Binary.Node<DataType, NodeType, TreeType> {
                void setLeftChild(@Nullable NodeType leftChild);

                void setRightChild(@Nullable NodeType rightChild);
            }
        }

        interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends Binary<DataType, NodeType, TreeType>> extends NodedTree.Node<DataType, NodeType, TreeType> {
            @Nullable NodeType getLeftChild();

            @Nullable NodeType getRightChild();
        }
    }

    interface Traversal<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> {
        @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root);

        /**
         * TODO DOC
         * Conditional implies testing occurs before adding
         * Filter implies testing occurs after adding
         */
        interface Conditional<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends Traversal<DataType, NodeType, TreeType> {

            @Override
            default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                fillNodes(root.specificThis(), seq, getCondition());
                return Traversed.of(seq, this);
            }

            @NotNull Predicate<NodeType> getCondition();

            default void fillNodes(NodeType node, CanAppendTo<? super NodeType> canAppendTo, Predicate<? super NodeType> condition) {
                if (node == null) return;
                if (condition.test(node)) {
                    canAppendTo.add(node);
                }
                for (NodeType child : node.getChildren()) {
                    fillNodes(child, canAppendTo, condition);
                }
            }
        }

        interface Binary<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> extends Traversal<DataType, NodeType, TreeType> {
            @Override
            default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                assert root.getDegree() <= 2;
                fillData(root.specificThis(), seq);
                return Traversed.of(seq, this);
            }

            void fillData(NodeType node, CanAppendTo<? super NodeType> canAppendTo);

            class PreOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> implements Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node, CanAppendTo<? super NodeType> canAppendTo) {
                    if (node == null) return;
                    canAppendTo.add(node);
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                }
            }

            class InOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> implements Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node, CanAppendTo<? super NodeType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    canAppendTo.add(node);
                    fillData(node.getRightChild(), canAppendTo);
                }
            }

            class PostOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> implements Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node, CanAppendTo<? super NodeType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                    canAppendTo.add(node);
                }
            }
        }

        @FunctionalInterface
        interface ByOrder<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends Traversal<DataType, NodeType, TreeType> {

            @Override
            default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                Order<NodeType> order = makeOrder(root.getCount());
                order.enter(root);
                while (true) {
                    NodeType node = order.poll();
                    if (node == null) break;
                    seq.append(node);
                    for (NodeType child : node.getChildren()) {
                        order.enter(child);
                    }
                }
                return Traversed.of(seq, this);
            }

            Order<NodeType> makeOrder(int capacity);
        }
        @FunctionalInterface
        interface ByOrderReverseChildren<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends Traversal<DataType, NodeType, TreeType> {

            @Override
            default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                Order<NodeType> order = makeOrder(root.getCount());
                order.enter(root);
                while (true) {
                    NodeType node = order.poll();
                    if (node == null) break;
                    seq.append(node);
                    for (NodeType child : node.getChildren().inReverse()) {
                        order.enter(child);
                    }
                }
                return Traversed.of(seq, this);
            }

            Order<NodeType> makeOrder(int capacity);
        }

        class LeafOnly<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> implements Conditional<DataType, NodeType, TreeType> {
            @Override
            public final @NotNull Predicate<NodeType> getCondition() {
                return NodeType::isLeaf;
            }
        }

        class NonLeafOnly<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> implements Conditional<DataType, NodeType, TreeType> {
            @Override
            public final @NotNull Predicate<NodeType> getCondition() {
                return Predicate.not(NodeType::isLeaf);
            }
        }

        class HasDataOnly<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> implements Conditional<DataType, NodeType, TreeType> {
            @Override
            public final @NotNull Predicate<NodeType> getCondition() {
                return NodeType::hasData;
            }
        }
    }

    interface Traversed<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> {
        static <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> empty(Traversal<DataType, NodeType, TreeType> type) {
            //noinspection Convert2Diamond
            return new TraversedImpl<DataType, NodeType, TreeType>(Sequential.empty(), type);
        }

        static <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> of(Sequential<NodeType> sequential, Traversal<DataType, NodeType, TreeType> type) {
            return new TraversedImpl<>(sequential, type);
        }

        @NotNull Sequential<NodeType> getNodes();

        @NotNull Sequential<DataType> getData();

        @NotNull NodedTree.Traversal<DataType, NodeType, TreeType> getType();
    }
}