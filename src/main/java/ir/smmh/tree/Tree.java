package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.tree.impl.InOrderConstructor;
import ir.smmh.tree.impl.PostOrderConstructor;
import ir.smmh.tree.impl.PreOrderConstructor;
import ir.smmh.tree.impl.TraversedDataImpl;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
public interface Tree<DataType> extends CanContain<DataType>, Serializable {

    @NotNull Sequential<?> getImmediateSubtrees();

    int getDegree();

    int getHeight();

    int getCount();

    @Override
    default int getSize() {
        return getCount(); // TODO rename this
    }

    int getLeafCount();

    DataType getRootData();

    @NotNull
    default TraversedData<DataType> traverseData(@NotNull DataTraversal type) {
        return with(this, type::traverseData, TraversedData.empty(type));
    }

    interface Mutable<DataType> extends Tree<DataType>, CanClear, ir.smmh.util.Mutable {
        void setRootData(DataType data);
    }

    interface Binary<DataType> extends Tree<DataType> {
        @NotNull TraversedData<DataType> traverseDataPreOrder();

        @NotNull TraversedData<DataType> traverseDataInOrder();

        @NotNull TraversedData<DataType> traverseDataPostOrder();

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

            @NotNull TraversedData<DataType> getFirstSource();

            @NotNull TraversedData<DataType> getSecondSource();

            @NotNull TraversedData<DataType> getTarget();

            @NotNull Tree.Binary<DataType> getTree();
        }
    }

    interface DataTraversal {
        @NotNull <DataType, TreeType extends Tree<DataType>> TraversedData<DataType> traverseData(@NotNull TreeType tree);
    }

    interface TraversedData<DataType> {
        static <DataType> TraversedData<DataType> empty(DataTraversal type) {
            return new TraversedDataImpl<>(Sequential.empty(), type);
        }

        static <DataType> TraversedData<DataType> of(Sequential<DataType> data, DataTraversal type) {
            return new TraversedDataImpl<>(data, type);
        }

        @NotNull Sequential<DataType> getData();

        @NotNull DataTraversal getType();
    }
}
