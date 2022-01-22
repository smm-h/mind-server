package ir.smmh.tree;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.util.FunctionalUtil;
import org.jetbrains.annotations.NotNull;

/**
 * A {@code SpecificTree} is a {@link Tree} whose subtrees are all of the same type as itself.
 *
 * @param <DataType> Data type
 * @param <TreeType> Specific tree type
 */
@SuppressWarnings("unused")
public interface SpecificTree<DataType, TreeType extends SpecificTree<DataType, TreeType>> extends Tree<DataType>, FunctionalUtil.RecursivelySpecific<TreeType> {

    @Override
    @NotNull Sequential<TreeType> getImmediateSubtrees();

    interface Mutable<DataType, TreeType extends Mutable<DataType, TreeType>> extends SpecificTree<DataType, TreeType>, Tree.Mutable<DataType> {
    }

    interface Binary<DataType, TreeType extends Binary<DataType, TreeType>> extends SpecificTree<DataType, TreeType>, Tree.Binary<DataType> {
        interface Mutable<DataType, TreeType extends Mutable<DataType, TreeType>> extends SpecificTree.Binary<DataType, TreeType>, SpecificTree.Mutable<DataType, TreeType>, Tree.Binary.Mutable<DataType> {
        }
    }
}
