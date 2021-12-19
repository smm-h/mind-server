package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTests {

    private Mind.Mutable m;
    private Idea.Mutable a, b, c, t;

    private void reset() {
        m = new MutableMindImpl();
        a = m.imagine("a");
        b = m.imagine("b");
        c = m.imagine("c");
        t = m.imagine("t");
    }

    @Test
    public void testIntension() {
        reset();
        a.become(b);
        assertTrue(a.is(b));
    }

    @Test
    public void testTransitivity() {
        reset();
        a.become(b);
        b.become(c);
        assertTrue(a.is(c));
    }

    @Test
    public void testPossession() {
        reset();
        final Property p = a.possess("p", t);
        assertTrue(a.has(p));
    }

    @Test
    public void testTransitivePossession() {
        reset();
        a.become(b);
        final Property p = b.possess("p", t);
        assertTrue(a.has(p));
    }
}
