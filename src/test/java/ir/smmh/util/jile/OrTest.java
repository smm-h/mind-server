package ir.smmh.util.jile;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class OrTest {

    @NotNull
    final DoubleOrString add(DoubleOrString a, DoubleOrString b) {
        if (a.sameTypeAs(b)) {
            return (a.isThis())
                    ? newDoubleOrString(a.getThis() + b.getThis())
                    : newDoubleOrString(a.getThat() + b.getThat());
        } else {
            throw new IllegalArgumentException();
        }
    }

    abstract DoubleOrString newDoubleOrString(Double value);

    abstract DoubleOrString newDoubleOrString(String value);

    @Test
    final void addStrings() {
        assertEquals(add(
                newDoubleOrString(5.0),
                newDoubleOrString(6.0)
        ).getThis(), 11.0);
    }

    @Test
    final void addDoubles() {
        assertEquals(add(
                newDoubleOrString("Hello, "),
                newDoubleOrString("World!")
        ).getThat(), "Hello, World!");
    }

    interface DoubleOrString extends Or<Double, String> {
    }
}