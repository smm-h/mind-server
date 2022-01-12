package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.nile.verbs.CanClear;
import ir.smmh.nile.verbs.CanContain;
import org.jetbrains.annotations.NotNull;

import static ir.smmh.util.FunctionalUtil.with;

@SuppressWarnings("unused")
public interface SpecificTree<T, Q extends SpecificTree<T, Q>> extends Tree<T> {

    @NotNull Q specificThis();

    @NotNull
    default Sequential<T> traverseData(@NotNull DataTraversal method) {
        return with(specificThis(), method::traverseData, Sequential.empty());
    }

    interface Mutable<T, Q extends Mutable<T, Q>> extends SpecificTree<T, Q>, CanClear, ir.smmh.util.Mutable {

        void setRoot(T data);

    }

    interface Binary<T, Q extends Binary<T, Q>> extends SpecificTree<T, Q>, Tree.Binary<T> {
        interface Mutable<T, Q extends Mutable<T, Q>> extends SpecificTree.Binary<T, Q>, SpecificTree.Mutable<T, Q> {
        }
    }
}
