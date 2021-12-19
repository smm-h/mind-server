package ir.smmh.mind;

public interface Property<T> {

    Idea getOrigin();

    String getName();

    Idea getType();

    T getDefaultValue();
}
