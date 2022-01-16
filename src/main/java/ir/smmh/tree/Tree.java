package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
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
    default Sequential<DataType> traverseData(@NotNull DataTraversal method) {
        return with(this, method::traverseData, Sequential.empty());
    }

    interface Mutable<DataType> extends Tree<DataType>, CanClear, ir.smmh.util.Mutable {
        void setRootData(DataType data);
    }

    interface Binary<DataType> extends Tree<DataType> {
        @NotNull Sequential<DataType> traverseDataPreOrder();

        @NotNull Sequential<DataType> traverseDataInOrder();

        @NotNull Sequential<DataType> traverseDataPostOrder();

        interface Mutable<DataType> extends Tree.Binary<DataType>, Tree.Mutable<DataType> {
        }

        @Override
        default int getDegree() {
            return 2;
        }
    }

    interface DataTraversal {
        @NotNull <DataType, TreeType extends Tree<DataType>> Sequential<DataType> traverseData(@NotNull TreeType tree);
    }
}
