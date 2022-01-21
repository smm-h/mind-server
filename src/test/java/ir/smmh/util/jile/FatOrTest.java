package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.FatOr;

class FatOrTest extends OrTest {

    @Override
    final OrTest.DoubleOrString newDoubleOrString(Double value) {
        return new FatDoubleOrString(value, true);
    }

    @Override
    final OrTest.DoubleOrString newDoubleOrString(String value) {
        return new FatDoubleOrString(value, false);
    }

    private static final class FatDoubleOrString extends FatOr<Double, String> implements OrTest.DoubleOrString {
        private FatDoubleOrString(Object object, boolean isThis) {
            super(object, isThis);
        }
    }
}
