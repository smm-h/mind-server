package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
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

    @NotNull
    default Sequential<T> traverseData(@NotNull DataTraversal method) {
        return with(this, method::traverseData, Sequential.empty());
    }

    interface Binary<T> extends Tree<T> {
        @NotNull Sequential<T> traverseDataPreOrder();
        @NotNull Sequential<T> traverseDataInOrder();
        @NotNull Sequential<T> traverseDataPostOrder();
    }

    // TODO BFS, DFS

    interface DataTraversal {
        @NotNull <T, Q extends Tree<T>> Sequential<T> traverseData(@NotNull Q tree);
    }
}
