package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MindTest {

    @SuppressWarnings("FieldCanBeLocal")
    private Mind.Mutable m;
    private Idea.Mutable a, b, c;
    private String t;

    @BeforeEach
    void beforeEach() {
        m = new MutableMindImpl("test" + RandomUtil.generateRandomHex(6), null);
        a = m.imagine("a");
        b = m.imagine("b");
        c = m.imagine("c");
        m.imagine(t = "t");
    }

    @Test
    public void testIntension() {
        a.become(b);
        assertTrue(a.is(b));
    }

    @Test
    public void testTransitivity() {
        a.become(b);
        b.become(c);
        assertTrue(a.is(c));
    }

    @Test
    public void testPossession() {
        final Property p = a.possess("p", t);
        assertTrue(a.has(p));
    }

    @Test
    public void testTransitivePossession() {
        a.become(b);
        final Property p = b.possess("p", t);
        assertTrue(a.has(p));
    }
}
