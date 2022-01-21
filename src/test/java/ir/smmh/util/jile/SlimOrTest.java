package ir.smmh.util.jile;

import ir.smmh.util.jile.impl.SlimOr;

public class SlimOrTest extends OrTest {

    @Override
    final OrTest.DoubleOrString newDoubleOrString(Double value) {
        return new SlimDoubleOrString(value, true);
    }

    @Override
    final OrTest.DoubleOrString newDoubleOrString(String value) {
        return new SlimDoubleOrString(value, false);
    }

    private static class SlimDoubleOrString extends SlimOr<Double, String> implements OrTest.DoubleOrString {
        private SlimDoubleOrString(Object object, boolean isThis) {
            super(object, isThis);
        }
    }
}
