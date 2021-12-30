package ir.smmh.lingu;

import ir.smmh.lingu.impl.CodeImpl;

/**
 * Implying the possibility that "this was decoded from a {@link CodeImpl}"
 */
public interface Decodeable {
    CodeImpl getSourceCode();
}
