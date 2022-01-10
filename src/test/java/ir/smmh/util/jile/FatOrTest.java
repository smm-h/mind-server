package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.FatOr;

public class FatOrTest extends OrTest {

    @Override
    DoubleOrString newDoubleOrString(Double value) {
        return new FatDoubleOrString(value, true);
    }

    @Override
    DoubleOrString newDoubleOrString(String value) {
        return new FatDoubleOrString(value, false);
    }

    private static class FatDoubleOrString extends FatOr<Double, String> implements DoubleOrString {
        private FatDoubleOrString(Object object, boolean isThis) {
            super(object, isThis);
        }
    }
}
