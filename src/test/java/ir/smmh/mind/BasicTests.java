package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import ir.smmh.mind.impl.PropertyImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTests {

    @Test
    public void testIntension() {
        final Mind.Mutable mind = new MutableMindImpl();
        final Idea.Mutable a = mind.imagine("a");
        final Idea.Mutable b = mind.imagine("b");
        a.become(b);
        assertTrue(a.is(b));
    }

    @Test
    public void testTransitivity() {
        final Mind.Mutable mind = new MutableMindImpl();
        final Idea.Mutable a = mind.imagine("a");
        final Idea.Mutable b = mind.imagine("b");
        final Idea.Mutable c = mind.imagine("c");
        a.become(b);
        b.become(c);
        assertTrue(a.is(c));
    }

    @Test
    public void testPossession() {
        final Mind.Mutable mind = new MutableMindImpl();
        final Idea.Mutable a = mind.imagine("a");
        final Idea.Mutable t = mind.imagine("t");
        final Property<Object> p = new PropertyImpl<>(a, "p", t, null);
        a.possess(p);
        assertTrue(a.has(p));
    }
}
