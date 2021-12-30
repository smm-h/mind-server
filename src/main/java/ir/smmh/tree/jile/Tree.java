package ir.smmh.tree.jile;

import ir.smmh.tree.jile.impl.LinkedTree;

import java.util.List;

public interface Tree<T> {

    boolean contains(T data);

    boolean isEmpty();

    T getRoot();

    List<T> getLeaf();

    T getParent(T data);

    Iterable<T> getChildren(T data);

    boolean hasChildren(T data);

    String getRepresenation();

    @SuppressWarnings("rawtypes")
    LinkedTree EMPTY_TREE = new LinkedTree<>();

    @SuppressWarnings("unchecked")
    static <T> Tree<T> emptyTree() {
        return (LinkedTree<T>) EMPTY_TREE;
    }

    /**
     * To convert an n-ary tree to 2-ary, in every node, iterate over the siblings
     * from left to right, and make each one; but before that for each node
     * disassociate it from all its children children except the leftmost one.
     */
    // public void toBinary(); TODO
}
