package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.adj.impl.SequentialImpl;
import ir.smmh.nile.verbs.CanAppendTo;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.impl.TraversedNodesImpl;
import ir.smmh.util.FunctionalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

import static ir.smmh.util.FunctionalUtil.with;

/**
 * A {@code NodedTree} is a {@link SpecificTree} and a wrapper over a single {@link Node} called
 * its "root", to which it delegates most methods required by {@link Tree}.
 */
@SuppressWarnings("unused")
public interface NodedTree<DataType, NodeType extends NodedTree.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> extends SpecificTree<DataType, TreeType> {

    @NotNull CanContain<NodeType> nodes();

    @Nullable NodeType getRootNode();

    @Override
    default int getDegree() {
        return with(getRootNode(), NodeType::getDegree, 0);
    }

    @Override
    default int getHeight() {
        return with(getRootNode(), NodeType::getHeight, -1);
    }

    @Override
    default int getCount() {
        return with(getRootNode(), NodeType::getCount, 0);
    }

    @Override
    default int getLeafCount() {
        return with(getRootNode(), NodeType::getLeafCount, 0);
    }

    @NotNull
    default TraversedNodes<NodeType> traverseNodes(@NotNull NodeTraversal type) {
        return with(getRootNode(), type::traverseNodes, TraversedNodes.empty(type));
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

        default @NotNull Sequential<NodeType> getSiblings() {
            return new Sequential.View.AllButOne<>(with(getParent(), Node::getChildren, Sequential.empty()), getIndexInParent());
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

        @NotNull
        DataType getData();

        void setData(DataType data);

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

        default @NotNull TraversedNodes<NodeType> traverseNodesPreOrder() {
            return with(getRootNode(), NodeTraversal.Binary.PRE_ORDER::traverseNodesBinary, TraversedNodes.empty(NodeTraversal.Binary.PRE_ORDER));
        }

        default @NotNull TraversedNodes<NodeType> traverseNodesInOrder() {
            return with(getRootNode(), NodeTraversal.Binary.IN_ORDER::traverseNodesBinary, TraversedNodes.empty(NodeTraversal.Binary.IN_ORDER));
        }

        default @NotNull TraversedNodes<NodeType> traverseNodesPostOrder() {
            return with(getRootNode(), NodeTraversal.Binary.POST_ORDER::traverseNodesBinary, TraversedNodes.empty(NodeTraversal.Binary.POST_ORDER));
        }

        @Override
        default @NotNull TraversedData<DataType> traverseDataPreOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.PRE_ORDER::traverseDataBinary, TraversedData.empty(NodedTree.DataTraversal.Binary.PRE_ORDER));
        }

        @Override
        default @NotNull TraversedData<DataType> traverseDataInOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.IN_ORDER::traverseDataBinary, TraversedData.empty(NodedTree.DataTraversal.Binary.IN_ORDER));
        }

        @Override
        default @NotNull TraversedData<DataType> traverseDataPostOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.POST_ORDER::traverseDataBinary, TraversedData.empty(NodedTree.DataTraversal.Binary.POST_ORDER));
        }

        /**
         * An order constructor is a binary tree constructor that uses two data traversals
         * out of the three available for binary trees (pre, in and post order) to construct
         * the binary tree itself and get the third data traversal.
         */
        interface OrderConstructor<DataType> {
            @NotNull TraversedData<DataType> getFirstSource();

            @NotNull TraversedData<DataType> getSecondSource();

            @NotNull TraversedData<DataType> getTarget();

            @NotNull Tree.Binary<DataType> getTree();
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

    interface NodeTraversal {
        @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> TraversedNodes<NodeType> traverseNodes(@NotNull NodeType root);

        interface Binary extends NodeTraversal {
            Binary PRE_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillNodes(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    canAppendTo.add(node);
                    fillNodes(node.getLeftChild(), canAppendTo);
                    fillNodes(node.getRightChild(), canAppendTo);
                }

                @Override
                public String toString() {
                    return "PRE_ORDER";
                }
            };

            Binary IN_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillNodes(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    fillNodes(node.getLeftChild(), canAppendTo);
                    canAppendTo.add(node);
                    fillNodes(node.getRightChild(), canAppendTo);
                }

                @Override
                public String toString() {
                    return "IN_ORDER";
                }
            };

            Binary POST_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillNodes(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    fillNodes(node.getLeftChild(), canAppendTo);
                    fillNodes(node.getRightChild(), canAppendTo);
                    canAppendTo.add(node);
                }

                @Override
                public String toString() {
                    return "POST_ORDER";
                }
            };

            // TODO TEST
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            default @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> TraversedNodes<NodeType> traverseNodes(@NotNull NodeType root) {
                return traverseNodesBinary((NodedTree.Binary.Node) root);
            }

            default @NotNull <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> TraversedNodes<NodeType> traverseNodesBinary(@NotNull NodedTree.Binary.Node<DataType, NodeType, TreeType> root) {
                final Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new LinkedList<>());
                assert root.getDegree() <= 2;
                fillNodes(root.specificThis(), seq);
                return TraversedNodes.of(seq, this);
            }

            <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillNodes(NodeType node, CanAppendTo<NodeType> canAppendTo);

        }
    }

    interface DataTraversal extends Tree.DataTraversal {

        // NodedBinaryDataTraversal
        interface Binary extends SpecificTree.DataTraversal {
            Binary PRE_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<DataType> canAppendTo) {
                    if (node == null) return;
                    canAppendTo.add(node.getData());
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                }

                @Override
                public String toString() {
                    return "PRE_ORDER";
                }
            };

            Binary IN_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<DataType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    canAppendTo.add(node.getData());
                    fillData(node.getRightChild(), canAppendTo);
                }

                @Override
                public String toString() {
                    return "IN_ORDER";
                }
            };

            Binary POST_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<DataType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                    canAppendTo.add(node.getData());
                }

                @Override
                public String toString() {
                    return "POST_ORDER";
                }
            };

            @Override
            @NotNull
            @SuppressWarnings({"unchecked", "rawtypes"})
            default <DataType, TreeType extends Tree<DataType>> TraversedData<DataType> traverseData(@NotNull TreeType tree) {
                return with(((NodedTree.Binary/*<DataType, ?, ?>*/) tree).getRootNode(), r -> traverseDataBinary((NodedTree.Binary.Node/*<DataType, ?, ?>*/) r), TraversedData.empty(this));
            }

            default @NotNull <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> TraversedData<DataType> traverseDataBinary(@NotNull NodedTree.Binary.Node<DataType, NodeType, TreeType> root) {
                final Sequential.Mutable.VariableSize<DataType> seq = Sequential.Mutable.VariableSize.of(new LinkedList<>());
                assert root.getDegree() <= 2;
                fillData(root.specificThis(), seq);
                return TraversedData.of(seq, this);
            }

            <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<DataType> canAppendTo);

        }
    }

    interface TraversedNodes<NodeType> {
        static <NodeType> TraversedNodes<NodeType> empty(NodeTraversal type) {
            return new TraversedNodesImpl<>(Sequential.empty(), type);
        }

        static <NodeType> TraversedNodes<NodeType> of(Sequential<NodeType> nodes, NodeTraversal type) {
            return new TraversedNodesImpl<>(nodes, type);
        }

        @NotNull Sequential<NodeType> getData();

        @NotNull NodeTraversal getType();
    }
}