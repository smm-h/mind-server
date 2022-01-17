package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
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
