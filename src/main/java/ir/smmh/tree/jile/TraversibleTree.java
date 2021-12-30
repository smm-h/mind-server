package ir.smmh.tree.jile;

import ir.smmh.tree.jile.impl.LinkedTree;

import java.util.function.Function;

public interface TraversibleTree<T> extends Tree<T> {

    <T2> LinkedTree<T2> convert(Function<T, T2> convertor);

    <T2> Tree<T2> cast();

    void goTo(T data);

    T getPointer();

    void goToLastAdded();

    void goToChild(int childIndex) throws IndexOutOfBoundsException;

    void goToRoot();

    void goBack();

}