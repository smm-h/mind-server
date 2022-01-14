package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.FunctionalUtil;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code SpecificTree} is a {@link Tree} whose subtrees are all of the same type as itself.
 *
 * @param <T> Data type
 * @param <Q> Specific tree type
 */
@SuppressWarnings("unused")
public interface SpecificTree<T, Q extends SpecificTree<T, Q>> extends Tree<T>, FunctionalUtil.RecursivelySpecific<Q> {

    @NotNull Sequential<Q> getSpecificImmediateSubtrees();

    interface Mutable<T, Q extends Mutable<T, Q>> extends SpecificTree<T, Q>, Tree.Mutable<T> {
        void setRootData(T data);
    }

    interface Binary<T, Q extends Binary<T, Q>> extends SpecificTree<T, Q>, Tree.Binary<T> {
        interface Mutable<T, Q extends Mutable<T, Q>> extends SpecificTree.Binary<T, Q>, SpecificTree.Mutable<T, Q>, Tree.Binary.Mutable<T> {
        }
    }
}
