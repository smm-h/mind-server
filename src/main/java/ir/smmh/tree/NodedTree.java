package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanContain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

import static ir.smmh.util.FunctionalUtil.with;

/**
 * A tree is just a thin wrapper over its root node. It offers direct interaction with
 * some of the useful root methods like traverse, get degree, get height, and get count.
 */
@SuppressWarnings("unused")
public interface NodedTree<T, N extends NodedTree.Node<T, N, Q>, Q extends NodedTree<T, N, Q>> extends SpecificTree<T, Q> {

    @NotNull CanContain<N> nodes();

    @NotNull Q specificThis();

    @Nullable N getRootNode();

    @Override
    default int getDegree() {
        return with(getRootNode(), N::getDegree, 0);
    }
    @Override
    default int getHeight() {
        return with(getRootNode(), N::getHeight, -1);
    }
    @Override
    default int getCount() {
        return with(getRootNode(), N::getCount, 0);
    }
    @Override
    default int getLeafCount() {
        return with(getRootNode(), N::getLeafCount, 0);
    }

    @NotNull
    default Sequential<N> traverseNodes(@NotNull NodeTraversal method) {
        return with(getRootNode(), method::traverseNodes, Sequential.empty());
    }

    interface Mutable<T, N extends Node<T, N, Q>, Q extends Mutable<T, N, Q>> extends NodedTree<T, N, Q>, SpecificTree.Mutable<T, Q> {

        @Override
        default void clear() {
            setRoot((N) null);
        }

        void setRoot(T data);

        void setRoot(N node);

        interface Node<T, N extends Node<T, N, Q>, Q extends Mutable<T, N, Q>> extends NodedTree.Node<T, N, Q>, Sequential.Mutable<N> {
        }
    }

    interface Node<T, N extends Node<T, N, Q>, Q extends NodedTree<T, N, Q>> extends Sequential<N> {

        @NotNull N specificThis();

        @NotNull
        T getData();

        @Nullable
        N getParent();

        @NotNull
        Q getTree();

        default int getDegree() {
            if (isEmpty()) {
                return 0;
            } else {
                int degree = getLength();
                for (N node : this) {
                    degree = Math.max(degree, node.getDegree());
                }
                return degree;
            }
        }

        default int getHeight() {
            if (isEmpty()) {
                return 0;
            } else {
                int height = 0;
                for (N node : this) {
                    height = Math.max(height, node.getDegree());
                }
                return height + 1;
            }
        }

        default int getCount() {
            if (isEmpty()) {
                return 1;
            } else {
                int count = 0;
                for (N node : this) {
                    count += node.getCount();
                }
                return count;
            }
        }

        default int getLeafCount() {
            if (isEmpty()) {
                return 1;
            } else {
                int leafCount = 0;
                for (N node : this) {
                    leafCount += node.getLeafCount();
                }
                return leafCount;
            }
        }
    }

    interface Binary<T, N extends Binary.Node<T, N, Q>, Q extends Binary<T, N, Q>> extends NodedTree<T, N, Q>, SpecificTree.Binary<T, Q> {
        default @NotNull Sequential<N> traverseNodesPreOrder() {
            return with(getRootNode(), NodeTraversal.Binary.PRE_ORDER::traverseNodesBinary, Sequential.empty());
        }

        default @NotNull Sequential<N> traverseNodesInOrder() {
            return with(getRootNode(), NodeTraversal.Binary.IN_ORDER::traverseNodesBinary, Sequential.empty());
        }

        default @NotNull Sequential<N> traverseNodesPostOrder() {
            return with(getRootNode(), NodeTraversal.Binary.POST_ORDER::traverseNodesBinary, Sequential.empty());
        }

