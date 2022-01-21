package ir.smmh.nile.verbs;

import ir.smmh.nile.adj.Multitude;

public interface CanContainPlace<P> extends Multitude {
    boolean containsPlace(P toCheck);
}
