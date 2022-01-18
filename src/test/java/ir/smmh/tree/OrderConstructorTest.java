package ir.smmh.tree;

import org.junit.jupiter.api.Test;

import static ir.smmh.nile.adj.Sequential.of;
import static ir.smmh.tree.Tree.Binary.OrderConstructor.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"SpellCheckingInspection", "SameParameterValue"})
class OrderConstructorTest {

    @Test
    void pre1() {
        pre("ABCDEFGH", "CBADHGFE", "EDABCFGH");
    }

    @Test
    void pre2() {
        pre("GFHKDLAWRQPZ", "FGHDALPQRZWK", "KHGFWLDAZRQP");
    }

    @Test
    void post1() {
        post("124536", "425136", "452631");
    }

    @Test
    void in1() {
        in("124895367", "894526731", "849251637");
    }

    void pre(String inOrder, String postOrder, String expectedPreOrder) {
        assertEquals(
                of(expectedPreOrder), targetPreOrder(
                        of(inOrder),
                        of(postOrder))
                        .getTarget());
    }

    void post(String preOrder, String inOrder, String expectedPostOrder) {
        assertEquals(
                of(expectedPostOrder), targetPostOrder(
                        of(preOrder),
                        of(inOrder))
                        .getTarget());
    }

    void in(String preOrder, String postOrder, String expectedInOrder) {
        assertEquals(
                of(expectedInOrder), targetInOrder(
                        of(preOrder),
                        of(postOrder))
                        .getTarget());
    }

}