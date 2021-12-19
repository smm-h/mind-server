package ir.smmh.mind;

public interface MutableIdea extends Mutable, Idea {

    void become(Idea idea);

    // TODO default value for properties
    void possess(Property<?> property);

    void reify(Property<?> property, String value);
}
