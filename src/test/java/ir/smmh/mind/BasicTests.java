package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import ir.smmh.mind.impl.PropertyImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTests {

    private Mind.Mutable m;
    private Idea.Mutable a, b, c, t;
    private Property<Instance> p;

    private void reset() {
        m = new MutableMindImpl();
        a = m.imagine("a");
        b = m.imagine("b");
        c = m.imagine("c");
        t = m.imagine("t");
        p = new PropertyImpl<>(a, "p", t, null);
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
        a.possess(p);
        assertTrue(a.has(p));
    }

    @Test
    public void testTransitivePossession() {
        reset();
        b.possess(p);
        assertTrue(a.has(p));
    }
}
