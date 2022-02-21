package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.nile.verbs.CanSerialize;
import ir.smmh.tree.impl.InOrderConstructor;
import ir.smmh.tree.impl.PostOrderConstructor;
import ir.smmh.tree.impl.PreOrderConstructor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface Tree<DataType> extends
        CanContain<DataType>,
//        CanGetAtPlace<Sequential<DataType>, DataType>,
        CanSerialize {

    @NotNull Sequential<?> getImmediateSubtrees();

    int getDegree();

    int getHeight();

    /**
     * @return the number of leaves
     */
    int getBreadth();

    /**
     * Returns the width of the tree in a given level which is the number
     * of all the nodes that have the same distance from root.
     *
     * @param level the exact distance between root and nodes
     * @return the width of the tree in a given level
     */
    int getWidth(int level);

    @NotNull Sequential<DataType> getLeafData();

    @NotNull Sequential<DataType> getBreadthFirstData();

    @NotNull Sequential<DataType> getDepthFirstData();

    DataType getRootData();

    interface Mutable<DataType> extends
            Tree<DataType>,
//            CanSetAtPlace<Sequential<DataType>, DataType>,
            CanClear {
        void setRootData(DataType data);
    }

    interface Binary<DataType> extends Tree<DataType> {
        @NotNull Sequential<DataType> traverseDataPreOrder();

        @NotNull Sequential<DataType> traverseDataInOrder();

        @NotNull Sequential<DataType> traverseDataPostOrder();

        @Override
        default int getDegree() {
            return 2;
        }

        interface Mutable<DataType> extends Tree.Binary<DataType>, Tree.Mutable<DataType> {
        }

        /**
         * An order constructor is a binary tree constructor that uses two data traversals
         * out of the three available for binary trees (pre, in and post order) to construct
         * the binary tree itself and get the third data traversal.
         */
        interface OrderConstructor<DataType> {
            static <DataType> OrderConstructor<DataType> targetPreOrder(Sequential<DataType> inOrder, Sequential<DataType> postOrder) {
                return new PreOrderConstructor<>(inOrder, postOrder);
            }

            static <DataType> OrderConstructor<DataType> targetInOrder(Sequential<DataType> preOrder, Sequential<DataType> postOrder) {
                return new InOrderConstructor<>(preOrder, postOrder);
            }

            static <DataType> OrderConstructor<DataType> targetPostOrder(Sequential<DataType> preOrder, Sequential<DataType> inOrder) {
                return new PostOrderConstructor<>(preOrder, inOrder);
            }

            @NotNull Sequential<DataType> getFirstSource();

            @NotNull Sequential<DataType> getSecondSource();

            @NotNull Sequential<DataType> getTarget();

            @NotNull Tree.Binary<DataType> getTree();
        }
    }
}
