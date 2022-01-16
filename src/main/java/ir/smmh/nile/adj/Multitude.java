package ir.smmh.nile.adj;

import ir.smmh.util.Mutable;

public interface Multitude {
    int getSize();

    interface VariableSize extends Multitude, Mutable {
    }
}
