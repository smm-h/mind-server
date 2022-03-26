package ir.smmh.chem;

import ir.smmh.chem.impl.ValueImpl;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueTest {

    private static final Function<String, Value> convert = ValueImpl::new;

    @Test
    void testSigFigs() {
        assertEquals(5, convert.apply("0.035000").getSignificantFigures());
        assertEquals(2, convert.apply("0.00035").getSignificantFigures());
        assertEquals(5, convert.apply("69.420").getSignificantFigures());
        assertEquals(4, convert.apply("6.003").getSignificantFigures());
        assertEquals(4, convert.apply("070.00").getSignificantFigures());
        assertEquals(3, convert.apply("316").getSignificantFigures());
        assertEquals(2, convert.apply("35000").getSignificantFigures());
    }

    @Test
    void testDecimalPlaces() {
        assertEquals(6, convert.apply("0.035000").getDecimalPlaces());
        assertEquals(5, convert.apply("0.00035").getDecimalPlaces());
        assertEquals(3, convert.apply("69.420").getDecimalPlaces());
        assertEquals(3, convert.apply("6.003").getDecimalPlaces());
        assertEquals(2, convert.apply("070.00").getDecimalPlaces());
        assertEquals(0, convert.apply("316").getDecimalPlaces());
        assertEquals(0, convert.apply("35000").getDecimalPlaces());
    }

    @Test
    void testOps() {
        assertEquals(convert.apply("3.13"), convert.apply("5.4589").subtract(convert.apply("2.33")));
        assertEquals(convert.apply("20.7"), convert.apply("16.872").add(convert.apply("3.8")));
        assertEquals(convert.apply("42"), convert.apply("2.33").multiply(convert.apply("18")));
        assertEquals(convert.apply("4.36"), convert.apply("31.9").divide(convert.apply("7.318")));
    }
}