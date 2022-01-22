package ir.smmh.tree;

import ir.smmh.nile.adj.Order;
import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.FIFO;
import ir.smmh.nile.adj.impl.LIFO;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanClone;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.impl.NodedBinaryTreeImpl;
import ir.smmh.tree.impl.NodedTreeImpl;
import ir.smmh.tree.impl.TraversedImpl;
import ir.smmh.util.FunctionalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static ir.smmh.util.FunctionalUtil.with;

/**
 * A {@code NodedTree} is a {@link SpecificTree} and a wrapper over a single {@link Node} called
 * its "root", to which it delegates most methods required by {@link Tree}.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public interface NodedTree<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends SpecificTree<DataType, TreeType>, CanClone<TreeType> {

    @NotNull CanContain<NodeType> nodes();

    @Nullable NodeType getRootNode();

    /**
     * @param a a node in this tree
     * @param b and another node in this tree
     * @return the nearest node to them that is an ancestor of both
     */
    default @NotNull NodeType lowestCommonAncestor(NodeType a, NodeType b) {
        if (a == b) return a;
        if (a.getParent() == b) return b;
        if (b.getParent() == a) return a;
        Sequential<NodeType> as = Sequential.View.reversed(a.getAncestorsAndSelf());
        Sequential<NodeType> bs = Sequential.View.reversed(b.getAncestorsAndSelf());
        int i = 0;
        while (Objects.equals(as.getAtIndex(i), bs.getAtIndex(i))) i++;
        return as.getAtIndex(i - 1);
    }

    @Override
    default int getWidth(int level) {
        return getNodesAtLevel(level).getSize();
    }

    default @NotNull Sequential<NodeType> getNodesAtLevel(int level) {
        Sequential.Mutable.VariableSize<NodeType> nodes = new SequentialImpl<>();
        with(getRootNode(), r -> r.fillDescendantsAtExactDepth(nodes, 0, level));
        return nodes;
    }

    default @Nullable Sequential<NodeType> findByData(DataType data) {
        return filter(node -> Objects.equals(node.getData(), data));
    }

    /**
     * An out-of-place filter done on all the nodes of this tree.
     *
     * @param condition the condition to test the nodes with
     * @return a sequence of nodes that passed the test
     */
    default @Nullable Sequential<NodeType> filter(Predicate<? super NodeType> condition) {
        return traverse((Traversal.Conditional<DataType, NodeType, TreeType>) () -> condition).getNodes();
    }

    /**
     * An out-of-place prune done on all the nodes of this tree.
     *
     * @param condition the condition to test the nodes with
     * @return a sequence of nodes that passed the test
     */
    default @Nullable NodedTreeImpl<DataType> pruneOutOfPlace(Predicate<? super NodeType> condition, boolean leafOnly) {
        Predicate<? super NodeType> finalCondition = leafOnly ? FunctionalUtil.and(Node::isLeaf, condition) : condition;
        NodedTreeImpl<DataType> otherTree = new NodedTreeImpl<>();
        with(getRootNode(), r -> otherTree.setRootNode(r.pruneOutOfPlace(otherTree, null, finalCondition)));
        return otherTree;
    }

    default <OtherDataType> @NotNull NodedTreeImpl<OtherDataType> applyOutOfPlace(Function<? super DataType, ? extends OtherDataType> toApply) {
        NodedTreeImpl<OtherDataType> otherTree = new NodedTreeImpl<>();
        NodeType rootNode = getRootNode();
        with(getRootNode(), r -> otherTree.setRootNode(r.applyOutOfPlace(otherTree, null, toApply)));
        return otherTree;
    }

    @SuppressWarnings("unchecked")
    @Override
    default @NotNull TreeType clone(boolean deepIfPossible) {
        NodedTreeImpl<DataType> otherTree = new NodedTreeImpl<>();
        NodeType rootNode = getRootNode();
        with(getRootNode(), r -> otherTree.setRootNode(r.clone(otherTree, null, deepIfPossible)));
        return (TreeType) otherTree;
    }

    default @NotNull NodedBinaryTreeImpl<DataType> toBinary() {
        NodedBinaryTreeImpl<DataType> otherTree = new NodedBinaryTreeImpl<>();
        NodeType rootNode = getRootNode();
        with(getRootNode(), r -> otherTree.setRootNode(toBinary(r, otherTree, null)));
        return otherTree;
    }

    default NodedBinaryTreeImpl<DataType>.Node toBinary(NodeType node, NodedBinaryTreeImpl<DataType> otherTree, @Nullable NodedBinaryTreeImpl<DataType>.Node parent) {
        NodedBinaryTreeImpl<DataType>.Node prev = null, curr, otherChild = otherTree.new Node(node.getData(), parent);
        boolean leftMost = true;
        for (NodeType child : node.getChildren()) {
            curr = toBinary(child, otherTree, otherChild);
            if (leftMost) {
                leftMost = false;
                otherChild.setLeftChild(curr);
            } else {
                curr.setRightChild(prev);
            }
            prev = curr;
        }
        return otherChild;
    }

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
    default int getBreadth() {
        return with(getRootNode(), NodeType::getLeafCount, 0);
    }

    @Override
    default @NotNull Sequential<TreeType> getImmediateSubtrees() {
        return with(getRootNode(), Node::getImmediateSubtrees, Sequential.empty());
    }

    interface Mutable<DataType, NodeType extends Mutable.Node<DataType, NodeType, TreeType>, TreeType extends Mutable<DataType, NodeType, TreeType>> extends NodedTree<DataType, NodeType, TreeType>, SpecificTree.Mutable<DataType, TreeType> {

        default void replaceData(Function<? super DataType, ? extends DataType> toReplace) {
            preMutate();
            for (NodeType node : traverseBreadthFirst().getNodes()) {
                node.replaceData(toReplace);
            }
            postMutate();
        }

        default void mutateData(Consumer<DataType> toApply) {
            preMutate();
            for (NodeType node : traverseBreadthFirst().getNodes()) {
                node.mutateData(toApply);
            }
            postMutate();
        }

        @Override
        default void clear() {
            setRootNode(null);
        }

        void setRootNode(@Nullable NodeType node);

        interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Mutable<DataType, NodeType, TreeType>> extends NodedTree.Node<DataType, NodeType, TreeType> {
            @Override
            @NotNull Sequential.Mutable<NodeType> getChildren();

            default void replaceData(Function<? super DataType, ? extends DataType> toReplace) {
                setData(toReplace.apply(getData()));
            }

            default void mutateData(Consumer<DataType> toApply) {
                toApply.accept(getData());
            }
        }
    }

    interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends FunctionalUtil.RecursivelySpecific<NodeType> {
        /**
         * @return parent and children
         */
        default @NotNull Sequential<NodeType> getNeighbors() {
            Sequential.Mutable.VariableSize<NodeType> neighbors = new SequentialImpl<>();
            with(getParent(), neighbors::append);
            for (NodeType child : getChildren()) {
                neighbors.append(child);
            }
            return neighbors;
        }

        default @NotNull Sequential<NodeType> getDescendants() {
            Sequential.Mutable.VariableSize<NodeType> descendants = new SequentialImpl<>();
            for (NodeType child : getChildren()) {
                fillDescendants(descendants);
            }
            return descendants;
        }

        default @NotNull Sequential<NodeType> getDescendantsAndSelf() {
            Sequential.Mutable.VariableSize<NodeType> descendants = new SequentialImpl<>();
            fillDescendants(descendants);
            return descendants;
        }

        default void fillDescendants(CanAppendTo<NodeType> canAppendTo) {
            canAppendTo.append(specificThis());
            for (NodeType child : getChildren()) {
                child.fillDescendants(canAppendTo);
            }
        }

        default @NotNull Sequential<NodeType> getAncestors() {
            Sequential.Mutable.VariableSize<NodeType> ancestors = new SequentialImpl<>();
            NodeType r = getParent();
            while (r != null) {
                ancestors.append(r);
                r = r.getParent();
            }
            return ancestors;
        }

        default @NotNull Sequential<NodeType> getAncestorsAndSelf() {
            Sequential.Mutable.VariableSize<NodeType> ancestors = new SequentialImpl<>();
            NodeType r = specificThis();
            while (r != null) {
                ancestors.append(r);
                r = r.getParent();
            }
            return ancestors;
        }

        default void fillDescendantsAtExactDepth(CanAppendTo<NodeType> canAppendTo, int depth, int exactDepth) {
            if (depth++ == exactDepth) {
                canAppendTo.append(specificThis());
            } else {
                for (NodeType child : getChildren()) {
                    child.fillDescendantsAtExactDepth(canAppendTo, depth, exactDepth);
                }
            }
        }

        default NodedTreeImpl<DataType>.Node pruneOutOfPlace(NodedTreeImpl<DataType> otherTree, @Nullable NodedTreeImpl<DataType>.Node parent, Predicate<? super NodeType> toTest) {
            NodedTreeImpl<DataType>.Node otherChild = otherTree.new Node(getData(), parent);
            for (NodeType child : getChildren()) {
                if (toTest.test(child)) {
                    otherChild.getChildren().append(child.pruneOutOfPlace(otherTree, otherChild, toTest));
                }
            }
            return otherChild;
        }

        default <OtherDataType> NodedTreeImpl<OtherDataType>.Node applyOutOfPlace(NodedTreeImpl<OtherDataType> otherTree, @Nullable NodedTreeImpl<OtherDataType>.Node parent, Function<? super DataType, ? extends OtherDataType> toApply) {
            NodedTreeImpl<OtherDataType>.Node otherChild = otherTree.new Node(toApply.apply(getData()), parent);
            for (NodeType child : getChildren()) {
                otherChild.getChildren().append(child.applyOutOfPlace(otherTree, otherChild, toApply));
            }
            return otherChild;
        }

        @SuppressWarnings("unchecked")
        default NodedTreeImpl<DataType>.Node clone(NodedTreeImpl<DataType> otherTree, @Nullable NodedTreeImpl<DataType>.Node parent, boolean deep) {
            DataType data = getData();
            if (deep && data instanceof CanClone) {
                data = ((CanClone<DataType>) data).clone(true);
            }
            NodedTreeImpl<DataType>.Node otherChild = otherTree.new Node(data, parent);
            for (NodeType child : getChildren()) {
                otherChild.getChildren().append(child.clone(otherTree, otherChild, deep));
            }
            return otherChild;
        }

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

        default @NotNull Sequential<DataType> getChildrenData() {
            return new Sequential.AbstractSequential<>() {
                @Override
                public DataType getAtIndex(int index) {
                    return getChildren().getAtIndex(index).getData();
                }

                @Override
                public int getSize() {
                    return getChildren().getSize();
                }
            };
        }

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

            interface Node<DataType, NodeType extends Node<DataType, NodeType, TreeType>,
                    TreeType extends NodedTree.Binary.Mutable<DataType, NodeType, TreeType>> extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, NodedTree.Mutable.Node<DataType, NodeType, TreeType> {
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

        interface Conditional<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends Traversal<DataType, NodeType, TreeType> {

            @Override
            default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@Nullable NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                with(root, r -> fillNodes(r.specificThis(), seq, getCondition()));
                return Traversed.of(seq, this);
            }

            @NotNull Predicate<? super NodeType> getCondition();

            default void fillNodes(NodeType node, CanAppendTo<? super NodeType> canAppendTo, Predicate<? super NodeType> condition) {
                if (condition.test(node)) {
                    canAppendTo.add(node);
                }
                for (NodeType child : node.getChildren()) {
                    fillNodes(child, canAppendTo, condition);
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

        abstract class Binary<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> implements Traversal<DataType, NodeType, TreeType> {
            protected final Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());

            @Override
            @NotNull
            public Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                assert root.getDegree() <= 2;
                seq.clear();
                with(root, r -> fillData(r.specificThis()));
                return Traversed.of(seq, this);
            }

            public abstract void fillData(NodeType node);

            public static class PreOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> extends Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node) {
                    seq.add(node);
                    with(node.getLeftChild(), this::fillData);
                    with(node.getRightChild(), this::fillData);
                }
            }

            public static class InOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> extends Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node) {
                    with(node.getLeftChild(), this::fillData);
                    seq.add(node);
                    with(node.getRightChild(), this::fillData);
                }
            }

            public static class PostOrder<DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> extends Binary<DataType, NodeType, TreeType> {
                @Override
                public final void fillData(NodeType node) {
                    with(node.getLeftChild(), this::fillData);
                    with(node.getRightChild(), this::fillData);
                    seq.add(node);
                }
            }
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