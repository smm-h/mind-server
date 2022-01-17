package ir.smmh.tree.impl;

import ir.smmh.nile.adj.Sequential;
import ir.smmh.tree.Tree;
import org.jetbrains.annotations.NotNull;

public class TraversedDataImpl<DataType> implements Tree.TraversedData<DataType> {

    private final Sequential<DataType> data;
    private final Tree.DataTraversal type;

    public TraversedDataImpl(Sequential<DataType> data, Tree.DataTraversal type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public @NotNull Sequential<DataType> getData() {
        return data;
    }

    @Override
    public @NotNull Tree.DataTraversal getType() {
        return type;
    }
}
