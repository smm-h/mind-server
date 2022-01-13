package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
import ir.smmh.util.Serializable;
import org.jetbrains.annotations.NotNull;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
public interface Tree<T> extends CanContain<T>, Serializable {

    int getDegree();

    int getHeight();

    int getCount();

    int getLeafCount();

    T getRootData();

    @NotNull Sequential<Tree<T>> getImmediateSubtrees();

    @NotNull
    default Sequential<T> traverseData(@NotNull DataTraversal method) {
        return with(this, method::traverseData, Sequential.empty());
    }

    interface Mutable<T> extends Tree<T>, CanClear, ir.smmh.util.Mutable {
        void setRootData(T data);
    }

    interface Binary<T> extends Tree<T> {
        @NotNull Sequential<T> traverseDataPreOrder();

        @NotNull Sequential<T> traverseDataInOrder();

        @NotNull Sequential<T> traverseDataPostOrder();

        interface Mutable<T> extends Tree.Binary<T>, Tree.Mutable<T> {
        }
    }

    interface DataTraversal {
        @NotNull <T, Q extends Tree<T>> Sequential<T> traverseData(@NotNull Q tree);
    }
}
