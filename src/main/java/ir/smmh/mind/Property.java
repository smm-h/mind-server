package ir.smmh.mind;

public interface Property {

    Idea getOrigin();

    String getName();

    Idea getType();

    Value getDefaultValue();
}
