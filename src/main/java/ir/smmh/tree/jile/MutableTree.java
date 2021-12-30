package ir.smmh.tree.jile;

public interface MutableTree<T> extends TraversibleTree<T> {

    void addAndGoTo(T data);

    void add(T data);

    T remove();

}
