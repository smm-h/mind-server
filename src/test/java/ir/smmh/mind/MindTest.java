package ir.smmh.mind;

import ir.smmh.mind.impl.MutableMindImpl;
import ir.smmh.util.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"StandardVariableNames", "ClassWithoutConstructor", "ClassNamePrefixedWithPackageName"})
class MindTest {

    @SuppressWarnings("FieldCanBeLocal")
    private Mind.Mutable mind;
    private Idea.Mutable a, b, c;
    private String t;

    @BeforeEach
    final void beforeEach() {
        mind = MutableMindImpl.createBlank("test" + RandomUtil.generateRandomHex(6), null);
        a = mind.imagine("a");
        b = mind.imagine("b");
        c = mind.imagine("c");
        mind.imagine(t = "t");
    }

    @Test
    final void testIntension() {
        a.become(b);
        assertTrue(a.is(b), "a is not b");
    }

    @Test
    final void testTransitivity() {
        a.become(b);
        b.become(c);
        assertTrue(a.is(c), "a is not c");
    }

    @Test
    final void testPossession() {
        Property p = a.possess("p", t);
        assertTrue(a.has(p), "a does not have p");
    }

    @Test
    final void testTransitivePossession() {
        a.become(b);
        Property p = b.possess("p", t);
        assertTrue(a.has(p), "a does not have p");
    }
}
