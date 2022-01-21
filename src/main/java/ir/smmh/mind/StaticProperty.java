package ir.smmh.mind;

import ir.smmh.nile.verbs.CanSerialize;
import ir.smmh.util.Named;

public interface StaticProperty extends CanSerialize, Named {

    Idea getOrigin();

    String getType();

    Value getValue();

    default String encode() {
        return "reify " + getName() + " as " + getType();
        // TODO encode static property
    }
}