        @Override
        default @NotNull Sequential<T> traverseDataPreOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.PRE_ORDER::traverseDataBinary, Sequential.empty());
        }

        @Override
        default @NotNull Sequential<T> traverseDataInOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.IN_ORDER::traverseDataBinary, Sequential.empty());
        }

        @Override
        default @NotNull Sequential<T> traverseDataPostOrder() {
            return with(getRootNode(), NodedTree.DataTraversal.Binary.POST_ORDER::traverseDataBinary, Sequential.empty());
        }

        interface Mutable<T, N extends Mutable.Node<T, N, Q>, Q extends Mutable<T, N, Q>> extends NodedTree.Binary<T, N, Q>, NodedTree.Mutable<T, N, Q>, SpecificTree.Binary.Mutable<T, Q> {

            interface Node<T, N extends Node<T, N, Q>, Q extends Mutable<T, N, Q>> extends NodedTree.Binary.Node<T, N, Q>, NodedTree.Mutable.Node<T, N, Q> {
                void setLeftChild(@Nullable N leftChild);

                void setRightChild(@Nullable N rightChild);
            }
        }

        interface Node<T, N extends Node<T, N, Q>, Q extends Binary<T, N, Q>> extends NodedTree.Node<T, N, Q> {
            @Nullable N getLeftChild();

            @Nullable N getRightChild();
        }
    }

    interface NodeTraversal {
        @NotNull <T, N extends Node<T, N, Q>, Q extends NodedTree<T, N, Q>> Sequential<N> traverseNodes(@NotNull N root);

        interface Binary extends NodeTraversal {
            Binary PRE_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillNodes(N node, Sequential.Mutable<N> seq) {
                    if (node == null) return;
                    seq.add(node);
                    fillNodes(node.getLeftChild(), seq);
                    fillNodes(node.getRightChild(), seq);
                }
            };

            Binary IN_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillNodes(N node, Sequential.Mutable<N> seq) {
                    if (node == null) return;
                    fillNodes(node.getLeftChild(), seq);
                    seq.add(node);
                    fillNodes(node.getRightChild(), seq);
                }
            };

            Binary POST_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillNodes(N node, Sequential.Mutable<N> seq) {
                    if (node == null) return;
                    fillNodes(node.getLeftChild(), seq);
                    fillNodes(node.getRightChild(), seq);
                    seq.add(node);
                }
            };

            // TODO TEST
            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            default @NotNull <T, N extends Node<T, N, Q>, Q extends NodedTree<T, N, Q>> Sequential<N> traverseNodes(@NotNull N root) {
                return traverseNodesBinary((NodedTree.Binary.Node) root);
            }

            default @NotNull <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> Sequential<N> traverseNodesBinary(@NotNull NodedTree.Binary.Node<T, N, Q> root) {
                final Sequential.Mutable<N> seq = Sequential.Mutable.of(new LinkedList<>());
                assert root.getDegree() <= 2;
                fillNodes(root.specificThis(), seq);
                return seq;
            }

            <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillNodes(N node, Sequential.Mutable<N> seq);

        }
    }

    interface DataTraversal extends Tree.DataTraversal {

        // NodedBinaryDataTraversal
        interface Binary extends SpecificTree.DataTraversal {
            Binary PRE_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillData(N node, Sequential.Mutable<T> seq) {
                    if (node == null) return;
                    seq.add(node.getData());
                    fillData(node.getLeftChild(), seq);
                    fillData(node.getRightChild(), seq);
                }
            };

            Binary IN_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillData(N node, Sequential.Mutable<T> seq) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), seq);
                    seq.add(node.getData());
                    fillData(node.getRightChild(), seq);
                }
            };

            Binary POST_ORDER = new Binary() {
                @Override
                public <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillData(N node, Sequential.Mutable<T> seq) {
                    if (node == null) return;
                    fillData(node.getLeftChild(), seq);
                    fillData(node.getRightChild(), seq);
                    seq.add(node.getData());
                }
            };

            @Override
            @NotNull
            @SuppressWarnings({"unchecked", "rawtypes"})
            default <T, Q extends Tree<T>> Sequential<T> traverseData(@NotNull Q tree) {
                return with(((NodedTree.Binary/*<T, ?, ?>*/) tree).getRootNode(), r -> traverseDataBinary((NodedTree.Binary.Node/*<T, ?, ?>*/) r), Sequential.empty());
            }

            default @NotNull <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> Sequential<T> traverseDataBinary(@NotNull NodedTree.Binary.Node<T, N, Q> root) {
                final Sequential.Mutable<T> seq = Sequential.Mutable.of(new LinkedList<>());
                assert root.getDegree() <= 2;
                fillData(root.specificThis(), seq);
                return seq;
            }

            <T, N extends NodedTree.Binary.Node<T, N, Q>, Q extends NodedTree.Binary<T, N, Q>> void fillData(N node, Sequential.Mutable<T> seq);

        }
    }
}