package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
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

    default @NotNull Traversed<DataType, NodeType, TreeType> traverse(@NotNull Traversal type) {
        return with(getRootNode(), type::traverse, Traversed.empty(type));
    }

    default @NotNull Traversed<DataType, NodeType, TreeType> traverseLeafOnly() {
        return traverse(Traversal.LEAF_ONLY);
    }

    default @NotNull Sequential<NodeType> getLeafNodes() {
        return traverseLeafOnly().getNodes();
    }


    @Override
    default @NotNull Sequential<DataType> getLeafData() {
        return traverseLeafOnly().getData();
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

        @Nullable
        DataType getData();

        default boolean hasData() {
            return getData() != null;
        }

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

        default @NotNull Traversed<DataType, NodeType, TreeType> traversePreOrder() {
            return with(getRootNode(), Traversal.Binary.PRE_ORDER::traverseBinary, Traversed.empty(Traversal.Binary.PRE_ORDER));
        }

        default @NotNull Traversed<DataType, NodeType, TreeType> traverseInOrder() {
            return with(getRootNode(), Traversal.Binary.IN_ORDER::traverseBinary, Traversed.empty(Traversal.Binary.IN_ORDER));
        }

        default @NotNull Traversed<DataType, NodeType, TreeType> traversePostOrder() {
            return with(getRootNode(), Traversal.Binary.POST_ORDER::traverseBinary, Traversed.empty(Traversal.Binary.POST_ORDER));
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

    interface Traversal {
        Conditional LEAF_ONLY = new Conditional() {
            @Override
            public @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Predicate<NodeType> getCondition() {
                return NodeType::isLeaf;
            }
        };
        Conditional NON_LEAF_ONLY = new Conditional() {
            @Override
            public @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Predicate<NodeType> getCondition() {
                return Predicate.not(NodeType::isLeaf);
            }
        };
        Conditional HAS_DATA_ONLY = new Conditional() {
            @Override
            public @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Predicate<NodeType> getCondition() {
                return NodeType::hasData;
            }
        };

        @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root);

        /**
         * TODO DOC
         * Conditional implies testing occurs before adding
         * Filter implies testing occurs after adding
         */
        interface Conditional extends Traversal {

            static Conditional of(Predicate<?> condition) {
                return new Conditional() {
                    @SuppressWarnings("unchecked") // TODO TEST
                    @Override
                    public @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Predicate<NodeType> getCondition() {
                        return (Predicate<NodeType>) condition;
                    }
                };
            }

            @Override
            default @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                fillNodes(root.specificThis(), seq, getCondition());
                return Traversed.of(seq, this);
            }

            @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Predicate<NodeType> getCondition();

            default <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> void fillNodes(NodeType node, CanAppendTo<NodeType> canAppendTo, Predicate<NodeType> condition) {
                if (node == null) return;
                if (condition.test(node)) {
                    canAppendTo.add(node);
                }
                for (NodeType child : node.getChildren()) {
                    fillNodes(child, canAppendTo, condition);
                }
            }
        }

        interface Binary extends Traversal {
            Binary PRE_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    canAppendTo.add(node);
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
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    canAppendTo.add(node);
                    fillData(node.getRightChild(), canAppendTo);
                }

                @Override
                public String toString() {
                    return "IN_ORDER";
                }
            };

            Binary POST_ORDER = new Binary() {
                @Override
                public <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<NodeType> canAppendTo) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), canAppendTo);
                    fillData(node.getRightChild(), canAppendTo);
                    canAppendTo.add(node);
                }

                @Override
                public String toString() {
                    return "POST_ORDER";
                }
            };

            // TODO TEST
            @SuppressWarnings({"unchecked", "rawtypes"})
            @Override
            default @NotNull <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> traverse(@NotNull NodeType root) {
                return traverseBinary((NodedTree.Binary.Node) root);
            }

            default @NotNull <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> NodedTree.Traversed<DataType, NodeType, TreeType> traverseBinary(@NotNull NodeType root) {
                Sequential.Mutable.VariableSize<NodeType> seq = Sequential.Mutable.VariableSize.of(new ArrayList<>());
                assert root.getDegree() <= 2;
                fillData(root.specificThis(), seq);
                return Traversed.of(seq, this);
            }

            <DataType, NodeType extends NodedTree.Binary.Node<DataType, NodeType, TreeType>, TreeType extends NodedTree.Binary<DataType, NodeType, TreeType>> void fillData(NodeType node, CanAppendTo<NodeType> canAppendTo);

        }
    }

    interface Traversed<DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> {
        static <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> empty(Traversal type) {
            return new TraversedImpl<DataType, NodeType, TreeType>(Sequential.empty(), type);
        }

        static <DataType, NodeType extends Node<DataType, NodeType, TreeType>, TreeType extends NodedTree<DataType, NodeType, TreeType>> Traversed<DataType, NodeType, TreeType> of(Sequential<NodeType> sequential, Traversal type) {
            return new TraversedImpl<>(sequential, type);
        }

        @NotNull Sequential<NodeType> getNodes();

        @NotNull Sequential<DataType> getData();

        @NotNull NodedTree.Traversal getType();
    }
}