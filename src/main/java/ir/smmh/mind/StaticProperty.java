package ir.smmh.mind;

import ir.smmh.util.Named;
import ir.smmh.util.Serializable;

public interface StaticProperty extends Serializable, Named {

    Idea getOrigin();

    String getType();

    Value getValue();

    default String encode() {
        return "reify " + getName() + " as " + getType();
        // TODO encode static property
    }
}
